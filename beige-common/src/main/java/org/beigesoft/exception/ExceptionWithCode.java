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
   * <p>Code.</p>
   **/
  private int code;

  /**
   * <p>Short message (code).</p>
   **/
  private String shortMessage;

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
   * @param pCause parent exception
   **/
  public ExceptionWithCode(final int pCode, final String pMsg,
    final Throwable pCause) {
    super(pMsg, pCause);
    this.code = pCode;
    this.shortMessage = pMsg;
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
}
