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
import java.util.HashMap;
import java.io.Reader;
import java.lang.reflect.Constructor;

import org.beigesoft.xml.service.ISrvXmlEscape;
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
   * <p>XML Escape service.</p>
   **/
  private ISrvXmlEscape srvXmpEscape;

  /**
   * <p>
   * Read entity(fill fields) from a stream (reader - file or through network).
   * It is invoked when it's start of &lt;entity
   * </p>
   * @param pReader reader.
   * @param pAddParam additional params
   * @return entity filled/refreshed.
   * @throws Exception - an exception
   **/
  @Override
  public final Object read(final Reader pReader,
    final Map<String, ?> pAddParam) throws Exception {
    Map<String, String> attributesMap = readAttributes(pReader, pAddParam);
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
        .get(entityClass.getCanonicalName());
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
        srvEntityFieldFiller.fill(entity, entry.getKey(), attributesMap
          .get(entry.getKey()), pAddParam);
      }
    }
    return entity;
  }

  /**
   * <p>
   * Read entity attributes from stream.
   * </p>
   * @param pReader reader.
   * @param pAddParam additional params
   * @return attributes map
   * @throws Exception - an exception
   **/
  @Override
  public final Map<String, String> readAttributes(final Reader pReader,
    final Map<String, ?> pAddParam) throws Exception {
    Map<String, String> attributesMap = new HashMap<String, String>();
    StringBuffer sb = new StringBuffer();
    int chi;
    while ((chi = pReader.read()) != -1) {
      char ch = (char) chi;
      if (ch == '>') {
        break;
      }
      switch (ch) {
        case '\\':
          sb.append("\\");
          break;
        case '"':
          sb.append("\"");
          break;
        case '\n':
          sb.append("\n");
          break;
        case '\r':
          sb.append("\r");
          break;
        case '\t':
          sb.append("\t");
          break;
        default:
          sb.append(ch);
          break;
      }
      evalAttributes(sb, attributesMap);
    }
    return attributesMap;
  }

  /**
   * <p>Try to eval content of string buffer if it's an attribute
   * with value then fill map and clear buffer.</p>
   * @param pSb StringBuffer
   * @param pAttributesMap Attributes Map
   * @throws Exception - an exception
   **/
  public final void evalAttributes(final StringBuffer pSb,
    final Map<String, String> pAttributesMap) throws Exception {
    String str = pSb.toString().trim();
    if (str.length() > 3 //minimum is a=""
      && str.endsWith("\"") && str.indexOf("\"") != str.length() - 1) {
      int equalsIdx = str.indexOf("=");
      if (equalsIdx == -1) {
        throw new ExceptionWithCode(ExceptionWithCode
          .SOMETHING_WRONG, "There is no equals character in " + str);
      }
      String attrName = str.substring(0, equalsIdx);
      String attrVal = str.substring(str.indexOf("\"") + 1, str.length() - 1);
      pAttributesMap.put(attrName, attrVal);
      pSb.delete(0, pSb.length());
    }
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
   * <p>Getter for srvXmpEscape.</p>
   * @return ISrvXmlEscape
   **/
  public final ISrvXmlEscape getSrvXmpEscape() {
    return this.srvXmpEscape;
  }

  /**
   * <p>Setter for srvXmpEscape.</p>
   * @param pSrvXmpEscape reference
   **/
  public final void setSrvXmpEscape(final ISrvXmlEscape pSrvXmpEscape) {
    this.srvXmpEscape = pSrvXmpEscape;
  }
}
