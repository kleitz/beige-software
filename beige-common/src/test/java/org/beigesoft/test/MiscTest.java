package org.beigesoft.test;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * <p>Java misc Tests.
 * </p>
 *
 * @author Yury Demidenko
 */
public class MiscTest {

  @Test
  public void test1() throws Exception {
    Integer integerVal = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("integerVal", integerVal);
    int intVal = 999;
    boolean wasException = false;
    try {
      intVal = (Integer) params.get("integerVal");
    } catch (Exception e) {
      wasException = true;
    }
    assertTrue(wasException);
    assertEquals(999, intVal);
  }
}
