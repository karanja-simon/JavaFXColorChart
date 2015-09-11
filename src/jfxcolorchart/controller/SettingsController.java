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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import jfxcolorchart.utils.Settings;

/**
 * FXML Controller class
 *
 * @author RESEARCH2
 */
public class SettingsController implements Initializable {

    @FXML
    private TextField txtIterations;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblMsg;
    private final Settings pref = new Settings();
    private int iterations = 0;

    private boolean validateEntry() {
        try {
            iterations = Integer.parseInt(txtIterations.getText());
            if (iterations > 255 || iterations < 0) {
                lblMsg.setOpacity(1.0);
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Ensure your input range is between 0 and 255!");
                return false;
            }
        } catch (NumberFormatException nfe) {
            lblMsg.setOpacity(1.0);
            lblMsg.setStyle("-fx-text-fill: red;");
            lblMsg.setText("Ensure your input is a number!");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblMsg.setOpacity(0);
        btnSave.setOnAction((ActionEvent event) -> {
            if (validateEntry()) {
                pref.setIterations(iterations);
                lblMsg.setOpacity(1.0);
                lblMsg.setStyle("-fx-text-fill: green;");
                lblMsg.setText("Saved. Please restart the app for changes to take effect!");
            }
        });
    }

}
