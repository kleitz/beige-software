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
 * <p>Implementation of file filter for "file is directory or file name is ".
 * Directory is accepted cause go through them to desired file.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FilterFileNameIs implements IFilter<File> {

  /**
   * <p>File name that accepted.</p>
   **/
  private String filterNameIs;

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>Default constructor (must present).</p>
   **/
  public FilterFileNameIs() {
  }

  /**
   * <p>Usable constructor with filter parameter.</p>
   * @param pFilterNameIs - file name
   **/
  public FilterFileNameIs(final String pFilterNameIs) {
    this.filterNameIs = pFilterNameIs;
  }

  @Override
  public final String toString() {
    if (getSrvI18n() != null) {
      return getSrvI18n().getMsg("file_name_is") + filterNameIs;
    }
    return "File name is " + filterNameIs;
  }

  /**
   * <p>Examine file for "file is directory".</p>
   * @param fl - file to be examined
   * @return boolean - is directory
   **/
  @Override
  public final boolean isAccepted(final File fl) {
    return this.filterNameIs == null || fl != null
      && (fl.isDirectory() || this.filterNameIs.equals(fl.getName()));
  }

  //Simple getters and setters:
  /**
   * <p>Geter of filterNameIs.</p>
   * @return String
   **/
  public final String getFilterNameIs() {
    return this.filterNameIs;
  }

  /**
   * <p>Setter of filterNameIs.</p>
   * @param pFilterNameIs reference/value
   **/
  public final void setFilterNameIs(final String pFilterNameIs) {
    this.filterNameIs = pFilterNameIs;
  }

  /**
   * <p>Geter of srvI18n.</p>
   * @return ISrvI18n
   **/
  public final ISrvI18n getSrvI18n() {
    return this.srvI18n;
  }

  /**
   * <p>Setter of srvI18n.</p>
   * @param pSrvI18n reference/value
   **/
  public final void setSrvI18n(final ISrvI18n pSrvI18n) {
    this.srvI18n = pSrvI18n;
  }
}
