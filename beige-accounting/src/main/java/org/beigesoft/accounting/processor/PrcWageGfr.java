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

import org.beigesoft.model.IRequestData;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.accounting.persistable.Wage;

/**
 * <p>Service that make Wage copy from DB for reverse.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcWageGfr<RS>
  implements IEntityProcessor<Wage, Long> {

  /**
   * <p>Acc-entity copy delegator.</p>
   **/
  private IEntityProcessor<Wage, Long> prcAccDocGetForReverse;

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
    Wage entity = this.prcAccDocGetForReverse
      .process(pAddParam, pEntity, pRequestData);
    entity.setTotalTaxesEmployee(pEntity.getTotalTaxesEmployee().negate());
    entity.setTotalTaxesEmployer(pEntity.getTotalTaxesEmployer().negate());
    entity.setNetWage(pEntity.getNetWage().negate());
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccDocGetForReverse.</p>
   * @return PrcAccDocGetForReverse<RS, Wage, Long>
   **/
  public final IEntityProcessor<Wage, Long>
    getPrcAccDocGetForReverse() {
    return this.prcAccDocGetForReverse;
  }

  /**
   * <p>Setter for prcAccDocGetForReverse.</p>
   * @param pPrcAccDocGetForReverse reference
   **/
  public final void setPrcAccDocGetForReverse(
    final IEntityProcessor<Wage, Long> pPrcAccDocGetForReverse) {
    this.prcAccDocGetForReverse = pPrcAccDocGetForReverse;
  }
}
