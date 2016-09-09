package org.beigesoft.accounting.servlet;

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
import java.util.HashMap;
import java.util.Date;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.accounting.model.LedgerPrevious;
import org.beigesoft.accounting.model.LedgerDetail;
import org.beigesoft.accounting.persistable.Account;
import org.beigesoft.accounting.service.ISrvLedger;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.accounting.service.SrvAccSettings;
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
        .lazyGet("srvLedger");
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
      Map<String, Object> addParams = new HashMap<String, Object>();
      addParams.put("user", userName);
      addParams.put("parameterMap", pReq.getParameterMap());
      Date date1 = new Date(Long.parseLong(pReq.getParameter("date1")));
      Date date2 = new Date(Long.parseLong(pReq.getParameter("date2")));
      String accId = pReq.getParameter("accId");
      String subaccName = pReq.getParameter("subaccName");
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
        account = srvOrm.retrieveEntityById(Account.class, accId);
        ledgerPrevious = srvLedger.retrievePrevious(addParams, account,
          date1, subaccName);
        ledgerDetail = srvLedger.retrieveDetail(addParams, account,
          date1, date2, subaccName, ledgerPrevious);
        srvDatabase.commitTransaction();
      } catch (Exception ex) {
        srvDatabase.rollBackTransaction();
        throw ex;
      } finally {
        srvDatabase.releaseResources();
      }
      String nameRenderer = pReq.getParameter("nameRenderer");
      String path = dirJsp + nameRenderer + ".jsp";
      RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
      ISrvI18n srvI18n = (ISrvI18n) this.factoryAppBeans
        .lazyGet("ISrvI18n");
      pReq.setAttribute("srvI18n", srvI18n);
      pReq.setAttribute("account", account);
      pReq.setAttribute("subaccName", subaccName);
      pReq.setAttribute("ledgerPrevious", ledgerPrevious);
      pReq.setAttribute("ledgerDetail", ledgerDetail);
      pReq.setAttribute("date1", date1);
      pReq.setAttribute("date2", date2);
      SrvAccSettings srvAccSettings = (SrvAccSettings) this.factoryAppBeans
        .lazyGet("srvAccSettings");
      pReq.setAttribute("accSettings", srvAccSettings.lazyGetAccSettings());
      rd.include(pReq, pResp);
    } catch (Exception e) {
      e.printStackTrace();
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
