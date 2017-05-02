package org.beigesoft.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.List;

/**
 * <p>Persistable model of message to email.</p>
 *
 * @author Yury Demidenko
 */
public class EmailMsg extends AHasIdLongVersion {

  /**
   * <p>Subject.</p>
   **/
  private String esubject;

  /**
   * <p>Text.</p>
   **/
  private String etext;

  /**
   * <p>Recipients emails.</p>
   **/
  private List<Erecipient> erecipients;

  /**
   * <p>Attachments.</p>
   **/
  private List<Eattachment> eattachments;

  /**
   * <p>If sent.</p>
   **/
  private Boolean isSent = false;

  /**
   * <p>Email connection data.</p>
   **/
  private EmailConnect emailConnect;

  //Simple getters and setters:
  /**
   * <p>Getter for esubject.</p>
   * @return String
   **/
  public final String getEsubject() {
    return this.esubject;
  }

  /**
   * <p>Setter for esubject.</p>
   * @param pEsubject reference
   **/
  public final void setEsubject(final String pEsubject) {
    this.esubject = pEsubject;
  }

  /**
   * <p>Getter for etext.</p>
   * @return String
   **/
  public final String getEtext() {
    return this.etext;
  }

  /**
   * <p>Setter for etext.</p>
   * @param pEtext reference
   **/
  public final void setEtext(final String pEtext) {
    this.etext = pEtext;
  }

  /**
   * <p>Getter for erecipients.</p>
   * @return List<String>
   **/
  public final List<Erecipient> getErecipients() {
    return this.erecipients;
  }

  /**
   * <p>Setter for erecipients.</p>
   * @param pErecipients reference
   **/
  public final void setErecipients(final List<Erecipient> pErecipients) {
    this.erecipients = pErecipients;
  }

  /**
   * <p>Getter for eattachments.</p>
   * @return List<String>
   **/
  public final List<Eattachment> getEattachments() {
    return this.eattachments;
  }

  /**
   * <p>Setter for eattachments.</p>
   * @param pEattachments reference
   **/
  public final void setEattachments(final List<Eattachment> pEattachments) {
    this.eattachments = pEattachments;
  }

  /**
   * <p>Getter for isSent.</p>
   * @return Boolean
   **/
  public final Boolean getIsSent() {
    return this.isSent;
  }

  /**
   * <p>Setter for isSent.</p>
   * @param pIsSent reference
   **/
  public final void setIsSent(final Boolean pIsSent) {
    this.isSent = pIsSent;
  }

  /**
   * <p>Getter for emailConnect.</p>
   * @return EmailConnect
   **/
  public final EmailConnect getEmailConnect() {
    return this.emailConnect;
  }

  /**
   * <p>Setter for emailConnect.</p>
   * @param pEmailConnect reference
   **/
  public final void setEmailConnect(final EmailConnect pEmailConnect) {
    this.emailConnect = pEmailConnect;
  }
}
