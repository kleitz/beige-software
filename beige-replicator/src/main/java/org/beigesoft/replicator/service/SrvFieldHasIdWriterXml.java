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
import java.io.Writer;

import org.beigesoft.model.IHasId;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.IUtilXml;

/**
 * <p>Service to write owned entity as ID.
 * According Beige Replicator specification #1.</p>
 *
 * @author Yury Demidenko
 */
public class SrvFieldHasIdWriterXml implements ISrvFieldWriter {

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
    } else {
      if (!IHasId.class.isAssignableFrom(pField.getClass())) {
        throw new ExceptionWithCode(ExceptionWithCode
          .CONFIGURATION_MISTAKE, "It's wrong service to write that field: "
            + pField + "/" + pFieldName);
      }
      fieldValue = ((IHasId) pField).getItsId().toString();
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
