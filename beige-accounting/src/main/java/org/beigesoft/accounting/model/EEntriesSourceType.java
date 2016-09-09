package org.beigesoft.accounting.model;

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
 * Entries SQL Source Type - DOCUMENT/DRAW_ITEM_ENTRY_BY_DOCUMENT/
 * DRAW_ITEM_ENTRY_BY_DOCUMENT_LINE.
 * This is to resolve dynamic filter for source ID,
 * e.g. where either PURCHASEINVOICE.ITSID=1 or DRAWINGOWNERID=1
 * </pre>
 *
 * @author Yury Demidenko
 */
 public enum EEntriesSourceType {

  /**
   * <p>SQL query for ordinal document.</p>
   **/
  DOCUMENT,

  /**
   * <p>SQL query for draw item entry that made by a document.</p>
   **/
  DRAW_ITEM_ENTRY_BY_DOCUMENT,

  /**
   * <p>SQL query for draw item entry that made by a document line.</p>
   **/
  DRAW_ITEM_ENTRY_BY_DOCUMENT_LINE;
}
