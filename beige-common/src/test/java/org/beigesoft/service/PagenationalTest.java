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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * <p>Pagination tests.</p>
 *
 * @author Yury Demidenko
 */
public class PagenationalTest {

  /**
   * <p>Page service.</p>
   **/
  private final SrvPage srvPage = new SrvPage();
  
  @Test
  public void tst1(){
    assertEquals(1, srvPage .evalPageCount(14, 15));
    assertEquals(1, srvPage .evalPageCount(0, 15));
    assertEquals(1, srvPage .evalPageCount(15, 15));
    assertEquals(2, srvPage .evalPageCount(16, 15));
    //must be:
    //1 ... 11 12 13 14pg 15
    assertEquals(7, srvPage .evalPages(14, 15, 3).size());
    assertEquals("1", srvPage.evalPages(14, 15, 3).get(0).getValue());
    assertEquals("...", srvPage.evalPages(14, 15, 3).get(1).getValue());
    assertEquals("11", srvPage.evalPages(14, 15, 3).get(2).getValue());
    assertEquals(false, srvPage.evalPages(14, 15, 3).get(2).getIsCurrent());
    assertEquals("12", srvPage.evalPages(14, 15, 3).get(3).getValue());
    assertEquals("13", srvPage.evalPages(14, 15, 3).get(4).getValue());
    assertEquals("14", srvPage.evalPages(14, 15, 3).get(5).getValue());
    assertEquals(true, srvPage.evalPages(14, 15, 3).get(5).getIsCurrent());
    assertEquals("15", srvPage.evalPages(14, 15, 3).get(6).getValue());
  }
  
  @Test
  public void tst2(){
    //must be:
    //1pg 2 3
    assertEquals(3, srvPage.evalPages(1, 3, 3).size());
    assertEquals("1", srvPage.evalPages(1, 3, 3).get(0).getValue());
    assertEquals(true, srvPage.evalPages(1, 3, 3).get(0).getIsCurrent());
    assertEquals("2", srvPage.evalPages(1, 3, 3).get(1).getValue());
    assertEquals(false, srvPage.evalPages(1, 3, 3).get(1).getIsCurrent());
    assertEquals("3", srvPage.evalPages(1, 3, 3).get(2).getValue());
  }
}
