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
import java.util.HashMap;

import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>Service that escape XML.
 * For improving performance unescapeXml should invoked
 * explicitly when it's need (for field like itsName or description).</p>
 *
 * @author Yury Demidenko
 */
public class UtilXml implements IUtilXml {

  /**
   * <p>
   * Escape XML for given string.
   * </p>
   * @param pSource source
   * @return escaped string
   * @throws Exception - an exception
   **/
  @Override
  public final String escapeXml(final String pSource) throws Exception {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < pSource.length(); i++) {
      char ch = pSource.charAt(i);
      sb.append(xmlEscape(ch));
    }
    return sb.toString();
  }

  /**
   * <p>
   * Escape XML for given char.
   * </p>
   * @param pChar char
   * @return escaped string
   * @throws Exception - an exception
   **/
  @Override
  public final String xmlEscape(final char pChar) throws Exception {
    if (pChar == '<') {
      return "&lt;";
    } else if (pChar == '>') {
      return "&gt;";
    } else if (pChar == '"') {
      return "&quot;";
    } else if (pChar == '\'') {
      return "&apos;";
    } else if (pChar == '&') {
      return "&amp;";
    }
    return String.valueOf(pChar);
  }

  /**
   * <p>
   * Unescape XML for given string.
   * For improving performance unescapeXml should invoked
   * explicitly when it's need (for field like itsName or description).
   * </p>
   * @param pSource source
   * @return unescaped string
   * @throws Exception - an exception
   **/
  @Override
  public final String unescapeXml(final String pSource) throws Exception {
    StringBuffer sb = new StringBuffer();
    StringBuffer sbEsc = new StringBuffer();
    boolean isStartEsc = false;
    for (int i = 0; i < pSource.length(); i++) {
      char ch = pSource.charAt(i);
      if (!isStartEsc && ch == '&') {
        isStartEsc = true;
        sbEsc.append(ch);
        continue;
      } else if (isStartEsc) {
        sbEsc.append(ch);
        if (ch == ';') {
          sb.append(xmlUnescape(sbEsc.toString()));
          sbEsc.delete(0, sbEsc.length());
          isStartEsc = false;
        }
      } else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  /**
   * <p>
   * Unescape XML for given string.
   * </p>
   * @param pEscaped Escaped
   * @return unescaped char
   * @throws Exception - an exception
   **/
  @Override
  public final char xmlUnescape(final String pEscaped) throws Exception {
    if ("&lt;".equals(pEscaped)) {
      return '<';
    } else if ("&gt;".equals(pEscaped)) {
      return '>';
    } else if ("&quot;".equals(pEscaped)) {
      return '"';
    } else if ("&apos;".equals(pEscaped)) {
      return '\'';
    } else if ("&amp;".equals(pEscaped)) {
      return '&';
    }
    throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
      "There is no escape char for " + pEscaped);
  }

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
  @Override
  public final Map<String, String> readAttributes(final Reader pReader,
    final Map<String, Object> pAddParam) throws Exception {
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
   * with value then fill map and clear buffer.
   * For improving performance unescapeXml should invoked
   * explicitly when it's need (for field like itsName or description).</p>
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

  /**
   * <p>
   * Read stream until start given element e.g. &lt;message.
   * </p>
   * @param pReader reader.
   * @param pElement element
   * @return true if start element is happen, false if end of stream
   * @throws Exception - an exception
   **/
  @Override
  public final boolean readUntilStart(final Reader pReader,
    final String pElement) throws Exception {
    StringBuffer sb = new StringBuffer();
    int chi;
    boolean isLtOccured = false;
    while ((chi = pReader.read()) != -1) {
      char ch = (char) chi;
      if (isLtOccured) {
        if (ch ==  '>' || ch == '\n' || ch == '\\' || ch == '"' || ch == '\r'
          || ch == '\t') {
          isLtOccured = false;
          sb.delete(0, sb.length());
          continue;
        }
        sb.append(ch);
        String readedStr = sb.toString();
        if (readedStr.length() > pElement.length()) {
          isLtOccured = false;
          sb.delete(0, sb.length());
          continue;
        }
        if (pElement.equals(readedStr)) {
          return true;
        }
      } else if (ch == '<') {
        isLtOccured = true;
      }
    }
    return false;
  }
}
