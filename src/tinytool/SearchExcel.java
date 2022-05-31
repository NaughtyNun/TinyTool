/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    October 2021
   @name    tinytool.SearchExcel

   @notes   class for searching EXCEL files, both XLS and XLSX
   @changes 2022-01-03 - using the Searcher class for searching
*/

package tinytool;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class SearchExcel {
  private File file;
  private String needle;
  private boolean exact;
  private final tinytool.Utilities utilities = new tinytool.Utilities();
  private final tinytool.Searcher searcher = new tinytool.Searcher();

  public int searchExcel(File file, String needle, boolean exact) {
    this.file = file;
    this.needle = needle;
    this.exact = exact;
    return getMatches();
  }

  private int getMatches() {
    int counter;

    if (utilities.getFileExtension(file).equals("xlsx"))
      counter = searchNewFormat(file);
    else
      counter = searchOldFormat(file);

    return counter;
  }

  private int searchNewFormat(File file) {
    String text = null;
    int matches = 0;

    try {
      FileInputStream fis = new FileInputStream(file);
      XSSFWorkbook wb = new XSSFWorkbook(fis);
      XSSFSheet sheet = wb.getSheetAt(0);

      for (Row row : sheet) {
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();

          switch (cell.getCellType()) {
            case STRING:
              text = cell.getStringCellValue();
              break;
            case NUMERIC:
              text = String.valueOf(cell.getNumericCellValue());
              break;
          }

          assert text != null;
          text = text.replaceAll("\\\\","");
          matches = searcher.searcher(needle, text, exact);
        }
      }

      fis.close();
    } catch (IOException e) {
      matches = -1;
    }

    return matches;
  }

  private int searchOldFormat(File file) {
    String text = null;
    int matches = 0;

    try {
      FileInputStream fis = new FileInputStream(String.valueOf(file));
      HSSFWorkbook wb = new HSSFWorkbook(fis);
      HSSFSheet sheet = wb.getSheetAt(0);

      for (Row row:sheet) {
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();

          switch (cell.getCellType()) {
            case STRING:
              text = cell.getStringCellValue();
              break;
            case NUMERIC:
              text = String.valueOf(cell.getNumericCellValue());
              break;
          }

          assert text != null;
          text = text.replaceAll("\\\\","");
          matches = searcher.searcher(needle, text, exact);
        }
      }

      fis.close();
    } catch (IOException e) {
      matches = -1;
    }

    return matches;
  }
}