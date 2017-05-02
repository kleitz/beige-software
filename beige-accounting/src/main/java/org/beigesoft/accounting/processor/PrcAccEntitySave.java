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
 * <p>Service that save entity into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class PrcAccEntitySave<RS, T extends IHasId<ID>, ID>
  implements IEntityProcessor<T, ID> {

  /**
   * <p>Entity Save delegator.</p>
   **/
  private IEntityProcessor<T, ID> prcEntitySave;

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
    return this.prcEntitySave.process(pAddParam, pEntity, pRequestData);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcEntitySave.</p>
   * @return PrcEntitySave<RS, T, ID>
   **/
  public final IEntityProcessor<T, ID> getPrcEntitySave() {
    return this.prcEntitySave;
  }

  /**
   * <p>Setter for prcEntitySave.</p>
   * @param pPrcEntitySave reference
   **/
  public final void setPrcEntitySave(
    final IEntityProcessor<T, ID> pPrcEntitySave) {
    this.prcEntitySave = pPrcEntitySave;
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
