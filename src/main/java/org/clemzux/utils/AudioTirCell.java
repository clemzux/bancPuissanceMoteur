package org.clemzux.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.clemzux.constants.Sizes;

public class AudioTirCell extends ListCell<AudioTir> {

    @Override
    public void updateItem(AudioTir tir, boolean empty) {

        super.updateItem(tir, empty);

        if (tir != null) {
            // nom du tir
            Label nomTir = new Label(tir.getFicName());

            // bouton supprimer tir
            Button deleteButton = new Button();
            Image deleteImage = new Image("img/close.png");
            ImageView deleteImageView = new ImageView(deleteImage);
            deleteImageView.setFitHeight(Sizes.homeWindowHeight * 0.01);
            deleteImageView.setFitWidth(Sizes.homeWindowWidth * 0.006);
            deleteButton.setGraphic(deleteImageView);

            // bouton palette couleur courbe
            Button colorButton = new Button();
            colorButton.setStyle("-fx-background-color: #FE0000");
            colorButton.setPrefSize(Sizes.homeWindowWidth * 0.018, Sizes.homeWindowHeight * 0.01);

            VBox buttonVbox = new VBox(deleteButton, colorButton);

            // image miniature de la courbe du tir
            ImageView graphImageView = new ImageView();
            graphImageView.setFitHeight(Sizes.homeWindowHeight * 0.1);
            graphImageView.setFitWidth(Sizes.homeWindowWidth * 0.1);

            HBox allTirHbox = new HBox(graphImageView, buttonVbox);
            VBox allTirVbox = new VBox(nomTir, allTirHbox);
            setGraphic(allTirVbox);
        }
    }
}