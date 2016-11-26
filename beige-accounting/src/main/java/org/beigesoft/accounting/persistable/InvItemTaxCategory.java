package org.beigesoft.accounting.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.List;

import org.beigesoft.persistable.APersistableBaseNameVersion;

/**
 * <pre>
 * Model of tax category of a goods/material or service.
 * This model used to assign tax or set of taxes for an item/service
 * e.g. "NY sales taX 6%" for pizza hot.
 * Version, changed time algorithm cause check dirty of
 * calculated from it (derived) records.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class InvItemTaxCategory extends APersistableBaseNameVersion {

  /**
   * <p>Taxes.</p>
   **/
  private List<InvItemTaxCategoryLine> taxes;

  /**
   * <p>Taxes description, uneditable,
   * e.g. "NY Sales Tax 10%".</p>
   **/
  private String taxesDescription;

  //Simple getters and setters:
  /**
   * <p>Geter for taxes.</p>
   * @return List<InvItemTaxCategoryLine>
   **/
  public final List<InvItemTaxCategoryLine> getTaxes() {
    return this.taxes;
  }

  /**
   * <p>Setter for taxes.</p>
   * @param pTaxes reference
   **/
  public final void setTaxes(final List<InvItemTaxCategoryLine> pTaxes) {
    this.taxes = pTaxes;
  }

  /**
   * <p>Geter for taxesDescription.</p>
   * @return String
   **/
  public final String getTaxesDescription() {
    return this.taxesDescription;
  }

  /**
   * <p>Setter for taxesDescription.</p>
   * @param pTaxesDescription reference
   **/
  public final void setTaxesDescription(final String pTaxesDescription) {
    this.taxesDescription = pTaxesDescription;
  }
}
