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

import java.util.Map;
import java.util.Hashtable;

import org.beigesoft.persistable.IPersistableBase;
import org.beigesoft.persistable.EmailMsg;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.orm.processor.PrcEntityRetrieve;
import org.beigesoft.orm.processor.PrcEntityPbEditDelete;
import org.beigesoft.orm.processor.PrcEntityFfolDelete;
import org.beigesoft.orm.processor.PrcEntityFfolSave;
import org.beigesoft.orm.processor.PrcEntityFolDelete;
import org.beigesoft.orm.processor.PrcEntityFolSave;
import org.beigesoft.orm.processor.PrcEntityPbCopy;
import org.beigesoft.orm.processor.PrcEntityCopy;
import org.beigesoft.orm.processor.PrcEntityPbSave;
import org.beigesoft.orm.processor.PrcEmailMsgSave;
import org.beigesoft.orm.processor.PrcEntitySave;
import org.beigesoft.orm.processor.PrcEntityDelete;
import org.beigesoft.orm.processor.PrcEntityPbDelete;
import org.beigesoft.orm.processor.PrcEntityCreate;

/**
 * <p>Generic service that assign entities processor name for class
 * and action name.</p>
 *
 * @author Yury Demidenko
 */
public class HldEntitiesProcessorNames
  implements IHolderForClassByName<String> {

  /**
   * <p>EntitiesProcessors names map:
   * "key = class simple name + action"-"processor name".</p>
   **/
  private final Map<String, String> processorsNamesMap =
      new Hashtable<String, String>();

  /**
   * <p>Get thing for given class and thing name.
   * findbugs: UG_SYNC_SET_UNSYNC_GET - this code is designed
   * for high performance. Getting name is happened very frequency
   * (e.g. 10 per second by multi-threads).
   * Setting is seldom (e.g. hot change configuration to fix program bug)
   * or may not be happen.</p>
   * @param pClass a Class
   * @param pThingName Thing Name
   * @return a thing
   **/
  @Override
  public final String getFor(final Class<?> pClass, final String pThingName) {
    if ("entityEdit".equals(pThingName)) {
      if (IPersistableBase.class.isAssignableFrom(pClass)) {
        return PrcEntityPbEditDelete.class.getSimpleName();
      } else {
        return PrcEntityRetrieve.class.getSimpleName();
      }
    } else if ("entityPrint".equals(pThingName)) {
      return PrcEntityRetrieve.class.getSimpleName();
    } else if ("entityCopy".equals(pThingName)) {
      if (IPersistableBase.class.isAssignableFrom(pClass)) {
        return PrcEntityPbCopy.class.getSimpleName();
      } else {
        return PrcEntityCopy.class.getSimpleName();
      }
    } else if ("entitySave".equals(pThingName)) {
      if (IPersistableBase.class.isAssignableFrom(pClass)) {
        return PrcEntityPbSave.class.getSimpleName();
      } else if (EmailMsg.class == pClass) {
        return PrcEmailMsgSave.class.getSimpleName();
      } else {
        return PrcEntitySave.class.getSimpleName();
      }
    } else if ("entityFfolDelete".equals(pThingName)) {
      return PrcEntityFfolDelete.class.getSimpleName();
    } else if ("entityFfolSave".equals(pThingName)) {
      return PrcEntityFfolSave.class.getSimpleName();
    } else if ("entityFolDelete".equals(pThingName)) {
      return PrcEntityFolDelete.class.getSimpleName();
    } else if ("entityFolSave".equals(pThingName)) {
      return PrcEntityFolSave.class.getSimpleName();
    } else if ("entityDelete".equals(pThingName)) {
      if (IPersistableBase.class.isAssignableFrom(pClass)) {
        return PrcEntityPbDelete.class.getSimpleName();
      } else {
        return PrcEntityDelete.class.getSimpleName();
      }
    } else if ("entityCreate".equals(pThingName)) {
        return PrcEntityCreate.class.getSimpleName();
    } else if ("entityConfirmDelete".equals(pThingName)) {
      if (IPersistableBase.class.isAssignableFrom(pClass)) {
        return PrcEntityPbEditDelete.class.getSimpleName();
      } else {
        return PrcEntityRetrieve.class.getSimpleName();
      }
    }
    return this.processorsNamesMap
      .get(pClass.getSimpleName() + pThingName);
  }

  /**
   * <p>Set thing for given class and thing name.</p>
   * @param pThing Thing
   * @param pClass Class
   * @param pThingName Thing Name
   **/
  @Override
  public final synchronized void setFor(final String pThing,
    final Class<?> pClass, final String pThingName) {
    if ("entityEdit".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityPrint".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityFfolDelete".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityFfolSave".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityFolDelete".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityFolSave".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityCopy".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entitySave".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityDelete".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityCreate".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    } else if ("entityConfirmDelete".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    }
    this.processorsNamesMap
      .put(pClass.getSimpleName() + pThingName, pThing);
  }
}
