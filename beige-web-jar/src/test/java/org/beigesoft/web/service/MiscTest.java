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
