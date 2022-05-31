/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    March 2020
   @name    tinytool.TinyTool

   @notes    main class of the TinyTool application
   @changes  2020-08-15 - rebuilding the entire application to be GUI and not command-line driven
             2021-09-06 - separate PDF tools from documents
             2021-09-11 - added function to clear the working directory
             2022-02-13 - added Document Tools for auto-generation of security declaration
*/

package tinytool;

import javafx.application.Application;
import javafx.stage.Stage;
import tinytool.views.ViewFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TinyTool extends Application {
  public final String HOMEDIR = System.getProperty("user.home");
  public final String WORKDIR = System.setProperty(HOMEDIR, HOMEDIR + "\\TinyTool\\");
  public final String IMAGES  = String.valueOf(TinyTool.class.getResource("/resources/"));

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    if (createDirectory()) {
      ViewFactory factory = new ViewFactory("TinyTool","MainWindow.fxml", "toolbox32.png",
        800,600, 1200,800);
      factory.showMainWindow();
    }
  }

  private boolean createDirectory() {
    boolean created = false;
    String tinytool = "\\TinyTool";
    File directory  = new File(HOMEDIR);
    Path path = Paths.get(directory + tinytool);

    if (!Files.exists(path)) {
      directory = new File(String.valueOf(path));
      created = directory.mkdir();
    } else
      if (Files.exists(path) && Files.isDirectory(path))
        created = true;

    return created;
  }
}