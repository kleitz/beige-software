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

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.beigesoft.persistable.APersistableBase;

/**
 * <p>Test for simple reflection service.
 * </p>
 *
 * @author Yury Demidenko
 */
public class UtlReflectionTest {

  @Test
  public void test1() throws Exception {
    UtlReflection utlReflection = new UtlReflection();
    Field[] fieldsArr = utlReflection.retrieveFields(org.beigesoft.persistable.UserJetty.class);
    /*for (Field fld : fieldsArr) {
      System.out.println(fld.getName());
    }*/
    assertEquals(6, fieldsArr.length);
  }
}
