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
import org.beigesoft.model.IHasId;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.accounting.service.ISrvAccSettings;

/**
 * <p>Service that retrieve entity and put into request
 * data for farther editing (or print or confirm delete).</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class PrcAccEntityRetrieve<RS, T extends IHasId<ID>, ID>
  implements IEntityProcessor<T, ID> {

  /**
   * <p>Entity retrieve delegator.</p>
   **/
  private IEntityProcessor<T, ID> prcEntityRetrieve;

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
    return this.prcEntityRetrieve.process(pAddParam, pEntity, pRequestData);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcEntityRetrieve.</p>
   * @return PrcEntityRetrieve<RS, T, ID>
   **/
  public final IEntityProcessor<T, ID> getPrcEntityRetrieve() {
    return this.prcEntityRetrieve;
  }

  /**
   * <p>Setter for prcEntityRetrieve.</p>
   * @param pPrcEntityRetrieve reference
   **/
  public final void setPrcEntityRetrieve(
    final IEntityProcessor<T, ID> pPrcEntityRetrieve) {
    this.prcEntityRetrieve = pPrcEntityRetrieve;
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
