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
import org.beigesoft.accounting.persistable.Manufacture;

/**
 * <p>Service that make Manufacture copy from DB for reverse.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcManufactureGfr<RS>
  implements IEntityProcessor<Manufacture, Long> {

  /**
   * <p>Acc-entity copy delegator.</p>
   **/
  private IEntityProcessor<Manufacture, Long> prcAccDocGetForReverse;

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
  public final Manufacture process(
    final Map<String, Object> pAddParam,
      final Manufacture pEntity,
        final IRequestData pRequestData) throws Exception {
    Manufacture entity = this.prcAccDocGetForReverse
      .process(pAddParam, pEntity, pRequestData);
    entity.setItsQuantity(pEntity.getItsQuantity().negate());
    entity.setTheRest(BigDecimal.ZERO);
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccDocGetForReverse.</p>
   * @return PrcAccDocGetForReverse<RS, Manufacture, Long>
   **/
  public final IEntityProcessor<Manufacture, Long>
    getPrcAccDocGetForReverse() {
    return this.prcAccDocGetForReverse;
  }

  /**
   * <p>Setter for prcAccDocGetForReverse.</p>
   * @param pPrcAccDocGetForReverse reference
   **/
  public final void setPrcAccDocGetForReverse(
    final IEntityProcessor<Manufacture, Long>
      pPrcAccDocGetForReverse) {
    this.prcAccDocGetForReverse = pPrcAccDocGetForReverse;
  }
}
