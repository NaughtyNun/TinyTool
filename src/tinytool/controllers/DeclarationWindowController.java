/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    January 2022
   @name    tinytool.controllers.DeclarationWindowController

   @notes   controller class for declaration window
   @changes

   @ToDo    implement Public Holiday function so that a user cannot choose a date on the declaration
            that falls on a public holiday. I need to however do this through a database, SQLite does
            seem to be the best fit for this.
*/

package tinytool.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import tinytool.CreateDeclaration;
import tinytool.LoadPersonnelRecord;
import tinytool.TinyTool;
import tinytool.Utilities;
import tinytool.models.PersonnelRecord;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

public class DeclarationWindowController  implements Initializable {
  private final LoadPersonnelRecord loadPersonnel = new LoadPersonnelRecord();
  private final CreateDeclaration declaration = new CreateDeclaration();
  private final Utilities utilities = new Utilities();
  private final TinyTool tinyTool = new TinyTool();
  private PersonnelRecord personnel = new PersonnelRecord();
  private List<String> dateList = new ArrayList<>();
  private int weekNumber;
  private File pdfFile;
  private File copyFile;

  @FXML private DatePicker datePicker;
  @FXML private ListView<String> listDates;
  @FXML private Label fileString;
  @FXML private Button btnAdd;
  @FXML private Button btnCreate;
  @FXML private Button btnCopy;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    File xmlPersonnel = new File(tinyTool.HOMEDIR + "\\personnel.xml");

    if (xmlPersonnel.exists() && !xmlPersonnel.isDirectory()) {
      personnel = loadPersonnel.loadPersonnelRecord(xmlPersonnel);
      pdfFile = new File(tinyTool.WORKDIR + personnel.getPersonnelNumber() + " " +
        personnel.getSurname() + " " + personnel.getName().charAt(0) + " - SD.pdf");
      copyFile = new File(personnel.getDeclarations() + "\\" + personnel.getPersonnelNumber() + " " +
        personnel.getSurname() + " " + personnel.getName().charAt(0) + " - SD.pdf");
    }

    resetAll();
  }

  @FXML void handleAdd() {
    String dayName;
    String monthName;
    int dayNumber;
    int yearNumber;
    int dayOfYear;
    LocalDate date = datePicker.getValue();
    Locale locale = Locale.US;

    if (dateList.size() >= 6)
      datePicker.setDisable(true);

    weekNumber = date.get(WeekFields.of(locale).weekOfYear());
    dayName = String.valueOf(date.getDayOfWeek());
    dayNumber = date.getDayOfMonth();
    monthName = String.valueOf(date.getMonth());
    yearNumber = date.getYear();
    dayOfYear = date.getDayOfYear();
//    String dateString = dayOfYear + ":" + dayName + " " + dayNumber + " " +
//      monthName + " " + yearNumber;
    String dateString = date.toString();

    if (listDates.getItems().size() == 0)
      listDates.getItems().add(dateString);
    else {
      dateList = new ArrayList<>(listDates.getItems());
      if (!dateList.contains(dateString))
        listDates.getItems().add(dateString);
      else {
        utilities.showFieldError("Duplicate Date", "You have already added " +
          dayName + " " + dayNumber + " " + monthName + " " + yearNumber +
          " to the list if dates that you worked from home");
        datePicker.requestFocus();
      }
    }
  }

  @FXML void handleCreate() {
    fileString.setText("Starting the process...");

    if (declaration.createDocument(personnel, sortEntries(), pdfFile)) {
      fileString.setText("Declaration Created Successfully");
      datePicker.getEditor().clear();
      datePicker.setDisable(true);
      btnCreate.setDisable(true);
      btnAdd.setDisable(true);
      btnCopy.setDisable(false);
    } else {
      utilities.showFieldError("Declaration Document",
        "The declaration document for week " + weekNumber + " could not be generated. " +
          "Please try to recreate this document");
      resetAll();
    }
  }

  @FXML void handleCopy() {
    try {
      FileUtils.copyFile(pdfFile, copyFile);
      fileString.setText("File copied successfully");
      btnCopy.setDisable(true);
    } catch (IOException e) {
      utilities.showFieldError("Declaration Copy", "The declaration could not be copied to the " +
        "destination folder.\n" + "Error: " + e.getMessage());
    }
  }

  @FXML void handleExit() {
    Stage stage = (Stage) fileString.getScene().getWindow();
    stage.close();
  }

  private void resetAll() {
    dateList = new ArrayList<>();
    listDates.getItems().clear();
    listDates.setPlaceholder(new Label("No dates selected"));
    datePicker.setShowWeekNumbers(false);
    datePicker.getEditor().clear();
    fileString.setText("");
    btnAdd.setDisable(true);
    btnCreate.setDisable(true);
    btnCopy.setDisable(true);
    initializeControls();
  }

  private void initializeControls() {
    datePicker.setOnAction(action -> {
      LocalDate date = datePicker.getValue();
      btnAdd.setDisable(date == null);
    });

    listDates.getItems().addListener((ListChangeListener) this::onChanged);

    listDates.setOnMouseClicked(click -> {
      if ((click.getButton() == MouseButton.SECONDARY) && (!listDates.getItems().isEmpty())) {
        int selectedItem = listDates.getSelectionModel().getSelectedIndex();
        listDates.getItems().remove(selectedItem);
      }
    });
  }

  private void onChanged(ListChangeListener.Change c) {
    if (listDates.getItems().size() > 6) {
      datePicker.setDisable(true);
      btnAdd.setDisable(true);
      utilities.showFieldError("Too many dates", "The declaration can have a maximum of 6 days " +
        "that you have worked from home.");
      listDates.getItems().remove(6);
    }

    if (listDates.getItems().size() >= 1 && listDates.getItems().size() < 7) {
      btnCreate.setDisable(false);
      btnCopy.setDisable(true);
    }
  }

  private List sortEntries() {
    List list = new ArrayList();

    if (listDates.getItems().size() != 0) {
      for (int i=0; i<listDates.getItems().size(); i++)
        list.add(listDates.getItems().get(i));

      Collections.sort(list);
      listDates.getItems().clear();
      listDates.getItems().addAll(list);
    }

    return list;
  }
}