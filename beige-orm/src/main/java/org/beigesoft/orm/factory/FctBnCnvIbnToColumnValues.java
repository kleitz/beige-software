package org.beigesoft.orm.factory;

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
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.converter.IConverterIntoByName;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.service.ISrvSqlEscape;
import org.beigesoft.orm.converter.CnvIbnLongToCv;
import org.beigesoft.orm.converter.CnvIbnFloatToCv;
import org.beigesoft.orm.converter.CnvIbnDoubleToCv;
import org.beigesoft.orm.converter.CnvIbnBigDecimalToCv;
import org.beigesoft.orm.converter.CnvIbnBooleanToCv;
import org.beigesoft.orm.converter.CnvIbnDateToCv;
import org.beigesoft.orm.converter.CnvIbnEnumToCv;
import org.beigesoft.orm.converter.CnvIbnStringToCv;
import org.beigesoft.orm.converter.CnvIbnIntegerToCv;
import org.beigesoft.orm.converter.CnvIbnEntitiesToCv;
import org.beigesoft.orm.converter.CnvIbnVersionToCv;

/**
 * <p>Converters fields into SQL ColumnValues factory.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FctBnCnvIbnToColumnValues
  implements IFactoryAppBeansByName<IConverterIntoByName<?, ColumnsValues>> {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Converters map "converter name"-"object' s converter".</p>
   **/
  private final Map<String, IConverterIntoByName<?, ColumnsValues>>
    convertersMap =
      new Hashtable<String, IConverterIntoByName<?, ColumnsValues>>();

  /**
   * <p>Tables SQL desccriptors map "EntitySimpleName - TableSql".
   * Ordered cause creating tables is order dependent.</p>
   **/
  private Map<String, TableSql> tablesMap;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> gettersRapiHolder;

  /**
   * <p>If need to SQL escape for value string.
   * Android do it itself.</p>
   **/
  private boolean isNeedsToSqlEscape = true;

  /**
   * <p>SQL Escape service.</p>
   **/
  private ISrvSqlEscape srvSqlEscape;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanName - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IConverterIntoByName<?, ColumnsValues> lazyGet(
    final Map<String, Object> pAddParam,
      final String pBeanName) throws Exception {
    IConverterIntoByName<?, ColumnsValues> convrt =
      this.convertersMap.get(pBeanName);
    if (convrt == null) {
      // locking:
      synchronized (this.convertersMap) {
        // make sure again whether it's null after locking:
        convrt = this.convertersMap.get(pBeanName);
        if (convrt == null) {
          if (pBeanName
            .equals(CnvIbnLongToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnLongToCv();
          } else if (pBeanName
            .equals(CnvIbnFloatToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnFloatToCv();
          } else if (pBeanName
            .equals(CnvIbnDoubleToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnDoubleToCv();
          } else if (pBeanName
            .equals(CnvIbnBigDecimalToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnBigDecimalToCv();
          } else if (pBeanName
            .equals(CnvIbnBooleanToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnBooleanToCv();
          } else if (pBeanName
            .equals(CnvIbnDateToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnDateToCv();
          } else if (pBeanName
            .equals(CnvIbnEnumToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnEnumToCv();
          } else if (pBeanName
            .equals(CnvIbnStringToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnStringToCv();
          } else if (pBeanName
            .equals(CnvIbnEntitiesToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnEntitiesToCv();
          } else if (pBeanName
            .equals(CnvIbnVersionToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnVersionToCv();
          } else if (pBeanName
            .equals(CnvIbnIntegerToCv.class.getSimpleName())) {
            convrt = createPutCnvIbnIntegerToCv();
          }
        }
      }
    }
    if (convrt == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no converter with name " + pBeanName);
    }
    return convrt;
  }

  /**
   * <p>Set bean.</p>
   * @param pBeanName - bean name
   * @param pBean bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void set(final String pBeanName,
    final IConverterIntoByName<?, ColumnsValues> pBean) throws Exception {
    this.convertersMap.put(pBeanName, pBean);
  }

  /**
   * <p>Get CnvIbnFloatToCv (create and put into map).</p>
   * @return requested CnvIbnFloatToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnFloatToCv
    createPutCnvIbnFloatToCv() throws Exception {
    CnvIbnFloatToCv convrt = new CnvIbnFloatToCv();
    this.convertersMap
      .put(CnvIbnFloatToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnDoubleToCv (create and put into map).</p>
   * @return requested CnvIbnDoubleToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnDoubleToCv
    createPutCnvIbnDoubleToCv() throws Exception {
    CnvIbnDoubleToCv convrt = new CnvIbnDoubleToCv();
    this.convertersMap
      .put(CnvIbnDoubleToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnBigDecimalToCv (create and put into map).</p>
   * @return requested CnvIbnBigDecimalToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnBigDecimalToCv
    createPutCnvIbnBigDecimalToCv() throws Exception {
    CnvIbnBigDecimalToCv convrt = new CnvIbnBigDecimalToCv();
    this.convertersMap
      .put(CnvIbnBigDecimalToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnBooleanToCv (create and put into map).</p>
   * @return requested CnvIbnBooleanToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnBooleanToCv
    createPutCnvIbnBooleanToCv() throws Exception {
    CnvIbnBooleanToCv convrt = new CnvIbnBooleanToCv();
    this.convertersMap
      .put(CnvIbnBooleanToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnDateToCv (create and put into map).</p>
   * @return requested CnvIbnDateToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnDateToCv
    createPutCnvIbnDateToCv() throws Exception {
    CnvIbnDateToCv convrt = new CnvIbnDateToCv();
    this.convertersMap
      .put(CnvIbnDateToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnEnumToCv (create and put into map).</p>
   * @return requested CnvIbnEnumToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnEnumToCv
    createPutCnvIbnEnumToCv() throws Exception {
    CnvIbnEnumToCv convrt = new CnvIbnEnumToCv();
    this.convertersMap
      .put(CnvIbnEnumToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnStringToCv (create and put into map).</p>
   * @return requested CnvIbnStringToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnStringToCv
    createPutCnvIbnStringToCv() throws Exception {
    CnvIbnStringToCv convrt = new CnvIbnStringToCv();
    convrt.setIsNeedsToSqlEscape(getIsNeedsToSqlEscape());
    convrt.setSrvSqlEscape(getSrvSqlEscape());
    this.convertersMap
      .put(CnvIbnStringToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnEntitiesToCv (create and put into map).</p>
   * @return requested CnvIbnEntitiesToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnEntitiesToCv
    createPutCnvIbnEntitiesToCv() throws Exception {
    CnvIbnEntitiesToCv convrt = new CnvIbnEntitiesToCv();
    convrt.setFieldsRapiHolder(getFieldsRapiHolder());
    convrt.setGettersRapiHolder(getGettersRapiHolder());
    convrt.setTablesMap(getTablesMap());
    this.convertersMap
      .put(CnvIbnEntitiesToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnVersionToCv (create and put into map).</p>
   * @return requested CnvIbnVersionToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnVersionToCv
    createPutCnvIbnVersionToCv() throws Exception {
    CnvIbnVersionToCv convrt = new CnvIbnVersionToCv();
    this.convertersMap
      .put(CnvIbnVersionToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnIntegerToCv (create and put into map).</p>
   * @return requested CnvIbnIntegerToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnIntegerToCv
    createPutCnvIbnIntegerToCv() throws Exception {
    CnvIbnIntegerToCv convrt = new CnvIbnIntegerToCv();
    this.convertersMap
      .put(CnvIbnIntegerToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvIbnLongToCv (create and put into map).</p>
   * @return requested CnvIbnLongToCv
   * @throws Exception - an exception
   */
  protected final CnvIbnLongToCv
    createPutCnvIbnLongToCv() throws Exception {
    CnvIbnLongToCv convrt = new CnvIbnLongToCv();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvIbnLongToCv.class.getSimpleName(), convrt);
    return convrt;
  }

  //Simple getters and setters:
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
   * <p>Getter for tablesMap.</p>
   * @return Map<String, TableSql>
   **/
  public final Map<String, TableSql> getTablesMap() {
    return this.tablesMap;
  }

  /**
   * <p>Setter for tablesMap.</p>
   * @param pTablesMap reference
   **/
  public final void setTablesMap(final Map<String, TableSql> pTablesMap) {
    this.tablesMap = pTablesMap;
  }

  /**
   * <p>Getter for fieldsRapiHolder.</p>
   * @return IHolderForClassByName<Field>
   **/
  public final IHolderForClassByName<Field> getFieldsRapiHolder() {
    return this.fieldsRapiHolder;
  }

  /**
   * <p>Setter for fieldsRapiHolder.</p>
   * @param pFieldsRapiHolder reference
   **/
  public final void setFieldsRapiHolder(
    final IHolderForClassByName<Field> pFieldsRapiHolder) {
    this.fieldsRapiHolder = pFieldsRapiHolder;
  }

  /**
   * <p>Getter for gettersRapiHolder.</p>
   * @return IHolderForClassByName<Method>
   **/
  public final IHolderForClassByName<Method> getGettersRapiHolder() {
    return this.gettersRapiHolder;
  }

  /**
   * <p>Setter for gettersRapiHolder.</p>
   * @param pGettersRapiHolder reference
   **/
  public final void setGettersRapiHolder(
    final IHolderForClassByName<Method> pGettersRapiHolder) {
    this.gettersRapiHolder = pGettersRapiHolder;
  }

  /**
   * <p>Getter for isNeedsToSqlEscape.</p>
   * @return boolean
   **/
  public final boolean getIsNeedsToSqlEscape() {
    return this.isNeedsToSqlEscape;
  }

  /**
   * <p>Setter for isNeedsToSqlEscape.</p>
   * @param pIsNeedsToSqlEscape reference
   **/
  public final void setIsNeedsToSqlEscape(final boolean pIsNeedsToSqlEscape) {
    this.isNeedsToSqlEscape = pIsNeedsToSqlEscape;
  }

  /**
   * <p>Getter for srvSqlEscape.</p>
   * @return ISrvSqlEscape
   **/
  public final ISrvSqlEscape getSrvSqlEscape() {
    return this.srvSqlEscape;
  }

  /**
   * <p>Setter for srvSqlEscape.</p>
   * @param pSrvSqlEscape reference
   **/
  public final void setSrvSqlEscape(final ISrvSqlEscape pSrvSqlEscape) {
    this.srvSqlEscape = pSrvSqlEscape;
  }
}
