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

    public ArrayList<Punto> vorazBidireccional(ArrayList<Punto> puntos) {

        //tendremos dos variables Punto que irán apuntando a los puntos actuales tanto por la izquierda como por la derecha
        Punto puntoActualIzq = new Punto(), puntoActualDer = new Punto();
        Punto[] puntosaux = new Punto[puntos.size() * 2];
        ArrayList<Punto> visitados = new ArrayList<>(), resultado = new ArrayList<>();
        int n = puntosaux.length;
        int contadorIzq = n / 2, contadorDer = (n / 2) + 1;
        double distanciaMin = Double.MAX_VALUE;

        //Colocamos el punto inicial en el centro del array
        puntoActualIzq = puntosaux[contadorIzq] = puntos.get(0);
        contadorIzq--;

        //Calcular el punto más cercano al inicial
        for (int i = 0; i < puntos.size(); i++) {
            double distancia = calcularDistancia(puntosaux[n / 2], puntos.get(i));
            if (distancia < distanciaMin) {
                distanciaMin = distancia;
                puntoActualDer = puntos.get(i);
            }
        }

        //el punto más cercano al inicial se coloca en la derecha
        puntosaux[contadorDer] = puntoActualDer;
        contadorDer++;

        visitados.add(puntoActualIzq);
        visitados.add(puntoActualDer);

        //Recorremos todo el arraylist para rellenar el array
        for (int i = 2; i < puntos.size(); i++) {
            Punto puntoMasCercanoIzq = new Punto(), puntoMasCercanoDer = new Punto();
            double distanciaMinIzq = Double.MAX_VALUE, distanciaMinDer = Double.MAX_VALUE;

            //Para el punto actual izquierdo comprobamos cual es su punto mas cercano que no haya sido visitado
            for (int j = 0; j < puntos.size(); j++) {
                if (!visitados.contains(puntos.get(j))) {
                    double distancia = calcularDistancia(puntoActualIzq, puntos.get(j));
                    if (distancia < distanciaMinIzq) {
                        distanciaMinIzq = distancia;
                        puntoMasCercanoIzq = puntos.get(j);
                    }
                }
            }

            //Para el punto actual derecho comprobamos cual es su punto mas cercano que no haya sido visitado
            for (int j = 0; j < puntos.size(); j++) {
                if (!visitados.contains(puntos.get(j))) {
                    double distancia = calcularDistancia(puntoActualDer, puntos.get(j));
                    if (distancia < distanciaMinDer) {
                        distanciaMinDer = distancia;
                        puntoMasCercanoDer = puntos.get(j);
                    }
                }
            }

            //Comprobamos cual de las dos distancias calculadas es menor, añadimos ese punto donde corresponde y actualizamos el punto actual
            if (distanciaMinIzq < distanciaMinDer) {
                puntosaux[contadorIzq] = puntoMasCercanoIzq;
                puntoActualIzq = puntoMasCercanoIzq;
                contadorIzq--;
                visitados.add(puntoMasCercanoIzq);
            } else {
                puntosaux[contadorDer] = puntoMasCercanoDer;
                puntoActualDer = puntoMasCercanoDer;
                contadorDer++;
                visitados.add(puntoMasCercanoDer);
            }
        }
        //Para unir los resultados
        for (int i = (n / 2); i > contadorIzq; i--) {
            if (puntosaux[i] != null && puntosaux[i].getId() != 0) {
                resultado.add(puntosaux[i]);
            }
        }
        for (int i = contadorDer; i > (n / 2); i--) {
            if (puntosaux[i] != null && puntosaux[i].getId() != 0) {
                resultado.add(puntosaux[i]);
            }
        }
        //parará cuando el punto de la izquierda y el punto de la derecha sean los mismos
        //para finalizar, reordenamos el array para que el resultado sea ciclico
        return resultado;
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
