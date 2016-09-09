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

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.accounting.service.SrvAccSettings;
import org.beigesoft.accounting.persistable.Account;
import org.beigesoft.accounting.persistable.SubaccountLine;
import org.beigesoft.accounting.model.AccountInChart;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>
 * Chart Of Accounts servlet.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WChartOfAccounts<RS> extends HttpServlet {

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
      @SuppressWarnings("unchecked")
      ISrvDatabase<RS> srvDatabase = (ISrvDatabase<RS>) this.factoryAppBeans
        .lazyGet("ISrvDatabase");
      @SuppressWarnings("unchecked")
      ISrvOrm<RS> srvOrm = (ISrvOrm<RS>) this.factoryAppBeans
        .lazyGet("ISrvOrm");
      List<Account> accounts = null;
      List<AccountInChart> accountsInChart = new ArrayList<AccountInChart>();
      try {
        srvDatabase.setIsAutocommit(false);
        srvDatabase.
          setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
        srvDatabase.beginTransaction();
        accounts = srvOrm.retrieveListWithConditions(Account.class,
          "where ISUSED=1 order by ITSNUMBER");
        for (Account acc : accounts) {
          AccountInChart accInChart = new AccountInChart();
          accInChart.setItsId(acc.getItsId());
          accInChart.setItsName(acc.getItsName());
          accInChart.setItsNumber(acc.getItsNumber());
          accInChart.setDescription(acc.getDescription());
          accInChart.setItsType(acc.getItsType());
          accInChart.setNormalBalanceType(acc.getNormalBalanceType());
          if (acc.getSubaccType() == null) {
            accountsInChart.add(accInChart);
          } else {
            @SuppressWarnings("unchecked")
            List<SubaccountLine> subaccounts = srvOrm
              .retrieveEntityOwnedlist(SubaccountLine.class, acc);
            if (subaccounts.size() > 0) {
              for (SubaccountLine subacc : subaccounts) {
                if (accInChart != null) {
                  accInChart.setSubacc(subacc.getSubaccName());
                  accountsInChart.add(accInChart);
                  accInChart = null;
                } else {
                  AccountInChart subaccInChart = new AccountInChart();
                  subaccInChart.setSubacc(subacc.getSubaccName());
                  accountsInChart.add(subaccInChart);
                }
              }
            } else {
              accountsInChart.add(accInChart);
            }
          }
        }
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
      SrvAccSettings srvAccSettings = (SrvAccSettings) this.factoryAppBeans
        .lazyGet("srvAccSettings");
      pReq.setAttribute("accSettings", srvAccSettings.lazyGetAccSettings());
      pReq.setAttribute("srvI18n", srvI18n);
      pReq.setAttribute("accounts", accountsInChart);
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
