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

import org.beigesoft.service.IUtilXml;

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
   * <p>XML service.</p>
   **/
  private IUtilXml utilXml;

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
    final Writer pWriter,
      final Map<String, Object> pAddParam) throws Exception {
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
        fieldValue = getUtilXml().escapeXml(fieldValue);
      }
    }
    pWriter.write(" " + pFieldName + "=\"" + fieldValue
      + "\"\n");
  }

  //Simple getters and setters:
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
