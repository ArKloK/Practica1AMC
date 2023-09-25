/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

/**
 *
 * @author usuario
 */
public class Linea {

    private Punto p1;
    private Punto p2;
    
    public Linea(){
        super();
    }

    public Linea(Punto p1, Punto p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
    }
    
    
    /**
     * @return the p1
     */
    public Punto getP1() {
        return p1;
    }

    /**
     * @param p1 the p1 to set
     */
    public void setP1(Punto p1) {
        this.p1 = p1;
    }

    /**
     * @return the p2
     */
    public Punto getP2() {
        return p2;
    }

    /**
     * @param p2 the p2 to set
     */
    public void setP2(Punto p2) {
        this.p2 = p2;
    }
    
    public double distancia(Punto p2){
        double distanciaX = p2.getX() - this.p1.getX();
        double distanciaY = p2.getY() - this.p1.getY();
        return Math.sqrt(Math.pow(distanciaX, 2) + Math.pow(distanciaY, 2));
    }

    @Override
    public String toString() {
        return "Linea{" + "p1=" + p1 + ", p2=" + p2 + '}';
    }
    
    

}
