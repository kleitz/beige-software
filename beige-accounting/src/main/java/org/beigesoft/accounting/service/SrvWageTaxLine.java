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
import org.beigesoft.accounting.persistable.WageTaxLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for Wage Tax Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvWageTaxLine<RS>
  extends ASrvAccEntitySimple<RS, WageTaxLine>
    implements ISrvEntityOwned<WageTaxLine, Wage> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvWageTaxLine() {
    super(WageTaxLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvWageTaxLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase,
      final ISrvAccSettings pSrvAccSettings) {
    super(WageTaxLine.class, pSrvOrm, pSrvAccSettings);
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final WageTaxLine createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    WageTaxLine entity = new WageTaxLine();
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
  public final WageTaxLine retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    WageTaxLine entity = getSrvOrm().retrieveCopyEntity(
      WageTaxLine.class, pId);
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
  public final WageTaxLine retrieveEntity(final Map<String, ?> pAddParam,
    final WageTaxLine pEntity) throws Exception {
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
  public final WageTaxLine retrieveEntityById(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(WageTaxLine.class, pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final WageTaxLine pEntity) throws Exception {
    if (pEntity.getItsOwner().getHasMadeAccEntries()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_accounted_document");
    }
    getSrvOrm().deleteEntity(WageTaxLine.class, pEntity.getItsId());
    updateOwner(pEntity);
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
    WageTaxLine entity = getSrvOrm()
      .retrieveEntityById(WageTaxLine.class, pId);
    if (entity.getItsOwner().getHasMadeAccEntries()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_accounted_document");
    }
    getSrvOrm().deleteEntity(WageTaxLine.class, entity.getItsId());
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
  public final void saveEntity(final Map<String, ?> pAddParam,
    final WageTaxLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getItsTotal().doubleValue() == 0d) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "total_is_0");
    }
    if (pEntity.getItsPercentage().doubleValue() <= 0d) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "percentage_less_eq_0");
    }
    Wage itsOwner = getSrvOrm().retrieveEntityById(
      Wage.class, pEntity.getItsOwner().getItsId());
    pEntity.setItsOwner(itsOwner);
    if (pEntity.getItsOwner().getHasMadeAccEntries()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_accounted_document");
    }
    BigDecimal taxDue = pEntity.getItsOwner().getItsTotal()
      .subtract(pEntity.getAllowance()).multiply(pEntity
        .getItsPercentage()).divide(BigDecimal.valueOf(100),
          getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
            getSrvAccSettings().lazyGetAccSettings().getRoundingMode())
              .add(pEntity.getPlusAmount());
    if (Math.abs(taxDue.doubleValue() - pEntity.getItsTotal()
      .doubleValue()) > 1d) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "total_does_not_conform_percentage");
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
  public final WageTaxLine createEntityWithOwnerById(
    final Map<String, ?> pAddParam,
      final Object pIdOwner) throws Exception {
    WageTaxLine entity = new WageTaxLine();
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
  public final WageTaxLine createEntityWithOwner(
    final Map<String, ?> pAddParam,
      final Wage pEntityItsOwner) throws Exception {
    WageTaxLine entity = new WageTaxLine();
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
  public final List<WageTaxLine> retrieveOwnedListById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(WageTaxLine.class,
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
  public final List<WageTaxLine> retrieveOwnedList(
    final Map<String, ?> pAddParam,
      final Wage pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(WageTaxLine.class,
      Wage.class, pEntityItsOwner.getItsId());
  }

  //Utils:

  /**
   * <p>Insert immutable line into DB.</p>
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  public final void updateOwner(
    final WageTaxLine pEntity) throws Exception {
    String query =
    "select sum(ITSTOTAL) as ITSTOTAL from WAGETAXLINE"
      + " join TAX on TAX.ITSID = WAGETAXLINE.TAX"
        + " where TAX.ITSTYPE=3 and ITSOWNER="
          + pEntity.getItsOwner().getItsId();
    Double total = getSrvDatabase().evalDoubleResult(query, "ITSTOTAL");
    if (total == null) {
      total = 0d;
    }
    pEntity.getItsOwner().setTotalTaxesEmployee(BigDecimal.valueOf(total)
      .setScale(getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
    query =
    "select sum(ITSTOTAL) as ITSTOTAL from WAGETAXLINE"
      + " join TAX on TAX.ITSID = WAGETAXLINE.TAX"
        + " where TAX.ITSTYPE=4 and ITSOWNER="
          + pEntity.getItsOwner().getItsId();
    total = getSrvDatabase().evalDoubleResult(query, "ITSTOTAL");
    if (total == null) {
      total = 0d;
    }
    pEntity.getItsOwner().setTotalTaxesEmployer(BigDecimal.valueOf(total)
      .setScale(getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
    pEntity.getItsOwner().setNetWage(pEntity.getItsOwner()
      .getItsTotal().subtract(pEntity.getItsOwner()
        .getTotalTaxesEmployee()));
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
