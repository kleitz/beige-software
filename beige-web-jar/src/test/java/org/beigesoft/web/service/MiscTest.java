package org.beigesoft.web.service;

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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * <p>Miscellaneous tests.</p>
 *
 * @author Yury Demidenko
 */
public class MiscTest {

  @Test
  public void tst1(){
    String urlSource = "http://localhost:8080/beige-accounting-web/secure/sendEntities";
    String urlBase = urlSource.substring(0, urlSource.indexOf("secure") - 1);
    assertEquals("http://localhost:8080/beige-accounting-web", urlBase);
  }
}
