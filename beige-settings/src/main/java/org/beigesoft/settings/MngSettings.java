package org.beigesoft.settings;

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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.net.URL;
import java.lang.reflect.Field;

import org.beigesoft.properties.LinkedProperties;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.comparator.CmprEntryValueStringInt;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.log.ILogger;

/**
 * <p>Make settings  for application, classes and its fields
 * according properties XML.
 * According Beige-Settings specification #2.</p>
 *
 * @author Yury Demidenko
 */
public class MngSettings implements IMngSettings {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>LinkedProperties service.</p>
   **/
  private UtlProperties utlProperties;

  /**
   * <p>All involved clases.</p>
   **/
  private LinkedHashSet<Class<?>> classes;

  /**
   * <p>Application settings.</p>
   */
  private Map<String, String> appSettings;

  /**
   * <p>Classes settings.</p>
   */
  private Map<Class<?>, Map<String, String>> classesSettings;

  /**
   * <p>Fields settings.</p>
   */
  private Map<Class<?>, Map<String, Map<String, String>>> fieldsSettings;

  /**
   * <p>Cached if show debug messages.</p>
   */
  private boolean isShowDebugMessages;

  /**
   * <p>Load configuration(settings) from given folder and file.</p>
   * @param pDirName base properties directory name
   * @param pFileName base properties file name
   * @throws Exception - an exception
   */
  @Override
  public final void loadConfiguration(final String pDirName,
    final String pFileName) throws Exception {
    this.isShowDebugMessages = this.logger
      .getIsShowDebugMessagesFor(getClass());
    String nameBaseXml = "/" + pDirName + "/"
      + pFileName;
    this.logger.info(null, MngSettings.class, "try to load: " + nameBaseXml);
    LinkedProperties props = this.utlProperties.loadPropertiesXml(nameBaseXml);
    if (props == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no  base configuration file "
        + nameBaseXml);
    }
    retrieveAppSettings(pDirName, props);
    this.classes = new LinkedHashSet<Class<?>>();
    String strClasses = props.getProperty(KEY_CLASSES);
    if (strClasses == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no property " + KEY_CLASSES
        + " in base configuration file!");
    }
    this.logger.info(null, MngSettings.class, "classes: "
        + strClasses);
    LinkedHashSet<String> classesNames = this.utlProperties
      .evalPropsStringsSet(strClasses);
    for (String className : classesNames) {
      this.classes.add(Class.forName(className));
    }
    retrieveClasesSettings(pDirName, props);
    retrieveFieldsSettings(pDirName, props);
  }

  /**
   * <p>Sorted entries and also removed those with negative value.</p>
   * @param pSetEntries Set<Map.Entry<String, Map<String, String>>> source
   * @param pOrderKey key of order property
   * @return List<Map.Entry<String, Map<String, String>>> list ordered according
   * entry.getValue().get(pOrderKey) that is Integer.toString()
   * @throws Exception - an exception
   */
  @Override
  public final List<Map.Entry<String, Map<String, String>>>
    sortRemoveIntVal(
      final Set<Map.Entry<String, Map<String, String>>> pSetEntries,
        final String pOrderKey) throws Exception {
    List<Map.Entry<String, Map<String, String>>> listEntries =
      new ArrayList<Map.Entry<String, Map<String, String>>>();
    for (Map.Entry<String, Map<String, String>> entry : pSetEntries) {
      Integer intVal = Integer.valueOf(entry.getValue()
        .get(pOrderKey));
      if (intVal >= 0) {
        listEntries.add(entry);
      }
    }
    CmprEntryValueStringInt cmpr = new CmprEntryValueStringInt(pOrderKey);
    Collections.sort(listEntries, cmpr);
    return listEntries;
  }

  /**
   * <p>Make for given class sorted entries of fields settings
   * and also removed those with negative value of sodted property.</p>
   * @param pClass entity class
   * @param pOrderKey key of order property
   * @return List<Map.Entry<String, Map<String, String>>> list ordered according
   * entry.getValue().get(pOrderKey) that is Integer.toString()
   * @throws Exception - an exception
   */
  @Override
  public final List<Map.Entry<String, Map<String, String>>> makeFldPropLst(
    final Class<?> pClass, final String pOrderKey) throws Exception {
    Map<String, Map<String, String>> map =
      this.fieldsSettings.get(pClass);
    if (map == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no entries of fields settings for "
          + pClass + " order key " + pOrderKey);
    }
    return sortRemoveIntVal(map.entrySet(), pOrderKey);
  }

  /**
   * <p>Get application settings.</p>
   * @return Map<String, String> application settings
   */
  @Override
  public final Map<String, String> getAppSettings() {
    return this.appSettings;
  }

  /**
   * <p>Set application settings.</p>
   * @param pAppSettings Map<String, String> application settings
   */
  @Override
  public final void setAppSettings(final Map<String, String> pAppSettings) {
    this.appSettings = pAppSettings;
  }

  /**
   * <p>Get all involved clases.</p>
   * @return LinkedHashSet<Class<?> all involved clase
   */
  @Override
  public final LinkedHashSet<Class<?>> getClasses() {
    return this.classes;
  }

  /**
   * <p>Set all involved classes.</p>
   * @param pClasses LinkedHashSet<Class<?> all involved clase
   */
  @Override
  public final void setClasses(final LinkedHashSet<Class<?>> pClasses) {
    this.classes = pClasses;
  }

  /**
   * <p>Get class settings.
   * This is Map "Class<?>" -
   * "Map<String, String>>"</p>
   * @return Map<Class<?>, Map<String, String>> class settings
   */
  @Override
  public final Map<Class<?>, Map<String, String>> getClassesSettings() {
    return this.classesSettings;
  }

  /**
   * <p>Set class settings.
   * This is Map "Class<?>" -
   * "Map<String, String>>"</p>
   * @param pClassSettings Map<Class<?>, Map<String, String>> class settings
   */
  @Override
  public final void setClassesSettings(
    final Map<Class<?>, Map<String, String>> pClassSettings) {
      this.classesSettings = pClassSettings;
  }

  /**
   * <p>Get fields settings.
   * This is Map "Class<?>" -
   * "Map "fieldName" - "Map "settingName"-"settingValue"""</p>
   * @return Map<Class<?>, Map<String, Map<String, String>>> fields settings
   */
  @Override
  public final Map<Class<?>, Map<String, Map<String, String>>>
    getFieldsSettings() {
    return this.fieldsSettings;
  }

  /**
   * <p>Set fields settings.
   * This is Map "Class<?>" -
   * "Map "fieldName" - "Map "settingName"-"settingValue"""</p>
   * @param pFieldsSettings Map<Class<?>, Map<String, Map<String, String>>>
   *  fields settings
   */
  @Override
  public final void setFieldsSettings(
    final Map<Class<?>, Map<String, Map<String, String>>> pFieldsSettings) {
    this.fieldsSettings = pFieldsSettings;
  }

  //Utils:
  /**
   * <p>Retrieve app settings from XML propeties files.</p>
   * @param pDirName base properties directory name
   * @param pProps - base properties
   * @throws Exception - an exception
   */
  public final void retrieveAppSettings(final String pDirName,
    final LinkedProperties pProps) throws Exception {
    this.appSettings =
      new HashMap<String, String>();
    String subFolder = evalSubfolder(pProps);
    String nameAppStXml = "/" + pDirName
      + "/" + subFolder + APP_SETTINGS_FILE_NAME;
    URL urlAppStXml = MngSettings.class
      .getResource(nameAppStXml);
    if (urlAppStXml != null) {
      LinkedProperties propAppStXml = this.utlProperties
        .loadPropertiesXml(nameAppStXml);
      for (String settingName : propAppStXml.stringPropertyNames()) {
        String settingVal = propAppStXml.getProperty(settingName);
        if (this.isShowDebugMessages) {
          this.logger.debug(null, MngSettings.class, "add app setting: "
            + settingName + " - " + settingVal);
        }
        this.appSettings.put(settingName,
          settingVal);
      }
    }
  }

  /**
   * <p>Retrieve classes settings from XML propeties files.</p>
   * @param pDirName base properties directory name
   * @param pProps - base properties
   * @throws Exception - an exception
   */
  public final void retrieveClasesSettings(final String pDirName,
    final LinkedProperties pProps) throws Exception {
    this.classesSettings =
      new HashMap<Class<?>, Map<String, String>>();
    Map<String, LinkedProperties> javaTypeToStMap
      = new HashMap<String, LinkedProperties>();
    String subFolder = evalSubfolder(pProps);
    String strClasses = pProps.getProperty(KEY_CLASS_SETTINGS_NAMES);
    if (strClasses == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no property " + KEY_CLASS_SETTINGS_NAMES
          + " in base configuration file!");
    }
    if (this.isShowDebugMessages) {
      this.logger.debug(null, MngSettings.class, "classes settings keys: "
        + strClasses);
    }
    LinkedHashSet<String> settingsNames = this.utlProperties
      .evalPropsStringsSet(strClasses);
    for (String settingName : settingsNames) {
      LinkedProperties propJaTyToCs = this.utlProperties.loadPropertiesXml(
        "/" + pDirName + "/" + subFolder
          + FOLDER_CLASS_TYPE_TO_CS + "/" + settingName + ".xml");
      if (propJaTyToCs != null) {
        if (this.isShowDebugMessages) {
          this.logger.debug(null, MngSettings.class, "add class setting file: "
            + settingName + " in " + FOLDER_CLASS_TYPE_TO_CS);
        }
        javaTypeToStMap.put(settingName, propJaTyToCs);
      }
    }
    for (Class<?> clazz : classes) {
      Map<String, String> classSettings = new HashMap<String, String>();
      LinkedProperties propsClassToSetting = this.utlProperties
        .loadPropertiesXml("/" + pDirName + "/"
          + subFolder + FOLDER_CLASS_TO_CS + "/"
            + clazz.getSimpleName() + ".xml");
      if (propsClassToSetting != null
        && this.isShowDebugMessages) {
        this.logger.debug(null, MngSettings.class, "add class setting file: "
          + clazz.getSimpleName() + " in " + FOLDER_CLASS_TO_CS);
      }
      for (String settingName : settingsNames) {
        if (propsClassToSetting != null) {
          putValueIfExist(propsClassToSetting, settingName,
            settingName, classSettings, clazz.getSimpleName());
        }
        if (!classSettings.containsKey(settingName)) {
          LinkedProperties jaTyToCsProp = javaTypeToStMap.get(settingName);
          if (jaTyToCsProp != null) {
            putValueIfExistByType(jaTyToCsProp, clazz, settingName,
              classSettings, clazz.getSimpleName());
          }
        }
      }
      this.classesSettings.put(clazz, classSettings);
    }
  }

  /**
   * <p>Put value(include null) to map if assignable by type in XML.</p>
   * @param pProps - properties exactly "javaType"-"propertyValue"
   * @param pClazzKey - property key e.g. java.lang.Integer
   * @param pSettingName - setting name e.g.
   * @param pSettings - settings
   * @param pDebugMsg Debug Message
   * @throws Exception - an exception
   */
  public final void putValueIfExistByType(final LinkedProperties pProps,
    final Class<?> pClazzKey, final String pSettingName,
      final Map<String, String> pSettings,
        final String pDebugMsg) throws Exception {
    if (!putValueIfExist(pProps, pClazzKey.getCanonicalName(),
      pSettingName, pSettings, pDebugMsg)) {
      //check superclass, ORDER in file is essential,
      //the LAST value will be used
      for (String javaTypeName : pProps.getOrderedKeys()) {
        Class<?> javaTypeClass = Class.forName(javaTypeName);
        if (javaTypeClass.isAssignableFrom(pClazzKey)) {
          String settingVal = this.utlProperties
            .evalPropVal(pProps, javaTypeName);
          if (this.isShowDebugMessages) {
            this.logger.debug(null, MngSettings.class, pDebugMsg
              + " add setting by type : " + pSettingName + " - "
                + settingVal);
          }
          pSettings.put(pSettingName,
            settingVal);
        }
      }
    }
  }

  /**
   * <p>Put value(include null) to map if exist in XML by key.</p>
   * @param pProps - properties
   * @param pPropKey - property key e.g. java.lang.Integer
   * @param pSettingName - setting name e.g.
   * @param pSettings - settings
   * @param pDebugMsg Debug Message
   * @return boolean - if has put
   */
  public final boolean putValueIfExist(final LinkedProperties pProps,
    final String pPropKey, final String pSettingName,
      final Map<String, String> pSettings,
        final String pDebugMsg) {
    String settingValue = pProps.getProperty(pPropKey);
    if (settingValue != null) {
      if (this.utlProperties.constNull().equals(settingValue)) {
        settingValue = null;
      }
      if (this.isShowDebugMessages) {
        this.logger.debug(null, MngSettings.class, pDebugMsg + " add setting : "
          + pSettingName + " - " + settingValue);
      }
      pSettings.put(pSettingName, settingValue);
      return true;
    }
    return false;
  }

  /**
   * <p>Retrieve subfolder.</p>
   * @param pProps - base properties
   * @return empty string or [dirname]+[fileSeparator]
   */
  public final String evalSubfolder(final LinkedProperties pProps) {
    String subFolder = this.utlProperties.evalPropVal(pProps,
      KEY_USE_SUB_FOLDER);
    if (subFolder == null) {
      subFolder = "";
    } else {
      subFolder += "/";
    }
    return subFolder;
  }

  /**
   * <p>Retrieve fields settings from XML propeties files.</p>
   * @param pDirName base properties directory name
   * @param pProps - base properties
   * @throws Exception - an exception
   */
  public final void retrieveFieldsSettings(final String pDirName,
    final LinkedProperties pProps) throws Exception {
    this.fieldsSettings =
      new HashMap<Class<?>, Map<String, Map<String, String>>>();
    String subFolder = evalSubfolder(pProps);
    Map<String, LinkedProperties> javaTypeToStMap =
      new HashMap<String, LinkedProperties>();
    Map<String, LinkedProperties> fieldNameToStMap =
      new HashMap<String, LinkedProperties>();
    Map<String, LinkedProperties> fldNmJaTyToFsMap =
      new HashMap<String, LinkedProperties>();
    Map<String, LinkedProperties> fldNmClassTyToFsMap =
      new HashMap<String, LinkedProperties>();
    String excludedFieldsStr = pProps.getProperty(KEY_EXCLUDED_FIELDS);
    LinkedHashSet<String> excludedFields = null;
    if (excludedFieldsStr != null) {
      excludedFields = this.utlProperties
        .evalPropsStringsSet(excludedFieldsStr);
    }
    String strFields = pProps.getProperty(KEY_FIELD_SETTINGS_NAMES);
    if (strFields == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no property "
        + KEY_FIELD_SETTINGS_NAMES + " in base configuration file!");
    }
    if (this.isShowDebugMessages) {
      this.logger.debug(null, MngSettings.class, "fields settings keys: "
        + strFields);
    }
    LinkedHashSet<String> settingsNames = this.utlProperties
      .evalPropsStringsSet(strFields);
    for (String settingName : settingsNames) {
      LinkedProperties propJaTyToSt = this.utlProperties
        .loadPropertiesXml("/" + pDirName
        + "/" + subFolder + FOLDER_FIELD_TYPE_TO_FS
          + "/" + settingName + ".xml");
      if (propJaTyToSt != null) {
        if (this.isShowDebugMessages) {
          this.logger.debug(null, MngSettings.class, "add field setting file: "
            + settingName + " in " + FOLDER_FIELD_TYPE_TO_FS);
        }
        javaTypeToStMap.put(settingName, propJaTyToSt);
      }
      LinkedProperties propFldNmToSt = this.utlProperties
        .loadPropertiesXml("/" + pDirName
        + "/" + subFolder + FOLDER_FIELD_NAME_TO_FS
          + "/" + settingName + ".xml");
      if (propFldNmToSt != null) {
        if (this.isShowDebugMessages) {
          this.logger.debug(null, MngSettings.class, "add field setting file: "
            + settingName + " in " + FOLDER_FIELD_NAME_TO_FS);
        }
        fieldNameToStMap.put(settingName, propFldNmToSt);
      }
    }
    for (Class<?> clazz : this.classes) {
      Map<String, Map<String, String>> fieldsDescriptor =
        new HashMap<String, Map<String, String>>();
      LinkedProperties propsClassToSetting = this.utlProperties
          .loadPropertiesXml("/" + pDirName
        + "/" + subFolder + FOLDER_CLASS_TO_FS
          + "/" + clazz.getSimpleName() + ".xml");
      Field[] fields = getUtlReflection().retrieveFields(clazz);
      if (propsClassToSetting != null
        && this.isShowDebugMessages) {
        this.logger.debug(null, MngSettings.class, "add field setting file: "
          + clazz.getSimpleName() + " in " + FOLDER_CLASS_TO_FS);
      }
      for (Field field : fields) {
        String strExldFldForClass = getClassesSettings().get(clazz)
          .get(KEY_EXCLUDED_FIELDS);
        if (!java.util.Collection.class.isAssignableFrom(field.getType())
          && (strExldFldForClass == null || !strExldFldForClass
            .contains(field.getName())) && (excludedFields == null
              || !excludedFields.contains(field.getName()))) {
          Map<String, String> fldSettings =
            new HashMap<String, String>();
          for (String settingName : settingsNames) {
            if (propsClassToSetting != null) {
              putValueIfExist(propsClassToSetting, field.getName()
                + settingName, settingName, fldSettings, "final for "
                  + clazz.getSimpleName() + "->" + field.getName());
            }
            if (!fldSettings.containsKey(settingName)) {
              LinkedProperties propJaTyToFs = javaTypeToStMap.get(settingName);
              if (propJaTyToFs != null) { //1st
                Class<?> fieldType = field.getType();
                putValueIfExistByType(propJaTyToFs, fieldType,
                  settingName, fldSettings, "1st for " + clazz.getSimpleName()
                    + "->" + field.getName());
              }
              LinkedProperties propFldNmToFs = fieldNameToStMap
                .get(settingName);
              if (propFldNmToFs != null) { //2nd
                putValueIfExist(propFldNmToFs, field.getName(),
                  settingName, fldSettings, "2nd for " + clazz.getSimpleName()
                    + "->" + field.getName());
              }
              String nmFlJavaTypeToFs = "/" + pDirName
                + "/" + subFolder + DIR_FIELD_NAME_FIELD_TYPE_TO_FS
                  + "/" + field.getName() + settingName + ".xml";
              LinkedProperties propFldNmJaTyToFs = evalProps(fldNmJaTyToFsMap,
                field.getName() + settingName, nmFlJavaTypeToFs);
              if (propFldNmJaTyToFs != null) { //3d
                Class<?> fieldType = field.getType();
                putValueIfExistByType(propFldNmJaTyToFs, fieldType,
                  settingName, fldSettings, "3d for " + clazz.getSimpleName()
                    + "->" + field.getName());
              }
              String nmFlClassTypeToFs = "/" + pDirName
                + "/" + subFolder + DIR_FIELD_NAME_CLASS_TYPE_TO_FS
                  + "/" + field.getName() + settingName + ".xml";
              LinkedProperties propFldNmClassTyToFs = evalProps(
                fldNmClassTyToFsMap, field.getName() + settingName,
                  nmFlClassTypeToFs);
              if (propFldNmClassTyToFs != null) { //4th
                putValueIfExistByType(propFldNmClassTyToFs, clazz,
                  settingName, fldSettings, "4th for " + clazz.getSimpleName()
                    + "->" + field.getName());
              }
            }
          }
          //useful addition for enums:
          if (Enum.class.isAssignableFrom(field.getType())) {
            StringBuffer sbEnumVals = new StringBuffer();
            boolean isFirst = true;
            for (Object enm : field.getType().getEnumConstants()) {
              if (!isFirst) {
                sbEnumVals.append(",");
              } else {
                isFirst = false;
              }
              sbEnumVals.append(enm.toString());
            }
            fldSettings.put("enumValues", sbEnumVals.toString());
          }
          fieldsDescriptor.put(field.getName(), fldSettings);
        }
      }
      this.fieldsSettings.put(clazz, fieldsDescriptor);
    }
  }

  /**
   * <p>Load properties and put them into map.</p>
   * @param propsMap props Map
   * @param pKey Key
   * @param pFileName File Name
   * @return LinkedProperties LinkedProperties
   * @throws Exception - an exception
   **/
  public final LinkedProperties evalProps(
    final Map<String, LinkedProperties> propsMap,
      final String pKey, final String pFileName) throws Exception {
    LinkedProperties result = propsMap.get(pKey);
    if (result == null) {
      result = this.utlProperties.loadPropertiesXml(pFileName);
      if (result != null) {
        if (this.isShowDebugMessages) {
          this.logger.debug(null, MngSettings.class, "added setting file: "
            + pFileName);
        }
        propsMap.put(pKey, result);
      }
    }
    return result;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Getter for utlReflection.</p>
   * @return IUtlReflection
   **/
  public final IUtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final IUtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }

  /**
   * <p>Getter for utlProperties.</p>
   * @return UtlProperties
   **/
  public final UtlProperties getUtlProperties() {
    return this.utlProperties;
  }

  /**
   * <p>Setter for utlProperties.</p>
   * @param pUtlProperties reference
   **/
  public final void setUtlProperties(final UtlProperties pUtlProperties) {
    this.utlProperties = pUtlProperties;
  }


  /**
   * <p>Getter for isShowDebugMessages.</p>
   * @return boolean
   **/
  public final boolean getIsShowDebugMessages() {
    return this.isShowDebugMessages;
  }

  /**
   * <p>Setter for isShowDebugMessages.</p>
   * @param pIsShowDebugMessages reference
   **/
  public final void setIsShowDebugMessages(final boolean pIsShowDebugMessages) {
    this.isShowDebugMessages = pIsShowDebugMessages;
  }
}
