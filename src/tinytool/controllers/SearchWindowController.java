/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    October 2021
   @name    tinytool.controllers.SearchWindowController

   @notes   controller class for the SearchWindow
   @changes
 */

package tinytool.controllers;

import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tinytool.TinyTool;
import tinytool.Utilities;
import tinytool.models.SearchRecord;
import tinytool.tasks.SearchTask;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchWindowController  implements Initializable {
  private final TinyTool tinyTool = new TinyTool();
  private final FileChooser fileChooser = new FileChooser();
  private final Utilities utilities = new Utilities();
  private final SearchTask task = new SearchTask();

  private boolean exactMatch;
  private List<File> fileList = new ArrayList<>();
  private String lastDirectory = null;
  private Stage stage = null;

  @FXML private ListView<File> listViewFiles;
  @FXML private TableView<SearchRecord> tableResults;
  @FXML private TableColumn<SearchRecord, String> fileNameColumn;
  @FXML private TableColumn<SearchRecord, String> fileTypeColumn;
  @FXML private TableColumn<SearchRecord, Integer> pagesColumn;
  @FXML private TableColumn<SearchRecord, Integer> matchesColumn;
  @FXML private TableColumn<SearchRecord, String> commentsColumn;
  @FXML private TextArea textAreaFeedback;
  @FXML private Label labelText;
  @FXML private TextField textFieldValue;
  @FXML private ProgressBar progressBar;
  @FXML private ToggleGroup SearchOptions;
  @FXML private RadioButton radioBtnExact;
  @FXML private RadioButton radioBtnFuzzy;
  @FXML private Button btnSelect;
  @FXML private Button btnSearch;
  @FXML private Button btnCancel;
  @FXML private Button btnExit;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
    fileTypeColumn.setCellValueFactory(cellData -> cellData.getValue().fileTypeProperty());
    pagesColumn.setCellValueFactory(cellData    -> cellData.getValue().pagesProperty().asObject());
    matchesColumn.setCellValueFactory(cellData  -> cellData.getValue().matchesProperty().asObject());
    commentsColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

    fileNameColumn.prefWidthProperty().bind(tableResults.widthProperty().multiply(0.35));
    fileTypeColumn.prefWidthProperty().bind(tableResults.widthProperty().multiply(0.1));
    pagesColumn.prefWidthProperty().bind(tableResults.widthProperty().multiply(0.1));
    matchesColumn.prefWidthProperty().bind(tableResults.widthProperty().multiply(0.1));
    commentsColumn.prefWidthProperty().bind(tableResults.widthProperty().multiply(0.35));

    fileNameColumn.setResizable(false);
    fileTypeColumn.setResizable(false);
    pagesColumn.setResizable(false);
    matchesColumn.setResizable(false);
    commentsColumn.setResizable(true);
    resetAll();
  }

  @FXML void handleSelect() {
    String directory;

    if (lastDirectory == null)
      directory = tinyTool.HOMEDIR;
    else
      directory = lastDirectory;

    stage = (Stage)labelText.getScene().getWindow();
    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text, CSV, EXCEL, Word, PDF",
      "*.pdf","*.PDF","*.txt","*.TXT","*.csv","*.CSV","*.xls","*.XLS","*.xlsx","*.XLSX","*.doc",
      "*.DOC","*.docx","*.DOCX");
    fileChooser.setInitialDirectory(new File(directory));
    fileChooser.getExtensionFilters().add(extensionFilter);
    fileChooser.setTitle("Select documents to be searched");
    fileList = fileChooser.showOpenMultipleDialog(stage.getOwner());

    if (fileList != null) {
      populateFileList(fileList);
      lastDirectory = fileList.get(0).getParent();
    }
  }

  @FXML void handleSearch() {
    int totalDocuments = 0;
    textAreaFeedback.appendText(utilities.generateFeedback("Starting Search Process"));
    List<File> fileList = listViewFiles.getItems();
    btnCancel.setDisable(false);
    btnSelect.setDisable(true);
    btnExit.setDisable(true);

    for (int i=0; i<fileList.size(); i++)
      totalDocuments++;

    progressBar.progressProperty().unbind();
    progressBar.progressProperty().bind(task.progressProperty());
    progressBar.visibleProperty().setValue(true);
    textAreaFeedback.textProperty().unbind();
    textAreaFeedback.textProperty().bind(task.messageProperty());

    task.searchTask(fileList, textFieldValue.getText(), exactMatch, totalDocuments);
    task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, workerStateEvent -> {
      btnSelect.setDisable(true);
      btnSearch.setDisable(true);
      btnCancel.setText("Done...");
      btnCancel.setDisable(false);
      btnExit.setDisable(true);

      textAreaFeedback.textProperty().unbind();
      textAreaFeedback.appendText(utilities.generateFeedback("\nSearch Done"));
      textAreaFeedback.appendText(utilities.generateFeedback("Clearing Buffers"));
      textAreaFeedback.appendText(utilities.generateFeedback("Closing Files"));
    });

    tableResults.setItems(task.getSearchData());
    new Thread(task).start();
  }

  @FXML void handleExit() {
    stage = (Stage)labelText.getScene().getWindow();
    stage.close();
  }

  private void populateFileList(List<File> fileList) {
    btnCancel.setDisable(true);
    btnSearch.setDisable(true);

    for (File file:fileList)
      listViewFiles.getItems().add(file);

    checkForSearch();
  }

  private void resetAll() {
    fileList = new ArrayList<>();
    populateFileList(fileList);
    listViewFiles.setPlaceholder(new Label("No Documents Selected"));
    tableResults.setPlaceholder(new Label("No Results Yet"));
    fileChooser.getExtensionFilters().clear();
    progressBar.progressProperty().unbind();
    progressBar.setProgress(0);
    progressBar.setVisible(false);
    textAreaFeedback.textProperty().unbind();
    textAreaFeedback.clear();
    textFieldValue.clear();
    btnCancel.setText("Cancel");
    btnSelect.setDisable(false);
    btnCancel.setDisable(true);
    btnSearch.setDisable(true);
    btnExit.setDisable(false);
    btnSelect.requestFocus();
    initializeControls();
  }

  private void initializeControls() {
    listViewFiles.setOnMouseClicked(click -> {
      if (click.getClickCount() == 2) {
        if ((click.getButton() == MouseButton.SECONDARY) && (!listViewFiles.getItems().isEmpty())) {
          int selectedItem = listViewFiles.getSelectionModel().getSelectedIndex();
          listViewFiles.getItems().remove(selectedItem);
        }
      }
    });

    textFieldValue.textProperty().addListener((observable, oldVal, newVal) -> checkForSearch());
    btnCancel.setOnAction(actionEvent -> {
      if (btnCancel.textProperty().getValue().equals("Cancel")) {
        task.cancel(true);
        textAreaFeedback.textProperty().unbind();
        textAreaFeedback.appendText(utilities.generateFeedback("Operation was cancelled"));
        resetAll();
      } else
        handleExit();
    });

    radioBtnExact.setToggleGroup(SearchOptions);
    radioBtnFuzzy.setToggleGroup(SearchOptions);
    SearchOptions.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
      RadioButton rb = (RadioButton)SearchOptions.getSelectedToggle();
      if (rb != null)
        exactMatch = !rb.getText().contains("Fuzzy");
    });
  }

  private void checkForSearch() {
    if (!listViewFiles.getItems().isEmpty() && (!textFieldValue.getText().isEmpty())) {
      btnSearch.setDisable(false);
      btnCancel.setDisable(false);
      textFieldValue.requestFocus();
    } else {
      btnSearch.setDisable(true);
      btnCancel.setDisable(true);
    }
  }
}