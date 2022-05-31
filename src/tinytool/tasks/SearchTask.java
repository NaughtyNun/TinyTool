/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    October 2021
   @name    tinytool.tasks.SearchTask

   @notes   task to search documents
   @changes
*/

package tinytool.tasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import tinytool.*;
import tinytool.models.SearchRecord;

import java.io.File;
import java.util.List;

public class SearchTask extends Task<Integer> {
  public List<File> fileList;
  public String needle;
  public boolean exact;
  public int totalDocuments;
  private int counter = 0;
  private int matches;
  private String fuzzy;
  private String text;

  private final ObservableList<SearchRecord> searchData = FXCollections.observableArrayList();
  private final StringBuilder feedBack = new StringBuilder();
  private final Utilities utilities = new Utilities();
  private SearchRecord searchRecord = new SearchRecord();

  public void searchTask(List<File> fileList, String needle, boolean exact, int totalDocuments) {
    this.fileList = fileList;
    this.needle = needle;
    this.exact = exact;
    this.totalDocuments = totalDocuments;

    if (exact)
      fuzzy = "Exact Matches Search Done";
    else
      fuzzy = "Fuzzy Search Done";
  }

  public ObservableList<SearchRecord> getSearchData() {
    return searchData;
  }

  @Override
  protected Integer call() throws Exception {
    for (File file : fileList) {
      String extension = utilities.getFileExtension(file);
      char type = Character.toUpperCase(extension.charAt(0));
      searchRecord = new SearchRecord();

      switch (type) {
        case 'C' : case 'T' :               // Process CSV/Text files
          if (type == 'C')
            searchRecord.setFileType("CSV");
          else
            searchRecord.setFileType("TEXT");

          searchRecord.setPages(1);
          searchRecord.setFileName(file.getName());
          searchTextFile(file);
          searchData.add(searchRecord);
          break;
        case 'D':                           // Process WORD documents
          searchRecord.setFileType("DOC");
          searchRecord.setFileName(file.getName());
          searchRecord.setPages(utilities.wordCount(file));
          searchWord(file);
          searchData.add(searchRecord);
          break;
        case 'X':                           // Process EXCEL files
          searchRecord.setFileType("EXCEL");
          searchRecord.setFileName(file.getName());
          searchRecord.setPages(utilities.excelCount(file));
          searchExcel(file);
          searchData.add(searchRecord);
          break;
        case 'P':                            // Process PDF documents
          searchRecord.setFileType("PDF");
          searchRecord.setFileName(file.getName());
          searchRecord.setPages(utilities.pdfCount(file));
          searchPdfFile(file);
          searchData.add(searchRecord);
      }
    }

    return counter;
  }

  private void searchTextFile(File file) throws Exception {
    matches = 0;

    try {
      text = "Searching... ";
      this.updater(text, file);
      searchRecord.setMatches(matches = new SearchText().searchText(file, needle, exact));

      switch (matches) {
        case 0:
          searchRecord.setComment("No matches found");
          break;
        case -1:
          searchRecord.setComment("Could not search CSV/TEXT file");
          break;
        default:
          searchRecord.setComment(fuzzy);
          break;
      }

      counter++;
      this.updateProgress(counter, totalDocuments);
    } catch (Exception e) {
      searchRecord.setMatches(0);
      searchRecord.setComment("Error: " + e.getMessage());
      text = "Error " + e.getMessage() + " on ";
      this.updater(text, file);
    }
  }

  private void searchWord(File file) throws Exception {
    matches = 0;

    try {
      text = "Searching... ";
      this.updater(text, file);
      searchRecord.setMatches(matches = new SearchWord().searchWord(file, needle, exact));

      switch (matches) {
        case 0:
          searchRecord.setComment("No matches found");
          break;
        case -1:
          searchRecord.setComment("Could not search WORD document");
          break;
        default:
          searchRecord.setComment(fuzzy);
          break;
      }

      counter++;
      this.updateProgress(counter, totalDocuments);
    } catch (Exception e) {
      searchRecord.setMatches(0);
      searchRecord.setComment("Error: " + e.getMessage());
      text = "Error " + e.getMessage() + " on ";
      this.updater(text, file);
    }
  }

  private void searchExcel(File file) throws Exception {
    matches = 0;

    try {
      text = "Searching... ";
      this.updater(text, file);
      searchRecord.setMatches(matches = new SearchExcel().searchExcel(file, needle, exact));

      switch (matches) {
        case 0:
          searchRecord.setComment("No matches found");
          break;
        case -1:
          searchRecord.setComment("Could not search EXCEL file");
          break;
        default:
          searchRecord.setComment(fuzzy);
          break;
      }

      counter++;
      this.updateProgress(counter, totalDocuments);
    } catch (Exception e) {
      searchRecord.setMatches(0);
      searchRecord.setComment("Error: " + e.getMessage());
      text = "Error " + e.getMessage() + " on ";
      this.updater(text, file);
    }
  }

  private void searchPdfFile(File file) throws Exception {
    try {
      text = "Searching... ";
      this.updater(text, file);
      searchRecord.setMatches(matches = new SearchPdf().searchPdf(file, needle, exact));

      switch (matches) {
        case 0:
          searchRecord.setComment("No matches found");
          break;
        case -1:
          searchRecord.setComment("Could not search PDF document");
          break;
        case -2:
          searchRecord.setComment("Protected PDF document");
          break;
        default:
          searchRecord.setComment(fuzzy);
          break;
      }

      counter++;
      this.updateProgress(counter, totalDocuments);
    } catch (Exception e) {
      searchRecord.setMatches(0);
      searchRecord.setComment("Error: " + e.getMessage());
      text = "Error " + e.getMessage() + " on ";
      this.updater(text, file);
    }
  }

  private void updater(String text, File file) throws Exception {
    StringBuilder append = feedBack.append(text).append(file.getName().toUpperCase()).append("\n");
    this.updateMessage(String.valueOf(append));
    Thread.sleep(4);
  }

}