package org.beigesoft.accounting.processor;

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

import java.util.Map;
import java.util.List;
import java.math.BigDecimal;

import org.beigesoft.model.IRequestData;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.accounting.persistable.PurchaseInvoiceLine;
import org.beigesoft.accounting.persistable.PurchaseInvoice;
import org.beigesoft.accounting.service.ISrvWarehouseEntry;
import org.beigesoft.accounting.service.ISrvAccSettings;

/**
 * <p>Service that save PurchaseInvoiceLine into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcPurchaseInvoiceLineSave<RS>
  implements IEntityProcessor<PurchaseInvoiceLine, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>It makes total for owner.</p>
   **/
  private UtlPurchaseGoodsServiceLine<RS> utlPurchaseGoodsServiceLine;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Process entity request.</p>
   * @param pAddParam additional param, e.g. return this line's
   * document in "nextEntity" for farther process
   * @param pRequestData Request Data
   * @param pEntity Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final PurchaseInvoiceLine process(
    final Map<String, Object> pAddParam,
      final PurchaseInvoiceLine pEntity,
        final IRequestData pRequestData) throws Exception {
    if (pEntity.getIsNew()) {
      if (pEntity.getItsQuantity().doubleValue() <= 0
        && pEntity.getReversedId() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "quantity_less_or_equal_zero::" + pAddParam.get("user"));
      }
      if (pEntity.getItsCost().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "cost_less_or_eq_zero::" + pAddParam.get("user"));
      }
      // Beige-Orm refresh:
      pEntity.setInvItem(getSrvOrm()
        .retrieveEntity(pAddParam, pEntity.getInvItem()));
      // optimistic locking (dirty check):
      pEntity.setItsOwner(getSrvOrm()
        .retrieveEntity(pAddParam, pEntity.getItsOwner()));
      Long ownerVersion = Long.valueOf(pRequestData
        .getParameter(PurchaseInvoice.class.getSimpleName() + ".ownerVersion"));
      pEntity.getItsOwner().setItsVersion(ownerVersion);
      if (!(InvItem.MATERIAL_ID.equals(pEntity.getInvItem().getItsType()
        .getItsId()) || InvItem.MERCHANDISE_ID.equals(pEntity.getInvItem()
          .getItsType().getItsId()))) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "type_must_be_material_or_merchandise::" + pAddParam.get("user"));
      }
      //rounding:
      pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
        getSrvAccSettings().lazyGetAccSettings(pAddParam)
          .getQuantityPrecision(), getSrvAccSettings()
            .lazyGetAccSettings(pAddParam).getRoundingMode()));
      pEntity.setItsCost(pEntity.getItsCost().setScale(getSrvAccSettings()
        .lazyGetAccSettings(pAddParam).getCostPrecision(), getSrvAccSettings()
          .lazyGetAccSettings(pAddParam).getRoundingMode()));
      pEntity.setSubtotal(pEntity.getItsTotal().setScale(getSrvAccSettings()
        .lazyGetAccSettings(pAddParam).getPricePrecision(), getSrvAccSettings()
          .lazyGetAccSettings(pAddParam).getRoundingMode()));
      pEntity.setTheRest(pEntity.getItsQuantity());
      BigDecimal totalTaxes = BigDecimal.ZERO;
      String taxesDescription = "";
      if (getSrvAccSettings().lazyGetAccSettings(pAddParam)
        .getIsExtractSalesTaxFromPurchase()
          && pEntity.getInvItem().getTaxCategory() != null) {
        InvItemTaxCategoryLine iitcLn = new InvItemTaxCategoryLine();
        iitcLn.setItsOwner(pEntity.getInvItem().getTaxCategory());
        List<InvItemTaxCategoryLine> iitcll = getSrvOrm()
          .retrieveListForField(pAddParam, iitcLn, "itsOwner");
        BigDecimal bigDecimal100 = new BigDecimal("100.00");
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (InvItemTaxCategoryLine iitcl : iitcll) {
          if (ETaxType.SALES_TAX_OUTITEM.equals(iitcl.getTax().getItsType())
            || ETaxType.SALES_TAX_INITEM.equals(iitcl.getTax().getItsType())) {
            BigDecimal addTx = pEntity.getSubtotal().multiply(iitcl
              .getItsPercentage()).divide(bigDecimal100, getSrvAccSettings()
                .lazyGetAccSettings(pAddParam).getPricePrecision(),
                  getSrvAccSettings()
                    .lazyGetAccSettings(pAddParam).getRoundingMode());
            totalTaxes = totalTaxes.add(addTx);
            if (i++ > 0) {
              sb.append(", ");
            }
            sb.append(iitcl.getTax().getItsName() + " "
              + iitcl.getItsPercentage() + "%=" + addTx);
          }
        }
        taxesDescription = sb.toString();
      }
      pEntity.setTaxesDescription(taxesDescription);
      pEntity.setTotalTaxes(totalTaxes);
      pEntity.setItsTotal(pEntity.getSubtotal().add(totalTaxes));
      if (pEntity.getReversedId() != null) {
        pEntity.setTheRest(BigDecimal.ZERO);
      }
      getSrvOrm().insertEntity(pAddParam, pEntity);
      if (pEntity.getReversedId() != null) {
        PurchaseInvoiceLine reversed = getSrvOrm().retrieveEntityById(
          pAddParam, PurchaseInvoiceLine.class, pEntity.getReversedId());
        if (reversed.getReversedId() != null) {
          throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
            "attempt_to_reverse_reversed::" + pAddParam.get("user"));
        }
        if (!reversed.getItsQuantity().equals(reversed.getTheRest())) {
          throw new ExceptionWithCode(ExceptionWithCode
            .WRONG_PARAMETER, "where_is_withdrawals_from_this_source::"
              + pAddParam.get("user"));
        }
        reversed.setTheRest(BigDecimal.ZERO);
        reversed.setReversedId(pEntity.getItsId());
        getSrvOrm().updateEntity(pAddParam, reversed);
      }
      srvWarehouseEntry.load(pAddParam, pEntity, pEntity.getWarehouseSite());
      this.utlPurchaseGoodsServiceLine
        .updateOwner(pAddParam, pEntity.getItsOwner());
      pAddParam.put("nextEntity", pEntity.getItsOwner());
      pAddParam.put("nameOwnerEntity", PurchaseInvoice.class.getSimpleName());
      pRequestData.setAttribute("accSettings",
        this.srvAccSettings.lazyGetAccSettings(pAddParam));
      return null;
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "edit_not_allowed::" + pAddParam.get("user"));
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for srvAccSettings.</p>
   * @return ISrvAccSettings
   **/
  public final ISrvAccSettings getSrvAccSettings() {
    return this.srvAccSettings;
  }

  /**
   * <p>Setter for srvAccSettings.</p>
   * @param pSrvAccSettings reference
   **/
  public final void setSrvAccSettings(final ISrvAccSettings pSrvAccSettings) {
    this.srvAccSettings = pSrvAccSettings;
  }

  /**
   * <p>Getter for utlPurchaseGoodsServiceLine.</p>
   * @return UtlPurchaseGoodsServiceLine<RS>
   **/
  public final UtlPurchaseGoodsServiceLine<RS>
    getUtlPurchaseGoodsServiceLine() {
    return this.utlPurchaseGoodsServiceLine;
  }

  /**
   * <p>Setter for utlPurchaseGoodsServiceLine.</p>
   * @param pUtlPurchaseGoodsServiceLine reference
   **/
  public final void setUtlPurchaseGoodsServiceLine(
    final UtlPurchaseGoodsServiceLine<RS> pUtlPurchaseGoodsServiceLine) {
    this.utlPurchaseGoodsServiceLine = pUtlPurchaseGoodsServiceLine;
  }

  /**
   * <p>Geter for srvWarehouseEntry.</p>
   * @return ISrvWarehouseEntry
   **/
  public final ISrvWarehouseEntry getSrvWarehouseEntry() {
    return this.srvWarehouseEntry;
  }

  /**
   * <p>Setter for srvWarehouseEntry.</p>
   * @param pSrvWarehouseEntry reference
   **/
  public final void setSrvWarehouseEntry(
    final ISrvWarehouseEntry pSrvWarehouseEntry) {
    this.srvWarehouseEntry = pSrvWarehouseEntry;
  }
}
