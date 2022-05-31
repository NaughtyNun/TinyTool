/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    June 2020
   @name    tinytool.controllers.SplitWindowController

   @notes   controller class for the Split Window
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
import org.apache.pdfbox.pdmodel.PDDocument;
import tinytool.TinyTool;
import tinytool.Utilities;
import tinytool.tasks.SplitTask;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SplitWindowController implements Initializable {
  private final FileChooser fileChooser = new FileChooser();
  private final Utilities utilities = new Utilities();
  private final TinyTool tinyTool = new TinyTool();
  private final SplitTask task = new SplitTask();
  private List<File> fileList = new ArrayList<>();
  private File currentItem = null;
  private String lastDir = null;
  private Stage stage = null;

  @FXML private ListView<File> listViewFiles;
  @FXML private TextArea textAreaFeedback;
  @FXML private Label labelName;
  @FXML private TextField textFieldName;
  @FXML private ProgressBar progressBar;
  @FXML private Button btnSelect;
  @FXML private Button btnSplit;
  @FXML private Button btnCancel;
  @FXML private Button btnExit;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    resetAll();
  }

  @FXML void handleSelect() {
    String directory;

    if (lastDir == null)
      directory = tinyTool.HOMEDIR;
    else
      directory = lastDir;

    stage = (Stage)labelName.getScene().getWindow();
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDF Documents",
      "*.pdf", "*.PDF");
    fileChooser.setInitialDirectory(new File(directory));
    fileChooser.getExtensionFilters().add(filter);
    fileChooser.setTitle("Select PDF Documents");
    fileList = fileChooser.showOpenMultipleDialog(stage.getOwner());

    if (fileList != null) {
      populateFileList(fileList);
      lastDir = fileList.get(0).getParent();
    }
  }

  @FXML void handleSplit() {
    textAreaFeedback.appendText(utilities.generateFeedback("Starting the splitting process"));
    String workDir = tinyTool.WORKDIR;
    List<File> splitList = listViewFiles.getItems();
    int totalPages = 0;

    btnSelect.setDisable(true);
    btnSplit.setDisable(true);
    btnExit.setDisable(true);
    btnCancel.setDisable(false);

    for (File file:splitList) {
      try {
        PDDocument document = PDDocument.load(file);
        totalPages = totalPages + document.getNumberOfPages();
        document.close();
      } catch (IOException e) {
        textAreaFeedback.textProperty().unbind();
        textAreaFeedback.appendText(utilities.generateFeedback(e.getMessage()));
      }
    }

    textAreaFeedback.textProperty().unbind();
    textAreaFeedback.appendText(utilities.generateFeedback("Creating " + totalPages + " new documents"));
    progressBar.progressProperty().unbind();
    progressBar.progressProperty().bind(task.progressProperty());
    progressBar.visibleProperty().setValue(true);
    textAreaFeedback.textProperty().unbind();
    textAreaFeedback.textProperty().bind(task.messageProperty());

    task.splitTask(workDir, splitList, textFieldName.getText(), totalPages);
    task.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
      btnCancel.setText("Done...");
      btnSelect.setDisable(true);
      btnSplit.setDisable(true);
      btnExit.setDisable(true);
      btnCancel.setDisable(false);

      textAreaFeedback.textProperty().unbind();
      textAreaFeedback.appendText(utilities.generateFeedback("Split Task Completed"));
      textAreaFeedback.appendText(utilities.generateFeedback("Clearing buffers..."));
      textAreaFeedback.appendText(utilities.generateFeedback("Closing all files..."));
      textAreaFeedback.appendText(utilities.generateFeedback("Documents saved in " + workDir));
    });

    new Thread(task).start();
  }

  @FXML void handleExit() {
    stage = (Stage)labelName.getScene().getWindow();
    stage.close();
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
    btnSplit.setDisable(true);
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
            checkForSplit();
          }
        }
      }
    });

    textFieldName.textProperty().addListener(((observable, oldVal, newVal) -> checkForSplit()));
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

  private void populateFileList(List<File> fileList) {
    for (File file:fileList)
      listViewFiles.getItems().add(file);

    checkForSplit();
    textFieldName.requestFocus();
  }

  private void checkForSplit() {
    if (!listViewFiles.getItems().isEmpty() && (!textFieldName.getText().isEmpty())) {
      btnSplit.setDisable(false);
      btnCancel.setDisable(false);
    } else {
      btnSplit.setDisable(true);
      btnCancel.setDisable(true);
    }
  }
}