package org.beigesoft.replicator.filter;

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
import java.util.Date;

import org.beigesoft.delegate.IDelegator;
import org.beigesoft.delegate.IDelegateEvalExt;
import org.beigesoft.handler.IHandlerModelChanged;
import org.beigesoft.accounting.persistable.AccountingEntry;
import org.beigesoft.accounting.persistable.ReplicationAccMethod;
import org.beigesoft.accounting.persistable.ReplExcludeAccountsCredit;
import org.beigesoft.accounting.persistable.ReplExcludeAccountsDebit;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Interactive filter of accounting entries.
 * User can elect accounts to avoid they replication.
 * It also prepares database after import.
 * Database replication from tax to market accounting specification #1.
 * It's untransactional service. Transaction must be started.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FilterAvoidAccDebtCredit<RS> implements IFilterEntities,
  IHandlerModelChanged<ReplicationAccMethod>, IDelegateEvalExt<Date>,
    IDelegator {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Usually FilterPersistableBaseImmutable.</p>
   **/
  private IFilterEntities filterId;

  /**
   * <p>Replication Method.</p>
   **/
  private ReplicationAccMethod replicationMethod;

  /**
   * <p>
   * Interactive filter of accounting entries.
   * </p>
   * @param pEntityClass Entity Class
   * @param pAddParams additional params (must present requestedDatabaseId
   * and replicationMethodId of String type (WEB parameters))
   * @return filter e.g. "((ITSID>0 and IDDATABASEBIRTH=2135)
   * and ((ACCDEBIT isnull or ACCDEBIT not in ('BadDebts'))
   * and (ACCCREDIT isnull or ACCCREDIT not in ('BadDebts','Property'))))"
   * @throws Exception - an exception
   **/
  @Override
  public final String makeFilter(final Class<?> pEntityClass,
    final Map<String, Object> pAddParams) throws Exception {
    if (!AccountingEntry.class.isAssignableFrom(pEntityClass)) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "This class not descendant of AccountingEntry: "
          + pEntityClass);
    }
    lazyEvalReplicationMethod(pAddParams);
    StringBuffer filterAvoidAccDbCr = new StringBuffer("");
    if (this.replicationMethod.getExcludeDebitAccounts().size() > 0) {
      filterAvoidAccDbCr.append(" and ((ACCDEBIT isnull or ACCDEBIT not in (");
      boolean isFirst = true;
      for (ReplExcludeAccountsDebit repExclAccDb
        : this.replicationMethod.getExcludeDebitAccounts()) {
        if (isFirst) {
          isFirst = false;
        } else {
          filterAvoidAccDbCr.append(",");
        }
        filterAvoidAccDbCr.append("'" + repExclAccDb.getAccount()
          .getItsId() + "'");
      }
    }
    if (this.replicationMethod.getExcludeCreditAccounts().size() > 0) {
      if (this.replicationMethod.getExcludeDebitAccounts().size() > 0) {
        filterAvoidAccDbCr
          .append(")) and (ACCCREDIT isnull or ACCCREDIT not in (");
      } else {
        filterAvoidAccDbCr
          .append(" and (ACCCREDIT isnull or ACCCREDIT not in (");
      }
      boolean isFirst = true;
      for (ReplExcludeAccountsCredit repExclAccCr
        : this.replicationMethod.getExcludeCreditAccounts()) {
        if (isFirst) {
          isFirst = false;
        } else {
          filterAvoidAccDbCr.append(",");
        }
        filterAvoidAccDbCr.append("'" + repExclAccCr.getAccount()
          .getItsId() + "'");
      }
    }
    if (this.replicationMethod.getExcludeDebitAccounts().size() > 0
      && this.replicationMethod.getExcludeCreditAccounts().size() > 0) {
      filterAvoidAccDbCr.append(")))");
    } else if (this.replicationMethod.getExcludeDebitAccounts().size() > 0
      || this.replicationMethod.getExcludeCreditAccounts().size() > 0) {
      filterAvoidAccDbCr.append("))");
    }
    if (this.replicationMethod.getExcludeDebitAccounts().size() > 0
      || this.replicationMethod.getExcludeCreditAccounts().size() > 0) {
      return "(" + this.filterId.makeFilter(pEntityClass, pAddParams)
        + filterAvoidAccDbCr.toString() + ")";
    }
    return this.filterId.makeFilter(pEntityClass, pAddParams);
  }


  /**
   * <p>Handle model changed event.</p>
   * @param pModel which changed
   **/
  @Override
  public final void handleModelChanged(final ReplicationAccMethod pModel) {
    if (this.replicationMethod != null
      && this.replicationMethod.getItsId().equals(pModel.getItsId())) {
      this.replicationMethod = null;
    }
  }

  /**
   * <p>Evaluate (retrieve) model.</p>
   * @param pAddParams additional params, (must present
   * replicationMethodId of String type (WEB parameters)).
   * @throws Exception - an exception
   * @return evaluated data
   **/
  @Override
  public final Date evalData(
    final Map<String, Object> pAddParams) throws Exception {
    lazyEvalReplicationMethod(pAddParams);
    if (this.replicationMethod.getLastDateReplication() == null) {
      return new Date(1L);
    }
    return this.replicationMethod.getLastDateReplication();
  }

  /**
   * <p>It prepares database after import.</p>
   * @param pAddParams additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void make(
    final Map<String, Object> pAddParams) throws Exception {
    this.replicationMethod.setLastDateReplication(new Date());
    getSrvOrm().updateEntity(this.replicationMethod);
  }

  //Utils:
  /**
   * <p>Lazy Evaluate Replication Method.</p>
   * @param pAddParams additional params, (must present
   * replicationMethodId of String type (WEB parameters)).
   * @throws Exception - an exception
   **/
  public final void lazyEvalReplicationMethod(
    final Map<String, Object> pAddParams) throws Exception {
    Long replicationMethodId;
    try {
      replicationMethodId = Long.parseLong(pAddParams
        .get("replicationMethodId").toString());
    } catch (Exception e) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "Wrong or missing parameter replicationMethodId (in pAddParams): "
          + pAddParams.get("replicationMethodId"));
    }
    if (this.replicationMethod == null || !this.replicationMethod
      .getItsId().equals(replicationMethodId)) {
      this.replicationMethod = getSrvOrm()
        .retrieveEntityById(ReplicationAccMethod.class, replicationMethodId);
      this.replicationMethod.setExcludeDebitAccounts(getSrvOrm()
        .retrieveEntityOwnedlist(ReplExcludeAccountsDebit.class,
          this.replicationMethod));
      this.replicationMethod.setExcludeCreditAccounts(getSrvOrm()
        .retrieveEntityOwnedlist(ReplExcludeAccountsCredit.class,
          this.replicationMethod));
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
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
   * <p>Getter for filterId.</p>
   * @return IFilterEntities
   **/
  public final IFilterEntities getFilterId() {
    return this.filterId;
  }

  /**
   * <p>Setter for filterId.</p>
   * @param pFilterId reference
   **/
  public final void setFilterId(
    final IFilterEntities pFilterId) {
    this.filterId = pFilterId;
  }

  /**
   * <p>Getter for replicationMethod.</p>
   * @return ReplicationAccMethod
   **/
  public final ReplicationAccMethod getReplicationAccMethod() {
    return this.replicationMethod;
  }

  /**
   * <p>Setter for replicationMethod.</p>
   * @param pReplicationAccMethod reference
   **/
  public final void setReplicationAccMethod(
    final ReplicationAccMethod pReplicationAccMethod) {
    this.replicationMethod = pReplicationAccMethod;
  }
}
