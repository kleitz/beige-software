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

import java.util.Map;
import java.util.List;

import org.beigesoft.accounting.persistable.base.ADrawItemSourcesLine;
import org.beigesoft.accounting.persistable.CogsEntry;

/**
 * <p>Business service for draw warehouse item for sale/loss/stole.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvCogsEntry<RS>
  extends ASrvDrawItemEntry<CogsEntry, RS> {

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
   * @param pAddParam additional param
   * @return draw item entry
   **/
  @Override
  public final CogsEntry createDrawItemEntry(
    final Map<String, Object> pAddParam) {
    return new CogsEntry();
  }

  /**
   * <p>Get draw item sources.</p>
   * @param pAddParam additional param
   * @return draw item sources
   * @throws Exception - an exception
   **/
  @Override
  public final List<? extends ADrawItemSourcesLine>
    getDrawItemSources(
      final Map<String, Object> pAddParam) throws Exception {
    return getSrvAccSettings().lazyGetAccSettings(pAddParam)
      .getCogsItemSources();
  }
}
