package org.clemzux.calculateinertiamoment.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.clemzux.constants.Constants;
import org.clemzux.constants.Sizes;
import org.clemzux.home.controler.HomeController;
import org.clemzux.home.view.HomeView;

public class CalculateInertiaMomentView {

    private Stage primaryStage;
    private Scene scene;

    private HomeView homeView;

    private CheckBox rollTypeCheckbox;

    private Button calculateButton;

    private TextField rollWeightTextField, rollLittleRadiusTextField, rollTallRadiusTextField;


    //////// builder ////////


    public CalculateInertiaMomentView(HomeView view) {

        homeView = view;
        primaryStage = new Stage();

        initializeWidgets();
        windowFinalization();

        // controller & model
    }


    //////// methods ////////


    private void initializeWidgets() {

        // type de rouleau
        Label rollTypeLabel = new Label(Constants.strings.getProperty("rollTypeLabel"));
        rollTypeCheckbox = new CheckBox();
        rollTypeCheckbox.setSelected(false);

        HBox rollTypeHbox = new HBox(rollTypeLabel, rollTypeCheckbox);

        // poids du rouleau
        Label rollWeightLabel = new Label(Constants.strings.getProperty("rollWeightLabel"));
        rollWeightTextField = new TextField();
        HBox rollWeightHbox = new HBox(rollWeightLabel, rollWeightTextField);

        // petit rayon du rouleau
        Label rollLittleRadiusLabel = new Label(Constants.strings.getProperty("rollLittleRadiusLabel"));
        rollLittleRadiusTextField = new TextField();
        HBox rollLittleRadiusHbox = new HBox(rollLittleRadiusLabel, rollLittleRadiusTextField);

        // grand rayon du rouleau
        Label rollTallRadiusLabel = new Label(Constants.strings.getProperty("rollTallRadiusLabel"));
        rollTallRadiusTextField = new TextField();
        HBox rollTallRadiusHbox = new HBox(rollTallRadiusLabel, rollTallRadiusTextField);

        calculateButton = new Button(Constants.strings.getProperty("calculateButton"));

        VBox fullWindowVbox = new VBox(rollTypeHbox, rollWeightHbox, rollLittleRadiusHbox, rollTallRadiusHbox, calculateButton);
        scene = new Scene(fullWindowVbox);
    }

    private void windowFinalization() {

        primaryStage.setTitle(Constants.strings.getProperty("inertiaWindowName"));
        primaryStage.setResizable(false);

        primaryStage.setWidth(Sizes.calculateInertiaMomentWindowWidth);
        primaryStage.setHeight(Sizes.calculateInertiaMomentWindowHeight);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Scene getScene() {
        return scene;
    }
}
