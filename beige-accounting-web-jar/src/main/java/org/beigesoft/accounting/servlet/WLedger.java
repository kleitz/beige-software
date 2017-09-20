package org.beigesoft.accounting.servlet;

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
import java.util.HashMap;
import java.util.Date;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.beigesoft.log.ILogger;
import org.beigesoft.service.ISrvI18n;
import org.beigesoft.service.ISrvDate;
import org.beigesoft.accounting.model.LedgerPrevious;
import org.beigesoft.accounting.model.LedgerDetail;
import org.beigesoft.accounting.persistable.Account;
import org.beigesoft.accounting.service.ISrvLedger;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>
 * Ledger servlet Accounting specification#2.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WLedger<RS> extends HttpServlet {

  /**
   * <p>App beans factort.</p>
   **/
  private IFactoryAppBeans factoryAppBeans;

  /**
   * <p>Folder for redirected JSP, e.g. "JSP WEB-INF/jsp/".
   * Settled through init params.</p>
   **/
  private String dirJsp;

  @Override
  public final void init() throws ServletException {
    this.dirJsp = getInitParameter("dirJsp");
    try {
      factoryAppBeans = (IFactoryAppBeans) getServletContext()
        .getAttribute("IFactoryAppBeans");
    } catch (Exception e) {
      if (this.factoryAppBeans != null) {
        ILogger logger = null;
        try {
          logger = (ILogger) this.factoryAppBeans.lazyGet("ILogger");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        if (logger != null) {
          logger.error(null, getClass(), "INIT", e);
        }
      }
      throw new ServletException(e);
    }
  }

  @Override
  public final void doGet(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    try {
      ISrvLedger srvLedger = (ISrvLedger) this.factoryAppBeans
        .lazyGet("ISrvLedger");
      @SuppressWarnings("unchecked")
      ISrvDatabase<RS> srvDatabase = (ISrvDatabase<RS>) this.factoryAppBeans
        .lazyGet("ISrvDatabase");
      @SuppressWarnings("unchecked")
      ISrvOrm<RS> srvOrm = (ISrvOrm<RS>) this.factoryAppBeans
        .lazyGet("ISrvOrm");
      String userName = null;
      if (pReq.getUserPrincipal() != null) {
        userName = pReq.getUserPrincipal().getName();
      }
      Map<String, Object> addParam = new HashMap<String, Object>();
      addParam.put("user", userName);
      addParam.put("parameterMap", pReq.getParameterMap());
      ISrvDate srvDate = (ISrvDate) this.factoryAppBeans
        .lazyGet("ISrvDate");
      Date date1 = srvDate
        .fromIso8601DateTimeNoTz(pReq.getParameter("date1"), null);
      Date date2 = srvDate
        .fromIso8601DateTimeNoTz(pReq.getParameter("date2"), null);
      String accId = pReq.getParameter("accId");
      String subaccId = pReq.getParameter("subaccId");
      String subaccName = pReq.getParameter("subaccName");
      if (subaccId != null && subaccId.trim().length() == 0) {
        subaccId = null;
      }
      if (subaccName != null && subaccName.trim().length() == 0) {
        subaccName = null;
      }
      LedgerPrevious ledgerPrevious = null;
      LedgerDetail ledgerDetail = null;
      Account account = null;
      try {
        srvDatabase.setIsAutocommit(false);
        srvDatabase.
          setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
        srvDatabase.beginTransaction();
        account = new Account();
        account.setItsId(accId);
        account = srvOrm.retrieveEntity(addParam, account);
        ledgerPrevious = srvLedger.retrievePrevious(addParam, account,
          date1, subaccId);
        ledgerDetail = srvLedger.retrieveDetail(addParam, account,
          date1, date2, subaccId, ledgerPrevious);
        srvDatabase.commitTransaction();
      } catch (Exception ex) {
        srvDatabase.rollBackTransaction();
        throw ex;
      } finally {
        srvDatabase.releaseResources();
      }
      String nmRnd = pReq.getParameter("nmRnd");
      String path = dirJsp + nmRnd + ".jsp";
      RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
      ISrvI18n srvI18n = (ISrvI18n) this.factoryAppBeans
        .lazyGet("ISrvI18n");
      pReq.setAttribute("srvI18n", srvI18n);
      pReq.setAttribute("srvDate", srvDate);
      pReq.setAttribute("account", account);
      pReq.setAttribute("subaccName", subaccName);
      pReq.setAttribute("ledgerPrevious", ledgerPrevious);
      pReq.setAttribute("ledgerDetail", ledgerDetail);
      pReq.setAttribute("date1", date1);
      pReq.setAttribute("date2", date2);
      ISrvAccSettings srvAccSettings = (ISrvAccSettings) this.factoryAppBeans
        .lazyGet("ISrvAccSettings");
      pReq.setAttribute("accSettings",
        srvAccSettings.lazyGetAccSettings(addParam));
      rd.include(pReq, pResp);
    } catch (Exception e) {
      if (this.factoryAppBeans != null) {
        ILogger logger = null;
        try {
          logger = (ILogger) this.factoryAppBeans.lazyGet("ILogger");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        if (logger != null) {
          logger.error(null, getClass(), "WORK", e);
        } else {
          e.printStackTrace();
        }
      } else {
        e.printStackTrace();
      }
      if (e instanceof ExceptionWithCode) {
        pReq.setAttribute("error_code",
          ((ExceptionWithCode) e).getCode());
        pReq.setAttribute("short_message",
          ((ExceptionWithCode) e).getShortMessage());
      } else {
        pReq.setAttribute("error_code",
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
      pReq.setAttribute("javax.servlet.error.status_code",
        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      pReq.setAttribute("javax.servlet.error.exception", e);
      pReq.setAttribute("javax.servlet.error.request_uri",
        pReq.getRequestURI());
      pReq.setAttribute("javax.servlet.error.servlet_name", this.getClass()
        .getCanonicalName());
      pResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for factoryAppBeans.</p>
   * @return IFactoryAppBeans
   **/
  public final IFactoryAppBeans getFactoryAppBeans() {
    return this.factoryAppBeans;
  }

  /**
   * <p>Setter for factoryAppBeans.</p>
   * @param pFactoryAppBeans reference
   **/
  public final void setFactoryAppBeans(
    final IFactoryAppBeans pFactoryAppBeans) {
    this.factoryAppBeans = pFactoryAppBeans;
  }

  /**
   * <p>Geter for dirJsp.</p>
   * @return String
   **/
  public final String getDirJsp() {
    return this.dirJsp;
  }

  /**
   * <p>Setter for dirJsp.</p>
   * @param pDirJsp reference
   **/
  public final void setDirJsp(final String pDirJsp) {
    this.dirJsp = pDirJsp;
  }
}
