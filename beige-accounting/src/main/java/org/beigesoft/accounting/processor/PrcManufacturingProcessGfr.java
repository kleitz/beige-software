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
import org.beigesoft.accounting.persistable.ManufacturingProcess;

/**
 * <p>Service that make ManufacturingProcess copy from DB for reverse.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcManufacturingProcessGfr<RS>
  implements IEntityProcessor<ManufacturingProcess, Long> {

  /**
   * <p>Acc-entity copy delegator.</p>
   **/
  private IEntityProcessor<ManufacturingProcess, Long> prcAccDocGetForReverse;

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
  public final ManufacturingProcess process(
    final Map<String, Object> pAddParam,
      final ManufacturingProcess pEntity,
        final IRequestData pRequestData) throws Exception {
    ManufacturingProcess entity = this.prcAccDocGetForReverse
      .process(pAddParam, pEntity, pRequestData);
    entity.setItsQuantity(pEntity.getItsQuantity().negate());
    entity.setTheRest(BigDecimal.ZERO);
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccDocGetForReverse.</p>
   * @return PrcAccDocGetForReverse<RS, ManufacturingProcess, Long>
   **/
  public final IEntityProcessor<ManufacturingProcess, Long>
    getPrcAccDocGetForReverse() {
    return this.prcAccDocGetForReverse;
  }

  /**
   * <p>Setter for prcAccDocGetForReverse.</p>
   * @param pPrcAccDocGetForReverse reference
   **/
  public final void setPrcAccDocGetForReverse(
    final IEntityProcessor<ManufacturingProcess, Long>
      pPrcAccDocGetForReverse) {
    this.prcAccDocGetForReverse = pPrcAccDocGetForReverse;
  }
}
