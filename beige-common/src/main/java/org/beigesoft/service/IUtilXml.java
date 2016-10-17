package org.beigesoft.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.io.Reader;
import java.util.Map;

/**
 * <p>Service that escape XML.</p>
 *
 * @author Yury Demidenko
 */
public interface IUtilXml {

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

  /**
   * <p>
   * Read attributes from stream. Start the XML element
   * must be read out.
   * </p>
   * @param pReader reader.
   * @param pAddParam additional params
   * @return attributes map
   * @throws Exception - an exception
   **/
  Map<String, String> readAttributes(Reader pReader,
    Map<String, ?> pAddParam) throws Exception;


  /**
   * <p>
   * Read stream until start given element e.g. &lt;message.
   * </p>
   * @param pReader reader.
   * @param pElement element
   * @return true if start element is happen, false if end of stream
   * @throws Exception - an exception
   **/
  boolean readUntilStart(Reader pReader,
    String pElement) throws Exception;
}
