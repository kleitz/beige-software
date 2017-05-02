package org.beigesoft.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.ArrayList;
import java.util.List;

import org.beigesoft.model.Page;


/**
 * <p>Page service for pagination.</p>
 *
 * @author Yury Demidenko
 */
public class SrvPage implements ISrvPage {

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
  @Override
  public final List<Page> evalPages(final int pCurrentPageNo,
    final int pTotalPages, final int pTailSize) {
    List<Page> pages = new ArrayList<Page>();
    boolean wasLeft3dots = false;
    boolean wasRight3dots = false;
    for (Integer i = 1; i <= pTotalPages; i++) {
      if (i == 1 || i == pTotalPages) {
        pages.add(new Page(i.toString(), pCurrentPageNo == i));
      } else if ((pCurrentPageNo - i) >= 0
          && (pCurrentPageNo - i) <= pTailSize) {
        pages.add(new Page(i.toString(), pCurrentPageNo == i));
      } else if ((pCurrentPageNo - i) < 0
          && (i - pCurrentPageNo) <= pTailSize) {
        pages.add(new Page(i.toString(), pCurrentPageNo == i));
      } else if ((pCurrentPageNo - i) > 0 && !wasLeft3dots) {
        wasLeft3dots = true;
        pages.add(new Page("...", false));
      } else if ((pCurrentPageNo - i) < 0 && !wasRight3dots) {
        wasRight3dots = true;
        pages.add(new Page("...", false));
      }
    }
    return pages;
  }

  /**
   * <p>Eval page count.</p>
   **/
  @Override
  public final int evalPageCount(final int pRowCount, final int pItemsPerPage) {
    return (int) (pRowCount - 1) / pItemsPerPage + 1;
  }
}
