package org.clemzux.calculateinertiamoment.model;

import org.clemzux.calculateinertiamoment.view.CalculateInertiaMomentView;
import org.clemzux.home.view.HomeView;

public class CalculateInertiaMomentModel {

    private CalculateInertiaMomentView calculateInertiaMomentView;
    private HomeView homeView;

    public CalculateInertiaMomentModel(CalculateInertiaMomentView view, HomeView pHomeView) {

        calculateInertiaMomentView = view;
        homeView = pHomeView;
    }

    public void calculateInertiaMoment() {

        float rollWeight = Float.valueOf(
                calculateInertiaMomentView.getRollWeightTextField().getText().replace(",", "."));
        float rollTallRadius = Float.valueOf(
                calculateInertiaMomentView.getRollTallRadiusTextField().getText().replace(",", "."));

        float rollLittleRadius;

        if (calculateInertiaMomentView.getRollTypeCheckbox().isSelected()) {
            rollLittleRadius = Float.valueOf(calculateInertiaMomentView.getRollLittleRadiusTextField().getText());
        }
        else {
            rollLittleRadius = 0;
        }

        // on convertit les rayons en m
        rollTallRadius /= 100;
        rollLittleRadius /= 100;

        float inertiaMoment = rollTallRadius * rollTallRadius;
        inertiaMoment += rollLittleRadius * rollLittleRadius;
        inertiaMoment *= rollWeight;
        inertiaMoment /= 2;

        homeView.getInertiaMomentTextField().setText(String.valueOf(inertiaMoment));
    }

    public void setLittleRadiusVisible(boolean isVisible) {

        calculateInertiaMomentView.getRollLittleRadiusLabel().setVisible(isVisible);
        calculateInertiaMomentView.getRollLittleRadiusTextField().setVisible(isVisible);
    }
}
