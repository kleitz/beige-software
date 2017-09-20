package org.beigesoft.replicator.persistable;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Date;

import org.beigesoft.persistable.APersistableBaseHasName;

/**
 * <pre>
 * Model of Replication Method.
 * Database replication one way specification #1.
 * itsVersion changed time algorithm.
 * </pre>
 *
 * @author Yury Demidenko
 */
public abstract class AReplicationMethod extends APersistableBaseHasName {

  /**
   * <p>Last date replication, nullable.</p>
   **/
  private Date lastDateReplication;

  /**
   * <p>Requested database ID, not null.</p>
   **/
  private Integer requestedDatabaseId;

  //Hiding references getters and setters:
  /**
   * <p>Getter for lastDateReplication.</p>
   * @return Date
   **/
  public final Date getLastDateReplication() {
    if (this.lastDateReplication == null) {
      return null;
    }
    return new Date(this.lastDateReplication.getTime());
  }

  /**
   * <p>Setter for lastDateReplication.</p>
   * @param pLastDateReplication reference
   **/
  public final void setLastDateReplication(final Date pLastDateReplication) {
    if (pLastDateReplication == null) {
      this.lastDateReplication = null;
    } else {
      this.lastDateReplication = new Date(pLastDateReplication.getTime());
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for requestedDatabaseId.</p>
   * @return Integer
   **/
  public final Integer getRequestedDatabaseId() {
    return this.requestedDatabaseId;
  }

  /**
   * <p>Setter for requestedDatabaseId.</p>
   * @param pRequestedDatabaseId reference
   **/
  public final void setRequestedDatabaseId(final Integer pRequestedDatabaseId) {
    this.requestedDatabaseId = pRequestedDatabaseId;
  }
}
