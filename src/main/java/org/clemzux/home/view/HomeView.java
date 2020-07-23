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

    private Label roundPerMinutesLabel;

    private TextField rollWeightTextField;

    private Canvas canvas;

    private ListView<AudioTir> tirListView;


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

        // on rafraichit le canvas
        drawCanvasContent(null);
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

        ////////////////////////////////////////////////////////////////////////
        // boutons du haut : ouvrir, photo, overlays, options, start, stop ...

        openTirButton = new Button(Constants.strings.getProperty("openTirButton"));
        homePhotoButton = new Button(Constants.strings.getProperty("homePhotoButton"));
        homeOverlaysButton = new Button(Constants.strings.getProperty("homeOverlaysButton"));
        homeOptionsButton = new Button(Constants.strings.getProperty("homeOptionsButton"));
        homeStartButton = new Button(Constants.strings.getProperty("homeStartButton"));
        homeStopButton = new Button(Constants.strings.getProperty("homeStopButton"));

        HBox homeTopButtonHbox = new HBox(openTirButton, homePhotoButton, homeOverlaysButton,
                homeOptionsButton, homeStartButton, homeStopButton);

        ////////////////////////////////////////////////////////////////////////
        // liste contenant tous les tirs
        initTirListView();

        ////////////////////////////////////////////////////////////////////////
        // canvas ou on va dessiner les courbes
        canvas = new Canvas(Sizes.canvasWidth, Sizes.canvasHeight);

        ////////////////////////////////////////////////////////////////////////
        // partie a droite du canvas concernant les parametres
        // TODO compte tours, en attendant il y a un vide
        Label compteToursLabel = new Label("Compte tours");
        Label compteToursNumeriqueLabel = new Label("roundPerMinutesLabel");
        roundPerMinutesLabel = new Label("0");

        Label rollWeightLabel = new Label(Constants.strings.getProperty("rollWeightLabel"));
        rollWeightTextField = new TextField("0");

        VBox parametersVbox = new VBox(compteToursLabel, compteToursNumeriqueLabel, rollWeightLabel, rollWeightTextField);

        VBox listViewVbox = new VBox(tirListView);

        // hbox contenant la liste des tirs, le canvas, le compte tours ...
        HBox listCanvasButtonHbox = new HBox(listViewVbox, canvas, parametersVbox);

        // ajout des widgets a la scene
        VBox homeFullWindowVbox = new VBox(menuBar, homeTopButtonHbox, listCanvasButtonHbox);
        scene = new Scene(homeFullWindowVbox);
    }

    // cette fonction permet de dessiner le canvas :
    // axes, courbes, renseignements
    // cette fonction sera de nouveau appelee a chaque ajout de tir
    public void drawCanvasContent(List<AudioTir> audioTirs) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // on commence par purger le canvas au cas ou il y aurait deja des choses dessinees
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // a enlever des que le canvas sera fonctionnel
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,Sizes.canvasWidth,Sizes.canvasHeight);

        // on determine le nombre de secondes en fonction du tir le plus long
        double nbSeconds = determineSecondsCanvas(audioTirs);
        // on l'incremente de 1 ppour affiche de 0 a 15 par exemple
        nbSeconds += 1;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        //////////////////////////////
        // on dessine l'axe X
        //////////////////////////////
        double xAxeHeight = Sizes.canvasHeight - (Sizes.canvasHeight * 0.05);
        double xAxeWidth = Sizes.canvasWidth;
        // ligne X (secondes)
        gc.strokeLine(0, xAxeHeight, xAxeWidth, xAxeHeight);
        // dessin des tirets sur l'axe X, par defaut a l'allumage du programme
        // par exemple si l'axe comportere 20 tirets => 20 secondes
        // on va decaller le premier et le dernier tiret de l'axe dans le but
        // de ne pas coller le 0 et le 20 contre les bords du canvas
        double firstSecondX = xAxeWidth * 0.01;
        double xLineHeight = xAxeHeight * 0.015;

        // on determine l'ecart entre les tirets de l'axe
        double sizeBetweenXLines = xAxeWidth - (firstSecondX * 2);
        sizeBetweenXLines /= nbSeconds -1;

        double secondsIndex = firstSecondX;

        // on dessine tous les tirets de l'axe X
        for (int index = 0; index < nbSeconds; index++) {

            // on trace les tirets
            gc.strokeLine(secondsIndex, xAxeHeight, secondsIndex, xAxeHeight + xLineHeight);
            gc.setLineWidth(2);
            // on ecrit les secondes en dessous de l'axe
            // le if sert a aligner les secondes suerieures a 10 bien sous le trait
            if (index < 10) {
                gc.strokeText(String.valueOf(index), secondsIndex - (firstSecondX * 0.3), xAxeHeight * 1.04);
            }
            else {
                gc.strokeText(String.valueOf(index), secondsIndex - (firstSecondX * 0.5), xAxeHeight * 1.04);
            }
            gc.setLineWidth(3);
            // on augmente l'ecart entre les tirets
            secondsIndex += sizeBetweenXLines;
        }

        // on dessine l'axe Y - 1 (Newton metre)

        // on dessine l'axe y - 2 (Kilo watt)
    }

    public void updateCanvas() {


    }

    private double determineSecondsCanvas(List<AudioTir> audioTirs) {

        // si la liste est vide ou si on efface tous les tirs, le nombre de sec
        // est par defaut a 15
        if (audioTirs == null || audioTirs.size() == 0) {

            return 15;
        }
        // sinon on recherche le tir le plus long
        else {

            double longestTir = 0;

            for (AudioTir tir : audioTirs) {

                if (tir.getDuration() > longestTir) {
                    longestTir = tir.getDuration();
                }
            }

            return longestTir;
        }
    }

    public void populateListView(List<AudioTir> audioTirs) {

        tirListView.getItems().clear();

        tirListView.getItems().addAll(audioTirs);
        tirListView.setCellFactory(new AudioTirCellFactory());
    }

    private void initTirListView() {

        tirListView = new ListView<>();

        tirListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tirListView.setPrefWidth(Sizes.listViewWidth);
        tirListView.setPrefHeight(Sizes.listViewHeight);
    }

    public Stage getPrimaryStage() { return primaryStage; }

    public Scene getScene() { return scene; }

    public Button getHomePhotoButton() { return homePhotoButton; }

    public Button getHomeOverlaysButton() { return homeOverlaysButton; }

    public Button getHomeOptionsButton() { return homeOptionsButton; }

    public Button getHomeStartButton() { return homeStartButton; }

    public Button getHomeStopButton() { return homeStopButton; }

    public Button getOpenTirButton() { return openTirButton; }

    public ListView<AudioTir> getTirListView() { return tirListView; }
}
