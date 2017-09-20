package org.beigesoft.replicator.service;

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

/**
 * <p>Abstraction of service that replicate data
 * from requested(source) database to requesting(destination) database.</p>
 *
 * @author Yury Demidenko
 */
public interface IReplicator {

  /**
   * <p>It will replicate data from requested(source) database
   * to requesting(destination) database.</p>
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  void replicate(Map<String, Object> pAddParam) throws Exception;
}
