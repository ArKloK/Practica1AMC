/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

import java.util.ArrayList;

/**
 *
 * @author Carlos
 */
public class Camino {

    private ArrayList<Punto> puntos;
    private ArrayList<Double> costePorAvance;
    private double coste;

    public ArrayList<Double> getCostePorAvance() {
        return costePorAvance;
    }

    public void setCostePorAvance(ArrayList<Double> costePorAvance) {
        this.costePorAvance = costePorAvance;
    }

    public ArrayList<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }
}
