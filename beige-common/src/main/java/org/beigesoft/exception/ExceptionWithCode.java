package org.beigesoft.exception;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <p>Generic exception with code that
 * useful in servlet to propagate by sendError(int code).
 * </p>
 *
 * @author Yury Demidenko
 */
public class ExceptionWithCode extends Exception {

  /**
   * <p>Forbidden.</p>
   **/
  public static final int FORBIDDEN = 403;

  /**
   * <p>Not yet implemented.</p>
   **/
  public static final int NOT_YET_IMPLEMENTED = 1000;

  /**
   * <p>Something goes wrong.</p>
   **/
  public static final int SOMETHING_WRONG = 1001;

  /**
   * <p>Configuration mistake.</p>
   **/
  public static final int CONFIGURATION_MISTAKE = 1002;

  /**
   * <p>Unexpectable parameter.</p>
   **/
  public static final int WRONG_PARAMETER = 1003;

  /**
   * <p>Wrong assemble, e.g.
   * @see org.beigesoft.converter.HasSimpleIdConverter#init(Class, String),
   * that throws this exception
   * then entity's ID type is not of Integer/Long/String.</p>
   **/
  public static final int WRONG_ASSEMBLE = 1004;

  /**
   * <p>When factory application beans has been changed
   * during runtime then it lock factory for clients.</p>
   **/
  public static final int SYSTEM_RECONFIGURING = 1005;

  /**
   * <p>Code.</p>
   **/
  private int code;

  /**
   * <p>Short message, it's usually i18n message code.</p>
   **/
  private String shortMessage;

  /**
   * <p>Add message, it's usually user name whose action
   * cause exception.</p>
   **/
  private String addMessage;

  /**
   * <p>Constructor default.</p>
   **/
  public ExceptionWithCode() {
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   **/
  public ExceptionWithCode(final int pCode) {
    this.code = pCode;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pCause parent exception
   **/
  public ExceptionWithCode(final int pCode, final Throwable pCause) {
    super(pCause);
    this.code = pCode;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pMsg message
   **/
  public ExceptionWithCode(final int pCode, final String pMsg) {
    super(pMsg);
    this.code = pCode;
    this.shortMessage = pMsg;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pMsg message
   * @param pAddMsg add message
   **/
  public ExceptionWithCode(final int pCode, final String pMsg,
    final String pAddMsg) {
    super(pMsg);
    this.code = pCode;
    this.shortMessage = pMsg;
    this.addMessage = pAddMsg;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pMsg message
   * @param pCause parent exception
   **/
  public ExceptionWithCode(final int pCode, final String pMsg,
    final Throwable pCause) {
    super(pMsg, pCause);
    this.code = pCode;
    this.shortMessage = pMsg;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pMsg message
   * @param pAddMsg add message
   * @param pCause parent exception
   **/
  public ExceptionWithCode(final int pCode, final String pMsg,
    final String pAddMsg, final Throwable pCause) {
    super(pMsg, pCause);
    this.code = pCode;
    this.shortMessage = pMsg;
    this.addMessage = pAddMsg;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for code.</p>
   * @return int
   **/
  public final int getCode() {
    return this.code;
  }

  /**
   * <p>Setter for code.</p>
   * @param pCode reference
   **/
  public final void setCode(final int pCode) {
    this.code = pCode;
  }

  /**
   * <p>Getter for shortMessage.</p>
   * @return String
   **/
  public final String getShortMessage() {
    return this.shortMessage;
  }

  /**
   * <p>Setter for shortMessage.</p>
   * @param pShortMessage reference
   **/
  public final void setShortMessage(final String pShortMessage) {
    this.shortMessage = pShortMessage;
  }

  /**
   * <p>Getter for addMessage.</p>
   * @return String
   **/
  public final String getAddMessage() {
    return this.addMessage;
  }

  /**
   * <p>Setter for addMessage.</p>
   * @param pAddMessage reference
   **/
  public final void setAddMessage(final String pAddMessage) {
    this.addMessage = pAddMessage;
  }
}
