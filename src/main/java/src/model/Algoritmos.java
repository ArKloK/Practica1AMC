/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        int n = puntos.size();
        ArrayList<Punto> tour = new ArrayList<>();
        Punto primerPunto = puntos.get(0);
        tour.add(primerPunto);
        while (tour.size() < puntos.size()) {
            Punto actual = tour.get(tour.size() - 1);
            Punto masCercano = null;
            double distanciaMin = Double.MAX_VALUE;
            int contador = 0;
            System.out.println("Punto actual es: " + actual.getId());
            for (Punto punto : puntos) {
                contador++;
                if (!tour.contains(punto)) {
                    double distancia = calcularDistancia(actual, punto);
                    System.out.println("Distancia del punto: " + actual.getId() + " al " + contador + " es : " + distancia);
                    if (distancia < distanciaMin) {
                        distanciaMin = distancia;
                        System.out.println("Me quedo con: " + distanciaMin);
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

   /* public ArrayList<Punto> vorazBidireccional(ArrayList<Punto> puntos) {
        ArrayList<Punto> tour = new ArrayList<>();
        int c = 0;
        Punto primerPunto = puntos.get(0);
        tour.add(primerPunto);
        ArrayList<Punto> tourFinal = new ArrayList<>();
        ArrayList<Punto> tourPrincipio = new ArrayList<>();
        ArrayList<Punto> tourTotalmenteFinal = new ArrayList<>();
        while (tour.size() < puntos.size() || c < 10) {
            c++;
            Punto actual = tour.get(tour.size() - 1);
            Punto masCercanoInicio = null;
            Punto masCercanoFinal = null;
            double distanciaMinInicio = Double.MAX_VALUE;
            double distanciaMinFinal = Double.MAX_VALUE;
            int contador = 0;
            for (Punto punto : puntos) {
                contador++; 
                if (!tour.contains(punto)) {
                    //double distanciaInicio = actual.distancia(punto);
                    double distanciaInicio = calcularDistancia(actual, punto);
                    //double distanciaFinal = tour.get(0).distancia(punto);
                    double distanciaFinal = calcularDistancia(tour.get(0), punto);
                    if (distanciaInicio < distanciaMinInicio) {
                        distanciaMinInicio = distanciaInicio;
                        masCercanoInicio = punto;
                    }
                    if (distanciaFinal < distanciaMinFinal) {
                        distanciaMinFinal = distanciaFinal;
                        masCercanoFinal = punto;
                    }
                }
            }
            if (distanciaMinInicio < distanciaMinFinal) {
                tourPrincipio.add(masCercanoInicio);
                tour.add(masCercanoInicio);
            } else {
                //Esto esta mal 
                tourFinal.add(masCercanoFinal);
                tour.add(0, masCercanoFinal);
            }
        }
        return tour;
    } 
    public ArrayList<Integer> vorazUnidireccional(ArrayList<Punto> puntos) {
        
        this.puntos = puntos;
        this.n = puntos.size();
        costeSolucion = new double[n];
        visitado = new boolean[n];

        int ciudadActual = 0;
        visitado[ciudadActual] = true;
        ArrayList<Integer> ruta = new ArrayList<>();
        ruta.add(ciudadActual);

        while (ruta.size() < n) {
            ciudadActual = calcularMasCercano(ciudadActual);
            visitado[ciudadActual] = true;
            ruta.add(ciudadActual);
        }

        rutaCompleta = new ArrayList<>(ruta);
        rutaCompleta.add(ruta.get(0)); // Agregar el primer punto al final para completar el ciclo

        costeSolucion[n - 1] = calcularDistancia(puntos.get(n - 1), puntos.get(0));

        return rutaCompleta;
    }
    public ArrayList<Punto> vorazBidireccional(ArrayList<Punto> puntos) {
        int n = puntos.size();
        costeSolucion = new ArrayList<>();
        ArrayList<Punto> masCerca;
        int contador = 0, contadorA = 0, contadorB = 0;
        ArrayList<Punto> tour = new ArrayList<>();

        ArrayList<Punto> ruta = new ArrayList<>();
        ArrayList<Punto> rutaA = new ArrayList<>();
        ArrayList<Punto> rutaB = new ArrayList<>();

        Punto puntoActual = puntos.get(0);
        Punto puntoExtremoA = puntoActual;
        Punto puntoExtremoB = puntoActual;

        ruta.add(contador, puntoActual);
        rutaA.add(contadorA, puntoActual);
        rutaB.add(contadorB, puntoActual);

        tour.add(puntoActual); //AÑADIR PARA VER QUE ESTA VISITADO

        contador++;
        contadorA++;
        contadorB++;

        for (int i = 1; i < n; i++) {
            masCerca = calcularMasCercano(puntos, n, puntoExtremoA, puntoExtremoB, tour, costeSolucion, contador);
            if (masCerca.get(0) == null) {
                puntoExtremoB = masCerca.get(1);
                puntoActual = puntoExtremoB;
                rutaB.add(contadorB, puntoActual);
                contadorB++;
            } else {
                puntoExtremoA = masCerca.get(0);
                puntoActual = puntoExtremoA;
                rutaA.add(contadorA, puntoActual);
                ruta.add(contador, puntoActual);
                contador++;
                contadorA++;
            }
            tour.add(puntoActual);
        }
        for (int i = 1; i <= contadorB; i++) {
            ruta.add(contadorA - 1 + i, rutaB.get(contadorB - i));
        }

        costeSolucion.add(n - 1, calcularDistancia(puntos.get(n - 1), puntos.get(0)));
        return ruta;
    }

    public ArrayList<Punto> calcularMasCercano(ArrayList<Punto> puntos, int size, Punto puntoActualA, Punto puntoActualB, ArrayList<Punto> tour, ArrayList<Double> costeSolucion, int contador) {
        double distanciaMinA = Integer.MAX_VALUE;
        double distanciaMinB = Integer.MAX_VALUE;
        ArrayList<Punto> masCerca = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!tour.contains(puntos.get(i)) && calcularDistancia(puntos.get(i), puntoActualA) < distanciaMinA) {
                distanciaMinA = calcularDistancia(puntos.get(i), puntoActualA);
                masCerca.add(0, puntos.get(i));
            }
        }
        for (int j = 0; j < size; j++) {
            if (!tour.contains(puntos.get(j)) && calcularDistancia(puntos.get(j), puntoActualB) < distanciaMinB) {
                distanciaMinB = calcularDistancia(puntos.get(j), puntoActualB);
                masCerca.add(1, puntos.get(j));
            }
        }
        if (distanciaMinA < distanciaMinB) {
            masCerca.add(1, null);
            costeSolucion.add(contador - 1, distanciaMinA);
        } else {
            masCerca.add(0, null);
            costeSolucion.add(contador - 1, distanciaMinB);
        }
        return masCerca;
    }

    public double calcularCosteTotal() {
        double costeTotal = 0.0;
        for (int i = 0; i < n - 1; i++) {
            costeTotal += calcularDistancia(puntos.get(rutaCompleta.get(i)), puntos.get(rutaCompleta.get(i + 1)));
        }
        return costeTotal;
    }
*/
    private double calcularDistancia(Punto p1, Punto p2) {
        double[][] matriz = {{-1, -1, -1, -1, -1, -1},
                            {-1, -1, 12, 30, 100, 10},
                            {-1, 12, -1, 5, 13, -1},
                            {-1, 30, 5, -1, 15, -1},
                            {-1, 100, 13, 15, -1, 10},
                            {-1, 10, -1, -1, 10, -1}};
        if (matriz[p1.getId()][p2.getId()] == -1) {
            matriz[p1.getId()][p2.getId()] = Double.MAX_VALUE;
        }
        return matriz[p1.getId()][p2.getId()];
    }

    /*private double calcularDistancia(Punto p1, Punto p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    private int calcularMasCercano(int ciudadActual) {
        int masCercano = -1;
        double distanciaMinima = Double.MAX_VALUE;
        Punto puntoActual = puntos.get(ciudadActual);
        System.out.println("Punto " + ciudadActual);
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                double distancia = calcularDistancia(puntoActual, puntos.get(i));
                System.out.println("La distancia al punto " + i + " es de " + distancia);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    masCercano = i;
                }
            }
        }
        return masCercano;
    }*/
}
