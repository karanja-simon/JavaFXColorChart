/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcolorchart.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import jfxcolorchart.config.Config;

/**
 * FXML Controller class
 *
 * @author RESEARCH2
 */
public class MainUIController implements Initializable {
    @FXML
    private Button btnHome;
    @FXML
    private Button btnPrefs;
    @FXML
    private Button btnAbout;
    @FXML
    private AnchorPane anchorWrapper;
    private final Config config = new Config();
    private boolean loadGeneratorUI = false;

    private void loadHomeUI() {
        if (!loadGeneratorUI) {
            String resource = "/jfxcolorchart/view/ColorUI.fxml";
            config.setHomeUILoaded(true);
            config.loadView(anchorWrapper, resource);
            loadGeneratorUI = true;
        } else {
            config.loadView(anchorWrapper);
        }
    }

    private void loadPreferenceUI() {
        String resource = "/jfxcolorchart/view/SettingUI.fxml";
        config.loadView(anchorWrapper, resource);
    }

    private void loadAboutUI() {
        String resource = "/jfxcolorchart/view/About.fxml";
        config.loadView(anchorWrapper, resource);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadHomeUI();
        btnHome.setOnAction((ActionEvent event) -> {
            loadHomeUI();
        });
        btnPrefs.setOnAction((ActionEvent event) -> {
            loadPreferenceUI();
        });
        btnAbout.setOnAction((ActionEvent event) -> {
            loadAboutUI();
        });

    }

}
