package org.beigesoft.replicator.service;

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
import java.io.Reader;
import java.lang.reflect.Constructor;

import org.beigesoft.service.IUtilXml;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.settings.IMngSettings;

/**
 * <p>Service to read a replicable/persistable entity.
 * According Beige Replicator specification #1.</p>
 *
 * @author Yury Demidenko
 */
public class SrvEntityReaderXml implements ISrvEntityReader {

  /**
   * <p>Manager Settings.</p>
   **/
  private IMngSettings mngSettings;

  /**
   * <p>Fields fillers map.</p>
   **/
  private Map<String, ISrvEntityFieldFiller> fieldsFillersMap;

  /**
   * <p>XML service.</p>
   **/
  private IUtilXml utilXml;

  /**
   * <p>
   * Read entity(fill fields) from a stream (reader - file or through network).
   * It is invoked when it's start of &lt;entity
   * </p>
   * @param pAddParam additional params
   * @param pReader reader.
   * @return entity filled/refreshed.
   * @throws Exception - an exception
   **/
  @Override
  public final Object read(final Map<String, Object> pAddParam,
    final Reader pReader) throws Exception {
    Map<String, String> attributesMap = readAttributes(pAddParam, pReader);
    if (attributesMap.get("class") == null) {
      throw new ExceptionWithCode(ExceptionWithCode
        .CONFIGURATION_MISTAKE, "There is no class attribute for entity!");
    }
    Class entityClass = Class.forName(attributesMap.get("class"));
    @SuppressWarnings("unchecked")
    Constructor constructor = entityClass.getDeclaredConstructor();
    Object entity = constructor.newInstance();
    Map<String, Map<String, String>> fieldsSettingsMap =
      getMngSettings().getFieldsSettings()
        .get(entityClass);
    for (Map.Entry<String, Map<String, String>> entry
      : fieldsSettingsMap.entrySet()) {
      if ("true".equals(entry.getValue().get("isEnabled"))
        && attributesMap.get(entry.getKey()) != null) {
        ISrvEntityFieldFiller srvEntityFieldFiller = getFieldsFillersMap()
          .get(entry.getValue().get("ISrvEntityFieldFiller"));
        if (srvEntityFieldFiller == null) {
          throw new ExceptionWithCode(ExceptionWithCode
            .CONFIGURATION_MISTAKE, "There is no ISrvEntityFieldFiller "
              + entry.getValue().get("ISrvEntityFieldFiller") + " for "
                + entityClass + " / " + entry.getKey());
        }
        srvEntityFieldFiller.fill(pAddParam, entity, entry.getKey(),
          attributesMap.get(entry.getKey()));
      }
    }
    return entity;
  }

  /**
   * <p>
   * Read entity attributes from stream.
   * </p>
   * @param pAddParam additional params
   * @param pReader reader.
   * @return attributes map
   * @throws Exception - an exception
   **/
  @Override
  public final Map<String, String> readAttributes(
    final Map<String, Object> pAddParam,
      final Reader pReader) throws Exception {
    return this.utilXml.readAttributes(pReader, pAddParam);
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
   * <p>Getter for fieldsFillersMap.</p>
   * @return Map<String, ISrvEntityFieldFiller>
   **/
  public final Map<String, ISrvEntityFieldFiller> getFieldsFillersMap() {
    return this.fieldsFillersMap;
  }

  /**
   * <p>Setter for fieldsFillersMap.</p>
   * @param pFieldsFillersMap reference
   **/
  public final void setFieldsFillersMap(
    final Map<String, ISrvEntityFieldFiller> pFieldsFillersMap) {
    this.fieldsFillersMap = pFieldsFillersMap;
  }

  /**
   * <p>Getter for utilXml.</p>
   * @return IUtilXml
   **/
  public final IUtilXml getUtilXml() {
    return this.utilXml;
  }

  /**
   * <p>Setter for utilXml.</p>
   * @param pUtilXml reference
   **/
  public final void setUtilXml(final IUtilXml pUtilXml) {
    this.utilXml = pUtilXml;
  }
}
