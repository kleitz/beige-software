package org.beigesoft.web.factory;

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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.io.File;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Custom JNDI factory to resolve database path
 * #currentParentDir# or #currentDir# for H2 datasource
 * that required full database path.
 * </p>
 *
 * @author Yury Demidenko
 */
public class DataSourceH2JndiFactory implements ObjectFactory {

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public final Object getObjectInstance(final Object obj, final Name name,
    final Context nameCtx, final Hashtable environment) throws NamingException,
      ClassNotFoundException {
    Properties props = new Properties();
    Reference ref = (Reference) obj;
    Enumeration addrs = ref.getAll();
    RefAddr addr = null;
    while (addrs.hasMoreElements()) {
      addr = (RefAddr) addrs.nextElement();
      if (addr.getType().equals("dataSourceClassName")) {
        props.setProperty("dataSourceClassName", (String) addr.getContent());
      } else if (addr.getType().equals("dataSourceUser")) {
        props.setProperty("dataSource.user", (String) addr.getContent());
      } else if (addr.getType().equals("dataSourcePassword")) {
        props.setProperty("dataSource.password", (String) addr.getContent());
      } else if (addr.getType().equals("dataSourceUrl")) {
        String url = (String) addr.getContent();
        if (url.contains(ISrvOrm.WORD_CURRENT_DIR)) {
          String currDir = System.getProperty("user.dir");
          url = url.replace(ISrvOrm.WORD_CURRENT_DIR, currDir + File.separator);
        } else if (url.contains(ISrvOrm.WORD_CURRENT_PARENT_DIR)) {
          File fcd = new File(System.getProperty("user.dir"));
          url = url.replace(ISrvOrm.WORD_CURRENT_PARENT_DIR, fcd.getParent()
            + File.separator);
        }
        props.setProperty("dataSource.Url", url);
      }
    }
    HikariConfig config = new HikariConfig(props);
    HikariDataSource ds = new HikariDataSource(config);
    return ds;
  }
}
