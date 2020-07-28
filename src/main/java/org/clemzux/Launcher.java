package org.clemzux;

import javafx.application.Application;
import javafx.stage.Stage;
import org.clemzux.calculateinertiamoment.view.CalculateInertiaMomentView;
import org.clemzux.constants.Constants;
import org.clemzux.home.view.HomeView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Launcher extends Application {

    public static Properties langage;

    public static void main(String[] args) {

        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        // internationalisation
        langage = selectLangage();
        Constants.strings = langage;

        // lancement du programme
        new HomeView(stage);
    }

    private Properties selectLangage() throws IOException {

        // TODO: 24/06/2020 faire une selection de langue
        return loadLangage("resources/stringsFR.properties");
    }

    public Properties loadLangage(String filename) throws IOException, FileNotFoundException {

        Properties properties = new Properties();

        FileInputStream input = new FileInputStream(filename);

        try{

            properties.load(input);
            return properties;
        }
        finally{

            input.close();
        }

    }
}
