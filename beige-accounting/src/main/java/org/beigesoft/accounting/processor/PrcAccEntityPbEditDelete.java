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
import org.beigesoft.persistable.IPersistableBase;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.accounting.service.ISrvAccSettings;

/**
 * <p>Service that retrieve entity, check if it foreign
 * and put into request data for farther editing or confirm delete.
 * Use PrcAccEntityRetrieve for printing.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class PrcAccEntityPbEditDelete<RS, T extends IPersistableBase>
  implements IEntityProcessor<T, Long> {

  /**
   * <p>EntityPb Edit/Confirm delete delegator.</p>
   **/
  private IEntityProcessor<T, Long> prcEntityPbEditDelete;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

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
    pRequestData.setAttribute("accSettings",
      this.srvAccSettings.lazyGetAccSettings(pAddParam));
    return this.prcEntityPbEditDelete
      .process(pAddParam, pEntity, pRequestData);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcEntityPbEditDelete.</p>
   * @return IEntityProcessor<T, Long>
   **/
  public final IEntityProcessor<T, Long> getPrcEntityPbEditDelete() {
    return this.prcEntityPbEditDelete;
  }

  /**
   * <p>Setter for prcEntityPbEditDelete.</p>
   * @param pPrcAccEntityPbEditDelete reference
   **/
  public final void setPrcEntityPbEditDelete(
    final IEntityProcessor<T, Long> pPrcAccEntityPbEditDelete) {
    this.prcEntityPbEditDelete = pPrcAccEntityPbEditDelete;
  }

  /**
   * <p>Getter for srvAccSettings.</p>
   * @return ISrvAccSettings
   **/
  public final ISrvAccSettings getSrvAccSettings() {
    return this.srvAccSettings;
  }

  /**
   * <p>Setter for srvAccSettings.</p>
   * @param pSrvAccSettings reference
   **/
  public final void setSrvAccSettings(final ISrvAccSettings pSrvAccSettings) {
    this.srvAccSettings = pSrvAccSettings;
  }
}
