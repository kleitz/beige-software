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
 * <p>Implementation of file filter for "file is directory".</p>
 *
 * @author Yury Demidenko
 */
public class FilterFileIsDirectory implements IFilter<File> {

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  @Override
  public final String toString() {
    if (getSrvI18n() != null) {
      return getSrvI18n().getMsg("directory_only");
    }
    return "Directory only";
  }

  /**
   * <p>Examine file for "file is directory".</p>
   * @param fl - file to be examined
   * @return boolean - is directory
   **/
  @Override
  public final boolean isAccepted(final File fl) {
    return fl.isDirectory();
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
