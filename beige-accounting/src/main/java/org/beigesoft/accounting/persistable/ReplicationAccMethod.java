package org.beigesoft.accounting.persistable;

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

import  org.beigesoft.replicator.persistable.AReplicationMethod;

/**
 * <pre>
 * Model of Replication Method with exclude accounts filter.
 * Database replication from tax to market accounting specification #1.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class ReplicationAccMethod extends AReplicationMethod {

  /**
   * <p>Exclude accounting entries with debits.</p>
   **/
  private List<ReplExcludeAccountsDebit> excludeDebitAccounts;

  /**
   * <p>Exclude accounting entries with credits.</p>
   **/
  private List<ReplExcludeAccountsCredit> excludeCreditAccounts;

  //Simple getters and setters:
  /**
   * <p>Getter for excludeDebitAccounts.</p>
   * @return List<ReplExcludeAccountsDebit>
   **/
  public final List<ReplExcludeAccountsDebit> getExcludeDebitAccounts() {
    return this.excludeDebitAccounts;
  }

  /**
   * <p>Setter for excludeDebitAccounts.</p>
   * @param pExcludeDebitAccounts reference
   **/
  public final void setExcludeDebitAccounts(
    final List<ReplExcludeAccountsDebit> pExcludeDebitAccounts) {
    this.excludeDebitAccounts = pExcludeDebitAccounts;
  }

  /**
   * <p>Getter for excludeCreditAccounts.</p>
   * @return List<ReplExcludeAccountsCredit>
   **/
  public final List<ReplExcludeAccountsCredit> getExcludeCreditAccounts() {
    return this.excludeCreditAccounts;
  }

  /**
   * <p>Setter for excludeCreditAccounts.</p>
   * @param pExcludeCreditAccounts reference
   **/
  public final void setExcludeCreditAccounts(
    final List<ReplExcludeAccountsCredit> pExcludeCreditAccounts) {
    this.excludeCreditAccounts = pExcludeCreditAccounts;
  }
}
