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
import org.beigesoft.accounting.persistable.IDoc;
import org.beigesoft.accounting.persistable.AccountingEntry;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.accounting.service.ISrvAccEntry;

/**
 * <p>Service that retrieve Acc-Document and (if required) their acc-entries
 * and put into request data for farther printing.
 * These documents are: Wage.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class PrcAccDocRetrieve<RS, T extends IDoc>
  implements IEntityProcessor<T, Long> {

  /**
   * <p>Acc-entity retrieve delegator.</p>
   **/
  private IEntityProcessor<T, Long> prcAccEntityRetrieve;

  /**
   * <p>Business service for accounting entries.</p>
   **/
  private ISrvAccEntry srvAccEntry;

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
    T entity = this.prcAccEntityRetrieve
      .process(pAddParam, pEntity, pRequestData);
    String actionAdd = pRequestData.getParameter("actionAdd");
    if ("full".equals(actionAdd)) {
      pRequestData.setAttribute("accEntries", this.srvAccEntry
        .retrieveAccEntriesFor(pAddParam, entity));
      pRequestData.setAttribute("classAccountingEntry", AccountingEntry.class);
    }
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcAccEntityRetrieve.</p>
   * @return PrcAccEntityRetrieve<T, Long>
   **/
  public final IEntityProcessor<T, Long> getPrcAccEntityRetrieve() {
    return this.prcAccEntityRetrieve;
  }

  /**
   * <p>Setter for prcAccEntityRetrieve.</p>
   * @param pPrcAccEntityRetrieve reference
   **/
  public final void setPrcAccEntityRetrieve(
    final IEntityProcessor<T, Long> pPrcAccEntityRetrieve) {
    this.prcAccEntityRetrieve = pPrcAccEntityRetrieve;
  }

  /**
   * <p>Getter for srvAccEntry.</p>
   * @return ISrvAccEntry
   **/
  public final ISrvAccEntry getSrvAccEntry() {
    return this.srvAccEntry;
  }

  /**
   * <p>Setter for srvAccEntry.</p>
   * @param pSrvAccEntry reference
   **/
  public final void setSrvAccEntry(final ISrvAccEntry pSrvAccEntry) {
    this.srvAccEntry = pSrvAccEntry;
  }
}
