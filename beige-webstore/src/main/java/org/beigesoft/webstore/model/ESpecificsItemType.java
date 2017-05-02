package org.beigesoft.webstore.model;

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
 * <pre>
 * Specifics Type described how to treat (edit/print/filter) assigned specifics.
 * </pre>
 *
 * @author Yury Demidenko
 */
public enum ESpecificsItemType {

  /**
   * <p>0, default, printed as text.</p>
   **/
  TEXT,

  /**
   * <p>1, for specifics like "Weight", stringValue
   * may hold unit of measure.</p>
   **/
  BIGDECIMAL,

  /**
   * <p>2, for specifics like "MemorySize",
   * stringValue may hold unit of measure.</p>
   **/
  INTEGER,

  /**
   * <p>3, stringValue1 hold URL to image,
   * stringValue2 - uploaded file path if it was uploaded.</p>
   **/
  IMAGE,

  /**
   * <p>4, stringValue1 hold URL to image,
   * stringValue2 - uploaded file path if it was uploaded.
   * Image that belongs to set of images ordered and gathered
   * (they must have adjacent indexes) by itsIndex,
   * longValue1 may hold "showSizeTypeClass".</p>
   **/
  IMAGE_IN_SET,

  /**
   * <p>5, stringValue1 hold URL to file, e.g. "get brochure",
   * stringValue2 - uploaded file path if it was uploaded.</p>
   **/
  FILE,

  /**
   * <p>6, show it on page, stringValue1 hold URL to file e.g. a PDF,
   * stringValue2 - uploaded file path if it was uploaded,
   * longValue1 may hold "showSizeTypeClass", e.g. class=1 means
   * show 30% of page size.</p>
   **/
  FILE_EMBEDDED,

  /**
   * <p>7, stringValue1 hold URL.</p>
   **/
  LINK,

  /**
   * <p>8, show HTML page. stringValue1 hold URL,
   * longValue1 may hold "showSizeClass".</p>
   **/
  LINK_EMBEDDED,

  /**
   * <p>10, longValue1 hold ID of chosen from list of ChooseableSpecifics,
   * stringValue1 hold appearance to improve performance,
   * and so does longValue2 - ChooseableSpecificsType.
   * This is the mostly used method.</p>
   **/
  CHOOSEABLE_SPECIFICS;
}
