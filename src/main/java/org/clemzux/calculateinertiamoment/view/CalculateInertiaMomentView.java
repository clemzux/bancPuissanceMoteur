package org.clemzux.calculateinertiamoment.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.clemzux.calculateinertiamoment.controler.CalculateInertiaMomentControler;
import org.clemzux.constants.Constants;
import org.clemzux.constants.Sizes;
import org.clemzux.home.view.HomeView;

public class CalculateInertiaMomentView {

    private Stage primaryStage;
    private Scene scene;

    private HomeView homeView;

    private CheckBox rollTypeCheckbox;

    private Button calculateButton;

    private Label rollLittleRadiusLabel;
    private TextField rollWeightTextField, rollLittleRadiusTextField, rollTallRadiusTextField;


    //////// builder ////////


    public CalculateInertiaMomentView(HomeView view) {

        homeView = view;
        primaryStage = new Stage();

        initializeWidgets();
        windowFinalization();

        // controller & model
        new CalculateInertiaMomentControler(this, homeView);
    }


    //////// methods ////////


    private void initializeWidgets() {

        // type de rouleau
        Label rollTypeLabel = new Label(Constants.strings.getProperty("rollTypeLabel"));
        rollTypeCheckbox = new CheckBox();
        rollTypeCheckbox.setSelected(true);

        // poids du rouleau
        Label rollWeightLabel = new Label(Constants.strings.getProperty("rollWeightLabel"));
        rollWeightTextField = new TextField();

        // petit rayon du rouleau
        rollLittleRadiusLabel = new Label(Constants.strings.getProperty("rollLittleRadiusLabel"));
        rollLittleRadiusTextField = new TextField();

        // grand rayon du rouleau
        Label rollTallRadiusLabel = new Label(Constants.strings.getProperty("rollTallRadiusLabel"));
        rollTallRadiusTextField = new TextField();

        calculateButton = new Button(Constants.strings.getProperty("calculateButton"));

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(25,25,25,25));

        pane.add(rollTypeLabel, 0, 0);
        pane.add(rollTypeCheckbox, 1, 0);
        pane.add(rollWeightLabel, 0, 1);
        pane.add(rollWeightTextField, 1, 1);
        pane.add(rollTallRadiusLabel, 0, 2);
        pane.add(rollTallRadiusTextField, 1, 2);
        pane.add(rollLittleRadiusLabel, 0, 3);
        pane.add(rollLittleRadiusTextField, 1, 3);
        pane.add(calculateButton, 1, 4);

        VBox fullWindowVbox = new VBox(pane);
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

    public CheckBox getRollTypeCheckbox() { return rollTypeCheckbox; }

    public Button getCalculateButton() { return calculateButton; }

    public TextField getRollWeightTextField() { return rollWeightTextField; }

    public TextField getRollLittleRadiusTextField() { return rollLittleRadiusTextField; }

    public TextField getRollTallRadiusTextField() { return rollTallRadiusTextField; }

    public Label getRollLittleRadiusLabel() { return rollLittleRadiusLabel; }
}
