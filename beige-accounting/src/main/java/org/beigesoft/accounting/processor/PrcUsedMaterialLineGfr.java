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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.IRequestData;
import org.beigesoft.accounting.persistable.UsedMaterialLine;
import org.beigesoft.service.IEntityProcessor;

/**
 * <p>Service that make UsedMaterialLine copy from DB for reverse.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcUsedMaterialLineGfr<RS>
  implements IEntityProcessor<UsedMaterialLine, Long> {

  /**
   * <p>Acc-EntityPb Copy delegator.</p>
   **/
  private IEntityProcessor<UsedMaterialLine, Long> prcAccEntityPbCopy;

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
  public final UsedMaterialLine process(
    final Map<String, Object> pAddParam,
      final UsedMaterialLine pEntityPb,
        final IRequestData pRequestData) throws Exception {
    UsedMaterialLine entity = this.prcAccEntityPbCopy
      .process(pAddParam, pEntityPb, pRequestData);
    if (entity.getReversedId() != null) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_reverse_reversed");
    }
    entity.setReversedId(pEntityPb.getItsId());
    entity.setItsQuantity(entity.getItsQuantity().negate());
    entity.setItsTotal(entity.getItsTotal().negate());
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccEntityPbCopy.</p>
   * @return PrcAccEntityPbCopy<RS, UsedMaterialLine, Long>
   **/
  public final IEntityProcessor<UsedMaterialLine, Long>
    getPrcAccEntityPbCopy() {
    return this.prcAccEntityPbCopy;
  }

  /**
   * <p>Setter for prcAccEntityPbCopy.</p>
   * @param pPrcAccEntityPbCopy reference
   **/
  public final void setPrcAccEntityPbCopy(
    final IEntityProcessor<UsedMaterialLine, Long> pPrcAccEntityPbCopy) {
    this.prcAccEntityPbCopy = pPrcAccEntityPbCopy;
  }
}
