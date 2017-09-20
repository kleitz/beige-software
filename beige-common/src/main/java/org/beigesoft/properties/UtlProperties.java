package org.beigesoft.properties;

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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.URL;

/**
 * <p>Simple helper to working with XML properties.
 * </p>
 *
 * @author Yury Demidenko
 */
public class UtlProperties {

  // Constants:
  /**
   * <p>Null value string constant.</p>
   * @return null
   **/
  public final String constNull() {
    return "null";
  }

  // Utils:
  /**
   * <p>Evaluate string array(include non-unique) properties
   * from string with comma delimeter
   * and removed new lines and trailing spaces.</p>
   * @param pSource string
   * @return String[] array
   **/
  public final String[] evalPropsStringsArray(final String pSource) {
    List<String> resultList = evalPropsStringsList(pSource);
    return resultList.toArray(new String[resultList.size()]);
  }

  /**
   * <p>Evaluate string set properties
   * from string with comma delimeter
   * and removed new lines and trailing spaces.</p>
   * @param pSource string
   * @return LinkedHashSet<String> properties set
   **/
  public final LinkedHashSet<String> evalPropsStringsSet(final String pSource) {
    String sourceCorr = pSource.replace("\n", "");
    LinkedHashSet<String> resultSet = new LinkedHashSet<String>();
    for (String str : sourceCorr.split(",")) {
      resultSet.add(str.trim());
    }
    return resultSet;
  }

  /**
   * <p>Evaluate string list(include non-unique) properties
   * from string with comma delimeter
   * and removed new lines and trailing spaces.</p>
   * @param pSource string
   * @return List<String> properties set
   **/
  public final List<String> evalPropsStringsList(final String pSource) {
    String sourceCorr = pSource.replace("\n", "");
    List<String> resultList = new ArrayList<String>();
    for (String str : sourceCorr.split(",")) {
      resultList.add(str.trim());
    }
    return resultList;
  }

  /**
   * <p>Load properties from XML file.</p>
   * @param pFileName file name
   * @return props properties
   * @throws Exception - an exception
   **/
  public final LinkedProperties
    loadPropertiesXml(final String pFileName)
        throws Exception {
    LinkedProperties props = null;
    URL urlSettingXml = UtlProperties.class
      .getResource(pFileName);
    if (urlSettingXml != null) {
      props = new LinkedProperties();
      InputStream inputStream = null;
      try {
        inputStream = UtlProperties.class.getResourceAsStream(pFileName);
        props.loadFromXML(inputStream);
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return props;
  }

  /**
   * <p>Evaluate null if value is string "null".</p>
   * @param pProperties properties
   * @param pPropName properties
   * @return String string or NULL
   **/
  public final String evalPropVal(final LinkedProperties pProperties,
    final String pPropName) {
    String result = pProperties.getProperty(pPropName);
    if (constNull().equals(result)) {
      return null;
    }
    return result;
  }
}
