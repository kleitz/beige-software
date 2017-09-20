package org.beigesoft.webstore.persistable;

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

import org.beigesoft.persistable.AHasNameIdLongVersion;

/**
 * <pre>
 * Model of SeGoods.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SeGoods extends AHasNameIdLongVersion {

  /**
   * <p>Seller.</p>
   **/
  private SeSeller seller;

  //Simple getters and setters:
  /**
   * <p>Getter for seller.</p>
   * @return SeSeller
   **/
  public final SeSeller getSeller() {
    return this.seller;
  }

  /**
   * <p>Setter for seller.</p>
   * @param pSeller reference
   **/
  public final void setSeller(final SeSeller pSeller) {
    this.seller = pSeller;
  }
}
