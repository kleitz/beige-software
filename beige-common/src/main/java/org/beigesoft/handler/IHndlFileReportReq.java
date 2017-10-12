package org.beigesoft.handler;

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


import java.io.OutputStream;

import org.beigesoft.model.IRequestData;

/**
 * <p>Handler that get parameters (include report service name)
 * from HTTP-request, and invoke report e.g.
 * "reportInvoice.make(invoice, OutputStream)".
 * This is usually PDF report.</p>
 *
 * @author Yury Demidenko
 */
public interface IHndlFileReportReq {

  /**
   * <p>Handle file-report request.</p>
   * @param pRequestData Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   */
  void handle(IRequestData pRequestData, OutputStream pSous) throws Exception;
}
