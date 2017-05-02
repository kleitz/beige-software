package org.beigesoft.orm.holder;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Date;
import java.util.Map;
import java.util.Hashtable;
import java.math.BigDecimal;
import java.lang.reflect.Field;

import org.beigesoft.model.IHasId;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.orm.converter.CnvBnRsToEntity;
import org.beigesoft.orm.converter.CnvBnRsToDouble;
import org.beigesoft.orm.converter.CnvBnRsToInteger;
import org.beigesoft.orm.converter.CnvBnRsToLong;
import org.beigesoft.orm.converter.CnvBnRsToBoolean;
import org.beigesoft.orm.converter.CnvBnRsToDate;
import org.beigesoft.orm.converter.CnvBnRsToString;
import org.beigesoft.orm.converter.CnvBnRsToBigDecimal;
import org.beigesoft.orm.converter.CnvBnRsToFloat;
import org.beigesoft.orm.converter.CnvBnRsToEnum;

/**
 * <p>Generic service that assign field's converter
 * name depending of field's type. For performance purpose it could be
 * replaced by another non-generic (dedicated to the class) one.</p>
 *
 * @author Yury Demidenko
 */
public class HldCnvFromRsNames
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
  public HldCnvFromRsNames() {
    this.convertersNamesMap.put(IHasId.class,
      CnvBnRsToEntity.class.getSimpleName());
    this.convertersNamesMap.put(Long.class,
      CnvBnRsToLong.class.getSimpleName());
    this.convertersNamesMap.put(Float.class,
      CnvBnRsToFloat.class.getSimpleName());
    this.convertersNamesMap.put(Double.class,
      CnvBnRsToDouble.class.getSimpleName());
    this.convertersNamesMap.put(BigDecimal.class,
      CnvBnRsToBigDecimal.class.getSimpleName());
    this.convertersNamesMap.put(Boolean.class,
      CnvBnRsToBoolean.class.getSimpleName());
    this.convertersNamesMap.put(Date.class,
      CnvBnRsToDate.class.getSimpleName());
    this.convertersNamesMap.put(Enum.class,
      CnvBnRsToEnum.class.getSimpleName());
    this.convertersNamesMap.put(String.class,
      CnvBnRsToString.class.getSimpleName());
    this.convertersNamesMap.put(Integer.class,
      CnvBnRsToInteger.class.getSimpleName());
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
