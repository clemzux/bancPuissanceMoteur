package org.clemzux.utils;

import javafx.scene.control.Alert;

public class ShowAlert {

    public static void showMessage(Alert.AlertType type, String title, String header, String content) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
