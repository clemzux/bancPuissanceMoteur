package org.clemzux.calculateinertiamoment.model;

import javafx.scene.control.Alert;
import org.clemzux.calculateinertiamoment.view.CalculateInertiaMomentView;
import org.clemzux.constants.Constants;
import org.clemzux.home.view.HomeView;
import org.clemzux.utils.ShowAlert;

import java.util.concurrent.ExecutionException;

public class CalculateInertiaMomentModel {

    private CalculateInertiaMomentView calculateInertiaMomentView;
    private HomeView homeView;

    public CalculateInertiaMomentModel(CalculateInertiaMomentView view, HomeView pHomeView) {

        calculateInertiaMomentView = view;
        homeView = pHomeView;
    }

    public void calculateInertiaMoment() {

        float rollWeight, rollTallRadius, rollLittleRadius;

        boolean isAlertShow = false;
        String alertTitle, header, content;
        content = "";

        try {

            float inertiaMoment = 0;

            rollWeight = Float.valueOf(
                    calculateInertiaMomentView.getRollWeightTextField().getText().replace(",", "."));
            rollTallRadius = Float.valueOf(
                    calculateInertiaMomentView.getRollTallRadiusTextField().getText().replace(",", "."));

            if (calculateInertiaMomentView.getRollTypeCheckbox().isSelected()) {
                rollLittleRadius = Float.valueOf(calculateInertiaMomentView.getRollLittleRadiusTextField().getText());
            }
            else {
                rollLittleRadius = 0;
            }

            // on verifie que les valeurs sont positives
            if (rollWeight <= 0) {

                isAlertShow = true;
                content = Constants.strings.getProperty("rollWeightError");
            }
            else if (rollTallRadius <= 0) {

                isAlertShow = true;
                content = Constants.strings.getProperty("rollTallRadiusError");
            }
            // si on rentre danxs ce if, c'est le calcul du rouleau creux (avec petit rayon)
            else if (calculateInertiaMomentView.getRollTypeCheckbox().isSelected()) {

                if (rollLittleRadius <=0) {

                    isAlertShow = true;
                    content = Constants.strings.getProperty("rollLittleRadiusError");
                }
                else {

                    // on convertit les rayons en m
                    rollTallRadius /= 100;
                    rollLittleRadius /= 100;

                    inertiaMoment = rollTallRadius * rollTallRadius;
                    inertiaMoment += rollLittleRadius * rollLittleRadius;
                    inertiaMoment *= rollWeight;
                    inertiaMoment /= 2;
                }
            }
            // si on arrive dans ce else, on calcule le moment d'un rouleau plein
            else {

                rollTallRadius /= 100;
                inertiaMoment = rollTallRadius * rollTallRadius;
                inertiaMoment *= rollWeight;
                inertiaMoment /= 2;
            }

            homeView.getInertiaMomentTextField().setText(String.valueOf(inertiaMoment));
            calculateInertiaMomentView.getPrimaryStage().close();
        }
        catch (Exception e) {

            isAlertShow = true;
            content = Constants.strings.getProperty("missingValueError");
        }

        if (isAlertShow) {

            alertTitle = Constants.strings.getProperty("inertiaWindowName");
            header = Constants.strings.getProperty("warning");
            ShowAlert.showMessage(Alert.AlertType.WARNING, alertTitle, header, content);
        }
    }

    public void setLittleRadiusVisible(boolean isVisible) {

        calculateInertiaMomentView.getRollLittleRadiusLabel().setVisible(isVisible);
        calculateInertiaMomentView.getRollLittleRadiusTextField().setVisible(isVisible);
    }
}
