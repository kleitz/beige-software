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

import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;

import org.beigesoft.model.IRequestData;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.accounting.persistable.PaymentFrom;

/**
 * <p>Service that make PaymentFrom copy from DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcPaymentFromCopy<RS>
  implements IEntityProcessor<PaymentFrom, Long> {

  /**
   * <p>Acc-entity copy delegator.</p>
   **/
  private IEntityProcessor<PaymentFrom, Long> prcAccEntityPbWithSubaccCopy;

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
  public final PaymentFrom process(
    final Map<String, Object> pAddParam,
      final PaymentFrom pEntity,
        final IRequestData pRequestData) throws Exception {
    PaymentFrom entity = this.prcAccEntityPbWithSubaccCopy
      .process(pAddParam, pEntity, pRequestData);
    entity.setReversedId(null);
    entity.setItsTotal(BigDecimal.ZERO);
    entity.setItsDate(new Date());
    entity.setHasMadeAccEntries(false);
    entity.setSalesInvoice(null);
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccEntityPbWithSubaccCopy.</p>
   * @return PrcAccEntityPbWithSubaccCopy<RS, PaymentFrom, Long>
   **/
  public final IEntityProcessor<PaymentFrom, Long>
    getPrcAccEntityPbWithSubaccCopy() {
    return this.prcAccEntityPbWithSubaccCopy;
  }

  /**
   * <p>Setter for prcAccEntityPbWithSubaccCopy.</p>
   * @param pPrcAccEntityPbWithSubaccCopy reference
   **/
  public final void setPrcAccEntityPbWithSubaccCopy(
    final IEntityProcessor<PaymentFrom, Long> pPrcAccEntityPbWithSubaccCopy) {
    this.prcAccEntityPbWithSubaccCopy = pPrcAccEntityPbWithSubaccCopy;
  }
}
