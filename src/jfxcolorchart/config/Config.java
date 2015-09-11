/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcolorchart.config;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Karanja
 */
public class Config {

    private boolean loadGeneratorUI = false;
    private AnchorPane generatorUI = null;

    public void loadView(AnchorPane parent, String a) {
        try {
            AnchorPane p = (AnchorPane) FXMLLoader.load(getClass().getResource(a));
            AnchorPane.setTopAnchor(p, Double.valueOf(0));
            AnchorPane.setLeftAnchor(p, Double.valueOf(0));
            AnchorPane.setRightAnchor(p, Double.valueOf(0));
            AnchorPane.setBottomAnchor(p, Double.valueOf(0));
            if (loadGeneratorUI) {
                generatorUI = p;
                loadGeneratorUI = false;
            }
            parent.getChildren().setAll(p);
        } catch (IOException e) {

        }
    }

    public void setHomeUILoaded(boolean status) {
        loadGeneratorUI = status;
    }

    public void loadView(AnchorPane parent) {
        parent.getChildren().setAll(generatorUI);
    }
}
