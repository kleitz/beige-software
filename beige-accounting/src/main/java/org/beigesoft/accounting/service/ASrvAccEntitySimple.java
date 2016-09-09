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
 * <p>Simple base business service for accounting entity.
 * It put ISrvAccSettings into request attributes.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @author Yury Demidenko
 */
public abstract class ASrvAccEntitySimple<RS, T extends IHasId<?>>
  implements ISrvEntity<T> {

  /**
   * <p>Entity class.</p>
   **/
  private final Class<T> entityClass;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public ASrvAccEntitySimple(final Class<T> pEntityClass) {
    this.entityClass = pEntityClass;
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public ASrvAccEntitySimple(final Class<T> pEntityClass,
    final ISrvOrm<RS> pSrvOrm,
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
    final Map<String, ?> pAddParam) throws Exception {
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
    final Map<String, ?> pAddParam,
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
  public final List<T> retrievePage(final Map<String, ?> pAddParam,
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
    final Map<String, ?> pAddParam, final String pQueryConditions,
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
    final Map<String, ?> pAddParam) throws Exception {
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
  public final Integer evalRowCountWhere(final Map<String, ?> pAddParam,
    final String pWhere) throws Exception {
    return getSrvOrm().evalRowCountWhere(this.entityClass, pWhere);
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
