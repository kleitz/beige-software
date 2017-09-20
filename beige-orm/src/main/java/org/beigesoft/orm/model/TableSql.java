package org.beigesoft.orm.model;

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

import java.util.LinkedHashMap;

/**
 * <p>Represent SQL model of an entity.</p>
 *
 * @author Yury Demidenko
 */
public class TableSql {

  /**
   * <p>SQL field descriptor. Field name - SQL definition.
   * E.g. id - INTEGER NOT NULL</p>
   **/
  private LinkedHashMap<String, FieldSql> fieldsMap =
    new LinkedHashMap<String, FieldSql>();

  /**
   * <p>Version algorithm.
   * 0 - autoincrement, 1 - date changed.</p>
   **/
  private int versionAlgorithm;

  /**
   * <p>Table all CONSTRAINTs e.g. PRIMARY KEY ("id"), FOREIGN KEY....</p>
   **/
  private String constraint;

  /**
   * <p>ID names from XML-setting <b>idColumnsNames</b> e.g. "itsId"
   * or comma separated names for composite ID,
   * e.g. "goods,priceCategory".
   * It's used to make DDL/DML and filling/retrieving ID.</p>
   **/
  private String[] idColumnsNames;

  /**
   * <p>ID field (in class) name, e.g. composite ID in class "itsId"
   * but in table ID columns {"goods","place"}.</p>
   **/
  private String idFieldName;

  /**
   * <p>Owner's field (in class) name if exist,
   * e.g. "customerIncoice" for InvoiceLine.</p>
   **/
  private String ownerFieldName;


  @Override
  public final String toString() {
    StringBuffer sbIdNames = new StringBuffer("");
    if (this.idColumnsNames != null) {
      boolean isFirst = true;
      for (String idNm : this.idColumnsNames) {
        if (isFirst) {
          isFirst = false;
        } else {
          sbIdNames.append(",");
        }
        sbIdNames.append(idNm);
      }
    }
    return "ID name: " + sbIdNames + "\n"
      + "constraint: " + this.constraint + "\n"
      + "version algorithm: " + this.versionAlgorithm;
  }

  //Hiding source getters and setters:
  /**
   * <p>Getter for idColumnsNames.</p>
   * @return String[]
   **/
  public final String[] getIdColumnsNames() {
    if (this.idColumnsNames == null) {
      return null;
    } else {
      return java.util.Arrays.copyOf(this.idColumnsNames,
        this.idColumnsNames.length);
    }
  }

  /**
   * <p>Setter for idColumnsNames.</p>
   * @param pIdColumnsNames reference
   **/
  public final void setIdColumnsNames(final String[] pIdColumnsNames) {
    if (pIdColumnsNames == null) {
      this.idColumnsNames = null;
    } else {
      this.idColumnsNames = java.util.Arrays.copyOf(pIdColumnsNames,
        pIdColumnsNames.length);
    }
  }

  //Utils:
  /**
   * <p>Getter for comma separated string of ID columns.</p>
   * @return String
   **/
  public final String getIdColumnsString() {
    StringBuffer sb = new StringBuffer("");
    for (int i = 0; i < this.idColumnsNames.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(this.idColumnsNames[i]);
    }
    return sb.toString();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for ownerFieldName.</p>
   * @return String
   **/
  public final String getOwnerFieldName() {
    return this.ownerFieldName;
  }

  /**
   * <p>Setter for ownerFieldName.</p>
   * @param pOwnerFieldName reference
   **/
  public final void setOwnerFieldName(final String pOwnerFieldName) {
    this.ownerFieldName = pOwnerFieldName;
  }

  /**
   * <p>Getter for idFieldName.</p>
   * @return String
   **/
  public final String getIdFieldName() {
    return this.idFieldName;
  }

  /**
   * <p>Setter for idFieldName.</p>
   * @param pIdFieldName reference
   **/
  public final void setIdFieldName(final String pIdFieldName) {
    this.idFieldName = pIdFieldName;
  }

  /**
   * <p>Geter for fieldsMap.</p>
   * @return LinkedHashMap<String, FieldSql>
   **/
  public final LinkedHashMap<String, FieldSql> getFieldsMap() {
    return this.fieldsMap;
  }

  /**
   * <p>Setter for fieldsMap.</p>
   * @param pFieldsMap reference
   **/
  public final void
    setFieldsMap(final LinkedHashMap<String, FieldSql> pFieldsMap) {
    this.fieldsMap = pFieldsMap;
  }

  /**
   * <p>Geter for constraint.</p>
   * @return String
   **/
  public final String getConstraint() {
    return this.constraint;
  }

  /**
   * <p>Setter for constraint.</p>
   * @param pConstraint reference
   **/
  public final void setConstraint(final String pConstraint) {
    this.constraint = pConstraint;
  }

  /**
   * <p>Geter for versionAlgorithm.</p>
   * @return int
   **/
  public final int getVersionAlgorithm() {
    return this.versionAlgorithm;
  }

  /**
   * <p>Setter for versionAlgorithm.</p>
   * @param pVersionAlgorithm reference
   **/
  public final void setVersionAlgorithm(final int pVersionAlgorithm) {
    this.versionAlgorithm = pVersionAlgorithm;
  }
}
