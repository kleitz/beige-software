package org.beigesoft.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <p>Abstraction of persistable model with Long ID and version.</p>
 *
 * @author Yury Demidenko
 */
public interface IHasIdLongVersion extends IHasId<Long>, IHasVersion {

}
