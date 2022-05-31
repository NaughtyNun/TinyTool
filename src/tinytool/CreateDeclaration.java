/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    February 2022
   @name    tinytool.CreateDeclaration

   @notes   class to produce the security declaration form (PDF)
   @changes 2022-04-22 - changed the openPDF creation to produce the declaration using a spreadsheet.
                         shitload of less coding and maintenance!
            2022-04-22 - remove the temporary file once the declaration is done, unlike some commercial shit
                         that leaves their rubbish on your drive.
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

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class CreateDeclaration {
  private final Utilities utilities = new Utilities();
  private final TinyTool tinyTool = new TinyTool();
  private final File template = new File(tinyTool.HOMEDIR + "\\security.xlsx");
  private final LocalDate today = LocalDate.now();
  private PersonnelRecord record = new PersonnelRecord();
  private File tempFile = null;
  private File pdfFile = null;
  private List dateList;

  public boolean createDocument(PersonnelRecord record, List dateList, File pdfFile) {
    this.record = record;
    this.dateList = dateList;
    this.pdfFile = pdfFile;

    if (createTempFile())
      return createPdf();
    else
      return false;
  }

  private boolean createTempFile() {
    tempFile = new File(tinyTool.WORKDIR + "dec_temp_" + today + "_.xlsx");
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
      worksheet.getPictures().add(32, 3, signature);
      worksheet.saveToPdf(String.valueOf(pdfFile));
      FileUtils.deleteQuietly(tempFile);
      return true;
    } else
      return false;
  }

  private boolean createSpreadSheet() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    StringBuilder append = new StringBuilder();
//    String reference;
    String dateText;
//    int startAt;

    try {
      FileInputStream fis = new FileInputStream(template);
      XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook(OPCPackage.open(fis));

      writeData(workbook, "C3", record.getName());
      writeData(workbook, "G3", record.getSurname());
      writeData(workbook, "C4", record.getIdNumber());

      dateText = "I have worked remotely for the following dates ";
      append.append(dateText);
      for (Object o : dateList) {
        String tempString = (String) o;
        String dateString = tempString.replaceAll("-", "/");
//        int index = tempString.indexOf(":");
//        String dateString = tempString.substring(index + 1);
        append.append(dateString).append(", ");
      }
      append.append("for the PERSAL system.");
      writeData(workbook, "B6", String.valueOf(append));

      /*
      startAt = 7;
      for (Object o : dateList) {
        String tempString = (String) o;
        int index = tempString.indexOf(":");
        String dateString = tempString.substring(index + 1);
        reference = "B" + startAt;
        writeData(workbook, reference, dateString);
        startAt++;
      }*/

      LocalDate nextMonday = today;
      nextMonday = nextMonday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

      writeData(workbook, "G32", dtf.format(today));
      writeData(workbook, "C37", record.getOneUpName());
      writeData(workbook, "G37", record.getOneUpSurname());
      writeData(workbook, "G39", dtf.format(nextMonday));

      fis.close();
      FileOutputStream fos = new FileOutputStream(tempFile);
      workbook.write(fos);
      workbook.close();
      fos.close();
      return true;
    } catch (FileNotFoundException e) {
      utilities.showFieldError("XLSX Template", "The template spreadsheet could not be found.\n" +
        template + "\nError: " + e.getMessage());
      return false;
    } catch (IOException e) {
      utilities.showFieldError("XLSX Template", "The template spreadsheet could not be loaded.\n" +
        template + "\nError: " + e.getMessage());
      return false;
    } catch (InvalidFormatException e) {
      utilities.showFieldError("XLSX Workbook", "TinyTool encountered an Invalid Format Error.\n" +
        template + "\nError: " + e.getMessage());
      return false;
    }
  }

  private void writeData(XSSFWorkbook workbook, String reference, String writeValue) {
    XSSFSheet sheet = workbook.getSheetAt(0);
    CellReference cellReference = new CellReference(reference);
    Row row = sheet.getRow(cellReference.getRow());
    Cell cell = row.getCell(cellReference.getCol());

    if (writeValue != null) {
      cell.setCellValue(writeValue);
    }
  }
}