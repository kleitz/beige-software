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
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

import org.beigesoft.persistable.AHasIdLong;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.UtlReflection;

/**
 * <p>Service to fill a owned entity of replicable entity.
 * Only for database replication!!! For restoring from file it must be a filler
 * that will find out owned entity from list of already read entities.</p>
 *
 * @author Yury Demidenko
 */
public class SrvEntityFieldHasIdLongRepl implements ISrvEntityFieldFiller {

  /**
   * <p>Reflection service.</p>
   **/
  private UtlReflection utlReflection;

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
    Field rField = getUtlReflection().retrieveField(pEntity.getClass(),
      pFieldName);
    rField.setAccessible(true);
    if (!AHasIdLong.class.isAssignableFrom(rField.getType())) {
      throw new ExceptionWithCode(ExceptionWithCode
        .CONFIGURATION_MISTAKE, "It's wrong service to fill that field: "
          + pEntity + "/" + pFieldName + "/" + pFieldStrValue);
    }
    if ("NULL".equals(pFieldStrValue)) {
      rField.set(pEntity, null);
      return;
    }
    try {
      @SuppressWarnings("unchecked")
      Constructor constructor = rField.getType().getDeclaredConstructor();
      Object ownedEntity = constructor.newInstance();
      ((AHasIdLong) ownedEntity).setItsId(Long.parseLong(pFieldStrValue));
      rField.set(pEntity, ownedEntity);
    } catch (Exception ex) {
      throw new ExceptionWithCode(ExceptionWithCode
        .WRONG_PARAMETER, "Can not fill field: " + pEntity + "/" + pFieldName
          + "/" + pFieldStrValue + ", " + ex.getMessage(), ex);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for utlReflection.</p>
   * @return UtlReflection
   **/
  public final UtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final UtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }
}
