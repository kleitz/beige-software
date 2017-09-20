package org.beigesoft.replicator.filter;

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

import java.util.Map;

import org.beigesoft.persistable.APersistableBase;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Service that make SQL WHERE filter for immutable entities
 * of type APersistableBase.
 * According database replication one way specification #1.
 * It's untransactional service. Transaction must be started.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FilterPersistableBaseImmutable<RS> implements IFilterEntities {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>
   * Filter for immutable entities of type APersistableBase.
   * </p>
   * @param pEntityClass Entity Class
   * @param pAddParam additional params (must present requestedDatabaseId
   * of String type (WEB parameter))
   * @return filter e.g. "(ITSID>0 and IDDATABASEBIRTH=2135)"
   * @throws Exception - an exception
   **/
  @Override
  public final String makeFilter(final Class<?> pEntityClass,
    final Map<String, Object> pAddParam) throws Exception {
    if (!APersistableBase.class.isAssignableFrom(pEntityClass)) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "This class not descendant of APersistableBase: "
          + pEntityClass);
    }
    int requestedDatabaseId;
    try {
      requestedDatabaseId = Integer.parseInt(pAddParam
        .get("requestedDatabaseId").toString());
    } catch (Exception e) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "Wrong or missing parameter requestedDatabaseId (in pAddParam): "
          + pAddParam.get("requestedDatabaseId"));
    }
    String queryMaxIdBirth = "select max(IDBIRTH) as MAX_IDBIRTH from "
    + pEntityClass.getSimpleName().toUpperCase() + " where IDDATABASEBIRTH="
      + requestedDatabaseId + ";";
    Long maxIdBirth = this.srvDatabase
      .evalLongResult(queryMaxIdBirth, "MAX_IDBIRTH");
    if (maxIdBirth == null) {
      maxIdBirth = 0L;
    }
    String tblNm = pEntityClass.getSimpleName().toUpperCase();
    return "(" + tblNm + ".ITSID>" + maxIdBirth + " and " + tblNm
      + ".IDDATABASEBIRTH=" + requestedDatabaseId + ")";
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
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
