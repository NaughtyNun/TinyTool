/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    March 2022
   @name    tinytool.CreateTimeSheet

   @notes   class that will use existing spreadsheet and generate the TimeSheet PDF
   @changes
*/

package tinytool;

import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import tinytool.models.PersonnelRecord;
import tinytool.models.TmsRecord;

import java.io.*;
import java.time.LocalDate;

public class CreateTimeSheet {
  private final Utilities utilities = new Utilities();
  private final TinyTool tinyTool = new TinyTool();
  private final File template = new File(tinyTool.HOMEDIR + "\\timesheet.xlsx");
  private PersonnelRecord record = new PersonnelRecord();
  private TmsRecord timeRecord = new TmsRecord();
  private File tempFile = null;
  private File pdfFile = null;

  public boolean createDocument(PersonnelRecord record, TmsRecord timeRecord, File pdfFile) {
    this.record = record;
    this.timeRecord = timeRecord;
    this.pdfFile = pdfFile;

    if (createTempFile())
      return createPdf();
    else
      return false;
  }

  private boolean createTempFile() {
    LocalDate date = LocalDate.now();
    tempFile = new File(tinyTool.WORKDIR + "time_temp_" + date + "_.xlsx");

    try {
      FileUtils.copyFile(template, tempFile);
      return true;
    } catch (IOException e) {
      utilities.showFieldError("Temporary File", "TinyTool could not create the temporary XLSX file.\n" +
        tempFile);
      return false;
    }
  }

  private boolean createPdf() {
    if (createSpreadSheet()) {
      String signature = record.getUserSignature().getAbsolutePath();
      Workbook workbook = new Workbook();
      workbook.loadFromFile(String.valueOf(tempFile));
      Worksheet worksheet = workbook.getWorksheets().get(0);
      worksheet.getPictures().add(45, 10, signature);
      worksheet.saveToPdf(String.valueOf(pdfFile));
      FileUtils.deleteQuietly(tempFile);
      return true;
    } else
      return false;
  }

  private boolean createSpreadSheet() {
    double sun, mon, tue, wed, thu, fri, sat, total;
    String reference;
    int startAt;

    try {
      FileInputStream fis = new FileInputStream(template);
      XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook(OPCPackage.open(fis));

      // Write the header information
      writeSpreadSheet(workbook,"Q5", timeRecord.getCustomerName(), null);
      writeSpreadSheet(workbook,"AA5", timeRecord.getCustomerNr(), null);
      writeSpreadSheet(workbook,"B8", timeRecord.getWeekEnding(), null);
      writeSpreadSheet(workbook,"Q8", timeRecord.getPersonnelName(), null);
      writeSpreadSheet(workbook,"AA8", timeRecord.getPersonnelNr(), null);

      // Write non-billable information
      startAt = 12;
      sun = mon = tue = wed = thu = fri = sat = 0;

      for (int i = 0; i < timeRecord.nonCostType.size(); i++) {
        reference = "B" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getNonCostType(i), null);
        reference = "E" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getNonCostNr(i), null);
        reference = "H" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getNonCostItem(i), null);
        reference = "K" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getNonTaskLevel(i), null);
        reference = "N" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getNonTaskType(i),null);
        reference = "Q" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getNonTaskComponent(i),null);
        reference = "T" + startAt;
        writeSpreadSheet(workbook,reference, timeRecord.getNonComment(i),null);
        reference = "X" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonSunday(i));
        sun = sun + timeRecord.getNonSunday(i);
        reference = "Y" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonMonday(i));
        mon = mon + timeRecord.getNonMonday(i);
        reference = "Z" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonTuesday(i));
        tue = tue + timeRecord.getNonTuesday(i);
        reference = "AA" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonWednesday(i));
        wed = wed + timeRecord.getNonWednesday(i);
        reference = "AB" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonThursday(i));
        thu = thu + timeRecord.getNonThursday(i);
        reference = "AC" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonFriday(i));
        fri = fri + timeRecord.getNonFriday(i);
        reference = "AD" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonSaturday(i));
        sat = sat + timeRecord.getNonSaturday(i);
        reference = "AE" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getNonLineTotal(i));
        startAt++;
      }
      // Write the non-billable totals
      total = sun + mon + tue + wed + thu + fri + sat;
      writeSpreadSheet(workbook,"X20",null,sun);
      writeSpreadSheet(workbook,"Y20",null,mon);
      writeSpreadSheet(workbook,"Z20",null,tue);
      writeSpreadSheet(workbook,"AA20",null,wed);
      writeSpreadSheet(workbook,"AB20",null,thu);
      writeSpreadSheet(workbook,"AC20",null,fri);
      writeSpreadSheet(workbook,"AD20",null,sat);
      writeSpreadSheet(workbook,"AE20",null,total);


      // Write billable information
      startAt = 24;
      sun = mon = tue = wed = thu = fri = sat = 0;

      for (int i = 0; i < timeRecord.billCostType.size(); i++) {
        reference = "B" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getBillCostType(i), null);
        reference = "E" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getBillCostNr(i), null);
        reference = "H" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getBillCostItem(i), null);
        reference = "K" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getBillTaskLevel(i), null);
        reference = "N" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getBillTaskType(i),null);
        reference = "Q" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getBillTaskComponent(i),null);
        reference = "T" + startAt;
        writeSpreadSheet(workbook,reference, timeRecord.getBillComment(i),null);
        reference = "X" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillSunday(i));
        sun = sun + timeRecord.getBillSunday(i);
        reference = "Y" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillMonday(i));
        mon = mon + timeRecord.getBillMonday(i);
        reference = "Z" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillTuesday(i));
        tue = tue + timeRecord.getBillTuesday(i);
        reference = "AA" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillWednesday(i));
        wed = wed + timeRecord.getBillWednesday(i);
        reference = "AB" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillThursday(i));
        thu = thu + timeRecord.getBillThursday(i);
        reference = "AC" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillFriday(i));
        fri = fri + timeRecord.getBillFriday(i);
        reference = "AD" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillSaturday(i));
        sat = sat + timeRecord.getBillSaturday(i);
        reference = "AE" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getBillLineTotal(i));
        startAt++;
      }
      // Write the non-billable totals
      total = sun + mon + tue + wed + thu + fri + sat;
      writeSpreadSheet(workbook,"X29",null,sun);
      writeSpreadSheet(workbook,"Y29",null,mon);
      writeSpreadSheet(workbook,"Z29",null,tue);
      writeSpreadSheet(workbook,"AA29",null,wed);
      writeSpreadSheet(workbook,"AB29",null,thu);
      writeSpreadSheet(workbook,"AC29",null,fri);
      writeSpreadSheet(workbook,"AD29",null,sat);
      writeSpreadSheet(workbook,"AE29",null,total);

      // Write travel information
      startAt = 33;
      sun = mon = tue = wed = thu = fri = sat = 0;

      for (int i = 0; i < timeRecord.travelCostType.size(); i++) {
        reference = "B" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getTravelCostType(i), null);
        reference = "E" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getTravelCostNr(i), null);
        reference = "H" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getTravelCostItem(i), null);
        reference = "K" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getTravelTaskLevel(i), null);
        reference = "N" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getTravelTaskType(i),null);
        reference = "Q" + startAt;
        writeSpreadSheet(workbook, reference, timeRecord.getTravelTaskComponent(i),null);
        reference = "T" + startAt;
        writeSpreadSheet(workbook,reference, timeRecord.getTravelComment(i),null);
        reference = "X" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelSunday(i));
        sun = sun + timeRecord.getTravelSunday(i);
        reference = "Y" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelMonday(i));
        mon = mon + timeRecord.getTravelMonday(i);
        reference = "Z" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelTuesday(i));
        tue = tue + timeRecord.getTravelTuesday(i);
        reference = "AA" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelWednesday(i));
        wed = wed + timeRecord.getTravelWednesday(i);
        reference = "AB" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelThursday(i));
        thu = thu + timeRecord.getTravelThursday(i);
        reference = "AC" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelFriday(i));
        fri = fri + timeRecord.getTravelFriday(i);
        reference = "AD" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelSaturday(i));
        sat = sat + timeRecord.getTravelSaturday(i);
        reference = "AE" + startAt;
        writeSpreadSheet(workbook,reference,null, timeRecord.getTravelLineTotal(i));
        startAt++;
      }
      // Write the travel totals
      total = sun + mon + tue + wed + thu + fri + sat;
      writeSpreadSheet(workbook,"X39",null,sun);
      writeSpreadSheet(workbook,"Y39",null,mon);
      writeSpreadSheet(workbook,"Z39",null,tue);
      writeSpreadSheet(workbook,"AA39",null,wed);
      writeSpreadSheet(workbook,"AB39",null,thu);
      writeSpreadSheet(workbook,"AC39",null,fri);
      writeSpreadSheet(workbook,"AD39",null,sat);
      writeSpreadSheet(workbook,"AE39",null,total);

      // Write the audit & footer information
      writeSpreadSheet(workbook,"I42", timeRecord.getSignatureDate(), null);
      writeSpreadSheet(workbook,"I43", timeRecord.getEmployee(), null);
      writeSpreadSheet(workbook,"O42", timeRecord.getSignatureDate(), null);
      writeSpreadSheet(workbook,"O43", timeRecord.getManager(), null);
      writeSpreadSheet(workbook,"U42", timeRecord.getSignatureDate(), null);
      writeSpreadSheet(workbook,"U43", timeRecord.getOneup(), null);
      writeSpreadSheet(workbook,"AA42", timeRecord.getSignatureDate(), null);
      writeSpreadSheet(workbook,"AA43", timeRecord.getCustomer(), null);
      writeSpreadSheet(workbook,"D47", timeRecord.getPrintDate(), null);
      writeSpreadSheet(workbook,"F47", timeRecord.getPrintTime(), null);
      writeSpreadSheet(workbook,"K47", timeRecord.getPrintBy(), null);
      writeSpreadSheet(workbook,"Y47", timeRecord.getPrintVersion(), null);

      fis.close();
      FileOutputStream fos = new FileOutputStream(tempFile);
      workbook.write(fos);
      workbook.close();
      fos.close();
    } catch (FileNotFoundException e) {
      utilities.showFieldError("Resource File", "TinyTool could not load the resource file TMS\n" +
        e.getMessage());
      return false;
    } catch (IOException e) {
      utilities.showFieldError("Resource File", "TinyTool could not open the TMS workbook\n" +
        e.getMessage());
      return false;
    } catch (InvalidFormatException e) {
      utilities.showFieldError("Resource File", "TMS file has an invalid format\n" +
        e.getMessage());
      return false;
    }

    return true;
  }

  private void writeSpreadSheet(XSSFWorkbook workbook, String reference, String string, Double value) {
    XSSFSheet sheet = workbook.getSheetAt(0);
    CellReference cellReference = new CellReference(reference);
    Row row =  sheet.getRow(cellReference.getRow());
    Cell cell = row.getCell(cellReference.getCol());

    if (string != null)
      cell.setCellValue(string);

    if (value != null && value > 0)
      cell.setCellValue(value);
  }
}