package org.beigesoft.replicator.service;

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

import org.beigesoft.persistable.UserRoleTomcat;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>Service to fill UserTomcat in UserRoleTomcat.</p>
 *
 * @author Yury Demidenko
 */
public class SrvEntityFieldUserTomcatRepl implements ISrvEntityFieldFiller {

  /**
   * <p>
   * Fill given field of given entity according value represented as
   * string.
   * </p>
   * @param pAddParam additional params
   * @param pEntity Entity.
   * @param pFieldName Field Name
   * @param pFieldStrValue Field value
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Map<String, Object> pAddParam,
    final Object pEntity, final String pFieldName,
      final String pFieldStrValue) throws Exception {
    if (!UserRoleTomcat.class.isAssignableFrom(pEntity.getClass())) {
      throw new ExceptionWithCode(ExceptionWithCode
        .CONFIGURATION_MISTAKE, "It's wrong service to fill that field: "
          + pEntity + "/" + pFieldName + "/" + pFieldStrValue);
    }
    UserRoleTomcat userRoleTomcat = (UserRoleTomcat) pEntity;
    if ("NULL".equals(pFieldStrValue)) {
      userRoleTomcat.setItsUser(null);
      return;
    }
    try {
      UserTomcat ownedEntity = new UserTomcat();
      ownedEntity.setItsUser(pFieldStrValue);
      userRoleTomcat.setItsUser(ownedEntity);
    } catch (Exception ex) {
      throw new ExceptionWithCode(ExceptionWithCode
        .WRONG_PARAMETER, "Can not fill field: " + pEntity + "/" + pFieldName
          + "/" + pFieldStrValue + ", " + ex.getMessage(), ex);
    }
  }
}
