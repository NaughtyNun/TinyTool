/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    March 2022
   @name    tinytool.controllers.TmsController

   @notes   controller class for TimeSheet generation
   @changes
 */

package tinytool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tinytool.*;
import tinytool.models.PersonnelRecord;
import tinytool.models.TmsRecord;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

public class TmsController implements Initializable {
  private final LoadPersonnelRecord loadPersonnelRecord = new LoadPersonnelRecord();
  private final LoadTimeSheetData loadTimeSheetData = new LoadTimeSheetData();
  private final CreateTimeSheet timeSheet = new CreateTimeSheet();
  private final Utilities utilities = new Utilities();
  private final TinyTool tinyTool = new TinyTool();
  private PersonnelRecord personnelRecord = new PersonnelRecord();
  private TmsRecord timeRecord = new TmsRecord();
  private File xmlPersonnel;
  private Stage stage;

  @FXML private DatePicker datePicker;
  @FXML private Label lblNumber;
  @FXML private Label lblName;
  @FXML private Label lblDate;
  @FXML public Label lblFeedback;
  @FXML private Button btnGenerate;
  @FXML private Button btnCopy;
  @FXML private Button btnExit;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    xmlPersonnel = new File(tinyTool.HOMEDIR + "\\personnel.xml");
    if (xmlPersonnel.exists() && !xmlPersonnel.isDirectory()) {
      personnelRecord = loadPersonnelRecord.loadPersonnelRecord(xmlPersonnel);

      if (personnelRecord.getPersonnelNumber() == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Personnel Record");
        alert.setHeaderText(null);
        alert.setContentText("In order to use the TimeSheet function you need to complete the Personnel Record " +
          "information. This can be done by accessing the Setup Information menu item under PDF Tools.");
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(tinyTool.IMAGES + "error32.png"));
        alert.showAndWait();
        handleExit();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot Load Personnel XML File");
      alert.setHeaderText(null);
      alert.setContentText("In order to use the TimeSheet function TinyTool needs to load the data, the XML file " +
        "could not be found/loaded.");
      stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(tinyTool.IMAGES + "error32.png"));
      alert.showAndWait();
      handleExit();
    }

    resetAll();
  }

  @FXML void handleGenerate() {
    String xmlName = personnelRecord.getPersonnelNumber() + "_D" + lblDate.getText() + ".xml";
    xmlName = xmlName.replace("-", "");
    File xmlFile = new File(personnelRecord.getTimeSheetLocal() + "\\" + xmlName);

    if (loadTimeSheetData.loadData(xmlFile, timeRecord)) {
      lblFeedback.setText("Loaded XML data file");

      if (timeSheet.createDocument(personnelRecord, timeRecord, new File("demo.pdf"))) {
        lblFeedback.setText("TimeSheet created successfully");
        btnGenerate.setDisable(true);
        btnCopy.setDisable(false);
      }
    }
  }

  @FXML void handleCopy() {
    System.out.println("to do");
  }

  @FXML void handleExit() {
    stage = (Stage) lblFeedback.getScene().getWindow();
    stage.close();
  }

  private void resetAll() {
    datePicker.setShowWeekNumbers(false);
    datePicker.getEditor().clear();
    datePicker.setPromptText("Week Ending Date");
    lblNumber.setText(null);
    lblName.setText(null);
    lblDate.setText(null);
    lblFeedback.setText(null);
    btnGenerate.setDisable(true);
    btnCopy.setDisable(true);
    btnExit.setDisable(false);
    initializeControls();
  }

  private void initializeControls() {
    datePicker.setOnAction(action -> {
      btnGenerate.setDisable(getLastDay() == null);
      lblDate.setText(String.valueOf(getLastDay()));
    });

    lblNumber.setText(personnelRecord.getPersonnelNumber());
    lblName.setText(personnelRecord.getName() + " " + personnelRecord.getSurname());
  }

  private LocalDate getLastDay() {
    LocalDate date = datePicker.getValue();
    LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
    int datePicked = date.getDayOfWeek().getValue();
    int daysToAdd;

    if (datePicked <= 6) {
      daysToAdd = 6 - datePicked;
    } else {
      daysToAdd = 6;
    }

    date = date.plusDays(daysToAdd);
    if (date.compareTo(lastDay) > 0)
      date = lastDay;

    return date;
  }
}