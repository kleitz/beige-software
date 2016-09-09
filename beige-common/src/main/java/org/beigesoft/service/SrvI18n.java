/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.beigesoft.service;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>Standard implementation of I18N service.</p>
 *
 * @author Yury Demidenko
 */
public class SrvI18n implements ISrvI18n {

  /**
   * <p>Ordinal resource bundle with messages.</p>
   **/
  private ResourceBundle messages;

  /**
   * <p>Default constructor that load message bundle by default name.</p>
   **/
  public SrvI18n() {
    try { //to fix Android Java
      messages = ResourceBundle.getBundle("MessagesBundle");
    } catch (Exception e) {
      Locale locale = new Locale("en", "US");
      messages = ResourceBundle.getBundle("MessagesBundle", locale);
    }
  }

  /**
   * <p>Constructor that load message bundle by given name.</p>
   * @param messagesBundleName messages bundle name
   **/
  public SrvI18n(final String messagesBundleName) {
    messages = ResourceBundle.getBundle(messagesBundleName);
  }

  /**
   * <p>Evaluate message by given key.</p>
   * @param key of message
   **/
  @Override
  public final String getMsg(final String key) {
    try {
      return messages.getString(key);
    } catch (Exception e) {
      return "[" + key + "]";
    }
  }
}
