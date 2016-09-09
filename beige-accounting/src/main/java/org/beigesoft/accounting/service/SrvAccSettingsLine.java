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
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.holder.IAttributes;

/**
 * <p>Simple business service for accounting settings line.</p>
 *
 * @param <T> entity type
 * @param <O> owner entity type
 * @author Yury Demidenko
 */
public class SrvAccSettingsLine<T extends IHasId<?>, O extends IHasId<?>>
  implements ISrvEntityOwned<T, O> {

  /**
   * <p>Entity owner class.</p>
   **/
  private final Class<O> ownerClass;

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
   * @param pOwnerClass Owner Class
   **/
  public SrvAccSettingsLine(final Class<T> pEntityClass,
    final Class<O> pOwnerClass) {
    this.entityClass = pEntityClass;
    this.ownerClass = pOwnerClass;
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pOwnerClass Owner Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvAccSettingsLine(final Class<T> pEntityClass,
    final Class<O> pOwnerClass, final ISrvOrm<?> pSrvOrm,
      final ISrvAccSettings pSrvAccSettings) {
    this.entityClass = pEntityClass;
    this.srvAccSettings = pSrvAccSettings;
    this.srvOrm = pSrvOrm;
    this.ownerClass = pOwnerClass;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final T createEntity(final Map<String, ?> pAddParam) throws Exception {
    T entity = this.srvOrm.createEntity(this.entityClass);
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
  public final T retrieveEntity(final Map<String, ?> pAddParam,
    final T pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return this.srvOrm.retrieveEntityById(this.entityClass, pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntityById(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return this.srvOrm.retrieveEntityById(this.entityClass, pId);
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveCopyEntity(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    T entity = this.srvOrm.retrieveCopyEntity(this.entityClass, pId);
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
  public final void saveEntity(final Map<String, ?> pAddParam, final T pEntity,
    final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      this.srvOrm.insertEntity(pEntity);
    } else {
      this.srvOrm.updateEntity(pEntity);
    }
    srvAccSettings.clearAccSettings();
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final T pEntity) throws Exception {
    this.srvOrm.deleteEntity(this.entityClass, pEntity.getItsId());
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    this.srvOrm.deleteEntity(this.entityClass, pId);
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param pAddParam additional param
   * @return list of all business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveList(
    final Map<String, ?> pAddParam) throws Exception {
    return this.srvOrm.retrieveList(this.entityClass);
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
    final Map<String, ?> pAddParam,
      final String pQueryConditions) throws Exception {
    return this.srvOrm.retrieveListWithConditions(this.entityClass,
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
  public final List<T> retrievePage(
    final Map<String, ?> pAddParam,
      final Integer pFirst, final Integer pPageSize) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return this.srvOrm.retrievePage(this.entityClass, pFirst, pPageSize);
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCount(
    final Map<String, ?> pAddParam) throws Exception {
    return this.srvOrm.evalRowCount(this.entityClass);
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
    final Map<String, ?> pAddParam,
      final String pQueryConditions,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return this.srvOrm.retrievePageWithConditions(this.entityClass,
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
  public final Integer evalRowCountWhere(final Map<String, ?> pAddParam,
    final String pWhere) throws Exception {
    return this.srvOrm.evalRowCountWhere(this.entityClass, pWhere);
  }

  /**
   * <p>Create entity with its owner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdEntityOwner entity owner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final T createEntityWithOwnerById(final Map<String, ?> pAddParam,
    final Object pIdEntityOwner) throws Exception {
    T entity = getSrvOrm().createEntityWithOwner(getEntityClass(),
      this.ownerClass, pIdEntityOwner);
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Create entity with its owner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pEntityOwner owner
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final T createEntityWithOwner(final Map<String, ?> pAddParam,
    final O pEntityOwner) throws Exception {
    T entity = getSrvOrm().createEntityWithOwner(getEntityClass(),
      this.ownerClass, pEntityOwner.getItsId());
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve owned list of entities for owner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdEntityOwner ID owner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveOwnedListById(final Map<String, ?> pAddParam,
    final Object pIdEntityOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(getEntityClass(),
      this.ownerClass, pIdEntityOwner);
  }

  /**
   * <p>Retrieve owned list of entities for owner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pEntityOwner owner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveOwnedList(final Map<String, ?> pAddParam,
    final O pEntityOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(getEntityClass(),
      this.ownerClass, pEntityOwner.getItsId());
  }

  //Utils:
  /**
   * <p>Added accounting settings to attributes.</p>
   * @param pAddParam additional param
   * @throws Exception - an exception
   */
  public final void addAccSettingsIntoAttrs(
    final Map<String, ?> pAddParam) throws Exception {
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
   * @return ISrvOrm
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

  /**
   * <p>Geter for ownerClass.</p>
   * @return final Class<T>
   **/
  public final Class<O> getOwnerClass() {
    return this.ownerClass;
  }
}
