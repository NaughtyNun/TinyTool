/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    March 2020
   @name    tinytool.Utilities

   @notes   general stuff that is used throughout the application
   @changes
 */

package tinytool;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;

public class Utilities {
  private final TinyTool tinyTool = new TinyTool();
  private int pages;

  public long countFiles(String directory) {
    int count;
    count = Objects.requireNonNull(new File(directory).list()).length;
    return count;
  }

  public long folderSize(File folder) {
    File[] files = folder.listFiles();

    if (files == null)
      return 0L;
    else {
      long size = 0;

      for (File file:files) {
        if (file.isFile())
          size += file.length();
      }

      return size;
    }
  }

  public void showPDFError(String fileName) {
    String icons = tinyTool.IMAGES;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Failed to open document");
    alert.setHeaderText(null);
    alert.setContentText("TinyTool could not open:\n" + fileName +
      "\nPlease ensure that you have either Adobe Acrobat Reader or any other " +
      "application installed that is capable of opening PDF documents.");

    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image(icons + "acrobat.png"));
    alert.showAndWait();
  }

  public void showFieldError(String fieldName, String error) {
    String icons = tinyTool.IMAGES;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(fieldName);
    alert.setHeaderText(null);
    alert.setContentText(error);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image(icons + "error32.png"));
    alert.showAndWait();
  }

  public String generateFeedback(String text) {
    String content;
    content = text + "\n";
    return content;
  }

  public String getFileExtension(File file) {
    String extension = null;
    String fileName = file.toString().toLowerCase();
    int index = fileName.lastIndexOf('.');

    if (index > 0)
      extension = fileName.substring(index + 1).toLowerCase();

    return extension;
  }

  // document processing methods
  public int excelCount(File file) {
    String type = getFileExtension(file);

    try {
      FileInputStream fis = new FileInputStream(file);

      if (type.equals("xlsx")) {
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        pages = wb.getNumberOfSheets();
      } else {
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        pages = wb.getNumberOfSheets();
      }
    } catch (Exception e) {
      pages = -1;
    }

    return pages;
  }

  public int pdfCount(File file) {
    try {
      PDDocument document = PDDocument.load(file);
      pages = document.getNumberOfPages();
      document.close();
    } catch (Exception e) {
      pages = -1;
    }

    return pages;
  }

  public int wordCount (File file) {
    String type = getFileExtension(file);

    try {
      FileInputStream fis = new FileInputStream(file);

      if (type.equals("docx")) {
        XWPFDocument doc = new XWPFDocument(fis);
        pages = doc.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
      } else {
        HWPFDocument doc = new HWPFDocument(fis);
        pages = doc.getSummaryInformation().getPageCount();
      }
    } catch (Exception e) {
      pages = -1;
    }

    return pages;
  }
}