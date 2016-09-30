package org.beigesoft.xml.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */


/**
 * <p>Service that escape XML.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvXmlEscape {

  /**
   * <p>
   * Escape XML for given string.
   * </p>
   * @param pSource source
   * @return escaped string
   * @throws Exception - an exception
   **/
  String escapeXml(String pSource) throws Exception;

  /**
   * <p>
   * Escape XML for given char.
   * </p>
   * @param pChar char
   * @return escaped string
   * @throws Exception - an exception
   **/
  String xmlEscape(char pChar) throws Exception;

  /**
   * <p>
   * Unescape XML for given string.
   * </p>
   * @param pSource source
   * @return unescaped string
   * @throws Exception - an exception
   **/
  String unescapeXml(String pSource) throws Exception;

  /**
   * <p>
   * Unescape XML for given string.
   * </p>
   * @param pEscaped Escaped
   * @return unescaped char
   * @throws Exception - an exception
   **/
  char xmlUnescape(String pEscaped) throws Exception;
}
