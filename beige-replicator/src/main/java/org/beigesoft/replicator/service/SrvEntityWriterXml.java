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
import java.io.Writer;
import java.lang.reflect.Field;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.settings.IMngSettings;

/**
 * <p>Service to write a replicable/persistable entity as XML.</p>
 *
 * @author Yury Demidenko
 */
public class SrvEntityWriterXml implements ISrvEntityWriter {

  /**
   * <p>Manager Settings.</p>
   **/
  private IMngSettings mngSettings;

  /**
   * <p>Fields writers map.</p>
   **/
  private Map<String, ISrvFieldWriter> fieldsWritersMap;

  /**
   * <p>Reflection service.</p>
   **/
  private UtlReflection utlReflection;

  /**
   * <p>
   * Write entity into a stream (writer - file or pass it through network).
   * </p>
   * @param pAddParam additional params (e.g. exclude fields set)
   * @param pEntity object
   * @param pWriter writer
   * @throws Exception - an exception
   **/
  @Override
  public final void write(final Map<String, Object> pAddParam,
    final Object pEntity, final Writer pWriter) throws Exception {
    Map<String, Map<String, String>> fieldsSettingsMap =
      getMngSettings().getFieldsSettings()
        .get(pEntity.getClass());
    pWriter.write("<entity class=\"" + pEntity.getClass().getCanonicalName()
      + "\"\n");
    for (Map.Entry<String, Map<String, String>> entry
      : fieldsSettingsMap.entrySet()) {
      if ("true".equals(entry.getValue().get("isEnabled"))) {
        Field field = getUtlReflection()
          .retrieveField(pEntity.getClass(), entry.getKey());
        field.setAccessible(true);
        Object fieldValue = field.get(pEntity);
        ISrvFieldWriter srvFieldWriter = getFieldsWritersMap()
          .get(entry.getValue().get("ISrvFieldWriter"));
        if (srvFieldWriter == null) {
          throw new ExceptionWithCode(ExceptionWithCode
            .CONFIGURATION_MISTAKE, "There is no ISrvFieldWriter " + entry
              .getValue().get("ISrvFieldWriter") + " for "
                + pEntity.getClass() + " / " + field.getName());
        }
        srvFieldWriter.write(pAddParam, fieldValue, field.getName(), pWriter);
      }
    }
    pWriter.write("/>\n");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for mngSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngSettings() {
    return this.mngSettings;
  }

  /**
   * <p>Setter for mngSettings.</p>
   * @param pMngSettings reference
   **/
  public final void setMngSettings(final IMngSettings pMngSettings) {
    this.mngSettings = pMngSettings;
  }

  /**
   * <p>Getter for fieldsWritersMap.</p>
   * @return Map<String, ISrvFieldWriter>
   **/
  public final Map<String, ISrvFieldWriter> getFieldsWritersMap() {
    return this.fieldsWritersMap;
  }

  /**
   * <p>Setter for fieldsWritersMap.</p>
   * @param pFieldsWritersMap reference
   **/
  public final void setFieldsWritersMap(
    final Map<String, ISrvFieldWriter> pFieldsWritersMap) {
    this.fieldsWritersMap = pFieldsWritersMap;
  }

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
