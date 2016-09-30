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
import org.beigesoft.xml.service.ISrvXmlEscape;

/**
 * <p>Service to write owned entity as ID.
 * According Beige Replicator specification #1.</p>
 *
 * @author Yury Demidenko
 */
public class SrvFieldHasIdWriterXml implements ISrvFieldWriter {

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
