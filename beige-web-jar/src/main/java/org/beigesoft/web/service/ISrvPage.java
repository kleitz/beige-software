package org.beigesoft.web.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.List;

import org.beigesoft.web.model.Page;

/**
 * <p>Page service for pagination.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvPage {

  /**
   * <p>Evaluate pages list.</p>
   * <pre>
   * example:
   * 1 ... 13 14 15 currPg-16 17 18 19 ... 42
   * currPg-1 2 3 4 ...  42
   * 1 currPg-2 3 4 5 ...  42
   * 1 ... 37 38 39 currPg-40 41 42
   * 1 2 3 currPg-4 5 6
   * </pre>
   * @param pCurrentPageNo current page #
   * @param pTotalPages total pages
   * @param pTailSize quantity of pages after and before current page
   * @return List<Page> list of pages
   **/
  List<Page> evalPages(int pCurrentPageNo, int pTotalPages, int pTailSize);

  /**
   * <p>Eval page count.</p>
   * @param pRowCount Row Count
   * @param pItemsPerPage Items Per Page
   * @return int count of pages
   **/
  int evalPageCount(int pRowCount, int pItemsPerPage);
}
