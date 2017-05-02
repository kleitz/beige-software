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
import org.beigesoft.accounting.persistable.Wage;

/**
 * <p>Service that make Wage copy from DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcWageCopy<RS>
  implements IEntityProcessor<Wage, Long> {

  /**
   * <p>Acc-entity copy delegator.</p>
   **/
  private IEntityProcessor<Wage, Long> prcAccEntityPbCopy;

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
  public final Wage process(
    final Map<String, Object> pAddParam,
      final Wage pEntity,
        final IRequestData pRequestData) throws Exception {
    Wage entity = this.prcAccEntityPbCopy
      .process(pAddParam, pEntity, pRequestData);
    entity.setReversedId(null);
    entity.setItsTotal(BigDecimal.ZERO);
    entity.setItsDate(new Date());
    entity.setHasMadeAccEntries(false);
    entity.setTotalTaxesEmployer(BigDecimal.ZERO);
    entity.setTotalTaxesEmployee(BigDecimal.ZERO);
    entity.setNetWage(BigDecimal.ZERO);
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccEntityPbCopy.</p>
   * @return PrcAccEntityPbCopy<RS, Wage, Long>
   **/
  public final IEntityProcessor<Wage, Long>
    getPrcAccEntityPbCopy() {
    return this.prcAccEntityPbCopy;
  }

  /**
   * <p>Setter for prcAccEntityPbCopy.</p>
   * @param pPrcAccEntityPbCopy reference
   **/
  public final void setPrcAccEntityPbCopy(
    final IEntityProcessor<Wage, Long> pPrcAccEntityPbCopy) {
    this.prcAccEntityPbCopy = pPrcAccEntityPbCopy;
  }
}
