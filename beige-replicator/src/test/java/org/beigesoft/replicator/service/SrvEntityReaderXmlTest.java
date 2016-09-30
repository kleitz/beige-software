package org.beigesoft.replicator.service;

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
import java.math.BigDecimal;
import java.io.StringReader;
import java.io.File;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import org.beigesoft.log.LoggerSimple;
import org.beigesoft.xml.service.SrvXmlEscape;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.settings.MngSettings;

import org.beigesoft.test.model.EStatus;
import org.beigesoft.test.persistable.Department;
import org.beigesoft.test.persistable.PersistableHead;

/**
 * <p>SrvEntityReaderXml Test.</p>
 *
 * @author Yury Demidenko
 */
public class SrvEntityReaderXmlTest {

  LoggerSimple logger;

  UtlReflection utlReflection;

  MngSettings mngSettings;

  SrvEntityReaderXml srvEntityReaderXml;
  
  SrvXmlEscape srvXmlEscape;
  
  SrvEntityFieldFillerStd srvEntityFieldFillerStd;
  
  String strXmlDepartment = "class=\"org.beigesoft.test.persistable.Department\" itsId=\"1\" itsName=\"ICT\"/&gt;";

  String strXmlPersHead = "class=\"org.beigesoft.test.persistable.PersistableHead\" itsDate=\"1475156484845\" itsStatus=\"0\" isClosed=\"true\" itsTotal=\"523.66\" itsInteger=\"NULL\"/&gt;";

  SrvEntityWriterXml srvEntityWriterXml;

  SrvFieldWriterXmlStd srvFieldWriterXmlStd;
  
  SrvFieldHasIdWriterXml srvFieldHasIdWriterXml;
  
  SrvEntityFieldPersistableBaseRepl srvEntityFieldPersistableBaseRepl;

  public SrvEntityReaderXmlTest() throws Exception {
    this.logger = new LoggerSimple();
    this.srvEntityReaderXml = new SrvEntityReaderXml();
    this.srvXmlEscape = new SrvXmlEscape();
    this.srvEntityReaderXml.setSrvXmpEscape(srvXmlEscape);
    this.srvEntityFieldFillerStd = new SrvEntityFieldFillerStd();
    this.utlReflection = new UtlReflection();
    this.srvEntityFieldFillerStd.setUtlReflection(this.utlReflection);
    this.srvEntityFieldPersistableBaseRepl = new SrvEntityFieldPersistableBaseRepl();
    this.srvEntityFieldPersistableBaseRepl.setUtlReflection(this.utlReflection);
    Map<String, ISrvEntityFieldFiller> fieldsFillersMap =
      new HashMap<String, ISrvEntityFieldFiller> ();
    fieldsFillersMap.put("SrvEntityFieldFillerStd", this.srvEntityFieldFillerStd);
    fieldsFillersMap.put("SrvEntityFieldPersistableBaseRepl", this.srvEntityFieldPersistableBaseRepl);
    this.srvEntityReaderXml.setFieldsFillersMap(fieldsFillersMap);
    this.mngSettings = new MngSettings();
    this.mngSettings.setLogger(this.logger);
    this.mngSettings.loadConfiguration("beige-replicator","base.xml");
    this.srvEntityReaderXml.setMngSettings(this.mngSettings);
    this.srvEntityWriterXml = new SrvEntityWriterXml();
    this.srvFieldWriterXmlStd = new SrvFieldWriterXmlStd();
    this.srvFieldWriterXmlStd.setSrvXmpEscape(this.srvXmlEscape);
    this.srvFieldHasIdWriterXml = new SrvFieldHasIdWriterXml();
    this.srvFieldHasIdWriterXml.setSrvXmpEscape(this.srvXmlEscape);
    this.srvEntityWriterXml.setMngSettings(this.mngSettings);
    this.srvEntityWriterXml.setUtlReflection(this.utlReflection);
    Map<String, ISrvFieldWriter> fieldsWritersMap = new HashMap<String, ISrvFieldWriter>();
    fieldsWritersMap.put("SrvFieldWriterXmlStd", this.srvFieldWriterXmlStd);
    fieldsWritersMap.put("SrvFieldHasIdWriterXml", this.srvFieldHasIdWriterXml);
    this.srvEntityWriterXml.setFieldsWritersMap(fieldsWritersMap);
  }

  @Test
  public void testRead1() throws Exception {
    System.out.println(this.srvXmlEscape.unescapeXml(this.strXmlDepartment));
    StringReader reader = new StringReader(this.strXmlDepartment);
    Map<String, String> attributesMap = this.srvEntityReaderXml.readAttributes(reader, null);
    assertEquals("org.beigesoft.test.persistable.Department", attributesMap.get("class")); 
    assertEquals("1", attributesMap.get("itsId")); 
    assertEquals("ICT", attributesMap.get("itsName")); 
  }

  @Test
  public void testRead2() throws Exception {
    StringReader reader = new StringReader(this.strXmlDepartment);
    Department department = (Department) this.srvEntityReaderXml.read(reader, null);
    assertEquals(new Long(1L), department.getItsId()); 
    assertEquals("ICT", department.getItsName()); 
  }

  @Test
  public void testRead3() throws Exception {
    System.out.println(this.srvXmlEscape.unescapeXml(this.strXmlPersHead));
    StringReader reader = new StringReader(this.strXmlPersHead);
    PersistableHead persHead = (PersistableHead) this.srvEntityReaderXml.read(reader, null);
    assertEquals(new Date(1475156484845L), persHead.getItsDate()); 
    assertNull(persHead.getItsInteger()); 
    assertEquals(EStatus.STATUS_A, persHead.getItsStatus()); 
    assertEquals(new Boolean(true), persHead.getIsClosed()); 
    assertEquals(new BigDecimal("523.66"), persHead.getItsTotal()); 
  }

  @Test
  public void testWriteRead1() throws Exception {
    Department departmentOr =  new Department();
    departmentOr.setItsId(1L);
    departmentOr.setItsName("ICT");
    PersistableHead persHeadOrigin = new PersistableHead();
    persHeadOrigin.setIsClosed(true);
    persHeadOrigin.setItsDate(new Date(1475156484845L));
    persHeadOrigin.setItsStatus(EStatus.STATUS_A);
    persHeadOrigin.setItsTotal(new BigDecimal("523.66"));
    persHeadOrigin.setItsDepartment(departmentOr);
    File persHeadFile = new File("persistableHead.tst.xml");
    OutputStreamWriter writer = new OutputStreamWriter(
      new FileOutputStream(persHeadFile),
        Charset.forName("UTF-8").newEncoder());
    try {
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      writer.write("<data sourceId=\"127\">\n");
      this.srvEntityWriterXml.write(departmentOr, writer, null);
      this.srvEntityWriterXml.write(persHeadOrigin, writer, null);
      writer.write("</data>\n");
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    InputStreamReader reader = new InputStreamReader(
      new FileInputStream(persHeadFile),
        Charset.forName("UTF-8").newDecoder());
    PersistableHead persHead = null;
    Department department = null;
    try {
      readUntilStart(reader, "data");
      Map<String, String> attributesMap = this.srvEntityReaderXml.readAttributes(reader, null);
      assertEquals("127", attributesMap.get("sourceId")); 
      readUntilStart(reader, "entity");
      department = (Department) this.srvEntityReaderXml.read(reader, null);
      readUntilStart(reader, "entity");
      persHead = (PersistableHead) this.srvEntityReaderXml.read(reader, null);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    assertEquals(persHeadOrigin.getItsDate(), persHead.getItsDate()); 
    assertNull(persHead.getItsInteger()); 
    assertEquals(persHeadOrigin.getItsStatus(), persHead.getItsStatus()); 
    assertEquals(persHeadOrigin.getIsClosed(), persHead.getIsClosed()); 
    assertEquals(persHeadOrigin.getItsTotal(), persHead.getItsTotal()); 
    assertEquals(persHeadOrigin.getItsDepartment().getItsId(), persHead.getItsDepartment().getItsId()); 
    assertEquals(departmentOr.getItsId(), department.getItsId()); 
    assertEquals(departmentOr.getItsName(), department.getItsName()); 
  }


  /**
   * <p>
   * Read stream until start given element.
   * </p>
   * @param pReader reader.
   * @param pElement element
   * @throws Exception - an exception
   **/
  public final void readUntilStart(final Reader pReader,
    final String pElement) throws Exception {
    Map<String, String> attributesMap = new HashMap<String, String>();
    StringBuffer sb = new StringBuffer();
    int chi;
    boolean isLtOccured = false;
    while ((chi = pReader.read()) != -1) {
      char ch = (char) chi;
      if (isLtOccured) {
        if (ch ==  '>' || ch == '\n' || ch == '\\' || ch == '"' || ch == '\r'
          || ch == '\t') {
          isLtOccured = false;
          sb.delete(0, sb.length());
          continue;
        }
        sb.append(ch);
        String readedStr = sb.toString();
        if (readedStr.length() > pElement.length()) {
          isLtOccured = false;
          sb.delete(0, sb.length());
          continue;
        }
        if (pElement.equals(readedStr)) {
          break;
        }
      } else if (ch == '<') {
        isLtOccured = true;
      }
    }
  }
}
