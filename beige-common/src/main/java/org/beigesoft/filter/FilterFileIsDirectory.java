package org.beigesoft.filter;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
