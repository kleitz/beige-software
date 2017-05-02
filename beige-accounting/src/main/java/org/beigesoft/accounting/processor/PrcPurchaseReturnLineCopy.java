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
import org.beigesoft.accounting.persistable.PurchaseReturnLine;
import org.beigesoft.service.IEntityProcessor;

/**
 * <p>Service that make PurchaseReturnLine copy from DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcPurchaseReturnLineCopy<RS>
  implements IEntityProcessor<PurchaseReturnLine, Long> {

  /**
   * <p>Acc-EntityPb Copy delegator.</p>
   **/
  private IEntityProcessor<PurchaseReturnLine, Long> prcAccEntityPbCopy;

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
  public final PurchaseReturnLine process(
    final Map<String, Object> pAddParam,
      final PurchaseReturnLine pEntityPb,
        final IRequestData pRequestData) throws Exception {
    PurchaseReturnLine entity = this.prcAccEntityPbCopy
      .process(pAddParam, pEntityPb, pRequestData);
    entity.setPurchaseInvoiceLine(null);
    entity.setPurchInvLnAppearance(null);
    entity.setItsQuantity(BigDecimal.ZERO);
    entity.setItsTotal(BigDecimal.ZERO);
    entity.setTotalTaxes(BigDecimal.ZERO);
    entity.setSubtotal(BigDecimal.ZERO);
    entity.setTaxesDescription(null);
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccEntityPbCopy.</p>
   * @return PrcAccEntityPbCopy<RS, PurchaseReturnLine, Long>
   **/
  public final IEntityProcessor<PurchaseReturnLine, Long>
    getPrcAccEntityPbCopy() {
    return this.prcAccEntityPbCopy;
  }

  /**
   * <p>Setter for prcAccEntityPbCopy.</p>
   * @param pPrcAccEntityPbCopy reference
   **/
  public final void setPrcAccEntityPbCopy(
    final IEntityProcessor<PurchaseReturnLine, Long> pPrcAccEntityPbCopy) {
    this.prcAccEntityPbCopy = pPrcAccEntityPbCopy;
  }
}
