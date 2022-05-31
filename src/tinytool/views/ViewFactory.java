/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    December 2021
   @name    tinytool.views.ViewFactory

   @notes   basic class for rendering all the windows
   @changes 2021-12-17 added the centre screen function
 */

package tinytool.views;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tinytool.TinyTool;

import java.io.IOException;

public class ViewFactory {
  private final TinyTool tinyTool = new TinyTool();
  private final String title;
  private final String window;
  private final String icon;
  private final double prefWidth;
  private final double prefHeight;
  private final double maxWidth;
  private final double maxHeight;

  private Stage stage;
  private Parent parent;

  public ViewFactory(String title, String window, String icon, double prefWidth, double prefHeight,
                     double maxWidth, double maxHeight) {
    this.title = title;
    this.window = window;
    this.icon = icon;
    this.prefWidth = prefWidth;
    this.prefHeight = prefHeight;
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
  }

  public void showMainWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  public void showSplitWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  public void showMergeWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  public void showInformationWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  public void showSearchWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  public void showDeclarationWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  public void showTimeSheetWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  public void showAboutWindow() {
    setupWindow();
    stage.show();
    centerWindow();
  }

  private void setupWindow() {
    String views = "views/";
    String icons = tinyTool.IMAGES;
    parent = null;

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(TinyTool.class.getResource(views + window));
      parent = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Scene scene = new Scene(parent);
    stage = new Stage();
    stage.setTitle(title);
    stage.getIcons().add(new Image(icons + icon));
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setMaxWidth(maxWidth);
    stage.setMaxHeight(maxHeight);
    stage.setMinWidth(prefWidth);
    stage.setMinHeight(prefHeight);
    stage.setScene(scene);
  }

  private void centerWindow() {
    Rectangle2D bounds = Screen.getPrimary().getBounds();
    stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
    stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
  }
}