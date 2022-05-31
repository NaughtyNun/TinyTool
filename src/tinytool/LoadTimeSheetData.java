/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    March 2022
   @name    tinytool.LoadTimeSheetData

   @notes   class for loading the XML data into a record
   @changes
*/

package tinytool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tinytool.models.TmsRecord;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class LoadTimeSheetData {
  private final Utilities utilities = new Utilities();
  private TmsRecord record = new TmsRecord();
  private File xmlFile;

  public boolean loadData(File xmlFile, TmsRecord tmsRecord) {
    this.record = tmsRecord;
    this.xmlFile = xmlFile;
    return populateRecord();
  }

  private boolean populateRecord() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Element element;
    NodeList list;
    Node node;

    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document dom = builder.parse(xmlFile);

      // populate header information
      list = dom.getElementsByTagName("header");
      node = list.item(0);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        element = (Element) node;
        record.setCustomerName(element.getElementsByTagName("customerName").item(0).getTextContent());
        record.setCustomerNr(element.getElementsByTagName("customerNr").item(0).getTextContent());
        record.setPersonnelName(element.getElementsByTagName("personnelName").item(0).getTextContent());
        record.setPersonnelNr(element.getElementsByTagName("personnelNr").item(0).getTextContent());
        record.setWeekEnding(element.getElementsByTagName("weekEnding").item(0).getTextContent());
      }

      // populate non-billable items
      list = dom.getElementsByTagName("nonBillable");
      for (int i=0; i<list.getLength(); i++) {
        node = list.item(i);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
          element = (Element) node;
          record.setNonCostType(i, element.getElementsByTagName("costType").item(0).getTextContent());
          record.setNonCostNr(i, element.getElementsByTagName("costNr").item(0).getTextContent());
          record.setNonCostItem(i, element.getElementsByTagName("costItem").item(0).getTextContent());
          record.setNonTaskLevel(i, element.getElementsByTagName("taskLevel").item(0).getTextContent());
          record.setNonTaskType(i, element.getElementsByTagName("taskType").item(0).getTextContent());
          record.setNonTaskComponent(i, element.getElementsByTagName("taskComponent").item(0).getTextContent());
          record.setNonComment(i, element.getElementsByTagName("comment").item(0).getTextContent());
          record.setNonSunday(i, Double.parseDouble(element.getElementsByTagName("sunday").item(0).getTextContent()));
          record.setNonMonday(i, Double.parseDouble(element.getElementsByTagName("monday").item(0).getTextContent()));
          record.setNonTuesday(i, Double.parseDouble(element.getElementsByTagName("tuesday").item(0).getTextContent()));
          record.setNonWednesday(i, Double.parseDouble(element.getElementsByTagName("wednesday").item(0).getTextContent()));
          record.setNonThursday(i, Double.parseDouble(element.getElementsByTagName("thursday").item(0).getTextContent()));
          record.setNonFriday(i, Double.parseDouble(element.getElementsByTagName("friday").item(0).getTextContent()));
          record.setNonSaturday(i, Double.parseDouble(element.getElementsByTagName("saturday").item(0).getTextContent()));
          record.setNonLineTotal(i, Double.parseDouble(element.getElementsByTagName("total").item(0).getTextContent()));
        }
      }

      // populate billable items
      list = dom.getElementsByTagName("billable");
      for (int i=0; i<list.getLength(); i++) {
        node = list.item(i);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
          element = (Element) node;
          record.setBillCostType(i, element.getElementsByTagName("costType").item(0).getTextContent());
          record.setBillCostNr(i, element.getElementsByTagName("costNr").item(0).getTextContent());
          record.setBillCostItem(i, element.getElementsByTagName("costItem").item(0).getTextContent());
          record.setBillTaskLevel(i, element.getElementsByTagName("taskLevel").item(0).getTextContent());
          record.setBillTaskType(i, element.getElementsByTagName("taskType").item(0).getTextContent());
          record.setBillTaskComponent(i, element.getElementsByTagName("taskComponent").item(0).getTextContent());
          record.setBillComment(i, element.getElementsByTagName("comment").item(0).getTextContent());
          record.setBillSunday(i, Double.parseDouble(element.getElementsByTagName("sunday").item(0).getTextContent()));
          record.setBillMonday(i, Double.parseDouble(element.getElementsByTagName("monday").item(0).getTextContent()));
          record.setBillTuesday(i, Double.parseDouble(element.getElementsByTagName("tuesday").item(0).getTextContent()));
          record.setBillWednesday(i, Double.parseDouble(element.getElementsByTagName("wednesday").item(0).getTextContent()));
          record.setBillThursday(i, Double.parseDouble(element.getElementsByTagName("thursday").item(0).getTextContent()));
          record.setBillFriday(i, Double.parseDouble(element.getElementsByTagName("friday").item(0).getTextContent()));
          record.setBillSaturday(i, Double.parseDouble(element.getElementsByTagName("saturday").item(0).getTextContent()));
          record.setBillLineTotal(i, Double.parseDouble(element.getElementsByTagName("total").item(0).getTextContent()));
        }
      }

      // populate travel items
      list = dom.getElementsByTagName("travel");
      for (int i=0; i<list.getLength(); i++) {
        node = list.item(i);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
          element = (Element) node;
          record.setTravelCostType(i, element.getElementsByTagName("costType").item(0).getTextContent());
          record.setTravelCostNr(i, element.getElementsByTagName("costNr").item(0).getTextContent());
          record.setTravelCostItem(i, element.getElementsByTagName("costItem").item(0).getTextContent());
          record.setTravelTaskLevel(i, element.getElementsByTagName("taskLevel").item(0).getTextContent());
          record.setTravelTaskType(i, element.getElementsByTagName("taskType").item(0).getTextContent());
          record.setTravelTaskComponent(i, element.getElementsByTagName("taskComponent").item(0).getTextContent());
          record.setTravelComment(i, element.getElementsByTagName("comment").item(0).getTextContent());
          record.setTravelSunday(i, Double.parseDouble(element.getElementsByTagName("sunday").item(0).getTextContent()));
          record.setTravelMonday(i, Double.parseDouble(element.getElementsByTagName("monday").item(0).getTextContent()));
          record.setTravelTuesday(i, Double.parseDouble(element.getElementsByTagName("tuesday").item(0).getTextContent()));
          record.setTravelWednesday(i, Double.parseDouble(element.getElementsByTagName("wednesday").item(0).getTextContent()));
          record.setTravelThursday(i, Double.parseDouble(element.getElementsByTagName("thursday").item(0).getTextContent()));
          record.setTravelFriday(i, Double.parseDouble(element.getElementsByTagName("friday").item(0).getTextContent()));
          record.setTravelSaturday(i, Double.parseDouble(element.getElementsByTagName("saturday").item(0).getTextContent()));
          record.setTravelLineTotal(i, Double.parseDouble(element.getElementsByTagName("total").item(0).getTextContent()));
        }
      }

      // Populate Footer Information
      list = dom.getElementsByTagName("footer");
      node = list.item(0);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        element = (Element) node;
        record.setSignatureDate(element.getElementsByTagName("signatureDate").item(0).getTextContent());
        record.setEmployee(element.getElementsByTagName("employee").item(0).getTextContent());
        record.setManager(element.getElementsByTagName("manager").item(0).getTextContent());
        record.setOneup(element.getElementsByTagName("oneup").item(0).getTextContent());
        record.setCustomer(element.getElementsByTagName("customer").item(0).getTextContent());
      }

      // Populate Audit Information
      list = dom.getElementsByTagName("audit");
      node = list.item(0);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        element = (Element) node;
        record.setPrintDate(element.getElementsByTagName("printDate").item(0).getTextContent());
        record.setPrintTime(element.getElementsByTagName("printTime").item(0).getTextContent());
        record.setPrintBy(element.getElementsByTagName("printBy").item(0).getTextContent());
        record.setPrintVersion(element.getElementsByTagName("printVersion").item(0).getTextContent());
      }
      return true;
    } catch (ParserConfigurationException e) {
      utilities.showFieldError("XML Parser", "TinyTool could not parse the file\n" + e.getMessage());
      return false;
    } catch (IOException e) {
      utilities.showFieldError("XML Record", "TinyTool could not load the file\n" + e.getMessage());
      return false;
    } catch (SAXException e) {
      utilities.showFieldError("SAX Exception", "TinyTool could not parse the file\n" + e.getMessage());
      return false;
    }
  }
}