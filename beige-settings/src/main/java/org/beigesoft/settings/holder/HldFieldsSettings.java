package org.beigesoft.settings.holder;

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

import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.settings.IMngSettings;

/**
 * <p>Delegate that get field setting from Beige-Settings,
 * e.g. converter's to/from string name.</p>
 *
 * @author Yury Demidenko
 */
public class HldFieldsSettings
  implements IHolderForClassByName<String> {

  /**
   * <p>Manager settings.</p>
   **/
  private IMngSettings mngSettings;

  /**
   * <p>Setting name.</p>
   **/
  private final String settingName;

  /**
   * <p>Only constructor.</p>
   * @param pSettingName reference
   **/
  public HldFieldsSettings(final String pSettingName) {
    this.settingName = pSettingName;
  }

  /**
   * <p>Get thing for given class and field name.</p>
   * @param pClass a Class
   * @param pFieldName Field Name
   * @return a thing
   **/
  @Override
  public final String getFor(final Class<?> pClass,
    final String pFieldName) {
    String result;
    try {
      result = this.mngSettings.getFieldsSettings().get(pClass).get(pFieldName)
        .get(this.settingName);
    } catch (Exception e) {
      throw new RuntimeException(
        "Can't get class/field/setting: " + pClass + "/"
          + pFieldName + "/" + this.settingName);
    }
    return result;
  }

  /**
   * <p>Set thing for given class and field name.</p>
   * @param pThing Thing
   * @param pClass Class
   * @param pFieldName Field Name
   **/
  @Override
  public final void setFor(final String pThing,
    final Class<?> pClass, final String pFieldName) {
    this.mngSettings.getFieldsSettings().get(pClass).get(pFieldName)
      .put(this.settingName, pThing);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for mngSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngSettings() {
    return this.mngSettings;
  }

  /**
   * <p>Setter for mngSettings.</p>
   * @param pMngSettings reference
   **/
  public final void setMngSettings(final IMngSettings pMngSettings) {
    this.mngSettings = pMngSettings;
  }

  /**
   * <p>Getter for settingName.</p>
   * @return String
   **/
  public final String getSettingName() {
    return this.settingName;
  }
}
