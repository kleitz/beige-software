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
import java.io.File;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import com.zaxxer.hikari.HikariDataSource;

import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Custom JNDI factory to resolve multi-platform(\/) database path
 * #currentParentDir# or #currentDir# for SQLITE datasource.
 * </p>
 *
 * @author Yury Demidenko
 */
public class DataSourceSqliteJndiFactory implements ObjectFactory {

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public final Object getObjectInstance(final Object obj, final Name name,
    final Context nameCtx, final Hashtable environment) throws NamingException,
      ClassNotFoundException {
    HikariDataSource ds = null;
    Reference ref = (Reference) obj;
    Enumeration addrs = ref.getAll();
    RefAddr addr = null;
    while (addrs.hasMoreElements()) {
      addr = (RefAddr) addrs.nextElement();
      if (addr.getType().equals("jdbcUrl")) {
        String jdbcUrl = (String) addr.getContent();
        if (jdbcUrl.contains(ISrvOrm.WORD_CURRENT_DIR)) {
          String currDir = System.getProperty("user.dir");
          jdbcUrl = jdbcUrl.replace(ISrvOrm.WORD_CURRENT_DIR, currDir
            + File.separator);
        } else if (jdbcUrl.contains(ISrvOrm.WORD_CURRENT_PARENT_DIR)) {
          File fcd = new File(System.getProperty("user.dir"));
          jdbcUrl = jdbcUrl.replace(ISrvOrm.WORD_CURRENT_PARENT_DIR,
            fcd.getParent() + File.separator);
        }
        ds = new HikariDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setJdbcUrl(jdbcUrl);
      }
    }
    return ds;
  }
}
