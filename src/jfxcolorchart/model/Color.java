/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcolorchart.model;

/**
 *
 * @author RESEARCH2
 */
public class Color {

    private int No;
    private int R;
    private int G;
    private int B;
    String hex;
    String colColor;

    public String getColColor() {
        return colColor;
    }

    public void setColColor(String colColor) {
        this.colColor = colColor;
    }
    

    public int getNo() {
        return No;
    }

    public void setNo(int No) {
        this.No = No;
    }

    public int getR() {
        return R;
    }

    public void setR(int R) {
        this.R = R;
    }

    public int getG() {
        return G;
    }

    public void setG(int G) {
        this.G = G;
    }

    public int getB() {
        return B;
    }

    public void setB(int B) {
        this.B = B;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

}
