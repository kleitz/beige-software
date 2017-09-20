package org.beigesoft.service;

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
    Map<String, Object> pAddParam) throws Exception;


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
