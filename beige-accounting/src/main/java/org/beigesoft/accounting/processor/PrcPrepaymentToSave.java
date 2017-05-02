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
import org.beigesoft.accounting.persistable.PrepaymentTo;

/**
 * <p>Service that save PrepaymentTo into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcPrepaymentToSave<RS> extends APrcAccDocSave<RS, PrepaymentTo> {

  //To override:
  /**
   * <p>Make save preparations before insert/update block if it's need.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pRequestData Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void makeFirstPrepareForSave(final Map<String, Object> pAddParam,
    final PrepaymentTo pEntity,
      final IRequestData pRequestData) throws Exception {
    //BeigeORM refresh:
    pEntity.setAccCash(getSrvOrm()
      .retrieveEntity(pAddParam, pEntity.getAccCash()));
    if (pEntity.getAccCash().getSubaccType() != null
      && pEntity.getSubaccCashId() == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "select_subaccount");
    }
  }

  /**
   * <p>Make other entries include reversing if it's need when save.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pRequestData Request Data
   * @param pIsNew if entity was new
   * @throws Exception - an exception
   **/
  @Override
  public final void makeOtherEntries(final Map<String, Object> pAddParam,
    final PrepaymentTo pEntity, final IRequestData pRequestData,
      final boolean pIsNew) throws Exception {
    // nothing
  }

  /**
   * <p>Check other fraud update e.g. prevent change completed unaccounted
   * manufacturing process.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pRequestData Request Data
   * @param pOldEntity old saved entity
   * @throws Exception - an exception
   **/
  @Override
  public final void checkOtherFraudUpdate(final Map<String, Object> pAddParam,
    final PrepaymentTo pEntity, final IRequestData pRequestData,
      final PrepaymentTo pOldEntity) throws Exception {
    // nothing
  }

  /**
   * <p>Additional check document for ready to account (make acc.entries).</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pRequestData Request Data
   * @throws Exception - an exception if don't
   **/
  @Override
  public final void addCheckIsReadyToAccount(
    final Map<String, Object> pAddParam,
      final PrepaymentTo pEntity,
        final IRequestData pRequestData) throws Exception {
    // nothing
  }
}
