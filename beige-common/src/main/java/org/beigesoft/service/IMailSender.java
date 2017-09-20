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

import java.util.Map;

import org.beigesoft.persistable.EmailMsg;
import org.beigesoft.persistable.EmailConnect;

/**
 * <p>Abstraction of service to send email.</p>
 *
 * @author Yury Demidenko
 */
public interface IMailSender {

  /**
   * <p>Send email.</p>
   * @param pAddParam additional param
   * @param pMsg message to mail
   * @throws Exception - an exception
   **/
  void sendEmail(Map<String, Object> pAddParam,
    EmailMsg pMsg) throws Exception;

  /**
   * <p>Open email connection.</p>
   * @param pAddParam additional param
   * @param pEmailConnect Email Connect
   * @throws Exception - an exception
   **/
  void openConnection(Map<String, Object> pAddParam,
    EmailConnect pEmailConnect) throws Exception;

  /**
   * <p>Close email connection.</p>
   * @param pAddParam additional param
   * @param pEmailConnect Email Connect
   * @throws Exception - an exception
   **/
  void closeConnection(Map<String, Object> pAddParam,
    EmailConnect pEmailConnect) throws Exception;
}
