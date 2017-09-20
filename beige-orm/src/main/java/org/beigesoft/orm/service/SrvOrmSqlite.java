package org.beigesoft.orm.service;

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
import java.lang.reflect.Field;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.ColumnsValues;

/**
 * <p>ORM service with RDBMS specific INSERT implementation.
 * Insert must fill ID for entities that have autogenerated ID.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvOrmSqlite<RS> extends ASrvOrm<RS> {

  /**
   * <p>Insert entity into DB.
   * For autogenerated ID fill it in the entity</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void insertEntity(final Map<String, Object> pAddParam,
    final T pEntity) throws Exception {
    ColumnsValues columnsValues = evalColumnsValues(pAddParam, pEntity);
    long result = getSrvDatabase().executeInsert(pEntity.getClass()
      .getSimpleName().toUpperCase(), columnsValues);
    if (result != 1) {
      String query = getHlpInsertUpdate().evalSqlInsert(
        pEntity.getClass().getSimpleName().toUpperCase(),
          columnsValues);
      throw new ExceptionWithCode(ISrvDatabase.ERROR_INSERT_UPDATE,
        "It should be 1 row inserted but it is "
          + result + ", query:\n" + query + ";\n");
    }
    String[] idName = columnsValues.getIdColumnsNames();
    if (idName.length == 1) { // if non-composite PK
      Field fieldId = getUtlReflection()
                .retrieveField(pEntity.getClass(), idName[0]);
      fieldId.setAccessible(true);
      Object idValue = fieldId.get(pEntity);
      if (idValue == null) {
        //It must be an autogenerated Integer or Long ID
        IRecordSet<RS> recordSet = null;
        try {
          String insertedIdName = "INSERTEDID";
          recordSet = getSrvDatabase().retrieveRecords(
            "select last_insert_rowid() as " + insertedIdName + ";");
          recordSet.moveToFirst();
          if (fieldId.getType() == Long.class) {
            fieldId.set(pEntity, recordSet.getLong(insertedIdName));
          } else if (fieldId.getType() == Integer.class) {
            fieldId.set(pEntity, recordSet.getInteger(insertedIdName));
          } else {
            String msg = "There is no rule to fill ID "
              + fieldId.getName().toUpperCase()
                + " of " + fieldId.getType() + " in "
                  + pEntity;
            throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
              msg);
          }
        } finally {
          if (recordSet != null) {
            recordSet.close();
          }
        }
      }
    }
  }
}
