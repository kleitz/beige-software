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
import java.util.Date;
import java.math.RoundingMode;

import org.beigesoft.model.EPeriod;
import org.beigesoft.persistable.AHasIdLong;

/**
 * <pre>
 * Accounting settings.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class AccSettings extends AHasIdLong {

  /**
   * <p>Version, changed time algorithm cause check dirty of
   * calculated from it (derived) records.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Date current accounting year to prevent wrong accounting entries.</p>
   **/
  private Date currentAccYear;

  /**
   * <p>Not Null, if sales tax vendor sales taxes fields will be appeared
   * in sales invoice, and taxes will be extracted into SalesTaxPayable.</p>
   **/
  private Boolean isExtractSalesTaxFromSales = false;

  /**
   * <p>Not Null, if sales tax vendor sales taxes fields will
   * be appeared in purchase invoice, and taxes will be extracted
   * into SalesTaxFromPurchase, this is for methods where payed taxes
   * from purchase should be extracted from inventory e.g. VAT
   * or sales taxes that should be capitalized (USA producing).</p>
   **/
  private Boolean isExtractSalesTaxFromPurchase = false;

  /**
   * <p>Organization name.</p>
   **/
  private String organization;

  /**
   * <p>Cost precision.</p>
   **/
  private Integer costPrecision = 4;

  /**
   * <p>Price precision.</p>
   **/
  private Integer pricePrecision = 2;

  /**
   * <p>Balance precision.</p>
   **/
  private Integer balancePrecision = 1;

  /**
   * <p>Quantity precision.</p>
   **/
  private Integer quantityPrecision = 2;

  /**
   * <p>Rounding mode.</p>
   **/
  private RoundingMode roundingMode = RoundingMode.HALF_UP;

  /**
   * <p>Currency.</p>
   **/
  private Currency currency;

  /**
   * <p>COGS method, e.g. FIFO Perpetual.</p>
   **/
  private CogsMethod cogsMethod;

  /**
   * <p>Balance store period, not null, EPeriod.DAILY/WEEKLY/MONTHLY.</p>
   **/
  private EPeriod balanceStorePeriod = EPeriod.MONTHLY;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>Accounting entries sources.</p>
   **/
  private List<AccEntriesSourcesLine> accEntriesSources;

  /**
   * <p>Sources for InvItem to be draw (they have theRest>0).</p>
   **/
  private List<CogsItemSourcesLine> cogsItemSources;

  /**
   * <p>Sources for InvItem of type material to be draw by manufacture
   * (they have theRest>0).</p>
   **/
  private List<DrawMaterialSourcesLine> drawMaterialSources;

  /**
   * <p>Method(service in factory app-beans)
   * that fill wage tax lines, not null.</p>
   **/
  private WageTaxesMethod wageTaxesMethod;

  //Hiding references getters and setters:
  /**
   * <p>Getter for currentAccYear.</p>
   * @return Date
   **/
  public final Date getCurrentAccYear() {
    if (this.currentAccYear == null) {
      return null;
    }
    return new Date(this.currentAccYear.getTime());
   }

  /**
   * <p>Setter for currentAccYear.</p>
   * @param pCurrentAccYear reference
   **/
  public final void setCurrentAccYear(
    final Date pCurrentAccYear) {
    if (pCurrentAccYear == null) {
      this.currentAccYear = null;
    } else {
      this.currentAccYear = new Date(pCurrentAccYear.getTime());
    }
  }

  //Simple getters and setters:
  /**
   * <p>Setter for costPrecision.</p>
   * @param pCostPrecision reference
   **/
  public final void setCostPrecision(final Integer pCostPrecision) {
    this.costPrecision = pCostPrecision;
  }

  /**
   * <p>Setter for roundingMode.</p>
   * @param pRoundingMode reference
   **/
  public final void setRoundingMode(final RoundingMode pRoundingMode) {
    this.roundingMode = pRoundingMode;
  }

  /**
   * <p>Setter for pricePrecision.</p>
   * @param pPricePrecision reference
   **/
  public final void setPricePrecision(final Integer pPricePrecision) {
    this.pricePrecision = pPricePrecision;
  }

  /**
   * <p>Setter for balancePrecision.</p>
   * @param pBalancePrecision reference
   **/
  public final void setBalancePrecision(final Integer pBalancePrecision) {
    this.balancePrecision = pBalancePrecision;
  }

  /**
   * <p>Geter for itsVersion.</p>
   * @return Long
   **/
  public final Long getItsVersion() {
    return this.itsVersion;
  }

  /**
   * <p>Setter for itsVersion.</p>
   * @param pItsVersion reference
   **/
  public final void setItsVersion(final Long pItsVersion) {
    this.itsVersion = pItsVersion;
  }

  /**
   * <p>Geter for organization.</p>
   * @return String
   **/
  public final String getOrganization() {
    return this.organization;
  }

  /**
   * <p>Setter for organization.</p>
   * @param pOrganization reference
   **/
  public final void setOrganization(final String pOrganization) {
    this.organization = pOrganization;
  }

  /**
   * <p>Getter for isExtractSalesTaxFromSales.</p>
   * @return Boolean
   **/
  public final Boolean getIsExtractSalesTaxFromSales() {
    return this.isExtractSalesTaxFromSales;
  }

  /**
   * <p>Setter for isExtractSalesTaxFromSales.</p>
   * @param pIsExtractSalesTaxFromSales reference
   **/
  public final void setIsExtractSalesTaxFromSales(
    final Boolean pIsExtractSalesTaxFromSales) {
    this.isExtractSalesTaxFromSales = pIsExtractSalesTaxFromSales;
  }

  /**
   * <p>Getter for isExtractSalesTaxFromPurchase.</p>
   * @return Boolean
   **/
  public final Boolean getIsExtractSalesTaxFromPurchase() {
    return this.isExtractSalesTaxFromPurchase;
  }

  /**
   * <p>Setter for isExtractSalesTaxFromPurchase.</p>
   * @param pIsExtractSalesTaxFromPurchase reference
   **/
  public final void setIsExtractSalesTaxFromPurchase(
    final Boolean pIsExtractSalesTaxFromPurchase) {
    this.isExtractSalesTaxFromPurchase = pIsExtractSalesTaxFromPurchase;
  }

  /**
   * <p>Geter for currency.</p>
   * @return Currency
   **/
  public final Currency getCurrency() {
    return this.currency;
  }

  /**
   * <p>Setter for currency.</p>
   * @param pCurrency reference
   **/
  public final void setCurrency(final Currency pCurrency) {
    this.currency = pCurrency;
  }

  /**
   * <p>Geter for cogsMethod.</p>
   * @return CogsMethod
   **/
  public final CogsMethod getCogsMethod() {
    return this.cogsMethod;
  }

  /**
   * <p>Setter for cogsMethod.</p>
   * @param pCogsMethod reference
   **/
  public final void setCogsMethod(final CogsMethod pCogsMethod) {
    this.cogsMethod = pCogsMethod;
  }

  /**
   * <p>Getter for balanceStorePeriod.</p>
   * @return EPeriod
   **/
  public final EPeriod getBalanceStorePeriod() {
    return this.balanceStorePeriod;
  }

  /**
   * <p>Setter for balanceStorePeriod.</p>
   * @param pBalanceStorePeriod reference
   **/
  public final void setBalanceStorePeriod(final EPeriod pBalanceStorePeriod) {
    this.balanceStorePeriod = pBalanceStorePeriod;
  }

  /**
   * <p>Geter for description.</p>
   * @return String
   **/
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }

  /**
   * <p>Getter for accEntriesSources.</p>
   * @return List<AccEntriesSourcesLine>
   **/
  public final List<AccEntriesSourcesLine> getAccEntriesSources() {
    return this.accEntriesSources;
  }

  /**
   * <p>Setter for accEntriesSources.</p>
   * @param pAccEntriesSources reference
   **/
  public final void setAccEntriesSources(
    final List<AccEntriesSourcesLine> pAccEntriesSources) {
    this.accEntriesSources = pAccEntriesSources;
  }

  /**
   * <p>Getter for cogsItemSources.</p>
   * @return List<CogsItemSourcesLine>
   **/
  public final List<CogsItemSourcesLine> getCogsItemSources() {
    return this.cogsItemSources;
  }

  /**
   * <p>Setter for cogsItemSources.</p>
   * @param pCogsItemSources reference
   **/
  public final void setCogsItemSources(
    final List<CogsItemSourcesLine> pCogsItemSources) {
    this.cogsItemSources = pCogsItemSources;
  }

  /**
   * <p>Geter for drawMaterialSources.</p>
   * @return List<DrawMaterialSourcesLine>
   **/
  public final List<DrawMaterialSourcesLine> getDrawMaterialSources() {
    return this.drawMaterialSources;
  }

  /**
   * <p>Setter for drawMaterialSources.</p>
   * @param pDrawMaterialSources reference
   **/
  public final void setDrawMaterialSources(
    final List<DrawMaterialSourcesLine> pDrawMaterialSources) {
    this.drawMaterialSources = pDrawMaterialSources;
  }

  /**
   * <p>Getter for roundingMode.</p>
   * @return RoundingMode
   **/
  public final RoundingMode getRoundingMode() {
    return this.roundingMode;
  }

  /**
   * <p>Getter for costPrecision.</p>
   * @return Integer
   **/
  public final Integer getCostPrecision() {
    return this.costPrecision;
  }

  /**
   * <p>Getter for quantityPrecision.</p>
   * @return Integer
   **/
  public final Integer getQuantityPrecision() {
    return this.quantityPrecision;
  }

  /**
   * <p>Setter for quantityPrecision.</p>
   * @param pQuantityPrecision reference
   **/
  public final void setQuantityPrecision(final Integer pQuantityPrecision) {
    this.quantityPrecision = pQuantityPrecision;
  }

  /**
   * <p>Getter for pricePrecision.</p>
   * @return Integer
   **/
  public final Integer getPricePrecision() {
    return this.pricePrecision;
  }

  /**
   * <p>Getter for balancePrecision.</p>
   * @return Integer
   **/
  public final Integer getBalancePrecision() {
    return this.balancePrecision;
  }

  /**
   * <p>Getter for wageTaxesMethod.</p>
   * @return WageTaxesMethod
   **/
  public final WageTaxesMethod getWageTaxesMethod() {
    return this.wageTaxesMethod;
  }

  /**
   * <p>Setter for wageTaxesMethod.</p>
   * @param pWageTaxesMethod reference
   **/
  public final void setWageTaxesMethod(
    final WageTaxesMethod pWageTaxesMethod) {
    this.wageTaxesMethod = pWageTaxesMethod;
  }
}
