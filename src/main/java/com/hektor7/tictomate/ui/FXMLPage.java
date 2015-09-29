package com.hektor7.tictomate.ui;

import java.net.URL;

/**
 * Created by hector on 27/09/15.
 */
public enum FXMLPage {

    MAIN("/fxml/Main.fxml");

    private String page;

    FXMLPage(String page) {
        this.page = page;
    }

    /**
     * It gets page's URL.
     *
     * @return an URL object
     */
    public URL getPageUrl() {
        return getClass().getResource(this.page);

    }
}

