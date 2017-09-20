package org.beigesoft.filter;

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

import java.io.File;

import org.beigesoft.service.ISrvI18n;

/**
 * <p>Implementation of file filter for
 * "file is directory or file extension is ".
 * Directory is accepted cause go through them to desired file.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FilterFileExtensionIs implements IFilter<File> {

  /**
   * <p>Array of file extentions e.g. {"doc", "xls"}.</p>
   **/
  private String[] filterExtentions;

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>Default constructor (must present).</p>
   **/
  public FilterFileExtensionIs() {
  }

  /**
   * <p>Usable constructor with filter parameter.</p>
   * @param pFilterExtentions - array of extentions
   **/
  public FilterFileExtensionIs(final String[] pFilterExtentions) {
    setFilterExtentionsUtil(pFilterExtentions);
  }

  @Override
  public final boolean isAccepted(final File fl) {
    if (fl.isDirectory()) {
      return true;
    }
    boolean isAllow = false;
    for (String ext : this.filterExtentions) {
      if (fl.getName().endsWith("." + ext)) {
        isAllow = true;
        break;
      }
    }
    return isAllow;
  }

  @Override
  public final String toString() {
    if (getFilterExtentions() == null) {
      return "Filter file extentions = null";
    }
    StringBuffer descr = new StringBuffer(getFilterExtentions()[0]);
    for (int i = 1; i < getFilterExtentions().length; i++) {
      descr.append("/" + getFilterExtentions()[i]);
    }
    if (getSrvI18n() != null) {
      return getSrvI18n().getMsg("file_name_extentions_is") + descr.toString();
    }
    return "File name extentions is " + descr.toString();
  }

  //Non-standard setters and getters:
  /**
   * <p>Setter of filterExtentions.</p>
   * @param pFilterExtentions reference
   **/
  public final void setFilterExtentions(final String[] pFilterExtentions) {
    setFilterExtentionsUtil(pFilterExtentions);
  }

  /**
   * <p>Geter of filterExtentions.</p>
   * @return String[]
   **/
  public final String[] getFilterExtentions() {
    if (this.filterExtentions == null) {
      return null;
    } else {
      String[] lFilterExtentions = new String[this.filterExtentions.length];
      System.arraycopy(this.filterExtentions, 0, lFilterExtentions,
        0, this.filterExtentions.length);
      return lFilterExtentions;
    }
  }

  //Utils:
  /**
   * <p>Util for setter and constructor of filterExtentions.</p>
   * @param pFilterExtentions reference
   **/
  private void setFilterExtentionsUtil(final String[] pFilterExtentions) {
    if (pFilterExtentions == null) {
      this.filterExtentions = null;
    } else {
      this.filterExtentions = new String[pFilterExtentions.length];
      System.arraycopy(pFilterExtentions, 0, this.filterExtentions,
        0, pFilterExtentions.length);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter of srvI18n.</p>
   * @return ISrvI18n
   **/
  public final ISrvI18n getSrvI18n() {
    return this.srvI18n;
  }

  /**
   * <p>Setter of srvI18n.</p>
   * @param pSrvI18n reference
   **/
  public final void setSrvI18n(final ISrvI18n pSrvI18n) {
    this.srvI18n = pSrvI18n;
  }
}
