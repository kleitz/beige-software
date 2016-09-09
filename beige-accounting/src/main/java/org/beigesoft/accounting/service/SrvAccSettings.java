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

import java.util.Map;
import java.util.List;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.AccSettings;
import org.beigesoft.accounting.persistable.AccEntriesSourcesLine;
import org.beigesoft.accounting.persistable.CogsItemSourcesLine;
import org.beigesoft.accounting.persistable.DrawMaterialSourcesLine;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for accounting settings.</p>
 *
 * @author Yury Demidenko
 */
public class SrvAccSettings
  implements ISrvAccSettings {

  /**
   * <p>Current AccSettings.</p>
   **/
  private AccSettings accSettings;

  /**
   * <p>Entity class.</p>
   **/
  private final Class<AccSettings> entityClass =
    AccSettings.class;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<?> srvOrm;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvAccSettings() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   **/
  public SrvAccSettings(final ISrvOrm<?> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Retrieve/get Accounting settings.</p>
   * @return Accounting settings
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized AccSettings lazyGetAccSettings() throws Exception {
    if (this.accSettings == null) {
      retrieveAccSettings();
    }
    return this.accSettings;
  }

  /**
   * <p>Clear Accounting settings to retrieve from
   * database new version.</p>
   **/
  @Override
  public final synchronized void clearAccSettings() {
    this.accSettings = null;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final AccSettings createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    AccSettings lAccSettings = new AccSettings();
    lAccSettings.setIsNew(true);
    return lAccSettings;
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final AccSettings retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "Attempt to copy accounting settings by " + pAddParam.get("user"));
  }

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void saveEntity(final Map<String, ?> pAddParam,
    final AccSettings pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "Attempt to insert accounting settings by " + pAddParam.get("user"));
    } else {
      if (pEntity.getCostPrecision() < 0 || pEntity.getCostPrecision() > 4) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "precision_must_be_from_0_to_4");
      }
      if (pEntity.getPricePrecision() < 0 || pEntity.getPricePrecision() > 4) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "precision_must_be_from_0_to_4");
      }
      if (pEntity.getQuantityPrecision() < 0
        || pEntity.getQuantityPrecision() > 4) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "precision_must_be_from_0_to_4");
      }
      if (pEntity.getBalancePrecision() < 0
        || pEntity.getBalancePrecision() > 4) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "precision_must_be_from_0_to_4");
      }
      getSrvOrm().updateEntity(pEntity);
      retrieveAccSettings();
    }
  }


  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized AccSettings retrieveEntity(
    final Map<String, ?> pAddParam,
      final AccSettings pEntity) throws Exception {
    return lazyGetAccSettings();
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized AccSettings retrieveEntityById(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    return lazyGetAccSettings();
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final AccSettings pEntity) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "Attempt to delete line by " + pAddParam.get("user"));
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
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "Attempt to delete line by " + pAddParam.get("user"));
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param pAddParam additional param
   * @return list of all business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<AccSettings> retrieveList(
    final Map<String, ?> pAddParam) throws Exception {
    return getSrvOrm().retrieveList(this.entityClass);
  }

  /**
   * <p>Retrieve a list of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * or "where ITSDATE>21313211 order by ITSID"
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<AccSettings> retrieveListWithConditions(
    final Map<String, ?> pAddParam,
      final String pQueryConditions) throws Exception {
    return getSrvOrm()
      .retrieveListWithConditions(this.entityClass, pQueryConditions);
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
  public final List<AccSettings> retrievePage(
    final Map<String, ?> pAddParam,
      final Integer pFirst, final Integer pPageSize) throws Exception {
    return getSrvOrm().retrievePage(this.entityClass, pFirst, pPageSize);
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
  public final List<AccSettings> retrievePageWithConditions(
    final Map<String, ?> pAddParam,
      final String pQueryConditions,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    return getSrvOrm().retrievePageWithConditions(this.entityClass,
      pQueryConditions, pFirst, pPageSize);
  }
  /**
   * <p>There is only accounting settings.</p>
   * @param pAddParam additional param
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCount(
    final Map<String, ?> pAddParam) throws Exception {
    return 1;
  }
  /**
   * <p>There is only accounting settings.</p>
   * @param pAddParam additional param
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCountWhere(final Map<String, ?> pAddParam,
    final String pWhere) throws Exception {
    return 1;
  }

  //Utils:
  /**
   * <p>Retrieve Accounting settings from database.</p>
   * @throws Exception - an exception
   **/
  public final synchronized void retrieveAccSettings() throws Exception {
    this.accSettings = getSrvOrm().retrieveEntityById(this.entityClass, 1L);
    if (this.accSettings == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no accounting settings!!!");
    }
    List<DrawMaterialSourcesLine> drawMaterialSources = getSrvOrm()
      .retrieveEntityOwnedlist(DrawMaterialSourcesLine.class,
        AccSettings.class, 1L);
    this.accSettings.setDrawMaterialSources(drawMaterialSources);
    List<CogsItemSourcesLine> cogsItemSources = getSrvOrm()
      .retrieveEntityOwnedlist(CogsItemSourcesLine.class,
        AccSettings.class, 1L);
    this.accSettings.setCogsItemSources(cogsItemSources);
    List<AccEntriesSourcesLine> accEntriesSources = getSrvOrm()
      .retrieveEntityOwnedlist(AccEntriesSourcesLine.class,
        AccSettings.class, 1L);
    this.accSettings.setAccEntriesSources(accEntriesSources);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for entityClass.</p>
   * @return Class<AccSettings>
   **/
  public final Class<AccSettings> getEntityClass() {
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
}
