package com.hektor7.tictomate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by hector on 7/11/15.
 */
public class PlatformFactory {
    public static Platform getPlatform() {
        try {
            return (Platform) Class.forName(getPlatformClassName()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(PlatformFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private static String getPlatformClassName() {
        switch (System.getProperty("javafx.platform", "desktop")) {
            case "android":
                return "com.hektor7.tictomate.android.AndroidPlatform";
            case "ios":
                return "com.hektor7.tictomate.ios.IosPlatform";
            default:
                return "com.hektor7.tictomate.desktop.DesktopPlatform";
        }
    }

    public static String getName() {
        switch (System.getProperty("javafx.platform", "desktop")) {
            case "android":
                return "Android";
            case "ios":
                return "iOS";
            default:
                return "Desktop";
        }
    }

    public static boolean isDesktop(){
        return "Desktop".equals(getName());
    }
}
