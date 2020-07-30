package org.clemzux.calculateinertiamoment.controler;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import org.clemzux.calculateinertiamoment.model.CalculateInertiaMomentModel;
import org.clemzux.calculateinertiamoment.view.CalculateInertiaMomentView;
import org.clemzux.home.view.HomeView;

public class CalculateInertiaMomentControler {

    private CalculateInertiaMomentView calculateInertiaMomentView;
    private CalculateInertiaMomentModel calculateInertiaMomentModel;

    public CalculateInertiaMomentControler(CalculateInertiaMomentView view, HomeView homeView) {

        calculateInertiaMomentView = view;

        calculateInertiaMomentModel = new CalculateInertiaMomentModel(view, homeView);

        initListeners();
    }

    private void initListeners() {

        // listener du bouton calculer
        calculateInertiaMomentView.getCalculateButton().setOnAction((ActionEvent e) -> {

            calculateInertiaMomentModel.calculateInertiaMoment();
        });

        // listener de la checkbox
        calculateInertiaMomentView.getRollTypeCheckbox().selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {

                if (new_val) {
                    calculateInertiaMomentModel.setLittleRadiusVisible(true);
                }
                else {
                    calculateInertiaMomentModel.setLittleRadiusVisible(false);
                }
            }
        });
    }
}
