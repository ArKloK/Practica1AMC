package src.model;

public class Punto {

    private int id;
    private double x;
    private double y;

    public Punto() {
        super();
    }

    public Punto(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }
    
    public Punto(int id, double x, double y) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
    public double distancia(Punto otro) {
        double distanciaEntrePuntos;
        double distanciaX = this.x - otro.getX();
        double distanciaY = this.y - otro.getY();
        distanciaEntrePuntos = Math.abs(Math.sqrt(Math.pow(distanciaX, 2) + Math.pow(distanciaY, 2)));
        return distanciaEntrePuntos;
    }
    
    @Override
    public String toString() {
        return "Punto{" + "x=" + x + ", y=" + y + '}';
    }
}
