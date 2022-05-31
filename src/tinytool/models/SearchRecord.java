/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    August 2021
   @name    tinytool.models.SearchRecord

   @notes   basic record information when documents are searched
   @changes
*/

package tinytool.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchRecord {
  private final StringProperty fileName;
  private final StringProperty fileType;
  private final IntegerProperty pages;
  private final IntegerProperty matches;
  private final StringProperty comment;

  // Constructor
  public SearchRecord() {
    this.fileName = new SimpleStringProperty();
    this.fileType = new SimpleStringProperty();
    this.pages    = new SimpleIntegerProperty();
    this.matches  = new SimpleIntegerProperty();
    this.comment  = new SimpleStringProperty();
  }

  // Setters
  public void setFileName(String fileName) {this.fileName.set(fileName); }
  public void setFileType(String fileType) {this.fileType.set(fileType); }
  public void setPages(int pages) {this.pages.set(pages); }
  public void setMatches(int matches) {this.matches.set(matches); }
  public void setComment(String comment) {this.comment.set(comment); }

  // Properties
  public StringProperty fileNameProperty() {return fileName; }
  public StringProperty fileTypeProperty() {return fileType; }
  public IntegerProperty pagesProperty() {return pages; }
  public IntegerProperty matchesProperty() {return matches; }
  public StringProperty commentProperty() {return comment; }
}