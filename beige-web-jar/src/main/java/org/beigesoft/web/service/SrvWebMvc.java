package org.beigesoft.web.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Date;
import java.math.BigDecimal;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.beigesoft.service.UtlReflection;
import org.beigesoft.orm.service.ASrvOrm;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.FieldSql;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that fill entity from pequest.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class SrvWebMvc<RS> implements ISrvWebMvc {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>ORM service.</p>
   */
  private ASrvOrm<RS> srvOrm;

  /**
   * <p>Reflection service.</p>
   **/
  private UtlReflection utlReflection;

  /**
   * <p>Fill entity from pequest.</p>
   * @param T entity type
   * @param pEntity Entity to fill
   * @param pReq - servlet request
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fillEntity(final T pEntity,
    final HttpServletRequest pReq) throws Exception {
    getLogger().debug(SrvWebEntity.class,
      "SrvWebMvc: request encoding : "
        + pReq.getCharacterEncoding());
    getLogger().debug(SrvWebEntity.class,
      "Default charset = " + Charset.defaultCharset());
    TableSql tableSql = srvOrm.getTablesMap().get(
      pEntity.getClass().getSimpleName());
    Field[] fields = getUtlReflection().retrieveFields(pEntity.getClass());
    for (Field field : fields) {
      try {
        FieldSql fieldSql = tableSql.getFieldsMap().get(field.getName());
        if (fieldSql != null) {
          if (fieldSql.getForeignEntity() != null) {
            TableSql tableSqlFrn = srvOrm.getTablesMap().
              get(fieldSql.getForeignEntity());
            String valStr = pReq.getParameter(pEntity.getClass().getSimpleName()
              + "." + field.getName() + "." + tableSqlFrn.getIdName());
            getLogger().debug(SrvWebMvc.class,
              "SrvWebMvc: Try to fill : " + field.getName() + " with "
                + valStr);
            Object entityFrn = null;
            if (valStr != null && valStr.length() > 0) {
              Constructor<?> constructorFrn = field.getType()
                .getDeclaredConstructor();
              entityFrn = constructorFrn.newInstance();
              Field fieldIdFrn = getUtlReflection()
                .retrieveField(field.getType(), tableSqlFrn.getIdName());
              fieldIdFrn.setAccessible(true);
              if (!tryToFillIdable(fieldIdFrn, entityFrn,
                valStr, fieldIdFrn.getType())) {
                String msg =
                "There is no rule to fill column foreign id value from field!";
                throw new Exception(msg);
              }
              Object idFrnVal = fieldIdFrn.get(entityFrn);
              if (idFrnVal == null) {
                entityFrn = null;
              }
            }
            if (entityFrn != null) {
              field.setAccessible(true);
              field.set(pEntity, entityFrn);
            }
          } else if (!fillSimpleField(field, pEntity, pReq)) {
            String msg = "There is no rule to fill field!";
            throw new Exception(msg);
          }
        } else if (!java.util.Collection.class.isAssignableFrom(field.getType())
          && !fillSimpleField(field, pEntity, pReq)) { //it can be
          //nonpersistable field like isNew
          //but if it is not owned list
          String msg = "There is no rule to fill field";
          throw new Exception(msg);
        }
      } catch (Exception e) {
        String msg = "Exception occured for "
          + field.getName()
            + " of " + field.getType() + " in " + pEntity.getClass()
              + "\nmessage: " + e.getMessage();
        Exception exc = new Exception(msg);
        exc.setStackTrace(e.getStackTrace());
        throw exc;
      }
    }
  }

  /**
   * <p>Fill entity field with reflection if it is simple type.</p>
   * @param pField field
   * @param pEntity entity
   * @param pReq HttpServletRequest
   * @return boolean if field filled
   * @throws Exception an exception
   **/
  public final boolean fillSimpleField(final Field pField, final Object pEntity,
    final HttpServletRequest pReq) throws Exception {
    pField.setAccessible(true);
    String valStr = pReq.getParameter(pEntity.getClass().getSimpleName()
      + "." + pField.getName());
    getLogger().debug(SrvWebMvc.class,
      "SrvWebMvc: Try to fill : " + pField.getName() + " with " + valStr);
    if (valStr == null) {
      return true;
    }
    if (tryToFillIdable(pField, pEntity, valStr, pField.getType())) {
      return true;
    }
    if (pField.getType() == Double.class) {
      Double valDb = null;
      if (valStr.length() > 0 && !"null".equals(valStr)) {
        valDb = Double.valueOf(valStr);
      }
      pField.set(pEntity, valDb);
    } else if (pField.getType() == Float.class) {
      Float valFl = null;
      if (valStr.length() > 0 && !"null".equals(valStr)) {
        valFl = Float.valueOf(valStr);
      }
      pField.set(pEntity, valFl);
    } else if (pField.getType() == BigDecimal.class) {
      BigDecimal valBd = null;
      if (valStr.length() > 0 && !"null".equals(valStr)) {
        valBd = new BigDecimal(valStr);
      }
      pField.set(pEntity, valBd);
    } else if (pField.getType() == Date.class) {
      Date valDt = null;
      if (valStr.length() > 0 && !"null".equals(valStr)) {
        Long valLn = Long.valueOf(valStr);
        valDt = new Date(valLn);
      }
      pField.set(pEntity, valDt);
    } else if (pField.getType() == Boolean.class) {
      Boolean valBl = false;
      if ("true".equals(valStr) || "on".equals(valStr)) {
        valBl = true;
      }
      pField.set(pEntity, valBl);
    } else if (Enum.class.isAssignableFrom(pField.getType())) {
      Enum val = null;
      for (Object enm : pField.getType().getEnumConstants()) {
        if (enm.toString().equals(valStr)) {
          val = (Enum) enm;
          break;
        }
      }
      pField.set(pEntity, val);
    } else {
      return false;
    }
    return true;
  }

  /**
   * <p>Fill entity field with reflection if it is simple,
   * ID allowed type.</p>
   * @param pField field
   * @param pEntity entity
   * @param pValStr String value
   * @param pFieldType Field Type
   * @return boolean if field filled
   * @throws Exception an exception
   **/
  protected final boolean tryToFillIdable(final Field pField,
    final Object pEntity, final String pValStr,
      final Type pFieldType) throws Exception {
    if (Integer.class == pFieldType) {
      Integer valInt = null;
      if (pValStr.length() > 0 && !"null".equals(pValStr)) {
        valInt = Integer.valueOf(pValStr);
      }
      pField.set(pEntity, valInt);
      return true;
    } else if (Long.class == pFieldType) {
      Long valLn = null;
      if (pValStr.length() > 0 && !"null".equals(pValStr)) {
        valLn = Long.valueOf(pValStr);
      }
      pField.set(pEntity, valLn);
      return true;
    } else if (String.class == pFieldType) {
      pField.set(pEntity, pValStr); //Unnullable
      return true;
    }
    return false;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvOrm.</p>
   * @return ASrvOrm<?>
   **/
  public final ASrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ASrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Getter for utlReflection.</p>
   * @return UtlReflection
   **/
  public final UtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final UtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }
}
