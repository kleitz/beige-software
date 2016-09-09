package org.beigesoft.orm.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
   * <p>Key for Version algorithm.</p>
   **/
  public static final String KEY_VERSION_ALGORITHM = "versionAlgorithm";

  /**
   * <p>Table all CONSTRAINTs e.g. PRIMARY KEY ("id"), FOREIGN KEY....</p>
   **/
  private String constraint;

  /**
   * <p>Key for constraint in full table definition properties file.</p>
   **/
  public static final String KEY_CONSTRAINT = "constraint";

  /**
   * <p>Key for additional constraint.</p>
   **/
  public static final String KEY_CONSTRAINT_ADD = "constraintAdd";

  /**
   * <p>ID name (field primary non-complex key) e.g. "itsId".</p>
   **/
  private String idName;

  /**
   * <p>Key for simple ID name (complex is always null!!!).</p>
   **/
  public static final String KEY_ID_NAME = "idName";

  /**
   * <p>idDefinitionForeign - ID SQL definition for foreign key
   * (complex is always null!!!).
   * e.g. Customer.itsId has SQL definition "bigserial primary key",
   * but Invoice.Customer has definition "bigint".</p>
   **/
  private String idDefinitionForeign;

  /**
   * <p>Key for ID type.</p>
   **/
  public static final String KEY_ID_DEFINITION_FOREIGN = "idDefinitionForeign";

  /**
   * <p>If this table doesn't need to farther define by program.</p>
   **/
  private boolean isFullDefinedInXml;

  /**
   * <p>Key for "is full define in XML".</p>
   **/
  public static final String KEY_IF_FULL_DEFINE_IN_XML = "isFullDefinedInXml";


  @Override
  public final String toString() {
    return "ID name: " + this.idName + "\n"
      + "ID SQL definition for foreign key: " + this.idDefinitionForeign + "\n"
      + "constraint: " + this.constraint + "\n"
      + "is full defined in Xml: " + this.isFullDefinedInXml + "\n"
      + "version algorithm: " + this.versionAlgorithm;
  }

  //Simple getters and setters:
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

  /**
   * <p>Geter for idName.</p>
   * @return String
   **/
  public final String getIdName() {
    return this.idName;
  }

  /**
   * <p>Setter for idName.</p>
   * @param pIdName reference/value
   **/
  public final void setIdName(final String pIdName) {
    this.idName = pIdName;
  }

  /**
   * <p>Geter for idDefinitionForeign.</p>
   * @return String
   **/
  public final String getIdDefinitionForeign() {
    return this.idDefinitionForeign;
  }

  /**
   * <p>Setter for idDefinitionForeign.</p>
   * @param pIdDefinitionForeign reference/value
   **/
  public final void setIdDefinitionForeign(final String pIdDefinitionForeign) {
    this.idDefinitionForeign = pIdDefinitionForeign;
  }

  /**
   * <p>Geter for isFullDefinedInXml.</p>
   * @return boolean
   **/
  public final boolean getIsFullDefinedInXml() {
    return this.isFullDefinedInXml;
  }

  /**
   * <p>Setter for isFullDefinedInXml.</p>
   * @param pIsFullDefinedInXml reference/value
   **/
  public final void setIsFullDefinedInXml(final boolean pIsFullDefinedInXml) {
    this.isFullDefinedInXml = pIsFullDefinedInXml;
  }
}
