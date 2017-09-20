package org.beigesoft.test;

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
