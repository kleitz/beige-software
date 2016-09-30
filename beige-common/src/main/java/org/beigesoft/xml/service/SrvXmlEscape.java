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

import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>Service that escape XML.</p>
 *
 * @author Yury Demidenko
 */
public class SrvXmlEscape implements ISrvXmlEscape {

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
}
