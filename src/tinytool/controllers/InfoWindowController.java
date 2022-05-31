/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    January 2022
   @name    tinytool.controllers.InfoWindowController

   @notes   controller class for the Information Window
   @changes 2022-03-32 - added Tab navigation to make data entry more logical
            2022-04-09 - changed the input screens for the XML file that changed
            2022-05-06 - created a separate class for loading XML data for re-usability
*/

package tinytool.controllers;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tinytool.LoadPersonnelRecord;
import tinytool.SavePersonnelRecord;
import tinytool.Utilities;
import tinytool.TinyTool;
import tinytool.models.PersonnelRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoWindowController implements Initializable {
  private final LoadPersonnelRecord loadRecord = new LoadPersonnelRecord();
  private final SavePersonnelRecord saveRecord = new SavePersonnelRecord();
  private final Utilities utilities = new Utilities();
  private final TinyTool tinyTool = new TinyTool();
  private PersonnelRecord record = new PersonnelRecord();
  private String error;
  private Stage stage;
  private File xmlPersonnel;
  private File signature;
  private File initial;
  private File managerSignature;
  private File managerInitial;
  private File oneUpSignature;
  private File oneUpInitial;

  @FXML private Button buttonAccept;
  @FXML private Tab tabEmployee;
  @FXML private TextField textPersonnelNumber;
  @FXML private TextField textIdNumber;
  @FXML private TextField textSurname;
  @FXML private TextField textName;
  @FXML private TextField textEmail;
  @FXML private TextField textFmSurname;
  @FXML private TextField textFmName;
  @FXML private TextField textFmEmail;
  @FXML private TextField textOneUpSurname;
  @FXML private TextField textOneUpName;
  @FXML private TextField textOneUpEmail;
  @FXML private Tab tabSignatories;
  @FXML private Label lblSignature;
  @FXML private ImageView imgSignature;
  @FXML private ImageView imgInitial;
  @FXML private ImageView imgFmSignature;
  @FXML private ImageView imgFmInitial;
  @FXML private ImageView imgOneUpSignature;
  @FXML private ImageView imgOneUpInitial;
  @FXML private Button buttonSignature;
  @FXML private Button buttonInitial;
  @FXML private Tab tabLocations;
  @FXML private Label labelWorkDirectory;
  @FXML private TextField textDeclarations;
  @FXML private TextField textTimeData;
  @FXML private TextField textTimeCopy;
  @FXML private TextField textRegister;
  @FXML private TextField textClientSurname;
  @FXML private TextField textClientName;
  @FXML private TextField textClientEmail;
  @FXML private TextField textSystemId;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    xmlPersonnel = new File(tinyTool.HOMEDIR + "\\personnel.xml");
    labelWorkDirectory.setText(null);

    if (xmlPersonnel.exists() && !xmlPersonnel.isDirectory()) {
      loadData(xmlPersonnel);
      initializeControls();
    }
  }

  @FXML void handleSignature() {
    signature = processButton("Signature");
    if (signature != null) {
      processImageFile(signature, imgSignature, 200, 35, "Signature File");
      record.setUserSignature(signature);
    }
  }

  @FXML void handleInitial() {
    initial = processButton("Initial");
    if (initial != null) {
      processImageFile(initial, imgInitial, 80, 35, "Signature File");
      record.setUserInitials(initial);
    }
  }

  @FXML void handleManagerSignature() {
    managerSignature = processButton("Line Manager Signature");
    if (managerSignature != null) {
      processImageFile(managerSignature, imgFmSignature, 200, 80, "Manager Signature");
      record.setLineManagerSignature(managerSignature);
    }
  }

  @FXML void handleManagerInitial() {
    managerInitial = processButton("Line Manager Initial");
    if (managerInitial != null) {
      processImageFile(managerInitial, imgFmInitial, 80, 30, "Manager Initial");
      record.setLineManagerInitials(managerInitial);
    }
  }

  @FXML void handleOneUpSignature() {
    oneUpSignature = processButton("Project Manager Signature");
    if (oneUpSignature != null) {
      processImageFile(oneUpSignature, imgOneUpSignature, 200, 30, "Project Manager Signature");
      record.setOneUpSignature(oneUpSignature);
    }
  }

  @FXML void handleOneUpInitial() {
    oneUpInitial = processButton("Project Manager Initial");
    if (oneUpInitial != null) {
      processImageFile(oneUpInitial, imgOneUpSignature, 200, 30, "Project Manager Initial");
      record.setOneUpSignature(oneUpInitial);
    }
  }

  @FXML void handleAccept() {
    boolean nextField;

    tabEmployee.getTabPane().getSelectionModel().select(0);
    nextField = validateData(textPersonnelNumber, 1);
    nextField = (nextField) && validateIdNo();
    nextField = (nextField) && validateData(textSurname, 2);
    nextField = (nextField) && validateData(textName, 3);
    nextField = (nextField) && validateEmail(textEmail.getText(), textEmail);
    nextField = (nextField) && validateData(textFmSurname, 4);
    nextField = (nextField) && validateData(textFmName, 5);
    nextField = (nextField) && validateEmail(textFmEmail.getText(), textFmEmail);
    nextField = (nextField) && validateData(textOneUpSurname, 6);
    nextField = (nextField) && validateData(textOneUpName, 7);
    nextField = (nextField) && validateEmail(textOneUpEmail.getText(), textOneUpEmail);

    if (nextField) {
      tabSignatories.getTabPane().getSelectionModel().select(1);
      nextField = validateSignature(signature, buttonSignature, 1);
      nextField = (nextField) && validateSignature(initial, buttonInitial, 2);
    }

    if (nextField) {
      tabLocations.getTabPane().getSelectionModel().select(2);
      nextField = validateData(textDeclarations, 8);
      nextField = (nextField) && validateData(textTimeData, 9);
      nextField = (nextField) && validateData(textTimeCopy, 10);
      nextField = (nextField) && validateData(textRegister, 11);
      nextField = (nextField) && validateData(textClientSurname, 12);
      nextField = (nextField) && validateData(textClientName, 13);
      nextField = (nextField) && validateData(textSystemId, 14);

      if (nextField) {
        record.setPersonnelNumber(textPersonnelNumber.getText());
        record.setIdNumber(textIdNumber.getText());
        record.setSurname(textSurname.getText());
        record.setName(textName.getText());
        record.setEmail(textEmail.getText());
        record.setLineManagerSurname(textFmSurname.getText());
        record.setLineManagerName(textFmName.getText());
        record.setLineManagerEmail(textFmEmail.getText());
        record.setOneUpSurname(textOneUpSurname.getText());
        record.setOneUpName(textOneUpName.getText());
        record.setOneUpEmail(textOneUpEmail.getText());
        record.setDefaultDirectory(tinyTool.WORKDIR);
        record.setDeclarations(textDeclarations.getText());
        record.setTimeSheetLocal(textTimeData.getText());
        record.setTimeSheetCopy(textTimeCopy.getText());
        record.setAttendance(textRegister.getText());
        record.setClientSurname(textClientSurname.getText());
        record.setClientName(textClientName.getText());
        record.setClientEmail(textClientEmail.getText());
        record.setSystemId(textSystemId.getText());

        if (saveRecord.savePersonnelRecord(xmlPersonnel, record)) {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Saved");
          alert.setHeaderText(null);
          alert.setContentText("The Personnel Record was stored successfully.\n" + xmlPersonnel);
          stage = (Stage) alert.getDialogPane().getScene().getWindow();
          stage.getIcons().add(new Image(tinyTool.IMAGES + "info.png"));
          alert.showAndWait();
          handleCancel();
        }
      }else
          initializeControls();
    }
  }

  @FXML void handleCancel() {
    stage = (Stage) buttonAccept.getScene().getWindow();
    stage.close();
  }

  private void loadData(File xmlPersonnel) {
    record = loadRecord.loadPersonnelRecord(xmlPersonnel);

    if (!record.getPersonnelNumber().isEmpty()) {
      textPersonnelNumber.setText(record.getPersonnelNumber());
      textSystemId.setText(record.getSystemId());
      textIdNumber.setText(record.getIdNumber());
      textSurname.setText(record.getSurname());
      textName.setText(record.getName());
      textEmail.setText(record.getEmail());
      textFmSurname.setText(record.getLineManagerSurname());
      textFmName.setText(record.getLineManagerName());
      textFmEmail.setText(record.getLineManagerEmail());
      textOneUpSurname.setText(record.getOneUpSurname());
      textOneUpName.setText(record.getOneUpName());
      textOneUpEmail.setText(record.getOneUpEmail());
      textClientSurname.setText(record.getClientSurname());
      textClientName.setText(record.getClientName());
      textClientEmail.setText(record.getClientEmail());
      labelWorkDirectory.setText(record.getDefaultDirectory());
      textDeclarations.setText(record.getDeclarations());
      textTimeData.setText(record.getTimeSheetLocal());
      textTimeCopy.setText(record.getTimeSheetCopy());
      textRegister.setText(record.getAttendance());

      signature = record.getUserSignature();
      initial = record.getUserInitials();
      managerSignature = record.getLineManagerSignature();
      managerInitial = record.getLineManagerInitials();
      oneUpSignature = record.getOneUpSignature();
      oneUpInitial = record.getOneUpInitials();

      if (signature.length() >= 8)
        processImageFile(signature, imgSignature, 200, 35, "Signature");

      if (initial.length() >= 8)
        processImageFile(initial, imgInitial, 80, 35, "Initial");

      if (managerSignature.length() >= 8)
        processImageFile(managerSignature, imgFmSignature, 200, 35, "Manager Signature");

      if (managerInitial.length() >= 8)
        processImageFile(managerInitial, imgFmInitial, 80, 35, "Manager Initial");

      if (oneUpSignature.length() >= 8)
        processImageFile(oneUpSignature, imgOneUpSignature, 200, 35, "One Up Signature");

      if (oneUpInitial.length() >= 8)
        processImageFile(oneUpInitial, imgOneUpInitial, 80, 35, "One Up Initial");
    } else
      labelWorkDirectory.setText(tinyTool.WORKDIR);
  }

  private void initializeControls() {
    textPersonnelNumber.textProperty().addListener(
      (observable, oldValue, newValue) -> {
        if (newValue.length() > 8)
          ((StringProperty) observable).setValue(oldValue);
      });

    textIdNumber.textProperty().addListener(
      (observable, oldValue, newValue) -> {
        if (newValue.length() > 13)
          ((StringProperty) observable).setValue(oldValue);
      });
  }

  private void processImageFile(File file, ImageView image, int width, int height, String fieldName) {
    try {
      Image img = new Image(new FileInputStream(file));
      image.setFitWidth(width);
      image.setFitHeight(height);
      image.setPreserveRatio(true);
      image.setSmooth(true);
      image.setImage(img);
    } catch (FileNotFoundException e) {
      utilities.showFieldError(fieldName, "The file " + file + " could not be located or opened.\n" +
        "Error: " + e.getMessage());
    }
  }

  private boolean validateIdNo() {
    boolean correct;

    if (textIdNumber.getText().isEmpty() && textIdNumber.getText().length() != 13) {
      error = "Some automated features requires your ID number\n" +
        "in order to execute. Please enter your ID Number.";
      utilities.showFieldError("ID Number", error);
      textIdNumber.requestFocus();
      correct = false;
    } else
      correct = true;

    if (correct)
      if (!textIdNumber.getText().matches("\\d*")) {
        error = "Your ID Number cannot contain any alphanumeric characters\n" +
          "only numeric characters. Please correct your ID Number.";
        utilities.showFieldError("ID Number", error);
        textIdNumber.requestFocus();
        correct = false;
      }

    return correct;
  }

  private boolean validateEmail(String email, TextField field) {
    String valid = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$";
    Pattern pattern = Pattern.compile(valid);
    Matcher matcher = pattern.matcher(email);

    if (!matcher.matches() || field.getText().isEmpty()) {
      utilities.showFieldError("Email Address", "The email address is either empty or the " +
        "email address is invalid. Please enter a correct email address");
      field.requestFocus();
      return false;
    } else
      return true;
  }

  private boolean validateSignature(File signatureFile, Button focusButton, int signatureType) {
    boolean correct = false;
    String description;

    if (signatureType == 1)
      description = "Signature";
    else
      description = "Initial";

    if (signatureFile != null && signatureFile.exists() && !signatureFile.isDirectory())
      correct = true;
    else {
      utilities.showFieldError(description, "The " + description + " cannot be blank. Please choose a " +
        description.toLowerCase() + " file.");
      focusButton.requestFocus();
    }

    return correct;
  }

  private boolean validateData(TextField textField, int field) {
    String fieldName = null;
    boolean correct = false;

    if (textField.getText().isEmpty())
      switch (field) {
        case 1:
          error = "Your Personnel Number cannot be left empty.\n" +
            "Please enter your Personnel Number.";
          fieldName = "Personnel Number";
          break;
        case 2:
          error = "Your Surname cannot be left empty.\n" +
            "Please enter your Surname.";
          fieldName = "Surname";
          break;
        case 3:
          error = "Your Name cannot be left empty.\n" +
            "Please enter your Name.";
          fieldName = "Name";
          break;
        case 4:
          error = "Your Line Manager (Functional Manager) must have a Surname.\n" +
            "Please enter the Surname of your Line Manager.";
          fieldName = "Line Manager Surname";
          break;
        case 5:
          error = "Your Line Manager (Functional Manager) must have a Name.\n" +
            "Please enter the Name of your Line Manager.";
          fieldName = "Line Manager Name";
          break;
        case 6:
          error = "Your Project Manager (One-Up Manager) must have a Surname.\n" +
            "Please enter the Surname of your Project Manager.";
          fieldName = "Project Manager Surname";
          break;
        case 7:
          error = "Your Project Manager (One-Up Manager) must have a Name.\n" +
            "Please enter the Name of your Project Manager.";
          fieldName = "Project Manager Name";
          break;
        case 8:
          error = "The location where your Security Declarations are saved cannot be empty.\n" +
            "Please enter the location where you want your declarations to be saved.";
          fieldName = "Security Declarations";
          break;
        case 9:
          error = "The location where your Timesheet data files are located cannot be empty.\n" +
            "Please enter the location where timesheet data file are located.";
          fieldName = "Timesheet Data File";
          break;
        case 10:
          error = "The location where you want your Timesheet to be saved cannot be empty.\n" +
            "Please enter the location where you want your timesheet to be saved.";
          fieldName = "Timesheet Save";
          break;
        case 11:
          error = "The location where you want your Attendance Register to be saved cannot be empty.\n" +
            "Please enter the location where you want your attendance register to be saved.";
          fieldName = "Attendance Register Save";
          break;
        case 12:
          error = "Your Client must surely have a Surname? Please enter the surname of your client.";
          fieldName = "Client Surname";
          break;
        case 13:
          error = "Your Client must surely have a Name yes? Please enter the name of your client.";
          fieldName = "Client Name";
          break;
        case 14:
          error = "Your domain login ID cannot be blank. Please enter your domain login ID.";
          fieldName = "Login ID";
          break;
      }
    else
      correct = true;

    if (!correct) {
      utilities.showFieldError(fieldName, error);
      textField.requestFocus();
    }

    return correct;
  }

  private File processButton(String signature) {
    tabSignatories.getTabPane().getSelectionModel().select(1);
    stage = (Stage) lblSignature.getScene().getWindow();
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(signature, "*.png", "*.PNG",
      "*.jpg", "*.JPG", "*.bmp", "*.BMP");
    fileChooser.setInitialDirectory(new File(tinyTool.HOMEDIR));
    fileChooser.getExtensionFilters().add(filter);
    File signaturePath = fileChooser.showOpenDialog(stage.getOwner());

    assert signaturePath != null;
    if (signaturePath.isFile())
      return signaturePath;
    else
      return null;
  }
}