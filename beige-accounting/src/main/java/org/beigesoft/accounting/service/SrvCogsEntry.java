package org.beigesoft.accounting.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.List;
import java.text.DateFormat;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.accounting.persistable.base.ADrawItemSourcesLine;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Business service for draw warehouse item for sale/loss/stole.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvCogsEntry<RS>
  extends ASrvDrawItemEntry<CogsEntry, RS> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvCogsEntry() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvTypeCode service for code - java type map of material holders
   * @param pSrvAccSettings AccSettings service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   **/
  public SrvCogsEntry(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvTypeCode pSrvTypeCode,
      final ISrvAccSettings pSrvAccSettings,
        final ISrvI18n pSrvI18n, final DateFormat pDateFormatter) {
    super(pSrvOrm, pSrvDatabase, pSrvTypeCode, pSrvAccSettings, pSrvI18n,
      pDateFormatter);
  }

  /**
   * <p>Get draw item entry class.</p>
   * @return draw item entry class
   **/
  @Override
  public final Class<CogsEntry> getDrawItemEntryClass() {
    return CogsEntry.class;
  }

  /**
   * <p>Create draw item entry.</p>
   * @return draw item entry
   **/
  @Override
  public final CogsEntry createDrawItemEntry() {
    return new CogsEntry();
  }

  /**
   * <p>Get draw item sources.</p>
   * @return draw item sources
   * @throws Exception - an exception
   **/
  @Override
  public final List<? extends ADrawItemSourcesLine>
    getDrawItemSources() throws Exception {
    return getSrvAccSettings().lazyGetAccSettings()
      .getCogsItemSources();
  }
}
