package org.clemzux.utils.audiotir;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.clemzux.constants.Sizes;
import org.clemzux.utils.Models;

public class AudioTirCell extends ListCell<AudioTir> {

    @Override
    public void updateItem(AudioTir tir, boolean empty) {

        super.updateItem(tir, empty);

        if (tir != null) {
            // nom du tir
            Label nomTir = new Label(tir.getFicName());

            // bouton supprimer tir
            Button deleteButton = new Button();
            initDeleteButtonListener(deleteButton, tir);
            Image deleteImage = new Image("img/close.png");
            ImageView deleteImageView = new ImageView(deleteImage);
            deleteImageView.setFitHeight(Sizes.homeWindowHeight * 0.01);
            deleteImageView.setFitWidth(Sizes.homeWindowWidth * 0.006);
            deleteButton.setGraphic(deleteImageView);

            initDeleteButtonListener(deleteButton, tir);

            // bouton palette couleur courbe
            ColorPicker colorPicker = new ColorPicker();
            colorPicker.setStyle("-fx-color-label-visible: false ;");
            colorPicker.setValue(tir.getCurveColor());
            initColorButtonListener(colorPicker, tir);

            VBox buttonVbox = new VBox(deleteButton, colorPicker);

            // image miniature de la courbe du tir
            ImageView graphImageView = new ImageView();
            graphImageView.setFitHeight(Sizes.homeWindowHeight * 0.1);
            graphImageView.setFitWidth(Sizes.homeWindowWidth * 0.09);

            HBox allTirHbox = new HBox(graphImageView, buttonVbox);
            VBox allTirVbox = new VBox(nomTir, allTirHbox);
            setGraphic(allTirVbox);
        }
    }

    private void initColorButtonListener (ColorPicker colorPicker, AudioTir tir) {

        colorPicker.setOnAction((ActionEvent e) -> {

            tir.setCurveColor(colorPicker.getValue());
            Models.homeModel.updateListAndCanvas();
        });
    }

    // ce listener s'active quand on clique sur une croix d'un item de la listview
    private void initDeleteButtonListener (Button deleteButton, AudioTir tir) {

        deleteButton.setOnAction((ActionEvent e) -> {

            int i = 0;
            while (i < Models.homeModel.getAudioTirList().size()) {

                if (tir.getFicName().equals(Models.homeModel.getAudioTirList().get(i).getFicName())) {

                    Models.homeModel.getAudioTirList().remove(i);
                    Models.homeModel.updateListAndCanvas();

                    i = Models.homeModel.getAudioTirList().size();
                }
                i++;
            }
        });
    }
}
