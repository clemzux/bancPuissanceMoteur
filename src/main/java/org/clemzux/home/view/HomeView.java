package org.clemzux.home.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.clemzux.constants.Constants;
import org.clemzux.constants.Sizes;
import org.clemzux.home.controler.HomeController;
import org.clemzux.utils.AudioTir;
import org.clemzux.utils.AudioTirCellFactory;

import java.util.List;


public class HomeView {

    private Stage primaryStage;
    private Scene scene;

    private Button homePhotoButton, homeOverlaysButton, homeOptionsButton, homeStartButton, homeStopButton;
    private Button openTirButton;

    private Canvas canvas;

    private ListView<AudioTir> tirListView;

    private List<AudioTir> audioTirs;


    //////// builder ////////


    public HomeView(Stage pStage) {

        primaryStage = pStage;

        initializeWidgets();
//        widgetsPlacement();
        windowFinalization();

        // controller & model
        new HomeController(this);
    }


    //////// methods ////////


    private void windowFinalization() {

        primaryStage.setTitle(Constants.strings.getProperty("homeWindowName"));
        primaryStage.setResizable(false);

        primaryStage.setWidth(Sizes.homeWindowWidth);
        primaryStage.setHeight(Sizes.homeWindowHeight);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeWidgets() {

        // barre de menu
        Button button = new Button();
        Menu fileMenu = new Menu(Constants.strings.getProperty("homeMenuBarFile"));
        MenuItem fileMenuQuit = new MenuItem(Constants.strings.getProperty("homeMenuBarFileQuit"));
        fileMenu.getItems().addAll(fileMenuQuit);

        Menu editMenu = new Menu(Constants.strings.getProperty("homeMenuBarEdit"));
        Menu aboutMenu = new Menu(Constants.strings.getProperty("homeMenuBarAbout"));

        MenuBar menuBar = new MenuBar(fileMenu, editMenu, aboutMenu);

        // boutons du haut : ouvrir, photo, overlays, options, start, stop ...

        openTirButton = new Button(Constants.strings.getProperty("openTirButton"));
        homePhotoButton = new Button(Constants.strings.getProperty("homePhotoButton"));
        homeOverlaysButton = new Button(Constants.strings.getProperty("homeOverlaysButton"));
        homeOptionsButton = new Button(Constants.strings.getProperty("homeOptionsButton"));
        homeStartButton = new Button(Constants.strings.getProperty("homeStartButton"));
        homeStopButton = new Button(Constants.strings.getProperty("homeStopButton"));

        HBox homeTopButtonHbox = new HBox(openTirButton, homePhotoButton, homeOverlaysButton,
                homeOptionsButton, homeStartButton, homeStopButton);

        // liste contenant tous les tirs
        tirListView = new ListView<>();
        tirListView.setPrefWidth(Sizes.listViewWidth);
        tirListView.setPrefHeight(Sizes.listViewHeight);
        VBox listViewVbox = new VBox(tirListView);

        // canvas ou on va dessiner les courbes
        canvas = new Canvas(Sizes.canvasWidth, Sizes.canvasHeight);

        // a enlever des que le canvas sera fonctionnel
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.AQUA);
        gc.fillRect(0,0,Sizes.canvasWidth,Sizes.canvasHeight);

        drawCanvasAxes();

        // hbox contenant la liste des tirs, le canvas, le compte tours ...
        HBox listCanvasButtonHbox = new HBox(listViewVbox, canvas);

        // ajout des widgets a la scene
        VBox homeFullWindowVbox = new VBox(menuBar, homeTopButtonHbox, listCanvasButtonHbox);
        scene = new Scene(homeFullWindowVbox);
    }

    private void drawCanvasAxes() {

        GraphicsContext gc = canvas.getGraphicsContext2D();


        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        //////////////////////////////
        // on dessine l'axe X
        //////////////////////////////
        double xAxeHeight = Sizes.canvasHeight - (Sizes.canvasHeight * 0.05);
        double xAxeWidth = Sizes.canvasWidth;
        // ligne X
        gc.strokeLine(0, xAxeHeight, xAxeWidth, xAxeHeight);
        // dessin des tirets sur l'axe X, par defaut a l'allumage du programme
        // l'axe comportera 20 tirets => 20 secondes
        // on va decaller le premier et le dernier tiret de l'axe dans le but
        // de ne pas coller le 0 et le 20 contre les bords du canvas


        // on dessine l'axe Y
    }

    public void updateCanvas() {


    }

    public void populateListView(List<AudioTir> audioTirs) {

        this.audioTirs = audioTirs;
        tirListView.getItems().addAll(audioTirs);

        tirListView.setCellFactory(new AudioTirCellFactory());
    }

    public Stage getPrimaryStage() { return primaryStage; }

    public Scene getScene() { return scene; }

    public Button getHomePhotoButton() { return homePhotoButton; }

    public Button getHomeOverlaysButton() { return homeOverlaysButton; }

    public Button getHomeOptionsButton() { return homeOptionsButton; }

    public Button getHomeStartButton() { return homeStartButton; }

    public Button getHomeStopButton() { return homeStopButton; }

    public Button getOpenTirButton() { return openTirButton; }
}
