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
import org.clemzux.utils.audiotir.AudioTir;
import org.clemzux.utils.audiotir.AudioTirCellFactory;

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
    // axes (sec, Nm, Kw), courbes, renseignements
    // cette fonction sera de nouveau appelee a chaque ajout de tir
    public void drawCanvasContent(List<AudioTir> audioTirs) {

        if (audioTirs !=null)
        for (AudioTir t : audioTirs) {
            System.out.println(t.getCurveColor().toString());
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // on commence par purger le canvas au cas ou il y aurait deja des choses dessinees
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // a enlever des que le canvas sera fonctionnel
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,Sizes.canvasWidth,Sizes.canvasHeight);

        // on determine le nombre de secondes en fonction du tir le plus long
        // on l'incremente de 1 ppour afficher de 0 a 15 par exemple
        int nbSeconds = (int) determineSecondsInCanvas(audioTirs) + 1;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        //////////////////////////////
        // on dessine l'axe X
        //////////////////////////////

        int xAxeHeight = (int) (Sizes.canvasHeight - (Sizes.canvasHeight * 0.05));
        int xAxeWidth = (int) Sizes.canvasWidth;

        // ligne X (secondes)
        gc.strokeLine(0, xAxeHeight, xAxeWidth, xAxeHeight);
        // dessin des tirets sur l'axe X, par defaut a l'allumage du programme
        // par exemple si l'axe comportere 20 tirets => 20 secondes
        // on va decaller le premier et le dernier tiret de l'axe dans le but
        // de ne pas coller le 0 et le 20 contre les bords du canvas
        int firstSecondX = (int) (xAxeWidth * 0.03);
        int xLineHeight = (int) (xAxeHeight * 0.015);

        // on determine l'ecart entre les tirets de l'axe
        int sizeBetweenXLines = (xAxeWidth - (firstSecondX * 2));
        sizeBetweenXLines /= nbSeconds;

        int secondsIndex = firstSecondX;

        // on dessine tous les tirets de l'axe X
        for (int index = 0; index <= nbSeconds; index++) {

            // on met la largeur des traits a 3px pour tracer les traits
            gc.setLineWidth(3);

            // on trace les tirets
            gc.strokeLine(secondsIndex, xAxeHeight, secondsIndex, xAxeHeight + xLineHeight);
            // on change la largeur des traits pour ecrire des lettres lisibles
            gc.setLineWidth(1);
            // on ecrit les nombre a cote de l'axe
            // le if sert a aligner les nombres superieurs a 10 bien a cote du trait
            if (index < 10) {
                gc.strokeText(String.valueOf(index), secondsIndex - (firstSecondX * 0.1), xAxeHeight * 1.04);
            }
            else {
                gc.strokeText(String.valueOf(index), secondsIndex - (firstSecondX * 0.25), xAxeHeight * 1.04);
            }

            // on trace les lines en pointilles a chaque seconde verticalement
            gc.setStroke(Color.SLATEGRAY);
            gc.setLineWidth(0.7);
            gc.setLineDashes(Sizes.canvasHeight * 0.01);
            gc.strokeLine(secondsIndex, xAxeHeight, secondsIndex, 0);
            gc.setStroke(Color.BLACK);
            gc.setLineDashes(0);

            // on augmente l'ecart entre les tirets
            secondsIndex += sizeBetweenXLines;
        }

        // on ecrit l'unite de l'axe
        gc.setLineWidth(1);
        secondsIndex -= sizeBetweenXLines;
        secondsIndex += xAxeWidth * 0.015;
        gc.strokeText(Constants.strings.getProperty("seconds"),
                secondsIndex - (firstSecondX * 0.1), xAxeHeight * 1.04);

        gc.setLineWidth(3);

        System.out.println("\n\n");

        ///////////////////////////////////////
        // on dessine l'axe des newton metre
        ///////////////////////////////////////

        // cette variable represente la base de l'axe de Nm
        int newtonAxeWidth = firstSecondX;
        int newtonAxeHeight = xAxeHeight;

        // ligne representant l'axe
        gc.strokeLine(newtonAxeWidth, newtonAxeHeight, newtonAxeWidth, 0);

        // nombre de pas affiches a l'ecran (va de 0 a 15)
        int newtonStep = 16;

        int firstNewtonStep = newtonAxeHeight;
        int newtonStepIndex = firstNewtonStep;
        int sizeBetweenNewtonStep = newtonAxeHeight / newtonStep;

        newtonStepIndex -= sizeBetweenNewtonStep;

        for (int index = 1; index < newtonStep; index++) {

            gc.strokeLine(newtonAxeWidth, newtonStepIndex, newtonAxeWidth - Sizes.canvasWidth * 0.005, newtonStepIndex);

            // on change la largeur des trait pour ecrire des lettres lisibles
            gc.setLineWidth(1);

            if (index < 10) {
                gc.strokeText(String.valueOf(index), newtonAxeWidth * 0.45, newtonStepIndex + newtonAxeHeight * 0.0095);
            }
            else {
                gc.strokeText(String.valueOf(index), newtonAxeWidth * 0.2, newtonStepIndex + newtonAxeHeight * 0.0095);
            }

            // on remet la largeur des traits a 3px pour tracer les traits
            gc.setLineWidth(3);

            // on trace les lines en pointilles a chaque newton metre horizontalement
            gc.setStroke(Color.SLATEGRAY);
            gc.setLineWidth(0.7);
            gc.setLineDashes(Sizes.canvasHeight * 0.01);
            gc.strokeLine(newtonAxeWidth, newtonStepIndex, secondsIndex, newtonStepIndex);
            gc.setStroke(Color.BLACK);
            gc.setLineDashes(0);


            // on decremente pour le prochain tiret
            newtonStepIndex -= sizeBetweenNewtonStep;
        }

        // on ecrit l'unite de l'axe
        gc.setLineWidth(1);
        gc.strokeText(Constants.strings.getProperty("newtonMetre"),
                newtonAxeWidth * 0.2, newtonStepIndex + newtonAxeHeight * 0.0095);

        gc.setLineWidth(3);

        ///////////////////////////////////////
        // on dessine l'axe des kilo watts
        ///////////////////////////////////////

        // cette variable represente la base de l'axe des Kw
        int kiloWattAxeWidth = firstSecondX + nbSeconds * sizeBetweenXLines;
        int kiloWattAxeHeight = xAxeHeight;

        // ligne representant l'axe
        gc.strokeLine(kiloWattAxeWidth, kiloWattAxeHeight, kiloWattAxeWidth, 0);

        int kiloWattStep = 16;
        int firstKiloWattStep = kiloWattAxeHeight;
        int sizeBetweenKiloWattStep = kiloWattAxeHeight / kiloWattStep;
        int kiloWattStepIndex = firstKiloWattStep;

        kiloWattStepIndex -= sizeBetweenKiloWattStep;

        for (int index = 1; index < kiloWattStep; index++) {

            gc.strokeLine(kiloWattAxeWidth, kiloWattStepIndex, kiloWattAxeWidth + Sizes.canvasWidth * 0.005 , kiloWattStepIndex);

            // on change la largeur des trait pour ecrire des lettres lisibles
            gc.setLineWidth(1);

            gc.strokeText(String.valueOf(index),
                    kiloWattAxeWidth + Sizes.canvasWidth * 0.01, kiloWattStepIndex + kiloWattAxeHeight * 0.0095);

            // on remet la largeur des traits a 3px pour tracer les traits
            gc.setLineWidth(3);

            kiloWattStepIndex -= sizeBetweenKiloWattStep;
        }

        // on ecrit l'unite de l'axe
        gc.setLineWidth(1);
        gc.strokeText(Constants.strings.getProperty("kiloWatt"),
                kiloWattAxeWidth + Sizes.canvasWidth * 0.01, kiloWattStep + kiloWattAxeHeight * 0.0095);

        gc.setLineWidth(3);
    }

    private double determineNewtonMax(List<AudioTir> audioTirs) {
        // TODO: 23/07/2020  dssd

        return 100;
    }

    private double determineSecondsInCanvas(List<AudioTir> audioTirs) {

        // si la liste est vide ou si on efface tous les tirs, le nombre de sec
        // est par defaut a 14 pour faire un axe de 15 tirets
        if (audioTirs == null || audioTirs.size() == 0) {

            return 14;
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

    public void updateCanvas(List<AudioTir> audioTirs) {

        drawCanvasContent(audioTirs);
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
