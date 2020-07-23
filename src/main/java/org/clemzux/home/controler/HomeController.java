package org.clemzux.home.controler;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.clemzux.home.model.HomeModel;
import org.clemzux.home.view.HomeView;
import org.clemzux.utils.audiotir.AudioTir;

public class HomeController {

    private HomeView homeView;
    private HomeModel homeModel;

    public HomeController(HomeView view) {

        homeView = view;
        homeModel = new HomeModel(homeView);

        initListeners();
    }

    private void initListeners() {

        initOpenTirButtonListener();
        initListViewListener();
    }

    private void initListViewListener() {

        ListView<AudioTir> tirListView = homeView.getTirListView();

        tirListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AudioTir>() {

            @Override
            public void changed(ObservableValue<? extends AudioTir> observableValue, AudioTir oldTir, AudioTir tir) {

                System.out.println("Tir selectionne : " + tir.getFicName());
            }
        });
    }

    // listener du bouton open tir
    private void initOpenTirButtonListener() {

        Button openTirButton = homeView.getOpenTirButton();
        openTirButton.setOnAction((ActionEvent e) -> {

            homeModel.openTir();
        });
    }
}
