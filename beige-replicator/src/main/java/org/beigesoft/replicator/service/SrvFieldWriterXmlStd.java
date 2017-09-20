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
   * @param pAddParam additional params (e.g. exclude fields set)
   * @param pField value
   * @param pFieldName Field Name
   * @param pWriter writer
   * @throws Exception - an exception
   **/
  @Override
  public final void write(final Map<String, Object> pAddParam,
    final Object pField, final String pFieldName,
      final Writer pWriter) throws Exception {
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
