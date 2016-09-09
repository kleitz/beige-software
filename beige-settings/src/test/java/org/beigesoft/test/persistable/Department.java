package org.beigesoft.test.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.persistable.APersistableBaseHasName;

/**
 * <pre>
 * Model for test ORM.
 * For check that it is retrieved from 2-nd level only ID.
 * It is in PersistableHead, so when PersistableLine is retrieved
 * then persistableLine.getPersistableHead().getItsDepartment().getItsId() != null
 * persistableLine.getPersistableHead().getItsDepartment().getItsName() == null
 * </pre>
 *
 * @author Yury Demidenko
 */
public class Department extends APersistableBaseHasName {

}
