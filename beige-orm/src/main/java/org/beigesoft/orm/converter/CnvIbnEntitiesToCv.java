package org.beigesoft.orm.converter;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.converter.IConverterIntoByName;
import org.beigesoft.holder.IHolderForClassByName;

/**
 * <p>Converter any foreign entity to ColumnsValues
 * with transformation into FE ID. It uses foreign TableSql and RAPI.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnEntitiesToCv
  implements IConverterIntoByName<Object, ColumnsValues> {

  /**
   * <p>Tables SQL desccriptors map "EntitySimpleName - TableSql".
   * Ordered cause creating tables is order dependent.</p>
   **/
  private Map<String, TableSql> tablesMap;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Field's RAPI getters holder.</p>
   **/
  private IHolderForClassByName<Method> gettersRapiHolder;

  /**
   * <p>Put Foreign entity object to ColumnsValues with transformation
   * into its ID.</p>
   * @param pAddParam expected "fromClass" class of object.
   * @param pFrom from a Object object, may be null
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final Object pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
    Class<?> fromClass = (Class<?>) pAddParam.get("fromClass");
    //reveal foreign ID fields names:
    TableSql foreignTable = this.tablesMap.get(fromClass.getSimpleName());
    if (foreignTable.getIdColumnsNames().length == 1) {
      // NON-composite ID, e.g. GoodsRating.goods:
      // e.g. getter for itsId in foreign entity InvItem:
      Field field = this.fieldsRapiHolder.getFor(fromClass,
        foreignTable.getIdColumnsNames()[0]);
      Method getter = this.gettersRapiHolder.getFor(fromClass,
        foreignTable.getIdColumnsNames()[0]);
      if (!tryToPutIdable(field, getter, pFrom, pTo, pName)) {
        String idForeignEntityNm = foreignTable.getFieldsMap()
          .get(foreignTable.getIdColumnsNames()[0]).getForeignEntity();
        boolean isSolved = false;
        if (idForeignEntityNm != null) {
          // e.g. CartItem.itsOwner - ShoppinCart has ID OnlineBuyer
          TableSql idForeignTable = this.tablesMap.get(idForeignEntityNm);
          if (idForeignTable.getIdColumnsNames().length == 1) {
            // e.g. buyer in ShoppinCart
            Object idObject = getter.invoke(pFrom);
            // e.g. itsId of OnlineBuyer
            Field fieldId = this.fieldsRapiHolder.getFor(field.getType(),
              idForeignTable.getIdColumnsNames()[0]);
            Method getterId = this.gettersRapiHolder.getFor(field.getType(),
              idForeignTable.getIdColumnsNames()[0]);
            if (tryToPutIdable(fieldId, getterId, idObject, pTo, pName)) {
              isSolved = true;
            }
          }
        }
        if (!isSolved) {
          String msg =
            "There is no rule to fill column foreign id value from field "
              + pName + " of " + field.getType() + " in " + pFrom;
          throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
            msg);
        }
      }
    } else { // foreign entity with composite ID always represented
      // in SQL table as set of fields with names and types as
      // in original table, e.g.:
      // org.beigesoft.test.persistable.UserRoleTomcatPriority.userRoleTomcat
      // which be represented as itsUser='admin' and "itsRole='role'
      for (String idFldNm : foreignTable.getIdColumnsNames()) {
        Method getter = this.gettersRapiHolder.getFor(fromClass,
          idFldNm);
        Field field = this.fieldsRapiHolder.getFor(fromClass,
          idFldNm);
        if (!tryToPutIdable(field, getter, pFrom, pTo, idFldNm)) {
          TableSql foreignIdTable = this.tablesMap.get(field.getType()
            .getSimpleName());
          if (foreignIdTable == null
            || foreignIdTable.getIdColumnsNames().length > 1) {
            String msg =
              "There is no rule to fill column foreign id value from field "
                + pName + " of " + field.getType() + " in " + pFrom;
            throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
              msg);
          }
          // ID is also foreign key, and it must be of simple type
          // e.g. UserRoleTomcatPriority.userRoleTomcat.itsUser
          Method getterId = this.gettersRapiHolder.getFor(field.getType(),
            foreignIdTable.getIdColumnsNames()[0]);
          Field fieldId = this.fieldsRapiHolder.getFor(field.getType(),
            foreignIdTable.getIdColumnsNames()[0]);
          Object idVal;
          if (pFrom ==  null) {
            idVal = null;
          } else {
            idVal = getter.invoke(pFrom); // e.g. UserTomcat
          }
          if (!tryToPutIdable(fieldId, getterId, idVal, pTo,
            foreignIdTable.getIdColumnsNames()[0])) {
            String msg =
              "Can't fill FCID 2L: " + foreignIdTable.getIdColumnsNames()[0]
                + " of " + fieldId.getType() + " in " + idVal;
            throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
              msg);
          }
        }
      }
    }
  }

  /**
   * <p>Put ID-able field value to ColumnsValues.</p>
   * @param pField field RAPI
   * @param pGetter getter RAPI
   * @param pFrom from a Object object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @return if put
   * @throws Exception - an exception
   **/
  public final boolean tryToPutIdable(final Field pField, final Method pGetter,
    final Object pFrom, final ColumnsValues pTo,
      final  String pName) throws Exception {
    //reveal foreign ID fields names:
    if (Integer.class == pField.getType()) {
      Integer val;
      if (pFrom == null) {
        val = null;
      } else {
        val = (Integer) pGetter.invoke(pFrom);
      }
      pTo.put(pName, val);
      return true;
    } else if (Long.class == pField.getType()) {
      // the most used ID
      Long val;
      if (pFrom == null) {
        val = null;
      } else {
        val = (Long) pGetter.invoke(pFrom);
      }
      pTo.put(pName, val);
      return true;
    } else if (String.class == pField.getType()) {
      // e.g. UserRoleTomcatPriority.userRoleTomcat.itsRole
      // or UserRoleTomcatPriority.userRoleTomcat.itsUser.itsUser
      String val;
      if (pFrom == null) {
        val = null;
      } else {
        //String ID must not be needed to SQL escape
        val = (String) pGetter.invoke(pFrom);
      }
      pTo.put(pName, val);
      return true;
    }
    return false;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for tablesMap.</p>
   * @return Map<String, TableSql>
   **/
  public final Map<String, TableSql> getTablesMap() {
    return this.tablesMap;
  }

  /**
   * <p>Setter for tablesMap.</p>
   * @param pTablesMap reference
   **/
  public final void setTablesMap(final Map<String, TableSql> pTablesMap) {
    this.tablesMap = pTablesMap;
  }

  /**
   * <p>Getter for fieldsRapiHolder.</p>
   * @return IHolderForClassByName<Field>
   **/
  public final IHolderForClassByName<Field> getFieldsRapiHolder() {
    return this.fieldsRapiHolder;
  }

  /**
   * <p>Setter for fieldsRapiHolder.</p>
   * @param pFieldsRapiHolder reference
   **/
  public final void setFieldsRapiHolder(
    final IHolderForClassByName<Field> pFieldsRapiHolder) {
    this.fieldsRapiHolder = pFieldsRapiHolder;
  }

  /**
   * <p>Getter for gettersRapiHolder.</p>
   * @return IHolderForClassByName<Method>
   **/
  public final IHolderForClassByName<Method> getGettersRapiHolder() {
    return this.gettersRapiHolder;
  }

  /**
   * <p>Setter for gettersRapiHolder.</p>
   * @param pGettersRapiHolder reference
   **/
  public final void setGettersRapiHolder(
    final IHolderForClassByName<Method> pGettersRapiHolder) {
    this.gettersRapiHolder = pGettersRapiHolder;
  }
}
