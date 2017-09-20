package org.beigesoft.test.persistable;

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

import java.util.List;

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
