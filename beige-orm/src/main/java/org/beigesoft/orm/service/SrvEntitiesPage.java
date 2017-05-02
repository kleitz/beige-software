package org.beigesoft.orm.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Field;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.IRequestData;
import org.beigesoft.model.Page;
import org.beigesoft.service.ISrvI18n;
import org.beigesoft.service.ISrvDate;
import org.beigesoft.service.ISrvPage;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.holder.IHolderForClassByName;

/**
 * <p>Service that retrieve entities page or filter data according request.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvEntitiesPage<RS> implements ISrvEntitiesPage {

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>ORM service.</p>
   **/
  private ASrvOrm<RS> srvOrm;

  /**
   * <p>Page service.</p>
   */
  private ISrvPage srvPage;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDate srvDate;

  /**
   * <p>Manager UVD settings.</p>
   **/
  private IMngSettings mngUvdSettings;

  /**
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<?>> entitiesMap;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterToFromString<?>>
    convertersFieldsFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldConverterNamesHolder;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Retrieve entities page - entities list, pages, filter map etc.</p>
   * @param pAddParam additional param
   * @param pRequestData Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void retrievePage(final Map<String, Object> pAddParam,
    final IRequestData pRequestData) throws Exception {
    String nmEnt;
    if (pAddParam.get("nameOwnerEntity") != null) {
      // owned entity put it to refresh owner list
      nmEnt = (String) pAddParam.get("nameOwnerEntity");
    } else {
      nmEnt = pRequestData.getParameter("nmEnt");
    }
    Class<?> entityClass = getEntitiesMap().get(nmEnt);
    Set<String> filterAppearance = null;
    if (pRequestData.getParameter("flyNeedFltAppear") != null) {
      filterAppearance = new HashSet<String>();
      pAddParam.put("filterAppearance", filterAppearance);
    }
    revealPageFilterData(pAddParam, pRequestData, entityClass);
    StringBuffer sbWhere = (StringBuffer) pAddParam.get("sbWhere");
    Map<String, Object> filterMap =
      (Map<String, Object>) pAddParam.get("filterMap");
    //cause settled either from request or from settings
    Map<String, String> orderMap = new HashMap<String, String>();
    String queryOrderBy = "";
    String fltOrdPrefix;
    String nmRnd = pRequestData.getParameter("nmRnd");
    if (nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String orderBy = pRequestData.getParameter(fltOrdPrefix + "orderBy");
    if (orderBy != null && !orderBy.equals("disabled")) {
      orderMap.put(fltOrdPrefix + "orderBy", orderBy);
      String desc = "";
      if (pRequestData.getParameter(fltOrdPrefix + "orderByDesc") != null) {
        // HTML form send nothing for unchecked checkbox
        desc = " desc";
        orderMap.put(fltOrdPrefix + "orderByDesc", "on");
      }
      queryOrderBy = " order by " + nmEnt.toUpperCase() + "."
        + orderBy.toUpperCase() + desc;
    } else {
      orderMap.put(fltOrdPrefix + "orderBy", this.mngUvdSettings
        .getClassesSettings().get(entityClass)
          .get("orderByDefault"));
      String orderByDesc = this.mngUvdSettings.getClassesSettings()
        .get(entityClass).get("orderByDescDefault");
      String orderByDescStr = "";
      if (orderByDesc.equals("on")) {
        orderMap.put(fltOrdPrefix + "orderByDesc", orderByDesc);
        orderByDescStr = " desc";
      }
      queryOrderBy = " order by " + nmEnt.toUpperCase() + "."
        + orderMap.get(fltOrdPrefix + "orderBy").toUpperCase() + orderByDescStr;
    }
    Integer rowCount;
    if (sbWhere.length() > 0) {
      rowCount = this.srvOrm.evalRowCountWhere(pAddParam, entityClass,
        sbWhere.toString());
    } else {
      rowCount = this.srvOrm.evalRowCount(pAddParam, entityClass);
    }
    Integer page = Integer.valueOf(pRequestData.getParameter("page"));
    Integer itemsPerPage = Integer.valueOf(mngUvdSettings.getAppSettings()
      .get("itemsPerPage"));
    int totalPages = srvPage.evalPageCount(rowCount, itemsPerPage);
    if (page > totalPages) {
      page = totalPages;
    }
    int firstResult = (page - 1) * itemsPerPage; //0-20,20-40
    List entities;
    if (sbWhere.length() > 0 || queryOrderBy.length() > 0) {
      if (sbWhere.length() > 0) {
        entities = this.srvOrm.retrievePageWithConditions(pAddParam,
          entityClass, "where " + sbWhere.toString() + queryOrderBy,
            firstResult, itemsPerPage);
      } else {
        entities = this.srvOrm.retrievePageWithConditions(pAddParam,
          entityClass, queryOrderBy, firstResult, itemsPerPage);
      }
    } else {
      entities = this
        .srvOrm.retrievePage(pAddParam, entityClass, firstResult, itemsPerPage);
    }
    Integer paginationTail = Integer.valueOf(mngUvdSettings.getAppSettings()
      .get("paginationTail"));
    List<Page> pages = srvPage.evalPages(page, totalPages, paginationTail);
    pRequestData.setAttribute("totalItems", rowCount);
    pRequestData.setAttribute("pages", pages);
    pRequestData.setAttribute("orderMap", orderMap);
    pRequestData.setAttribute("filterMap", filterMap);
    pRequestData.setAttribute("entities", entities);
    pRequestData.setAttribute("classEntity", entityClass);
    pRequestData.setAttribute("mngUvds", this.mngUvdSettings);
    pRequestData.setAttribute("srvOrm", this.srvOrm);
    pRequestData.setAttribute("hldCnvFtfsNames",
      this.fieldConverterNamesHolder);
    pRequestData.setAttribute("fctCnvFtfs", this.convertersFieldsFatory);
    if (filterAppearance != null) {
      pRequestData.setAttribute("filterAppearance", filterAppearance);
    }
  }

  /**
   * <p>Retrieve page filter data like SQL where and filter map.</p>
   * @param pAddParam additional param to return revealed data
   * @param pRequestData - Request Data
   * @param pEntityClass Entity Class
   * @throws Exception - an Exception
   **/
  @Override
  public final void revealPageFilterData(final Map<String, Object> pAddParam,
    final IRequestData pRequestData,
      final Class<?> pEntityClass) throws Exception {
    // it is not null if need to client e.g. for bulk operations
    Set<String> filterAppearance =
      (Set<String>) pAddParam.get("filterAppearance");
    StringBuffer sbWhere = new StringBuffer("");
    Map<String, Object> filterMap = new HashMap<String, Object>();
    List<Map.Entry<String, Map<String, String>>> fields =
      this.mngUvdSettings.makeFldPropLst(pEntityClass,
        "orderPrintfullList"); //cause use hidden field for filter
    String nmEnt = pEntityClass.getSimpleName();
    for (Map.Entry<String, Map<String, String>> entry : fields) {
      String wdgFilter = entry.getValue().get("wdgFilter");
      if (wdgFilter != null) {
        if ("filterEntity".equals(wdgFilter)) {
          //Only simple ID - Integer/Long/String:
          Field fldRapi = this.fieldsRapiHolder
            .getFor(pEntityClass, entry.getKey());
          if (fldRapi.getType() == String.class) {
            tryMakeWhereEntityIdString(sbWhere, pRequestData, nmEnt,
              entry.getKey(), filterMap, filterAppearance);
          } else {
            tryMakeWhereEntity(sbWhere, pRequestData, nmEnt,
              entry.getKey(), filterMap, filterAppearance);
          }
        } else if ("filterString".equals(wdgFilter)) {
          tryMakeWhereString(sbWhere, pRequestData, nmEnt,
            entry.getKey(), filterMap, filterAppearance);
        } else if ("filterDateTime".equals(wdgFilter)) {
          tryMakeWhereDateTime(sbWhere, pRequestData, nmEnt,
            entry.getKey(), "1", filterMap, filterAppearance);
          tryMakeWhereDateTime(sbWhere, pRequestData, nmEnt,
            entry.getKey(), "2", filterMap, filterAppearance);
        } else if ("filterEnum".equals(wdgFilter)) {
          tryMakeWhereEnum(sbWhere, pRequestData, pEntityClass,
            entry.getKey(), filterMap, filterAppearance);
        } else if ("filterBoolean".equals(wdgFilter)) {
          tryMakeWhereBoolean(sbWhere, pRequestData, pEntityClass,
            entry.getKey(), filterMap, filterAppearance);
        } else if (wdgFilter.startsWith("explicitFilter")) {
          tryMakeWhereExplicitFilter(sbWhere, pRequestData, pEntityClass,
            entry.getKey(), filterMap, filterAppearance);
        } else {
          tryMakeWhereStd(sbWhere, pRequestData, nmEnt,
            entry.getKey(), "1", filterMap, filterAppearance);
          tryMakeWhereStd(sbWhere, pRequestData, nmEnt,
            entry.getKey(), "2", filterMap, filterAppearance);
        }
      }
    }
    pAddParam.put("filterMap", filterMap);
    pAddParam.put("sbWhere", sbWhere);
  }

  /**
   * <p>Make SQL WHERE clause if need.</p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereString(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final String pNameEntity,
      final String pFldNm,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd != null && nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd != null && nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pRequestData.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val";
    String fltVal = pRequestData.getParameter(nmFldVal);
    String nmFldOpr = fltOrdPrefix + pFldNm + "Opr";
    String valFldOpr = pRequestData.getParameter(nmFldOpr);
    String cond = null;
    if ("isnotnull".equals(valFldOpr) || "isnull".equals(valFldOpr)) {
        cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
    } else if (fltVal != null && valFldOpr != null
      && !valFldOpr.equals("disabled") && !valFldOpr.equals("")) {
      cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
          + toSqlOperator(valFldOpr)
          + " '" + fltVal + "'";
    }
    if (cond != null) {
      pFilterMap.put(nmFldVal, fltVal);
      pFilterMap.put(nmFldOpr, valFldOpr);
      if (pFilterAppearance != null) {
        pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
          + getSrvI18n().getMsg(valFldOpr) + " " + fltVal);
      }
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause if need.</p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pParSuffix - parameter suffix
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereStd(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final String pNameEntity,
      final String pFldNm, final String pParSuffix,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd != null && nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd != null && nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pRequestData.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val" + pParSuffix;
    String fltVal = pRequestData.getParameter(nmFldVal);
    String nmFldOpr = fltOrdPrefix + pFldNm
      + "Opr" + pParSuffix;
    String valFldOpr = pRequestData.getParameter(nmFldOpr);
    String cond = null;
    if ("isnotnull".equals(valFldOpr) || "isnull".equals(valFldOpr)) {
        cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
    } else if (fltVal != null && valFldOpr != null
      && !valFldOpr.equals("disabled") && !valFldOpr.equals("")) {
      cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
          + toSqlOperator(valFldOpr)
          + " " + fltVal;
    }
    if (cond != null) {
      pFilterMap.put(nmFldVal, fltVal);
      pFilterMap.put(nmFldOpr, valFldOpr);
      if (pFilterAppearance != null) {
        pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
          + getSrvI18n().getMsg(valFldOpr) + " " + fltVal);
      }
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for date-time if need.</p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pParSuffix - parameter suffix
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereDateTime(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final String pNameEntity,
      final String pFldNm, final String pParSuffix,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd != null && nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd != null && nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pRequestData.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val" + pParSuffix;
    String fltVal = pRequestData.getParameter(nmFldVal);
    String nmFldOpr = fltOrdPrefix + pFldNm
      + "Opr" + pParSuffix;
    String valFldOpr = pRequestData.getParameter(nmFldOpr);
    String cond = null;
    if ("isnotnull".equals(valFldOpr) || "isnull".equals(valFldOpr)) {
        cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
    } else if (fltVal != null && valFldOpr != null
      && !valFldOpr.equals("disabled") && !valFldOpr.equals("")) {
      Date valDt;
      if (fltVal.contains(".")) { //2001-07-04T12:08:56.235
        valDt = this.srvDate.fromIso8601FullNoTz(fltVal, null);
      } else if (fltVal.contains(":")) {
        if (fltVal.length() == 19) { //2001-07-04T12:08:56
          valDt = this.srvDate.fromIso8601DateTimeSecNoTz(fltVal, null);
        } else { //2001-07-04T12:08
          valDt = this.srvDate.fromIso8601DateTimeNoTz(fltVal, null);
        }
      } else { //2001-07-04
        valDt = this.srvDate.fromIso8601DateNoTz(fltVal, null);
      }
      cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
          + toSqlOperator(valFldOpr)
          + " " + valDt.getTime();
    }
    if (cond != null) {
      pFilterMap.put(nmFldVal, fltVal);
      pFilterMap.put(nmFldOpr, valFldOpr);
      if (pFilterAppearance != null) {
        pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
          + getSrvI18n().getMsg(valFldOpr) + " " + fltVal);
      }
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL operator e.g. 'eq'-&gt;'&gt;'.</p>
   * @param pOper operator - eq, gt, lt
   * @return SQL operator
   * @throws ExceptionWithCode - code 1003 WRONG_PARAMETER
   **/
  public final String toSqlOperator(
    final String pOper) throws ExceptionWithCode {
    if ("eq".equals(pOper)) {
      return "=";
    } else if ("ne".equals(pOper)) {
      return "!=";
    } else if ("gt".equals(pOper)) {
      return ">";
    } else if ("gte".equals(pOper)) {
      return ">=";
    } else if ("in".equals(pOper)) {
      return "in";
    } else if ("lt".equals(pOper)) {
      return "<";
    } else if ("lte".equals(pOper)) {
      return "<=";
    } else if ("isnull".equals(pOper)) {
      return "is null";
    } else if ("isnotnull".equals(pOper)) {
      return "is not null";
    } else if ("like".equals(pOper)) {
      return "like";
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can't match SQL operator: " + pOper);
    }
  }

  /**
   * <p>Make SQL WHERE clause for entity if need.</p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereEntity(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final String pNameEntity,
      final String pFldNm,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String nmFldValId = fltOrdPrefix + pFldNm + "ValId";
    String fltValId = pRequestData.getParameter(nmFldValId);
    String nmFldOpr = fltOrdPrefix + pFldNm + "Opr";
    String valFldOpr = pRequestData.getParameter(nmFldOpr);
    if (valFldOpr != null && !valFldOpr.equals("disabled")) {
      // equals or not to empty
      if (fltValId == null || fltValId.length() == 0) {
        if (valFldOpr.equals("eq")) {
          valFldOpr = "isnull";
        } else if (valFldOpr.equals("ne")) {
          valFldOpr = "isnotnull";
        }
      }
      pFilterMap.put(nmFldOpr, valFldOpr);
      String fltforcedName = fltOrdPrefix + "forcedFor";
      String fltforced = pRequestData.getParameter(fltforcedName);
      if (fltforced != null) {
        pFilterMap.put(fltforcedName, fltforced);
      }
      if (valFldOpr.equals("isnull")
        || valFldOpr.equals("isnotnull")) {
        String cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
        if (pFilterAppearance != null) {
          pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
            + getSrvI18n().getMsg(valFldOpr));
        }
      } else if (!valFldOpr.equals("disabled") && !valFldOpr.equals("")
        && fltValId != null && fltValId.length() > 0) {
        pFilterMap.put(nmFldValId, fltValId);
        String nmFldValAppearance = fltOrdPrefix + pFldNm
          + "ValAppearance";
        String fltValAppearance = pRequestData.getParameter(nmFldValAppearance);
        pFilterMap.put(nmFldValAppearance, fltValAppearance);
        String valId = fltValId;
        if (valFldOpr.equals("in")) {
          valId = "(" + valId + ")";
        }
        String cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
              + toSqlOperator(valFldOpr)
                + " " + valId;
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
        if (pFilterAppearance != null) {
          pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
            + getSrvI18n().getMsg(valFldOpr) + " " + fltValAppearance);
        }
      }
    }
  }


  /**
   * <p>Make SQL WHERE clause for entity if need.</p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereEntityIdString(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final String pNameEntity,
      final String pFldNm,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String nmFldValId = fltOrdPrefix + pFldNm + "ValId";
    String fltValId = pRequestData.getParameter(nmFldValId);
    String nmFldOpr = fltOrdPrefix + pFldNm + "Opr";
    String valFldOpr = pRequestData.getParameter(nmFldOpr);
    if (valFldOpr != null) {
      // equals or not to empty
      if (fltValId == null || fltValId.length() == 0) {
        if (valFldOpr.equals("eq")) {
          valFldOpr = "isnull";
        } else if (valFldOpr.equals("ne")) {
          valFldOpr = "isnotnull";
        }
      }
      pFilterMap.put(nmFldOpr, valFldOpr);
      String fltforcedName = fltOrdPrefix + "forcedFor";
      String fltforced = pRequestData.getParameter(fltforcedName);
      if (fltforced != null) {
        pFilterMap.put(fltforcedName, fltforced);
      }
      if (valFldOpr.equals("isnull")
        || valFldOpr.equals("isnotnull")) {
        String cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
        if (pFilterAppearance != null) {
          pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
            + getSrvI18n().getMsg(valFldOpr));
        }
      } else if (!valFldOpr.equals("disabled") && !valFldOpr.equals("")
        && fltValId != null && fltValId.length() > 0) {
        pFilterMap.put(nmFldValId, fltValId);
        String nmFldValAppearance = fltOrdPrefix + pFldNm
          + "ValAppearance";
        String fltValAppearance = pRequestData.getParameter(nmFldValAppearance);
        pFilterMap.put(nmFldValAppearance, fltValAppearance);
        String valId = fltValId;
        if (valFldOpr.equals("in")) {
          valId = "(" + valId + ")"; //usually made by hand forced in widget
        } else {
          valId = "'" + valId + "'";
        }
        String cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
              + toSqlOperator(valFldOpr)
                + " " + valId;
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
        if (pFilterAppearance != null) {
          pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
            + getSrvI18n().getMsg(valFldOpr) + " " + fltValAppearance);
        }
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for enum if need.</p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pEntityClass - entity class
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereEnum(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final Class<?> pEntityClass,
      final String pFldNm,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pRequestData.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val";
    String fltVal = pRequestData.getParameter(nmFldVal);
    String nmFldOpr = fltOrdPrefix + pFldNm + "Opr";
    String valFldOpr = pRequestData.getParameter(nmFldOpr);
    if (fltVal != null && fltVal.length() > 0 && valFldOpr != null
      && !valFldOpr.equals("disabled") && !valFldOpr.equals("")) {
      String val;
      String valAppear;
      Field fldEnum = this.fieldsRapiHolder.getFor(pEntityClass, pFldNm);
      Class classEnum = fldEnum.getType();
      if (valFldOpr.equals("in")) {
        StringBuffer sbVal = new StringBuffer("(");
        StringBuffer sbValAppear = new StringBuffer("(");
        boolean isFirst = true;
        for (String vl : fltVal.split(",")) {
          if (isFirst) {
            isFirst = false;
          } else {
            sbVal.append(", ");
            sbValAppear.append(", ");
          }
          Enum enVal = Enum.valueOf(classEnum, vl);
          sbVal.append(String.valueOf(enVal.ordinal()));
          sbValAppear.append(getSrvI18n().getMsg(vl));
        }
        val = sbVal.toString() + ")";
        valAppear = sbValAppear.toString() + ")";
      } else {
        Enum enVal = Enum.valueOf(classEnum, fltVal);
        val = String.valueOf(enVal.ordinal());
        valAppear = getSrvI18n().getMsg(fltVal);
      }
      pFilterMap.put(fltOrdPrefix + pFldNm + "ValAppearance", valAppear);
      pFilterMap.put(nmFldVal, fltVal);
      pFilterMap.put(nmFldOpr, valFldOpr);
      String cond = pEntityClass.getSimpleName().toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
          + toSqlOperator(valFldOpr)
          + " " + val;
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
      if (pFilterAppearance != null) {
        pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " "
          + getSrvI18n().getMsg(valFldOpr) + " " + valAppear);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for filter that
   * pass explicit escaped "where":
   * " gte "is ">="
   * " lte "is "<="
   * " lt "is "<"
   * " gt "is ">"
   * " apst "is "'"
   * " prcnt "is "%"
   * " undln "is "_"
   * e.g.:
   * "DESCRIPTION apst prcnt 200 undln apst and PAYMENTTOTAL/ITSTOTAL gte 0.05"
   * is treated as
   * "DESCRIPTION '%200' and PAYMENTTOTAL/ITSTOTAL >= 0.05".
   * </p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pEntityClass - entity class
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereExplicitFilter(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final Class<?> pEntityClass,
      final String pFldNm,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pRequestData.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val";
    String fltVal = pRequestData.getParameter(nmFldVal);
    if (fltVal != null && fltVal.length() > 0
      && !fltVal.equals("disabled")) {
      pFilterMap.put(nmFldVal, fltVal);
      String cond = fltVal.replace(" gte ", ">=");
      cond = cond.replace(" lte ", "<=");
      cond = cond.replace(" lt ", "<");
      cond = cond.replace(" gt ", ">");
      cond = cond.replace(" ne ", "!=");
      cond = cond.replace(" eq ", "=");
      cond = cond.replace(" apst ", "'");
      cond = cond.replace(" prcnt ", "%");
      cond = cond.replace(" undln ", "_");
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
      if (pFilterAppearance != null) {
        pFilterAppearance.add(cond);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for boolean if need.</p>
   * @param pSbWhere result clause
   * @param pRequestData - Request Data
   * @param pEntityClass - entity class
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @param pFilterAppearance - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereBoolean(final StringBuffer pSbWhere,
    final IRequestData pRequestData, final Class<?> pEntityClass,
      final String pFldNm,
        final Map<String, Object> pFilterMap,
          final Set<String> pFilterAppearance) throws Exception {
    String nmRnd = pRequestData.getParameter("nmRnd");
    String fltOrdPrefix;
    if (nmRnd.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nmRnd.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pRequestData.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val";
    String fltVal = pRequestData.getParameter(nmFldVal);
    if (fltVal != null && (fltVal.length() == 0
      || "null".equals(fltVal))) {
      fltVal = null;
    }
    pFilterMap.put(nmFldVal, fltVal);
    if (fltVal != null) {
      int intVal = 0;
      if (fltVal.equals("true")) {
        intVal = 1;
      }
      String cond = pEntityClass.getSimpleName().toUpperCase()
          + "." + pFldNm.toUpperCase() + " = " + intVal;
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
      if (pFilterAppearance != null) {
        pFilterAppearance.add(getSrvI18n().getMsg(pFldNm) + " = " + fltVal);
      }
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvI18n.</p>
   * @return ISrvI18n
   **/
  public final ISrvI18n getSrvI18n() {
    return this.srvI18n;
  }

  /**
   * <p>Setter for srvI18n.</p>
   * @param pSrvI18n reference
   **/
  public final void setSrvI18n(final ISrvI18n pSrvI18n) {
    this.srvI18n = pSrvI18n;
  }

  /**
   * <p>Getter for srvOrm.</p>
   * @return ASrvOrm<RS>
   **/
  public final ASrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ASrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for srvPage.</p>
   * @return ISrvPage
   **/
  public final ISrvPage getSrvPage() {
    return this.srvPage;
  }

  /**
   * <p>Setter for srvPage.</p>
   * @param pSrvPage reference
   **/
  public final void setSrvPage(final ISrvPage pSrvPage) {
    this.srvPage = pSrvPage;
  }

  /**
   * <p>Getter for srvDate.</p>
   * @return ISrvDate
   **/
  public final ISrvDate getSrvDate() {
    return this.srvDate;
  }

  /**
   * <p>Setter for srvDate.</p>
   * @param pSrvDate reference
   **/
  public final void setSrvDate(final ISrvDate pSrvDate) {
    this.srvDate = pSrvDate;
  }

  /**
   * <p>Getter for mngUvdSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngUvdSettings() {
    return this.mngUvdSettings;
  }

  /**
   * <p>Setter for mngUvdSettings.</p>
   * @param pMngUvdSettings reference
   **/
  public final void setMngUvdSettings(final IMngSettings pMngUvdSettings) {
    this.mngUvdSettings = pMngUvdSettings;
  }

  /**
   * <p>Getter for entitiesMap.</p>
   * @return Map<String, Class<?>>
   **/
  public final Map<String, Class<?>> getEntitiesMap() {
    return this.entitiesMap;
  }

  /**
   * <p>Setter for entitiesMap.</p>
   * @param pEntitiesMap reference
   **/
  public final void setEntitiesMap(final Map<String, Class<?>> pEntitiesMap) {
    this.entitiesMap = pEntitiesMap;
  }

  /**
   * <p>Getter for convertersFieldsFatory.</p>
   * @return IFactoryAppBeansByName<IConverterToFromString<?>>
   **/
  public final IFactoryAppBeansByName<IConverterToFromString<?>>
    getConvertersFieldsFatory() {
    return this.convertersFieldsFatory;
  }

  /**
   * <p>Setter for convertersFieldsFatory.</p>
   * @param pConvertersFieldsFatory reference
   **/
  public final void setConvertersFieldsFatory(
    final IFactoryAppBeansByName<IConverterToFromString<?>>
      pConvertersFieldsFatory) {
    this.convertersFieldsFatory = pConvertersFieldsFatory;
  }

  /**
   * <p>Getter for fieldConverterNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getFieldConverterNamesHolder() {
    return this.fieldConverterNamesHolder;
  }

  /**
   * <p>Setter for fieldConverterNamesHolder.</p>
   * @param pFieldConverterNamesHolder reference
   **/
  public final void setFieldConverterNamesHolder(
    final IHolderForClassByName<String> pFieldConverterNamesHolder) {
    this.fieldConverterNamesHolder = pFieldConverterNamesHolder;
  }

  /**
   * <p>Getter for fieldsRapiHolder.</p>
   * @return IHolderForClassByName<Field>
   **/
  public final IHolderForClassByName<Field> getFieldsRapiHolder() {
    return this.fieldsRapiHolder;
  }

  /**
   * <p>Setter for fieldsRapiHolder.</p>
   * @param pFieldsRapiHolder reference
   **/
  public final void setFieldsRapiHolder(
    final IHolderForClassByName<Field> pFieldsRapiHolder) {
    this.fieldsRapiHolder = pFieldsRapiHolder;
  }
}
