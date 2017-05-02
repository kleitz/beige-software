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
import org.beigesoft.service.UtilXml;
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
  
  UtilXml utilXml;
  
  SrvEntityFieldFillerStd srvEntityFieldFillerStd;
  
  String strXmlDepartment = "class=\"org.beigesoft.test.persistable.Department\" itsId=\"1\" itsName=\"ICT\"/&gt;";

  String strXmlPersHead = "class=\"org.beigesoft.test.persistable.PersistableHead\" itsDate=\"1475156484845\" itsStatus=\"0\" isClosed=\"true\" itsTotal=\"523.66\" itsInteger=\"NULL\"/&gt;";

  SrvEntityWriterXml srvEntityWriterXml;

  SrvFieldWriterXmlStd srvFieldWriterXmlStd;
  
  SrvFieldHasIdWriterXml srvFieldHasIdWriterXml;
  
  SrvEntityFieldPersistableBaseRepl srvEntityFieldPersistableBaseRepl;

  public SrvEntityReaderXmlTest() throws Exception {
    this.logger = new LoggerSimple();
    this.logger.info(SrvEntityReaderXmlTest.class, new Date().toString());
    this.srvEntityReaderXml = new SrvEntityReaderXml();
    this.utilXml = new UtilXml();
    this.srvEntityReaderXml.setUtilXml(utilXml);
    this.srvEntityFieldFillerStd = new SrvEntityFieldFillerStd();
    this.utlReflection = new UtlReflection();
    this.srvEntityFieldFillerStd.setUtlReflection(this.utlReflection);
    this.srvEntityFieldFillerStd.setUtilXml(this.utilXml);
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
    this.srvFieldWriterXmlStd.setUtilXml(this.utilXml);
    this.srvFieldHasIdWriterXml = new SrvFieldHasIdWriterXml();
    this.srvFieldHasIdWriterXml.setUtilXml(this.utilXml);
    this.srvEntityWriterXml.setMngSettings(this.mngSettings);
    this.srvEntityWriterXml.setUtlReflection(this.utlReflection);
    Map<String, ISrvFieldWriter> fieldsWritersMap = new HashMap<String, ISrvFieldWriter>();
    fieldsWritersMap.put("SrvFieldWriterXmlStd", this.srvFieldWriterXmlStd);
    fieldsWritersMap.put("SrvFieldHasIdWriterXml", this.srvFieldHasIdWriterXml);
    this.srvEntityWriterXml.setFieldsWritersMap(fieldsWritersMap);
  }

  @Test
  public void testRead1() throws Exception {
    System.out.println(this.utilXml.unescapeXml(this.strXmlDepartment));
    StringReader reader = new StringReader(this.strXmlDepartment);
    Map<String, String> attributesMap = this.srvEntityReaderXml.readAttributes(null, reader);
    assertEquals("org.beigesoft.test.persistable.Department", attributesMap.get("class")); 
    assertEquals("1", attributesMap.get("itsId")); 
    assertEquals("ICT", attributesMap.get("itsName")); 
  }

  @Test
  public void testRead2() throws Exception {
    StringReader reader = new StringReader(this.strXmlDepartment);
    Department department = (Department) this.srvEntityReaderXml.read(null, reader);
    assertEquals(new Long(1L), department.getItsId()); 
    assertEquals("ICT", department.getItsName()); 
  }

  @Test
  public void testRead3() throws Exception {
    System.out.println(this.utilXml.unescapeXml(this.strXmlPersHead));
    StringReader reader = new StringReader(this.strXmlPersHead);
    PersistableHead persHead = (PersistableHead) this.srvEntityReaderXml.read(null, reader);
    assertEquals(new Date(1475156484845L), persHead.getItsDate()); 
    assertNull(persHead.getItsInteger()); 
    assertEquals(EStatus.STATUS_A, persHead.getItsStatus()); 
    assertEquals(new Boolean(true), persHead.getIsClosed()); 
    assertEquals(new BigDecimal("523.66"), persHead.getItsTotal());
    Double randomDbl1 = Math.random() * 1000000000;
    int databaseIdGenerated1 = randomDbl1.intValue();
    System.out.println("randomDbl1/int1: " + randomDbl1 + "/"
      + databaseIdGenerated1);
    Double randomDbl2 = Math.random() * 1000000000;
    int databaseIdGenerated2 = randomDbl2.intValue();
    System.out.println("randomDbl2/int2: " + randomDbl2 + "/"
      + databaseIdGenerated2);
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
    persHeadOrigin.setTmpDescription(" Bob's pizza List<String> lst = \"alfa\" & b=a \n nstr");
    File persHeadFile = new File("persistableHead.tst.xml");
    OutputStreamWriter writer = new OutputStreamWriter(
      new FileOutputStream(persHeadFile),
        Charset.forName("UTF-8").newEncoder());
    try {
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      writer.write("<data sourceId=\"127\">\n");
      this.srvEntityWriterXml.write(null, departmentOr, writer);
      this.srvEntityWriterXml.write(null, persHeadOrigin, writer);
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
      this.utilXml.readUntilStart(reader, "data");
      Map<String, String> attributesMap = this.srvEntityReaderXml.readAttributes(null, reader);
      assertEquals("127", attributesMap.get("sourceId")); 
      this.utilXml.readUntilStart(reader, "entity");
      department = (Department) this.srvEntityReaderXml.read(null, reader);
      this.utilXml.readUntilStart(reader, "entity");
      persHead = (PersistableHead) this.srvEntityReaderXml.read(null, reader);
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
    System.out.println(persHead.getTmpDescription());
    assertEquals(persHeadOrigin.getTmpDescription(), persHead.getTmpDescription()); 
    assertEquals(departmentOr.getItsId(), department.getItsId()); 
    assertEquals(departmentOr.getItsName(), department.getItsName()); 
  }
}
