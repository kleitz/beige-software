package org.beigesoft.webstore.holder;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.accounting.holder.IHldAddEntitiesProcessorNames;
import org.beigesoft.webstore.persistable.SettingsAdd;
import org.beigesoft.webstore.persistable.TradingSettings;
import org.beigesoft.webstore.persistable.SubcatalogsCatalogsGs;
import org.beigesoft.webstore.persistable.AdvisedGoodsForGoods;
import org.beigesoft.webstore.persistable.GoodsAdviseCategories;
import org.beigesoft.webstore.persistable.GoodsSpecific;
import org.beigesoft.webstore.persistable.GoodsCatalogs;
import org.beigesoft.webstore.processor.PrcTradeEntityCopy;
import org.beigesoft.webstore.processor.PrcAdvisedGoodsForGoodsSave;
import org.beigesoft.webstore.processor.PrcGoodsCatalogsSave;
import org.beigesoft.webstore.processor.PrcSubcatalogsCatalogsGsSave;
import org.beigesoft.webstore.processor.PrcGoodsAdviseCategoriesSave;
import org.beigesoft.webstore.processor.PrcGoodsSpecificSave;
import org.beigesoft.webstore.processor.PrcGoodsSpecificDelete;
import org.beigesoft.webstore.processor.PrcSettingsAddRetrieve;
import org.beigesoft.webstore.processor.PrcSettingsAddSave;
import org.beigesoft.webstore.processor.PrcTradingSettingsRetrieve;
import org.beigesoft.webstore.processor.PrcTradingSettingsSave;

/**
 * <p>Additional service that assign entities processor name for class
 * and action name for webstore.
 * This is inner holder inside ACC-EPN.</p>
 *
 * @author Yury Demidenko
 */
public class HldTradeEntitiesProcessorNames
  implements IHldAddEntitiesProcessorNames {

  /**
   * <p>Get processor name for copy.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForCopy(final Class<?> pClass) {
    // for performance reason (do not check class type - accounting or trade)
    // it return trade copy processor that is same as accounting one but added
    // trading settings
    return PrcTradeEntityCopy.class.getSimpleName();
  }

  /**
   * <p>Get processor name for print.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForPrint(final Class<?> pClass) {
    if (pClass == TradingSettings.class) {
      return PrcTradingSettingsRetrieve.class.getSimpleName();
    } else if (pClass == SettingsAdd.class) {
      return PrcSettingsAddRetrieve.class.getSimpleName();
    }
    return null;
  }

  /**
   * <p>Get processor name for save.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForSave(final Class<?> pClass) {
    if (pClass == AdvisedGoodsForGoods.class) {
      return PrcAdvisedGoodsForGoodsSave.class.getSimpleName();
    } else if (pClass == SettingsAdd.class) {
      return PrcSettingsAddSave.class.getSimpleName();
    } else if (pClass == TradingSettings.class) {
      return PrcTradingSettingsSave.class.getSimpleName();
    } else if (pClass == GoodsCatalogs.class) {
      return PrcGoodsCatalogsSave.class.getSimpleName();
    } else if (pClass == SubcatalogsCatalogsGs.class) {
      return PrcSubcatalogsCatalogsGsSave.class.getSimpleName();
    } else if (pClass == GoodsAdviseCategories.class) {
      return PrcGoodsAdviseCategoriesSave.class.getSimpleName();
    } else if (pClass == GoodsSpecific.class) {
      return PrcGoodsSpecificSave.class.getSimpleName();
    }
    return null;
  }

  /**
   * <p>Get processor name for FFOL delete.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForFfolDelete(final Class<?> pClass) {
    return null;
  }

  /**
   * <p>Get processor name for FFOL save.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForFfolSave(final Class<?> pClass) {
    return null;
  }

  /**
   * <p>Get processor name for FOL delete.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForFolDelete(final Class<?> pClass) {
    return null;
  }

  /**
   * <p>Get processor name for FOL save.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForFolSave(final Class<?> pClass) {
    return null;
  }

  /**
   * <p>Get processor name for delete.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForDelete(final Class<?> pClass) {
    if (pClass == GoodsSpecific.class) {
      return PrcGoodsSpecificDelete.class.getSimpleName();
    }
    return null;
  }

  /**
   * <p>Get processor name for create.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForCreate(final Class<?> pClass) {
    return null;
  }

  /**
   * <p>Get processor name for retrieve to edit/delete.</p>
   * @param pClass a Class
   * @return a thing
   **/
  @Override
  public final String getForRetrieveForEditDelete(final Class<?> pClass) {
    if (pClass == TradingSettings.class) {
      return PrcTradingSettingsRetrieve.class.getSimpleName();
    } else if (pClass == SettingsAdd.class) {
      return PrcSettingsAddRetrieve.class.getSimpleName();
    }
    return null;
  }
}
