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

/**
 * <p>Represent column SQL model.</p>
 *
 * @author Yury Demidenko
 */
public class FieldSql {

  /**
   * <p>SQL field definition.</p>
   **/
  private String definition;

  /**
   * <p>Key SQL field definition.</p>
   **/
  public static final String KEY_DEFINITION = "definition";

  /**
   * <p>Simple name of foreign(owned) entity.
   * To resolve LEFT JOIN to fill foreign entity
   * in SELECT statement. E.g. "Department" for "itsDepartment"</p>
   **/
  private String foreignEntity;

  /**
   * <p>Key for simple name of foreign(owned) entity.</p>
   **/
  public static final String KEY_FOREIGN_ENTITY = "foreignEntity";

  /**
   * <p>Default constructor.</p>
   **/
  public FieldSql() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pDefinition SQL definition
   **/
  public FieldSql(final String pDefinition) {
    this.definition = pDefinition;
  }

  /**
   * <p>Useful constructor.</p>
   * @param pDefinition SQL definition
   * @param pForeignEntity Table name for foreign ID
   **/
  public FieldSql(final String pDefinition,
    final String pForeignEntity) {
    this(pDefinition);
    this.foreignEntity = pForeignEntity;
  }

  @Override
  public final String toString() {
    return this.definition + ", fe=" + this.foreignEntity;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for definition.</p>
   * @return String
   **/
  public final String getDefinition() {
    return this.definition;
  }

  /**
   * <p>Setter for definition.</p>
   * @param pDefinition reference
   **/
  public final void setDefinition(final String pDefinition) {
    this.definition = pDefinition;
  }

  /**
   * <p>Geter for foreignEntity.</p>
   * @return String
   **/
  public final String getForeignEntity() {
    return this.foreignEntity;
  }

  /**
   * <p>Setter for foreignEntity.</p>
   * @param pForeignEntity reference
   **/
  public final void setForeignEntity(final String pForeignEntity) {
    this.foreignEntity = pForeignEntity;
  }
}
