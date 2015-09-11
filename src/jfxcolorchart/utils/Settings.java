/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcolorchart.utils;

import java.util.prefs.Preferences;

/**
 *
 * @author RESEARCH2
 */
public class Settings {

    private final Preferences prefs = Preferences.userNodeForPackage(getClass());

    public void setIterations(int iterations) {
        prefs.putInt("iteration", iterations);
    }

    public int getIterations() {
        return prefs.getInt("iteration", 50);
    }

}
