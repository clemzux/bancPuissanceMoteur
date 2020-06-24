package org.clemzux.home.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.clemzux.constants.Constants;
import org.clemzux.constants.Sizes;

import javax.swing.text.html.ImageView;
import java.awt.*;

public class HomeView {

    private Stage primaryStage;
    private Scene scene;

    private Button homePhotoButton, homeOverlaysButton, homeOptionsButton, homeStartButton, homeStopButton;


    //////// builder ////////


    public HomeView(Stage pStage) {

        primaryStage = pStage;

        initializeWidgets();
//        widgetsPlacement();
        windowFinalization();
    }


    //////// methods ////////


    private void initializeWidgets() {

        // barre de menu
        Button button = new Button();
        Menu fileMenu = new Menu(Constants.strings.getProperty("homeMenuBarFile"));
        MenuItem fileMenuQuit = new MenuItem(Constants.strings.getProperty("homeMenuBarFileQuit"));
        fileMenu.getItems().addAll(fileMenuQuit);

        Menu editMenu = new Menu(Constants.strings.getProperty("homeMenuBarEdit"));
        Menu aboutMenu = new Menu(Constants.strings.getProperty("homeMenuBarAbout"));

        MenuBar menuBar = new MenuBar(fileMenu, editMenu, aboutMenu);

        // boutons du haut : photo, overlays, options, start, stop ...

        homePhotoButton = new Button(Constants.strings.getProperty("homePhotoButton"));
        homeOverlaysButton = new Button(Constants.strings.getProperty("homeOverlaysButton"));
        homeOptionsButton = new Button(Constants.strings.getProperty("homeOptionsButton"));
        homeStartButton = new Button(Constants.strings.getProperty("homeStartButton"));
        homeStopButton = new Button(Constants.strings.getProperty("homeStopButton"));

        HBox homeTopButtonHbox = new HBox(homePhotoButton, homeOverlaysButton, homeOptionsButton, homeStartButton, homeStopButton);

        // ajout des widgets a la scene
        VBox homeFullWindowVbox = new VBox(menuBar, homeTopButtonHbox);
        scene = new Scene(homeFullWindowVbox);
    }

    private void windowFinalization() {

        primaryStage.setTitle(Constants.strings.getProperty("homeWindowName"));
        primaryStage.setResizable(false);

        primaryStage.setWidth(Sizes.homeWindowWidth);
        primaryStage.setHeight(Sizes.homeWindowHeight);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
