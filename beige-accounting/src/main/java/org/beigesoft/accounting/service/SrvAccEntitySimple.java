package org.beigesoft.accounting.service;

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
import java.util.Map;

import org.beigesoft.model.IHasId;
import org.beigesoft.holder.IAttributes;
import org.beigesoft.service.ISrvEntity;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Simple business service for persistable accounting entity.
.</p>
 *
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class SrvAccEntitySimple<T extends IHasId<?>>
  implements ISrvEntity<T> {

  /**
   * <p>Entity class.</p>
   **/
  private final Class<T> entityClass;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<?> srvOrm;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public SrvAccEntitySimple(final Class<T> pEntityClass) {
    this.entityClass = pEntityClass;
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvAccEntitySimple(final Class<T> pEntityClass,
    final ISrvOrm<?> pSrvOrm,
        final ISrvAccSettings pSrvAccSettings) {
    this.entityClass = pEntityClass;
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
  public final List<T> retrieveList(
    final Map<String, Object> pAddParam) throws Exception {
    return getSrvOrm().retrieveList(this.entityClass);
  }

  /**
   * <p>Retrieve a list of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveListWithConditions(
    final Map<String, Object> pAddParam,
      final String pQueryConditions) throws Exception {
    return getSrvOrm().retrieveListWithConditions(this.entityClass,
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
  public final List<T> retrievePage(final Map<String, Object> pAddParam,
      final Integer pFirst, final Integer pPageSize) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrievePage(this.entityClass,
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
  public final List<T> retrievePageWithConditions(
    final Map<String, Object> pAddParam, final String pQueryConditions,
      final Integer pFirst, final Integer pPageSize) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrievePageWithConditions(this.entityClass,
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
    return getSrvOrm().evalRowCount(this.entityClass);
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
    return getSrvOrm().evalRowCountWhere(this.entityClass, pWhere);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final T createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    T entity = getSrvOrm().createEntity(this.entityClass);
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
  public final T retrieveEntity(final Map<String, Object> pAddParam,
    final T pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(this.entityClass, pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntityById(final Map<String, Object> pAddParam,
    final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(this.entityClass, pId);
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveCopyEntity(final Map<String, Object> pAddParam,
    final Object pId) throws Exception {
    T entity = getSrvOrm().retrieveCopyEntity(this.entityClass, pId);
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
    final Map<String, Object> pAddParam, final T pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
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
    final T pEntity) throws Exception {
    getSrvOrm().deleteEntity(this.entityClass, pEntity.getItsId());
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
    getSrvOrm().deleteEntity(this.entityClass, pId);
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

  //Simple getters and setters:
  /**
   * <p>Geter for entityClass.</p>
   * @return Class<T>
   **/
  public final Class<T> getEntityClass() {
    return this.entityClass;
  }

  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<?>
   **/
  public final ISrvOrm<?> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<?> pSrvOrm) {
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
