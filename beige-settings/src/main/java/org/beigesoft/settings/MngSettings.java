package org.beigesoft.settings;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.net.URL;
import java.lang.reflect.Field;

import org.beigesoft.properties.LinkedProperties;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.comparator.CmprEntryValueStringInt;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.log.ILogger;

/**
 * <p>Make settings  for application, classes and its fields
 * according properties XML. Specification #2.</p>
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
  private final UtlReflection utlReflection = new UtlReflection();

  /**
   * <p>LinkedProperties service.</p>
   **/
  private final UtlProperties utlProperties = new UtlProperties();

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
  private Map<String, Map<String, String>> classesSettings;

  /**
   * <p>Fields settings.</p>
   */
  private Map<String, Map<String, Map<String, String>>> fieldsSettings;

  /**
   * <p>Load configuration(settings) from given folder and file.</p>
   * @param pDirName base properties directory name
   * @param pFileName base properties file name
   * @throws Exception - an exception
   */
  @Override
  public final void loadConfiguration(final String pDirName,
    final String pFileName) throws Exception {
    String nameBaseXml = File.separator + pDirName + File.separator
      + pFileName;
    getLogger().debug(MngSettings.class, "try to load: "
      + nameBaseXml);
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
    getLogger().debug(MngSettings.class, "classes: "
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
   * @param pEntityName entity canonical name
   * @param pOrderKey key of order property
   * @return List<Map.Entry<String, Map<String, String>>> list ordered according
   * entry.getValue().get(pOrderKey) that is Integer.toString()
   * @throws Exception - an exception
   */
  @Override
  public final List<Map.Entry<String, Map<String, String>>> makeFldPropLst(
    final String pEntityName, final String pOrderKey) throws Exception {
    Map<String, Map<String, String>> map =
      this.fieldsSettings.get(pEntityName);
    if (map == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no entries of fields settings for "
          + pEntityName + " order key " + pOrderKey);
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
   * This is Map "classCanonicalName" -
   * "Map<String, String>>"</p>
   * @return Map<String, Map<String, String>> class settings
   */
  @Override
  public final Map<String, Map<String, String>> getClassesSettings() {
    return this.classesSettings;
  }

  /**
   * <p>Set class settings.
   * This is Map "classCanonicalName" -
   * "Map<String, String>>"</p>
   * @param pClassSettings Map<String, Map<String, String>> class settings
   */
  @Override
  public final void setClassesSettings(
    final Map<String, Map<String, String>> pClassSettings) {
      this.classesSettings = pClassSettings;
  }

  /**
   * <p>Get fields settings.
   * This is Map "classCanonicalName" -
   * "Map "fieldName" - "Map "settingName"-"settingValue"""</p>
   * @return Map<String, Map<String, Map<String, String>>> fields settings
   */
  @Override
  public final Map<String, Map<String, Map<String, String>>>
    getFieldsSettings() {
    return this.fieldsSettings;
  }

  /**
   * <p>Set fields settings.
   * This is Map "classCanonicalName" -
   * "Map "fieldName" - "Map "settingName"-"settingValue"""</p>
   * @param pFieldsSettings Map<String, Map<String, Map<String, String>>>
   *  fields settings
   */
  @Override
  public final void setFieldsSettings(
    final Map<String, Map<String, Map<String, String>>> pFieldsSettings) {
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
    String nameAppStXml = File.separator + pDirName
      + File.separator + subFolder + APP_SETTINGS_FILE_NAME;
    URL urlAppStXml = MngSettings.class
      .getResource(nameAppStXml);
    if (urlAppStXml != null) {
      LinkedProperties propAppStXml = this.utlProperties
        .loadPropertiesXml(nameAppStXml);
      for (String settingName : propAppStXml.stringPropertyNames()) {
        String settingVal = propAppStXml.getProperty(settingName);
        getLogger().debug(MngSettings.class, "add app setting: "
          + settingName + " - " + settingVal);
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
      new HashMap<String, Map<String, String>>();
    Map<String, LinkedProperties> javaTypeToStMap
      = new HashMap<String, LinkedProperties>();
    String subFolder = evalSubfolder(pProps);
    String strClasses = pProps.getProperty(KEY_CLASS_SETTINGS_NAMES);
    if (strClasses == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no property " + KEY_CLASS_SETTINGS_NAMES
          + " in base configuration file!");
    }
    getLogger().debug(MngSettings.class, "classes settings keys: "
      + strClasses);
    LinkedHashSet<String> settingsNames = this.utlProperties
      .evalPropsStringsSet(strClasses);
    for (String settingName : settingsNames) {
      LinkedProperties propJaTyToCs = this.utlProperties.loadPropertiesXml(
        File.separator + pDirName + File.separator + subFolder
          + FOLDER_CLASS_TYPE_TO_CS + File.separator + settingName + ".xml");
      if (propJaTyToCs != null) {
        getLogger().debug(MngSettings.class, "add class setting file: "
          + settingName + " in " + FOLDER_CLASS_TYPE_TO_CS);
        javaTypeToStMap.put(settingName, propJaTyToCs);
      }
    }
    for (Class<?> clazz : classes) {
      Map<String, String> classSettings = new HashMap<String, String>();
      LinkedProperties propsClassToSetting = this.utlProperties
        .loadPropertiesXml(File.separator + pDirName + File.separator
          + subFolder + FOLDER_CLASS_TO_CS + File.separator
            + clazz.getSimpleName() + ".xml");
      if (propsClassToSetting != null) {
        getLogger().debug(MngSettings.class, "add class setting file: "
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
      this.classesSettings.put(clazz.getCanonicalName(), classSettings);
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
          getLogger().debug(MngSettings.class, pDebugMsg
            + " add setting by type : " + pSettingName + " - "
              + settingVal);
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
      if (UtlProperties.NULL.equals(settingValue)) {
        settingValue = null;
      }
      getLogger().debug(MngSettings.class, pDebugMsg + " add setting : "
        + pSettingName + " - " + settingValue);
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
      subFolder += File.separator;
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
      new HashMap<String, Map<String, Map<String, String>>>();
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
    getLogger().debug(MngSettings.class, "fields settings keys: "
      + strFields);
    LinkedHashSet<String> settingsNames = this.utlProperties
      .evalPropsStringsSet(strFields);
    for (String settingName : settingsNames) {
      LinkedProperties propJaTyToSt = this.utlProperties
        .loadPropertiesXml(File.separator + pDirName
        + File.separator + subFolder + FOLDER_FIELD_TYPE_TO_FS
          + File.separator + settingName + ".xml");
      if (propJaTyToSt != null) {
        getLogger().debug(MngSettings.class, "add field setting file: "
          + settingName + " in " + FOLDER_FIELD_TYPE_TO_FS);
        javaTypeToStMap.put(settingName, propJaTyToSt);
      }
      LinkedProperties propFldNmToSt = this.utlProperties
        .loadPropertiesXml(File.separator + pDirName
        + File.separator + subFolder + FOLDER_FIELD_NAME_TO_FS
          + File.separator + settingName + ".xml");
      if (propFldNmToSt != null) {
        getLogger().debug(MngSettings.class, "add field setting file: "
          + settingName + " in " + FOLDER_FIELD_NAME_TO_FS);
        fieldNameToStMap.put(settingName, propFldNmToSt);
      }
    }
    for (Class<?> clazz : this.classes) {
      Map<String, Map<String, String>> fieldsDescriptor =
        new HashMap<String, Map<String, String>>();
      LinkedProperties propsClassToSetting = this.utlProperties
          .loadPropertiesXml(File.separator + pDirName
        + File.separator + subFolder + FOLDER_CLASS_TO_FS
          + File.separator + clazz.getSimpleName() + ".xml");
      Field[] fields = getUtlReflection().retrieveFields(clazz);
      if (propsClassToSetting != null) {
        getLogger().debug(MngSettings.class, "add field setting file: "
          + clazz.getSimpleName() + " in " + FOLDER_CLASS_TO_FS);
      }
      for (Field field : fields) {
        if (excludedFields == null
          || !excludedFields.contains(field.getName())) {
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
              String nmFlJavaTypeToFs = File.separator + pDirName
                + File.separator + subFolder + DIR_FIELD_NAME_FIELD_TYPE_TO_FS
                  + File.separator + field.getName() + settingName + ".xml";
              LinkedProperties propFldNmJaTyToFs = evalProps(fldNmJaTyToFsMap,
                field.getName() + settingName, nmFlJavaTypeToFs);
              if (propFldNmJaTyToFs != null) { //3d
                Class<?> fieldType = field.getType();
                putValueIfExistByType(propFldNmJaTyToFs, fieldType,
                  settingName, fldSettings, "3d for " + clazz.getSimpleName()
                    + "->" + field.getName());
              }
              String nmFlClassTypeToFs = File.separator + pDirName
                + File.separator + subFolder + DIR_FIELD_NAME_CLASS_TYPE_TO_FS
                  + File.separator + field.getName() + settingName + ".xml";
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
      this.fieldsSettings.put(clazz.getCanonicalName(), fieldsDescriptor);
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
        getLogger().debug(MngSettings.class, "added setting file: "
          + pFileName);
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
   * <p>Geter for utlReflection.</p>
   * @return UtlReflection
   **/
  public final UtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Geter for utlProperties.</p>
   * @return UtlProperties
   **/
  public final UtlProperties getUtlProperties() {
    return this.utlProperties;
  }
}
