package org.beigesoft.service;

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
