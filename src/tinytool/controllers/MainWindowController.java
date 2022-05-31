/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    March 2020
   @name    tinytool.controllers.MainWindowController

   @notes   class to handle all the main menu functions
   @changes 2020-03-06 - separated PDF and document functions
            2020-08-03 - added a "clear" menu item to clear out the working directory
            2021-12-20 - simplified the window handling with ViewFactory class
            2022-01-22 - added "quick buttons" to the main screen
*/

package tinytool.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tinytool.TinyTool;
import tinytool.Utilities;
import tinytool.views.ViewFactory;

import java.io.File;
import java.util.Optional;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MainWindowController {
  private final TinyTool tinyTool = new TinyTool();
  private final Utilities utilities = new Utilities();
  private final String icons = tinyTool.IMAGES;
  private Stage stage = new Stage();

  @FXML Button btnSplit;
  @FXML Button btnMerge;
  @FXML Button btnSearch;

  // Button Actions
  @FXML void buttonSplit() { btnSplit.setOnAction(event -> handleSplit()); }
  @FXML void buttonMerge() { btnMerge.setOnAction(event -> handleMerge()); }
  @FXML void buttonSearch() { btnSearch.setOnAction(event -> handleSearch()); }

  // PDF Tools Menu
  @FXML void handleSplit() {
    ViewFactory factory = new ViewFactory("Split PDF Documents", "SplitWindow.fxml", "split32.png",
      800,460,800,460);
    factory.showSplitWindow();
  }

  @FXML void handleMerge() {
    ViewFactory factory = new ViewFactory("Merge PDF Documents", "MergeWindow.fxml", "merge32.png",
      800,460,800,460);
    factory.showMergeWindow();
  }

  @FXML void handleInformation() {
    String filePath = tinyTool.HOMEDIR;
    File xmlPersonnel = new File(filePath + "\\personnel.xml");

    if (xmlPersonnel.exists() && !xmlPersonnel.isDirectory()) {
      ViewFactory factory = new ViewFactory("Information", "InfoWindow.fxml", "information32.png",
        620,450,620,450);
      factory.showInformationWindow();
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("XML Schema File Not Found");
      alert.setHeaderText(null);
      stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(icons + "error32.png"));
      alert.setContentText("Tool could not located the XML schema file in the default location " + filePath +
        ". Please check that this file exists, if you cannot find the file please contact IT Support so that " +
        "they can issue the file again.");
      alert.showAndWait();
    }
  }

  @FXML void handleClear() {
    String workDir = tinyTool.WORKDIR;
    long directorySize = utilities.folderSize(new File(workDir));
    long fileCount = utilities.countFiles(workDir);

    if (fileCount >= 1) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Working Directory - Files");
      alert.setHeaderText(null);
      stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(icons + "delete32.png"));
      alert.setContentText("The TinyTool working directory has the following\n" +
        "Files: " + fileCount +
        "\nSize: " + (directorySize / 1024) + "Kb" +
        "\nDo you want the clean the working directory?");
      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
        File[] fileList = new File(workDir).listFiles();

        assert fileList != null;
        for (File file : fileList)
          if (file.isFile())
            file.delete();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Working Directory - Files");
      alert.setContentText("There are no files in the working directory");
      alert.setHeaderText(null);
      stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(icons + "information32.png"));
      alert.showAndWait();
    }
  }

  @FXML void handleExit() {
    if (confirm()) {
      Platform.exit();
      System.exit(0);
    }
  }

  // Document Tools Menu
  @FXML void handleSearch() {
    ViewFactory factory = new ViewFactory("Search Facility", "SearchWindow.fxml", "search32.png",
      800,630,800,630);
    factory.showSearchWindow();
  }

  @FXML void handleDeclaration() {
    ViewFactory factory = new ViewFactory("Security Declaration", "DeclarationWindow.fxml",
      "declare32.png", 450, 280, 450, 280);
    factory.showDeclarationWindow();
  }

  @FXML void handleTimeSheet() {
    ViewFactory factory = new ViewFactory("Time Sheets", "TmsWindow.fxml", "timesheet32.png",
      450, 310, 450, 310);
    factory.showTimeSheetWindow();
  }

  @FXML void handleAbout() {
    ViewFactory factory= new ViewFactory("About - TinyTool", "AboutBox.fxml", "about.png",
      650, 370, 700, 400);
    factory.showAboutWindow();
  }

  private boolean confirm() {
    String icon = tinyTool.IMAGES;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Exit TinyTool");
    alert.setHeaderText(null);
    alert.setContentText("You are about to exit TinyTool");
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image(icon + "exit.png"));

    Optional<ButtonType> result = alert.showAndWait();
    return (result.isPresent()) && (result.get() == ButtonType.OK);
  }
}