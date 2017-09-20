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

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;

import org.beigesoft.log.ILogger;
import org.beigesoft.persistable.EmailMsg;
import org.beigesoft.persistable.EmailConnect;
import org.beigesoft.persistable.Eattachment;
import org.beigesoft.persistable.EmailIntegerProperty;
import org.beigesoft.persistable.EmailStringProperty;

/**
 * <p>Service to send email with java-mail.</p>
 *
 * @author Yury Demidenko
 */
public class MailSenderStd implements IMailSender {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Only constructor.</p>
   * @param pLogger logger
   **/
  public MailSenderStd(final ILogger pLogger) {
    this.logger = pLogger;
    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
    boolean mcapOk = false;
    for (String mcap : mc.getMimeTypes()) {
      if (mcap.contains("multipart")) {
        mcapOk = true;
        break;
      }
    }
    if (!mcapOk) { //Fix android
      mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
      mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
      mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
      mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
      mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
      this.logger.info(null, MailSenderStd.class, "Mailcap has been fixed");
    }
  }

  /**
   * <p>Send email.</p>
   * @param pAddParam additional param
   * @param pMsg msg to mail
   * @throws Exception - an exception
   **/
  @Override
  public final void sendEmail(final Map<String, Object> pAddParam,
    final EmailMsg pMsg) throws Exception {
    Properties props = new Properties();
    for (EmailStringProperty esp
      : pMsg.getEmailConnect().getStringProperties()) {
      props.put(esp.getPropertyName(), esp.getPropretyValue());
    }
    for (EmailIntegerProperty eip
      : pMsg.getEmailConnect().getIntegerProperties()) {
      props.put(eip.getPropertyName(), eip.getPropretyValue());
    }
    Session sess = Session.getInstance(props);
    Message msg = new MimeMessage(sess);
    msg.setFrom(new InternetAddress(pMsg.getEmailConnect().getUserEmail()));
    if (pMsg.getErecipients().size() == 1) {
      msg.setRecipient(Message.RecipientType.TO,
        new InternetAddress(pMsg.getErecipients().get(0).getItsEmail()));
    } else {
      InternetAddress[] address =
        new InternetAddress[pMsg.getErecipients().size()];
      for (int i = 0; i < pMsg.getErecipients().size(); i++) {
        address[i] = new InternetAddress(pMsg.getErecipients().get(i)
          .getItsEmail());
      }
      msg.setRecipients(Message.RecipientType.TO, address);
    }
    msg.setSubject(pMsg.getEsubject());
    if (pMsg.getEattachments().size() > 0) {
      MimeBodyPart mbpt = new MimeBodyPart();
      mbpt.setText(pMsg.getEtext());
      Multipart mp = new MimeMultipart();
      mp.addBodyPart(mbpt);
      for (Eattachment attch : pMsg.getEattachments()) {
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.attachFile(attch.getItsPath());
        mp.addBodyPart(mbp);
      }
      msg.setContent(mp);
    } else {
      msg.setText(pMsg.getEtext());
    }
    msg.setSentDate(new Date());
    Transport.send(msg, pMsg.getEmailConnect().getUserEmail(),
      pMsg.getEmailConnect().getUserPassword());
  }


  /**
   * <p>Open email connection.</p>
   * @param pAddParam additional param
   * @param pEmailConnect Email Connect
   * @throws Exception - an exception
   **/
  @Override
  public final void openConnection(final Map<String, Object> pAddParam,
    final EmailConnect pEmailConnect) throws Exception {
    // nothing cause Java Mail open and close connection in Transport.send
  }

  /**
   * <p>Close email connection.</p>
   * @param pAddParam additional param
   * @param pEmailConnect Email Connect
   * @throws Exception - an exception
   **/
  @Override
  public final void closeConnection(final Map<String, Object> pAddParam,
    final EmailConnect pEmailConnect) throws Exception {
    // nothing cause Java Mail open and close connection in Transport.send
  }

  //Simple getters and setters:
  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }
}
