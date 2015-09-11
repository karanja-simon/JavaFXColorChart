/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcolorchart.utils;

import javafx.scene.text.Font;
import jfxcolorchart.JFXColorChart;

/**
 *
 * @author RESEARCH_2
 */
public class UITweak {

    public static void setUIFont() {
        Font.loadFont(JFXColorChart.class.getResourceAsStream("/javafxqrgenerator/fonts/SEGOEUISL.TTF"), 10);
    }
}
