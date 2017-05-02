package org.beigesoft.webstore.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.persistable.AHasNameIdLongVersion;

/**
 * <pre>
 * Model of SeService.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SeService extends AHasNameIdLongVersion {

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
