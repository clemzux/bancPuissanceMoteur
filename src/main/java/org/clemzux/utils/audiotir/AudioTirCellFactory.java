package org.clemzux.utils.audiotir;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class AudioTirCellFactory implements Callback<ListView<AudioTir>, ListCell<AudioTir>> {

    @Override
    public ListCell<AudioTir> call(ListView<AudioTir> listview)
    {
        return new AudioTirCell();
    }
}
