package org.beigesoft.model;

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
