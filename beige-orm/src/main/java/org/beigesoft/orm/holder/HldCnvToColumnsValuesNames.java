package org.beigesoft.orm.holder;

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

import java.util.Date;
import java.util.Map;
import java.util.Hashtable;
import java.math.BigDecimal;
import java.lang.reflect.Field;

import org.beigesoft.model.IHasId;
import org.beigesoft.orm.converter.CnvIbnLongToCv;
import org.beigesoft.orm.converter.CnvIbnFloatToCv;
import org.beigesoft.orm.converter.CnvIbnDoubleToCv;
import org.beigesoft.orm.converter.CnvIbnBigDecimalToCv;
import org.beigesoft.orm.converter.CnvIbnBooleanToCv;
import org.beigesoft.orm.converter.CnvIbnDateToCv;
import org.beigesoft.orm.converter.CnvIbnEnumToCv;
import org.beigesoft.orm.converter.CnvIbnStringToCv;
import org.beigesoft.orm.converter.CnvIbnVersionToCv;
import org.beigesoft.orm.converter.CnvIbnIntegerToCv;
import org.beigesoft.orm.converter.CnvIbnEntitiesToCv;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.holder.IHolderForClassByName;

/**
 * <p>Generic service that assign field's converter
 * name depending of field's type. For performance purpose it could be
 * replaced by another non-generic (dedicated to the class) one.</p>
 *
 * @author Yury Demidenko
 */
public class HldCnvToColumnsValuesNames
  implements IHolderForClassByName<String> {

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Converters names map "field class/super class"-"converter name".</p>
   **/
  private final Map<Class<?>, String> convertersNamesMap =
      new Hashtable<Class<?>, String>();

  /**
   * <p>Only constructor.</p>
   **/
  public HldCnvToColumnsValuesNames() {
    this.convertersNamesMap.put(IHasId.class,
      CnvIbnEntitiesToCv.class.getSimpleName());
    this.convertersNamesMap.put(ISrvOrm.class,
      CnvIbnVersionToCv.class.getSimpleName());
    this.convertersNamesMap.put(Long.class,
      CnvIbnLongToCv.class.getSimpleName());
    this.convertersNamesMap.put(Float.class,
      CnvIbnFloatToCv.class.getSimpleName());
    this.convertersNamesMap.put(Double.class,
      CnvIbnDoubleToCv.class.getSimpleName());
    this.convertersNamesMap.put(BigDecimal.class,
      CnvIbnBigDecimalToCv.class.getSimpleName());
    this.convertersNamesMap.put(Boolean.class,
      CnvIbnBooleanToCv.class.getSimpleName());
    this.convertersNamesMap.put(Date.class,
      CnvIbnDateToCv.class.getSimpleName());
    this.convertersNamesMap.put(Enum.class,
      CnvIbnEnumToCv.class.getSimpleName());
    this.convertersNamesMap.put(String.class,
      CnvIbnStringToCv.class.getSimpleName());
    this.convertersNamesMap.put(Integer.class,
      CnvIbnIntegerToCv.class.getSimpleName());
  }

  /**
   * <p>Get thing for given class and thing name.</p>
   * @param pClass a Class
   * @param pThingName Thing Name
   * @return a thing
   **/
  @Override
  public final String getFor(final Class<?> pClass, final String pThingName) {
    Field field = this.fieldsRapiHolder.getFor(pClass, pThingName);
    Class<?> classKey = field.getType();
    if (IHasId.class.isAssignableFrom(classKey)) {
      classKey = IHasId.class;
    } else if (Enum.class.isAssignableFrom(classKey)) {
      classKey = Enum.class;
    } else if (ISrvOrm.VERSION_NAME.equals(pThingName)) {
      classKey = ISrvOrm.class;
    }
    return this.convertersNamesMap.get(classKey);
  }

  /**
   * <p>Set thing for given class and thing name.</p>
   * @param pThing Thing
   * @param pClass Class
   * @param pThingName Thing Name
   **/
  @Override
  public final void setFor(final String pThing, final Class<?> pClass,
    final String pThingName) {
    throw new RuntimeException("Forbidden code!");
  }

  //Simple getters and setters:
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
}
