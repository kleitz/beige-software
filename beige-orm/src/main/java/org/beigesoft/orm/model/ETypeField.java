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

/**
 * <pre>
 * Type FieldSql STANDARD/COMPOSITE_FK_PK/COMPOSITE_FK/DERIVED_FROM_COMPOSITE.
 * </pre>
 *
 * @author Yury Demidenko
 */
public enum ETypeField {

  /**
   * <p>Standard field that presents in class and table.</p>
   **/
  STANDARD,

  /**
   * <p>Composite that present in class but in table represented as
   * set of its fields, e.g. composite PK that can be also
   * foreign entity with composite ID.</p>
   **/
  COMPOSITE_FK_PK,

  /**
   * <p>Composite that present in class but in table represented as
   * set of its fields, e.g. foreign entity with composite ID.</p>
   **/
  COMPOSITE_FK,

  /**
   * <p>Exist in table but in class is in composite field.</p>
   **/
  DERIVED_FROM_COMPOSITE;
}
