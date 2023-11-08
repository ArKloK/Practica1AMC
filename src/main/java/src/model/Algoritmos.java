/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class Algoritmos {

    private ArrayList<Punto> puntos;
    private ArrayList<Integer> rutaCompleta;
    int puntosCalculados;
    private int n;
    private ArrayList<Double> costeSolucion;
    private boolean[] visitado;

    public void setPuntosCalculados(int pc) {
        this.puntosCalculados = pc;
    }

    public int getPuntosCalculados() {
        return this.puntosCalculados;
    }

    //****************************************ALGORITMOS DE ORDENACIÓN**********************************************
    public ArrayList<Punto> quicksortX(ArrayList<Punto> puntosaux, int primero, int ultimo) {
        if (primero < ultimo) {
            Punto pivote = puntosaux.get(ultimo);
            int posicion = partitionX(puntosaux, primero, ultimo, pivote);
            quicksortX(puntosaux, primero, posicion - 1);
            quicksortX(puntosaux, posicion + 1, ultimo);
        }

        return puntosaux;
    }

    public int partitionX(ArrayList<Punto> puntosaux, int primero, int ultimo, Punto pivote) {
        int i = primero;
        int j = primero;

        while (i <= ultimo) {
            if (puntosaux.get(i).getX() > pivote.getX()) {
                i++;
            } else {
                intercambiar(puntosaux, i, j);
                i++;
                j++;
            }
        }

        return j - 1;
    }

    public ArrayList<Punto> quicksortY(ArrayList<Punto> puntosaux, int primero, int ultimo) {
        if (primero < ultimo) {
            Punto pivote = puntosaux.get(ultimo);
            int posicion = partitionY(puntosaux, primero, ultimo, pivote);
            quicksortY(puntosaux, primero, posicion - 1);
            quicksortY(puntosaux, posicion + 1, ultimo);
        }

        return puntosaux;
    }

    public int partitionY(ArrayList<Punto> puntosaux, int primero, int ultimo, Punto pivote) {
        int i = primero;
        int j = primero;

        while (i <= ultimo) {
            if (puntosaux.get(i).getY() > pivote.getY()) {
                i++;
            } else {
                intercambiar(puntosaux, i, j);
                i++;
                j++;
            }
        }

        return j - 1;
    }

    public void intercambiar(ArrayList<Punto> puntosaux, int i, int j) {
        Punto temp = puntosaux.get(i);
        puntosaux.set(i, puntosaux.get(j));
        puntosaux.set(j, temp);
    }

    //****************************************ALGORITMOS DE BUSQUEDA**********************************************
    public Linea exhaustivo(ArrayList<Punto> puntos) {
        //Declaración de variables
        double mejorCamino = Double.MAX_VALUE;
        Linea mejorLinea = new Linea();
        //Empezamos la busqueda
        for (int i = 0; i < puntos.size(); i++) {
            for (int j = i + 1; j < puntos.size(); j++) {
                Linea l = new Linea(puntos.get(i), puntos.get(j));
                puntosCalculados++;
                if (l.distancia() < mejorCamino) {
                    mejorCamino = l.distancia();
                    mejorLinea = l;
                }
            }
        }
        return mejorLinea;
    }

    public Linea exhaustivoPoda(ArrayList<Punto> puntos) {
        //Declaración de variables
        Linea mejorLinea = new Linea();
        Linea actualLinea;
        Double distanciaMin;

        //Asignamos los puntos que vamos a seleccionar en la primera iteración, definimos la linea que une a esos puntos y calculamos la distancia minima
        Punto punto1 = puntos.get(0);
        Punto punto2 = puntos.get(1);
        actualLinea = new Linea(punto1, punto2);
        distanciaMin = actualLinea.distancia();

        for (int i = 0; i < puntos.size() - 1; i++) {
            Punto puntoBase = puntos.get(i);
            for (int j = i + 1; j < puntos.size(); j++) {
                Punto puntoActual = puntos.get(j);
                actualLinea = new Linea(puntoBase, puntoActual);
                puntosCalculados++;

                //Comprobamos si la distancia minima tiene que ser actualizada
                if (actualLinea.distancia() < distanciaMin) {
                    distanciaMin = actualLinea.distancia();
                    mejorLinea = actualLinea;
                    //punto1 = puntoBase;
                    //punto2 = puntoActual;
                }

                //Poda
                if (Math.abs(puntoBase.getX() - puntoActual.getX()) >= distanciaMin) {
                    break;
                }
            }
        }

        return mejorLinea;
    }

    public Linea divideyvenceras(ArrayList<Punto> puntos, int izquierda, int derecha) {
        Linea mejorLinea = null;
        if (derecha - izquierda <= 3) {
            double minDistancia = Double.MAX_VALUE;
            // Caso base: pocos puntos, calcular por fuerza bruta
            for (int i = izquierda; i <= derecha; i++) {
                for (int j = i + 1; j <= derecha; j++) {
                    Linea actualLinea = new Linea(puntos.get(i), puntos.get(j));
                    double distancia = actualLinea.distancia();
                    puntosCalculados++;
                    if (distancia < minDistancia) {
                        minDistancia = distancia;
                        mejorLinea = actualLinea;
                    }
                }
            }
            return mejorLinea;
        }

        int medio = (izquierda + derecha) / 2;
        Punto puntoMedio = puntos.get(medio);

        // Recursión en las mitades izquierda y derecha
        Linea lineaIzquierda = divideyvenceras(puntos, izquierda, medio);
        Linea lineaDerecha = divideyvenceras(puntos, medio, derecha);
        double distanciaMin;
        // Elegir la línea más corta de las dos
        if (lineaIzquierda.distancia() <= lineaDerecha.distancia()) {
            distanciaMin = lineaIzquierda.distancia();
            mejorLinea = lineaIzquierda;
        } else {
            distanciaMin = lineaDerecha.distancia();
            mejorLinea = lineaDerecha;
        }

        // Crear una lista de puntos en la banda de distanciaMin
        ArrayList<Punto> puntosEnRango = new ArrayList<>();
        for (int i = izquierda; i <= derecha; i++) {
            if (Math.abs(puntos.get(i).getX() - puntoMedio.getX()) < distanciaMin) {
                puntosEnRango.add(puntos.get(i));
            }
        }

        // Ordenar la lista de puntos en la banda por coordenada Y
        puntosEnRango = quicksortY(puntosEnRango, 0, puntosEnRango.size() - 1);
        // Búsqueda en la banda de distanciaMin
        for (int i = 0; i < puntosEnRango.size(); i++) {
            for (int j = i + 1; j < puntosEnRango.size() && (puntosEnRango.get(j).getY() - puntosEnRango.get(i).getY()) < distanciaMin; j++) {
                Linea l = new Linea(puntosEnRango.get(i), puntosEnRango.get(j));
                if (l.distancia() < distanciaMin) {
                    distanciaMin = l.distancia();
                    mejorLinea = l;
                }
            }
        }
        return mejorLinea;
    }

    public Linea dyvMejorado(ArrayList<Punto> puntosOrdenados, int inicio, int fin) {
        Linea mejorLinea = null;
        if (fin - inicio <= 3) {
            double distanciaMin = Double.POSITIVE_INFINITY;
            // Caso base: Fuerza bruta para un número pequeño de puntos
            for (int i = inicio; i < fin - 1; i++) {
                for (int j = i + 1; j < fin; j++) {
                    Linea actualLinea = new Linea(puntosOrdenados.get(i), puntosOrdenados.get(j));
                    double distancia = actualLinea.distancia();
                    puntosCalculados++;
                    if (distancia < distanciaMin) {
                        distanciaMin = distancia;
                        mejorLinea = actualLinea;
                    }
                }
            }

            return mejorLinea;
        }

        // Divide los puntos en dos mitades
        int mitad = (inicio + fin) / 2;
        Punto puntoMedio = puntosOrdenados.get(mitad);

        Linea lineaIzquierda = dyvMejorado(puntosOrdenados, inicio, mitad);
        Linea lineaDerecha = dyvMejorado(puntosOrdenados, mitad, fin);

        double distanciaMin;
        // Elegir la línea más corta de las dos
        if (lineaIzquierda.distancia() <= lineaDerecha.distancia()) {
            distanciaMin = lineaIzquierda.distancia();
            mejorLinea = lineaIzquierda;
        } else {
            distanciaMin = lineaDerecha.distancia();
            mejorLinea = lineaDerecha;
        }

        // Filtrar los puntos en la franja por coordenada x
        List<Punto> puntosEnRango = new ArrayList<>();
        for (int i = inicio; i < fin; i++) {
            if (Math.abs(puntosOrdenados.get(i).getX() - puntoMedio.getX()) < distanciaMin) {
                puntosEnRango.add(puntosOrdenados.get(i));
            }
        }

        // Ordenar los puntos en la franja por coordenada y
        Collections.sort(puntosEnRango, (p1, p2) -> Double.compare(p1.getY(), p2.getY()));

        // Búsqueda de pares cercanos en la franja
        for (int i = 0; i < puntosEnRango.size() - 1; i++) {
            for (int j = i + 1; j < puntosEnRango.size() && j - i < 12; j++) {
                Linea linea = new Linea(puntosEnRango.get(i), puntosEnRango.get(j));
                double distancia = linea.distancia();
                if (distancia < distanciaMin) {
                    distanciaMin = distancia;
                    mejorLinea = linea;
                }
            }
        }

        return mejorLinea;
    }

    //****************************************ALGORITMOS VORACES**********************************************
    public ArrayList<Punto> vorazUnidireccional(ArrayList<Punto> puntos) {
        double distanciaTotal = 0;
        ArrayList<Punto> tour = new ArrayList<>();
        Punto primerPunto = puntos.get(0);
        tour.add(primerPunto);
        while (tour.size() < puntos.size()) {
            Punto actual = tour.get(tour.size() - 1);
            Punto masCercano = null;
            double distanciaMin = Double.MAX_VALUE;
            for (Punto punto : puntos) {
                if (!tour.contains(punto)) {
                    double distancia = calcularDistancia(actual, punto);
                    if (distancia < distanciaMin) {
                        distanciaMin = distancia;
                        masCercano = punto;
                    }
                }
            }
            distanciaTotal = distanciaTotal + distanciaMin;
            if (masCercano != null) {
                tour.add(masCercano);
            }
        }
        tour.add(tour.get(0));
        System.out.println("Distancia total: " + distanciaTotal);
        return tour;
    }

    //El nodo inicial va a moverse al hijo mas cercano y al segundo mas cercano
    //En cada ruta parcial se va a realizar un algoritmo unidireccional hasta llegar al nodo inicial
    //Una vez que se ha realizado el proceso por cada una de las rutas parciales, comparamos los costes y nos quedamos con la que menos coste tenga
    public ArrayList<Punto> vorazBidireccional(ArrayList<Punto> puntos) {
        ArrayList<Punto> rutaResult = new ArrayList<>();
        ArrayList<Punto> visitadosDer = new ArrayList<>();
        ArrayList<Punto> visitadosIzq = new ArrayList<>();
        double distanciaMinIzq = Double.MAX_VALUE;
        double distanciaMinDer = Double.MAX_VALUE;

        double costeIzq = 0, costeDer = 0;

        Punto puntoIzq = new Punto();
        Punto puntoDer = new Punto();

        //Añadimos tanto en los arrays visitados como en los de ruta el punto inicial
        visitadosDer.add(puntos.get(0));
        visitadosIzq.add(puntos.get(0));

        //Calculamos los dos puntos más cercanos al punto inicial
        for (int i = 0; i < puntos.size(); i++) {
            double distancia = calcularDistancia(puntos.get(0), puntos.get(i));
            if (distancia < distanciaMinIzq) {
                distanciaMinIzq = distancia;
                puntoIzq = puntos.get(i);
            }
        }

        //Asignamos el punto más cercano a la rutaIzq y el segundo punto más cercano a la rutaDer
        visitadosIzq.add(puntoIzq);

        for (int i = 0; i < puntos.size(); i++) {
            if (!visitadosIzq.contains(puntos.get(i))) {
                double distancia = calcularDistancia(puntos.get(0), puntos.get(i));
                if (distancia < distanciaMinDer) {
                    distanciaMinDer = distancia;
                    puntoDer = puntos.get(i);
                }
            }

        }

        visitadosDer.add(puntoDer);

        System.out.println("Mas cercano : " + puntoIzq);
        System.out.println("Mas cercano 2 : " + puntoDer);

        while (visitadosIzq.size() < puntos.size()) {
            Punto puntoActual = visitadosIzq.get(visitadosIzq.size() - 1);
            System.out.println("PUNTO ACTUAL " + puntoActual.getId());
            Punto puntoMasCercano = new Punto();
            double distanciaMin = Double.MAX_VALUE;

            for (int i = 0; i < puntos.size(); i++) {
                if (!visitadosIzq.contains(puntos.get(i))) {
                    double distancia = calcularDistancia(puntoActual, puntos.get(i));
                    if (distancia < distanciaMin) {
                        distanciaMin = distancia;
                        puntoMasCercano = puntos.get(i);
                        System.out.println("PUNTO MAS CERCANO " + puntoMasCercano);
                    }
                }
            }
            costeIzq += distanciaMin;
            visitadosIzq.add(puntoMasCercano);
        }

        costeIzq += calcularDistancia(visitadosIzq.get(visitadosIzq.size() - 1), visitadosIzq.get(0));
        visitadosIzq.add(visitadosIzq.get(0));

        while (visitadosDer.size() < puntos.size()) {
            Punto puntoActual = visitadosDer.get(visitadosDer.size() - 1);
            System.out.println("PUNTO ACTUAL " + puntoActual.getId());
            Punto puntoMasCercano = new Punto();
            double distanciaMin = Double.MAX_VALUE;

            for (int i = 0; i < puntos.size(); i++) {
                if (!visitadosDer.contains(puntos.get(i))) {
                    double distancia = calcularDistancia(puntoActual, puntos.get(i));
                    if (distancia < distanciaMin) {
                        distanciaMin = distancia;
                        puntoMasCercano = puntos.get(i);
                        System.out.println("PUNTO MAS CERCANO " + puntoMasCercano);
                    }
                }
            }
            costeDer += distanciaMin;
            visitadosDer.add(puntoMasCercano);
        }

        costeDer += calcularDistancia(visitadosDer.get(visitadosDer.size() - 1), visitadosDer.get(0));
        visitadosDer.add(visitadosDer.get(0));

        if (costeIzq < costeDer) {
            rutaResult = visitadosIzq;
        } else {
            rutaResult = visitadosDer;
        }

        return rutaResult;
    }

    private double calcularDistancia(Punto p1, Punto p2) {
        if (p1 == p2) {
            return Double.MAX_VALUE;
        }
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /*private double calcularDistancia(Punto p1, Punto p2) {
        double[][] matriz = {
            {-1, -1, -1, -1, -1, -1},
            {-1, -1, 12, 30, 100, 10},
            {-1, 12, -1, 5, 13, -1},
            {-1, 30, 5, -1, 15, -1},
            {-1, 100, 13, 15, -1, 10},
            {-1, 10, -1, -1, 10, -1}};
        if (matriz[p1.getId()][p2.getId()] == -1) {
            matriz[p1.getId()][p2.getId()] = Double.MAX_VALUE;
        }
        return matriz[p1.getId()][p2.getId()];
    }*/

}
