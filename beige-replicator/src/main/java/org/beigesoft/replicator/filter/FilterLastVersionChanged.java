package org.beigesoft.replicator.filter;

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

import org.beigesoft.delegate.IDelegateEvalExt;

/**
 * <p>Service that make SQL WHERE filter for entities
 * with <b>itsVersion</b> and <b>changed time</b> algorithm.
 * According database replication one way specification #1.
 *
 * @author Yury Demidenko
 */
public class FilterLastVersionChanged implements IFilterEntities {

  /**
   * <p>Last replicated date evaluator.</p>
   **/
  private IDelegateEvalExt<Date> lastReplicatedDateEvaluator;

  /**
   * <p>
   * Filter that make SQL WHERE filter for entities
   * with <b>itsVersion</b> and <b>changed time</b> algorithm.
   * </p>
   * @param pEntityClass Entity Class
   * @param pAddParam additional params
   * @return filter e.g. "ITSVERSION>12312432300012"
   * @throws Exception - an exception
   **/
  @Override
  public final String makeFilter(final Class<?> pEntityClass,
    final Map<String, Object> pAddParam) throws Exception {
    return pEntityClass.getSimpleName().toUpperCase() + ".ITSVERSION>"
      + this.lastReplicatedDateEvaluator.evalData(pAddParam).getTime();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for lastReplicatedDateEvaluator.</p>
   * @return IDelegateEvalExt<Date>
   **/
  public final IDelegateEvalExt<Date> getLastReplicatedDateEvaluator() {
    return this.lastReplicatedDateEvaluator;
  }

  /**
   * <p>Setter for lastReplicatedDateEvaluator.</p>
   * @param pLastReplicatedDateEvaluator reference
   **/
  public final void setLastReplicatedDateEvaluator(
    final IDelegateEvalExt<Date> pLastReplicatedDateEvaluator) {
    this.lastReplicatedDateEvaluator = pLastReplicatedDateEvaluator;
  }
}
