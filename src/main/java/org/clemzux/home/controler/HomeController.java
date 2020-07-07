package org.clemzux.home.controler;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.clemzux.home.model.HomeModel;
import org.clemzux.home.view.HomeView;

public class HomeController {

    private HomeView homeView;
    private HomeModel homeModel;

    public HomeController(HomeView view) {

        homeView = view;
        homeModel = new HomeModel(homeView);

        initListeners();
    }

    private void initListeners() {

        initOpenTirButton(homeView.getOpenTirButton());
    }

    // controller du bouton open tir
    private void initOpenTirButton(Button openTirButton) {

        openTirButton.setOnAction((ActionEvent e) -> {

            homeModel.openTir();
        });
    }
}
