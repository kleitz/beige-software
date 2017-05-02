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
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.accounting.persistable.base.ADocWithTaxes;

/**
 * <p>Service that make document copy from DB and prepare for reversing.
 * Those documents are: PurchaseInvoice, SalesInvoice, SalesReturn,
 * PurchaseReturn.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class PrcAccDocWithTaxesGetForReverse<RS, T extends ADocWithTaxes>
  implements IEntityProcessor<T, Long> {

  /**
   * <p>Doc reverse delegator.</p>
   **/
  private IEntityProcessor<T, Long> prcDocReverse;

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
  public final T process(
    final Map<String, Object> pAddParam,
      final T pEntity, final IRequestData pRequestData) throws Exception {
    T entity = this.prcDocReverse.process(pAddParam, pEntity, pRequestData);
    entity.setTotalTaxes(BigDecimal.ZERO);
    entity.setSubtotal(BigDecimal.ZERO);
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcDocReverse.</p>
   * @return PrcDocReverse<RS, T, Long>
   **/
  public final IEntityProcessor<T, Long> getPrcDocReverse() {
    return this.prcDocReverse;
  }

  /**
   * <p>Setter for prcDocReverse.</p>
   * @param pPrcDocReverse reference
   **/
  public final void setPrcDocReverse(
    final IEntityProcessor<T, Long> pPrcDocReverse) {
    this.prcDocReverse = pPrcDocReverse;
  }
}
