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
import java.math.BigDecimal;
import java.util.Map;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.Wage;
import org.beigesoft.accounting.persistable.WageLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Business service for Wage Tax Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvWageLine<RS>
  extends ASrvAccEntitySimple<RS, WageLine>
    implements ISrvEntityOwned<WageLine, Wage> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvWageLine() {
    super(WageLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvWageLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase,
      final ISrvAccSettings pSrvAccSettings) {
    super(WageLine.class, pSrvOrm, pSrvAccSettings);
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final WageLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    WageLine entity = new WageLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    Wage itsOwner = new Wage();
    entity.setItsOwner(itsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final WageLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    WageLine entity = getSrvOrm().retrieveCopyEntity(
      WageLine.class, pId);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
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
  public final WageLine retrieveEntity(final Map<String, Object> pAddParam,
    final WageLine pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final WageLine retrieveEntityById(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(WageLine.class, pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final WageLine pEntity) throws Exception {
    if (pEntity.getItsOwner().getHasMadeAccEntries()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_accounted_document");
    }
    getSrvOrm().deleteEntity(WageLine.class, pEntity.getItsId());
    updateOwner(pEntity);
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
    WageLine entity = getSrvOrm()
      .retrieveEntityById(WageLine.class, pId);
    if (entity.getItsOwner().getHasMadeAccEntries()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_accounted_document");
    }
    getSrvOrm().deleteEntity(WageLine.class, entity.getItsId());
    updateOwner(entity);
  }

  /**
   * <p>Insert immutable line into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, Object> pAddParam,
    final WageLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getGrossWage().doubleValue() == 0d) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "total_is_0");
    }
    Wage itsOwner = getSrvOrm().retrieveEntityById(
      Wage.class, pEntity.getItsOwner().getItsId());
    pEntity.setItsOwner(itsOwner);
    if (pEntity.getItsOwner().getHasMadeAccEntries()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_accounted_document");
    }
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
    }
    updateOwner(pEntity);
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner entity itsOwner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final WageLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdOwner) throws Exception {
    WageLine entity = new WageLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    Wage itsOwner = getSrvOrm().retrieveEntityById(
      Wage.class, pIdOwner);
    entity.setItsOwner(itsOwner);
    if (entity.getItsOwner().getHasMadeAccEntries()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_accounted_document");
    }
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final WageLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final Wage pEntityItsOwner) throws Exception {
    WageLine entity = new WageLine();
    entity.setIsNew(true);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsOwner(pEntityItsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner ID itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<WageLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(WageLine.class,
      Wage.class, pIdEntityItsOwner);
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<WageLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final Wage pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(WageLine.class,
      Wage.class, pEntityItsOwner.getItsId());
  }

  //Utils:
  /**
   * <p>Insert immutable line into DB.</p>
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  public final void updateOwner(
    final WageLine pEntity) throws Exception {
    String query =
    "select sum(GROSSWAGE) as GROSSWAGE from " + WageLine.class
      .getSimpleName().toUpperCase() + " where ITSOWNER="
        + pEntity.getItsOwner().getItsId();
    Double total = getSrvDatabase().evalDoubleResult(query, "GROSSWAGE");
    if (total == null) {
      total = 0d;
    }
    pEntity.getItsOwner().setItsTotal(BigDecimal.valueOf(total).setScale(
        getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
    getSrvOrm().updateEntity(pEntity.getItsOwner());
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvDatabase.</p>
   * @return ISrvDatabase
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }
}
