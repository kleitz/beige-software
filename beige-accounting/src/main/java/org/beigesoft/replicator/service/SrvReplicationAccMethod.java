package org.beigesoft.replicator.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.ReplicationAccMethod;
import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.holder.IAttributes;
import org.beigesoft.service.ISrvEntity;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.handler.IHandlerModelChanged;

/**
 * <p>Simple business service for ReplicationAccMethod.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvReplicationAccMethod<RS>
  implements ISrvEntity<ReplicationAccMethod> {

  /**
   * <p>ReplicationAccMethod Changed Handlers.</p>
   **/
  private Set<IHandlerModelChanged<ReplicationAccMethod>>
    replAccMethChangedHandlers =
      new HashSet<IHandlerModelChanged<ReplicationAccMethod>>();

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvReplicationAccMethod(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings) {
    this.srvOrm = pSrvOrm;
    this.srvAccSettings = pSrvAccSettings;
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param pAddParam additional param
   * @return list of all business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<ReplicationAccMethod> retrieveList(
    final Map<String, Object> pAddParam) throws Exception {
    return getSrvOrm().retrieveList(ReplicationAccMethod.class);
  }

  /**
   * <p>Retrieve a list of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<ReplicationAccMethod> retrieveListWithConditions(
    final Map<String, Object> pAddParam,
      final String pQueryConditions) throws Exception {
    return getSrvOrm().retrieveListWithConditions(ReplicationAccMethod.class,
      pQueryConditions);
  }

  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<ReplicationAccMethod> retrievePage(
    final Map<String, Object> pAddParam,
      final Integer pFirst, final Integer pPageSize) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrievePage(ReplicationAccMethod.class,
      pFirst, pPageSize);
  }

  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<ReplicationAccMethod> retrievePageWithConditions(
    final Map<String, Object> pAddParam, final String pQueryConditions,
      final Integer pFirst, final Integer pPageSize) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrievePageWithConditions(ReplicationAccMethod.class,
      pQueryConditions, pFirst, pPageSize);
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCount(
    final Map<String, Object> pAddParam) throws Exception {
    return getSrvOrm().evalRowCount(ReplicationAccMethod.class);
  }
  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCountWhere(final Map<String, Object> pAddParam,
    final String pWhere) throws Exception {
    return getSrvOrm().evalRowCountWhere(ReplicationAccMethod.class, pWhere);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final ReplicationAccMethod createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    ReplicationAccMethod entity = getSrvOrm()
      .createEntity(ReplicationAccMethod.class);
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final ReplicationAccMethod retrieveEntity(
    final Map<String, Object> pAddParam,
      final ReplicationAccMethod pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm()
      .retrieveEntityById(ReplicationAccMethod.class, pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final ReplicationAccMethod retrieveEntityById(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(ReplicationAccMethod.class, pId);
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final ReplicationAccMethod retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    ReplicationAccMethod entity = getSrvOrm()
      .retrieveCopyEntity(ReplicationAccMethod.class, pId);
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(
    final Map<String, Object> pAddParam, final ReplicationAccMethod pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getRequestedDatabaseId() == getSrvOrm().getIdDatabase()) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "requested_database_must_be_different");
    }
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
    }
    for (IHandlerModelChanged<ReplicationAccMethod> replAccMethChangedHandler
      : this.replAccMethChangedHandlers) {
      replAccMethChangedHandler.handleModelChanged(pEntity);
    }
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final ReplicationAccMethod pEntity) throws Exception {
    getSrvOrm().deleteEntity(ReplicationAccMethod.class, pEntity.getItsId());
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final Object pId) throws Exception {
    getSrvOrm().deleteEntity(ReplicationAccMethod.class, pId);
  }

  //Utils:
  /**
   * <p>Added accounting settings to attributes.</p>
   * @param pAddParam additional param
   * @throws Exception - an exception
   */
  public final void addAccSettingsIntoAttrs(
    final Map<String, Object> pAddParam) throws Exception {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("accSettings", srvAccSettings.lazyGetAccSettings());
  }

  /**
   * <p>Added ReplicationAccMethod Changed Handler.</p>
   * @param pReplAccMethChngHandler IHandlerModelChanged<ReplicationAccMethod>
   */
  public final void addReplAccMethChangedHandler(
    final IHandlerModelChanged<ReplicationAccMethod> pReplAccMethChngHandler) {
    this.replAccMethChangedHandlers.add(pReplAccMethChngHandler);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Geter for srvAccSettings.</p>
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
