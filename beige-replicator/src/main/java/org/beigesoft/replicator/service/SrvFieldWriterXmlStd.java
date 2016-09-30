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

import java.util.Date;
import java.util.Map;
import java.io.Writer;

import org.beigesoft.xml.service.ISrvXmlEscape;

/**
 * <p>Service to write a standard (toString()) field
 * of replicable/persistable entity. Only fields of type String
 * will be XML-escaped (it's for good performance).
 * According Beige Replicator specification #1.</p>
 *
 * @author Yury Demidenko
 */
public class SrvFieldWriterXmlStd implements ISrvFieldWriter {

  /**
   * <p>XML Escape service.</p>
   **/
  private ISrvXmlEscape srvXmpEscape;

  /**
   * <p>
   * Write standard field of entity into a stream
   * (writer - file or pass it through network).
   * </p>
   * @param pField value
   * @param pFieldName Field Name
   * @param pWriter writer
   * @param pAddParam additional params (e.g. exclude fields set)
   * @throws Exception - an exception
   **/
  @Override
  public final void write(final Object pField, final String pFieldName,
    final Writer pWriter, final Map<String, ?> pAddParam) throws Exception {
    String fieldValue;
    if (pField == null) {
      fieldValue = "NULL";
    } else if (Enum.class.isAssignableFrom(pField.getClass())) {
      fieldValue = String.valueOf(((Enum) pField).ordinal());
    } else if (pField.getClass() == Date.class) {
      fieldValue = String.valueOf(((Date) pField).getTime());
    } else {
      fieldValue = pField.toString();
      if (pField instanceof String) {
        fieldValue = getSrvXmpEscape().escapeXml(fieldValue);
      }
    }
    pWriter.write(" " + pFieldName + "=\"" + fieldValue
      + "\"\n");
  }

  //Simple getters and setters:
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
