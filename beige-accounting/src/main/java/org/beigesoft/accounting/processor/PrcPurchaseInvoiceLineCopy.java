package org.beigesoft.accounting.processor;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;
import java.math.BigDecimal;

import org.beigesoft.model.IRequestData;
import org.beigesoft.accounting.persistable.PurchaseInvoiceLine;
import org.beigesoft.service.IEntityProcessor;

/**
 * <p>Service that make PurchaseInvoiceLine copy from DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcPurchaseInvoiceLineCopy<RS>
  implements IEntityProcessor<PurchaseInvoiceLine, Long> {

  /**
   * <p>Acc-EntityPb Copy delegator.</p>
   **/
  private IEntityProcessor<PurchaseInvoiceLine, Long> prcAccEntityPbCopy;

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
      final PurchaseInvoiceLine pEntityPb,
        final IRequestData pRequestData) throws Exception {
    PurchaseInvoiceLine entity = this.prcAccEntityPbCopy
      .process(pAddParam, pEntityPb, pRequestData);
    entity.setItsQuantity(BigDecimal.ZERO);
    entity.setItsCost(BigDecimal.ZERO);
    entity.setItsTotal(BigDecimal.ZERO);
    entity.setTotalTaxes(BigDecimal.ZERO);
    entity.setSubtotal(BigDecimal.ZERO);
    entity.setTaxesDescription(null);
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccEntityPbCopy.</p>
   * @return PrcAccEntityPbCopy<RS, PurchaseInvoiceLine, Long>
   **/
  public final IEntityProcessor<PurchaseInvoiceLine, Long>
    getPrcAccEntityPbCopy() {
    return this.prcAccEntityPbCopy;
  }

  /**
   * <p>Setter for prcAccEntityPbCopy.</p>
   * @param pPrcAccEntityPbCopy reference
   **/
  public final void setPrcAccEntityPbCopy(
    final IEntityProcessor<PurchaseInvoiceLine, Long> pPrcAccEntityPbCopy) {
    this.prcAccEntityPbCopy = pPrcAccEntityPbCopy;
  }
}
