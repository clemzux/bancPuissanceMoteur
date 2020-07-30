package org.clemzux.constants;

import java.awt.*;

public class Sizes {

    final static GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    final static GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

    // taille de la toolbar de windows
    final static Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsDevice.getDefaultConfiguration());

    /////////////////////////////////////////
    // home window sizes
    /////////////////////////////////////////

    // taille ecran (height sans la toolbar windows)
    public static int homeWindowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int homeWindowHeight = Toolkit.getDefaultToolkit().getScreenSize().height - screenInsets.bottom;

    // taille listView
    public static double listViewWidth = homeWindowWidth * 0.13;
    public static double listViewHeight = homeWindowHeight * 0.7;

    // taille canvas
    public static double canvasWidth = homeWindowWidth * 0.7;
    public static double canvasHeight = homeWindowHeight * 0.7;

    /////////////////////////////////////////
    // Calculate inertia moment window sizes
    /////////////////////////////////////////

    // taille fenetre
    public static int calculateInertiaMomentWindowWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.3);
    public static int calculateInertiaMomentWindowHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.3 - screenInsets.bottom);
}
