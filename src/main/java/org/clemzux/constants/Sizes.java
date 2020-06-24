package org.clemzux.constants;

import java.awt.*;

public class Sizes {

    /////////////////////////////////////////
    // home window sizes
    /////////////////////////////////////////

    // taille de la toolbar de windows
    final static GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    final static GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
    final static Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsDevice.getDefaultConfiguration());

    // taille ecran (height sans la toolbar windows)
    public static int homeWindowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int homeWindowHeight = Toolkit.getDefaultToolkit().getScreenSize().height - screenInsets.bottom;


}
