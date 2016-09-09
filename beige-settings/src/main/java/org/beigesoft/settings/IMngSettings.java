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

import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <p>Make settings for application, classes and its fields
 * according properties XML.</p>
 *
 * @author Yury Demidenko
 */
public interface IMngSettings {

  /**
   * <p>Key name for settings subfolder.</p>
   **/
  String KEY_USE_SUB_FOLDER = "useSubFolder";

  /**
   * <p>Key name for classes.</p>
   **/
  String KEY_CLASSES = "classes";

  /**
   * <p>Key name for fields settings names.</p>
   **/
  String KEY_CLASS_SETTINGS_NAMES = "settingsKeysForClasses";

  /**
   * <p>Key name for fields settings names.</p>
   **/
  String KEY_FIELD_SETTINGS_NAMES = "settingsKeysForFields";

  /**
   * <p>Key name for exclude fields.</p>
   **/
  String KEY_EXCLUDED_FIELDS = "excludeFields";

  /**
   * <p>App setting file name.</p>
   **/
  String APP_SETTINGS_FILE_NAME = "app-settings.xml";

  /**
   * <p>Folder class type to classes setting.</p>
   **/
  String FOLDER_CLASS_TYPE_TO_CS = "classTypeToCs";

  /**
   * <p>Folder class name to classes setting.</p>
   **/
  String FOLDER_CLASS_TO_CS = "forClassToCs";

  /**
   * <p>Folder field type to field setting.</p>
   **/
  String FOLDER_FIELD_TYPE_TO_FS = "fieldTypeToFs";

  /**
   * <p>Folder field name field type to field setting.</p>
   **/
  String DIR_FIELD_NAME_FIELD_TYPE_TO_FS = "fieldNameFieldTypeToFs";

  /**
   * <p>Folder field name class type to field setting.</p>
   **/
  String DIR_FIELD_NAME_CLASS_TYPE_TO_FS = "fieldNameClassTypeToFs";

  /**
   * <p>Folder field name to field setting.</p>
   **/
  String FOLDER_FIELD_NAME_TO_FS = "fieldNameToFs";

  /**
   * <p>Folder class name to fields setting.</p>
   **/
  String FOLDER_CLASS_TO_FS = "forClassToFs";

  /**
   * <p>Load configuration(settings) from given folder and file.</p>
   * @param pDirName base properties directory name
   * @param pFileName base properties file name
   * @throws Exception - an exception
   */
  void loadConfiguration(String pDirName, String pFileName) throws Exception;

  /**
   * <p>Sorted entries and also removed those with negative value.</p>
   * @param pSetEntries Set<Map.Entry<String, Map<String, String>>> source
   * @param pOrderKey key of order property
   * @return List<Map.Entry<String, Map<String, String>>> list ordered according
   * entry.getValue().get(pOrderKey) that is Integer.toString()
   * @throws Exception - an exception
   */
  List<Map.Entry<String, Map<String, String>>> sortRemoveIntVal(
    Set<Map.Entry<String,
      Map<String, String>>> pSetEntries, String pOrderKey) throws Exception;

  /**
   * <p>Make for given class sorted entries of fields settings
   * and also removed those with negative value of sodted property.</p>
   * @param pEntityName entity canonical name
   * @param pOrderKey key of order property
   * @return List<Map.Entry<String, Map<String, String>>> list ordered according
   * entry.getValue().get(pOrderKey) that is Integer.toString()
   * @throws Exception - an exception
   */
  List<Map.Entry<String, Map<String, String>>> makeFldPropLst(
    String pEntityName, String pOrderKey) throws Exception;

  //GS:
  /**
   * <p>Get all involved classes.</p>
   * @return LinkedHashSet<Class<?> all involved clase
   */
  LinkedHashSet<Class<?>> getClasses();

  /**
   * <p>Set all involved classes.</p>
   * @param pClasses LinkedHashSet<Class<?> all involved clase
   */
  void setClasses(LinkedHashSet<Class<?>> pClasses);

  /**
   * <p>Get application settings.</p>
   * @return Map<String, String> application settings
   */
  Map<String, String> getAppSettings();

  /**
   * <p>Set application settings.</p>
   * @param pAppSettings Map<String, String> application settings
   */
  void setAppSettings(Map<String, String> pAppSettings);

  /**
   * <p>Get class settings.
   * This is Map "classCanonicalName" -
   * "Map<String, String>>"</p>
   * @return Map<String, Map<String, String>> class settings
   */
  Map<String, Map<String, String>> getClassesSettings();

  /**
   * <p>Set class settings.
   * This is Map "classCanonicalName" -
   * "Map<String, String>>"</p>
   * @param pClassSettings Map<String, Map<String, String>> class settings
   */
  void setClassesSettings(Map<String, Map<String, String>> pClassSettings);

  /**
   * <p>Get fields settings.
   * This is Map "classCanonicalName" -
   * "Map "fieldName" - "Map "settingName"-"settingValue"""</p>
   * @return Map<String, Map<String, Map<String, String>>> fields settings
   */
  Map<String, Map<String, Map<String, String>>> getFieldsSettings();

  /**
   * <p>Set fields settings.
   * This is Map "classCanonicalName" -
   * "Map "fieldName" - "Map "settingName"-"settingValue"""</p>
   * @param pFieldsSettings Map<String, Map<String, Map<String, String>>>
   * fields settings
   */
  void setFieldsSettings(
    Map<String, Map<String, Map<String, String>>> pFieldsSettings);
}
