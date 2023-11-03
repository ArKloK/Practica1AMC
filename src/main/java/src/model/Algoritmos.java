/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Algoritmos {

    int puntosCalculados;

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
}
