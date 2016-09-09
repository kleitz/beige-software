package org.beigesoft.model;

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
 * <pre>
 * Abstract model of entity that used in other one,
 * but that using entity used entity of different types in same field.
 * For example warehouse entry can be made by purchase or sales.
 * Accounting record has sub-account that can be InvItem or DebtorCreditor...
 * So constTypeCode return unique code of entity type (OOP consumable constant).
 * </pre>
 *
 * @author Yury Demidenko
 */
public interface IHasTypeCode {

  /**
   * <p>Constant of code type.</p>
   * @return code type
   **/
  Integer constTypeCode();
}
