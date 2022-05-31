/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    June 2021
   @name    tinytool.controllers.MergeWindowController

   @notes   controller class for the Merge Window
   @changes 2021-08-06 - running the split task through a task manager
 */

package tinytool.controllers;

import javafx.application.HostServices;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tinytool.TinyTool;
import tinytool.Utilities;
import tinytool.tasks.MergeTask;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MergeWindowController implements Initializable {
  private final FileChooser fileChooser = new FileChooser();
  private final Utilities utilities = new Utilities();
  private final TinyTool tinyTool = new TinyTool();
  private final MergeTask task = new MergeTask();
  private List<File> fileList = new ArrayList<>();
  private String lastDirectory = null;
  private File currentItem = null;
  private Stage stage = null;

  @FXML private ListView<File> listViewFiles;
  @FXML private TextArea textAreaFeedback;
  @FXML private Label labelName;
  @FXML private TextField textFieldName;
  @FXML private ProgressBar progressBar;
  @FXML private Button btnSelect;
  @FXML private Button btnMerge;
  @FXML private Button btnCancel;
  @FXML private Button btnExit;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    resetAll();
  }

  @FXML void handleSelect() {
    String directory;

    if (lastDirectory == null)
      directory = tinyTool.HOMEDIR;
    else
      directory = lastDirectory;

    stage = (Stage)labelName.getScene().getWindow();
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDF Documents","*.pdf", "*.PDF");
    fileChooser.setInitialDirectory(new File(directory));
    fileChooser.getExtensionFilters().add(filter);
    fileChooser.setTitle("Select PDF Documents");
    fileList = fileChooser.showOpenMultipleDialog(stage.getOwner());

    if (fileList != null) {
      populateFileList(fileList);
      lastDirectory = fileList.get(0).getParent();
    }
  }

  @FXML void handleMerge() {
    textAreaFeedback.appendText(utilities.generateFeedback("Starting the merge process"));
    String workDir = tinyTool.WORKDIR;
    List<File> mergeList = listViewFiles.getItems();
    int totalDocs = mergeList.size();

    btnSelect.setDisable(true);
    btnMerge.setDisable(true);
    btnExit.setDisable(true);
    btnCancel.setDisable(false);

    textAreaFeedback.textProperty().unbind();
    textAreaFeedback.appendText(utilities.generateFeedback("Merging " + totalDocs + " into new document."));
    progressBar.progressProperty().unbind();
    progressBar.progressProperty().bind(task.progressProperty());
    progressBar.visibleProperty().setValue(true);
    textAreaFeedback.textProperty().unbind();
    textAreaFeedback.textProperty().bind(task.messageProperty());

    task.mergeTask(workDir, mergeList, textFieldName.getText(), totalDocs);
    task.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
      btnCancel.setText("Done...");
      btnSelect.setDisable(true);
      btnMerge.setDisable(true);
      btnExit.setDisable(true);
      btnCancel.setDisable(false);

      textAreaFeedback.textProperty().unbind();
      textAreaFeedback.appendText(utilities.generateFeedback("Merge Task Completed"));
      textAreaFeedback.appendText(utilities.generateFeedback("Clearing buffers..."));
      textAreaFeedback.appendText(utilities.generateFeedback("Closing all files..."));
      textAreaFeedback.appendText(utilities.generateFeedback("Document saved in " + workDir));
    });

    new Thread(task).start();
  }

  @FXML void handleExit() {
    stage = (Stage)labelName.getScene().getWindow();
    stage.close();
  }

  private void populateFileList(List<File> fileList) {
    for (File file:fileList)
      listViewFiles.getItems().add(file);

    checkForMerge();
    textFieldName.requestFocus();
  }

  private void resetAll() {
    fileList = new ArrayList<>();
    textFieldName.clear();
    populateFileList(fileList);
    listViewFiles.getItems().removeAll(fileList);
    listViewFiles.setPlaceholder(new Label("No PDF Documents Selected"));
    progressBar.progressProperty().unbind();
    textAreaFeedback.textProperty().unbind();
    progressBar.setProgress(0);
    textAreaFeedback.clear();
    btnSelect.setDisable(false);
    btnExit.setDisable(false);
    btnMerge.setDisable(true);
    btnCancel.setDisable(true);

    initializeControls();
  }

  private void initializeControls() {
    listViewFiles.setOnMouseClicked(click -> {
      currentItem = listViewFiles.getSelectionModel().getSelectedItem();

      if (click.getClickCount() == 2) {
        if (click.getButton() == MouseButton.PRIMARY) {
          try {
            HostServices hs = tinyTool.getHostServices();
            hs.showDocument(String.valueOf(currentItem));
          } catch (Exception e) {
            utilities.showPDFError(currentItem.getName());
          }
        } else {
          if ((click.getButton() == MouseButton.SECONDARY) && (!listViewFiles.getItems().isEmpty())) {
            int selectedItem = listViewFiles.getSelectionModel().getSelectedIndex();
            listViewFiles.getItems().remove(selectedItem);
            checkForMerge();
          }
        }
      }
    });

    textFieldName.textProperty().addListener(((observable, oldVal, newVal) -> checkForMerge()));
    btnCancel.setOnAction(actionEvent -> {
      if (btnCancel.textProperty().getValue().equals("Cancel")) {
        task.cancel(true);
        textAreaFeedback.textProperty().unbind();
        textAreaFeedback.appendText(utilities.generateFeedback("Operation was cancelled"));
        resetAll();
      } else
        handleExit();
    });

    progressBar.progressProperty().unbind();
    progressBar.setVisible(false);
  }

  private void checkForMerge() {
    if (!listViewFiles.getItems().isEmpty() && (!textFieldName.getText().isEmpty())) {
      btnMerge.setDisable(false);
      btnCancel.setDisable(false);
    } else {
      btnMerge.setDisable(true);
      btnCancel.setDisable(true);
    }
  }
}