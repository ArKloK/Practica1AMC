/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package src.controller;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import src.model.Linea;
import src.model.Punto;

/**
 *
 * @author Usuario
 */
@WebServlet(name = "AlgoritmosController", urlPatterns = {"/AlgoritmosController/*"})
public class AlgoritmosController extends HttpServlet {

    private String rutaDelProyecto;
    private File file;
    private boolean isPeorCaso;

    //****************************************MANEJO DE FICHERO**********************************************
    public ArrayList<Punto> leerPuntos(File archivo) {
        ArrayList<Punto> puntos = new ArrayList<>();
        if (archivo.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));

                String line;
                boolean isParsingCoordinates = false;

                while ((line = reader.readLine()) != null) {
                    if (isParsingCoordinates) {
                        if (line.equals("EOF")) {
                            break; // Termina cuando se encuentra "EOF"
                        }
                        String[] parts = line.split(" ");
                        if (parts.length == 3) {
                            int id = Integer.parseInt(parts[0]);
                            double x = Double.parseDouble(parts[1]);
                            double y = Double.parseDouble(parts[2]);
                            puntos.add(new Punto(id, x, y));
                        }
                    } else if (line.equals("NODE_COORD_SECTION")) {
                        isParsingCoordinates = true; // Comienza a analizar las coordenadas
                    }
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        } else {
            System.out.println("El archivo no existe.");
        }
        return puntos;
    }

    public File buscarRuta(String nombreFichero) {
        this.file = new File(this.rutaDelProyecto);

        for (int i = 0; i < 2; i++) {
            file = file.getParentFile();
        }

        file = new File(file.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "TSP" + File.separator + nombreFichero);
        System.out.println("Ruta del fichero " + file.getAbsolutePath());
        return file;
    }

    //****************************************GENERACIÓN DE PUNTOS**********************************************
    public ArrayList<Punto> GenerarPuntosAleatoriosPeor(int n) {
        Random rand = new Random();
        rand.setSeed(System.nanoTime());
        ArrayList<Punto> p = new ArrayList<Punto>();
        for (int i = 0; i < n; i++) {
            double aux1 = rand.nextInt(1000) + 7;
            double y = aux1 / ((double) i + 1 + i * 0.100);
            int num = rand.nextInt(3);
            y += ((i % 500) - num * (rand.nextInt(100)));
            double x = 1;
            Punto punto = new Punto(i + 1, x, y);
            p.add(punto);
        }
        return p;
    }

    public ArrayList<Punto> GenerarPuntosAleatorios(int n) {
        Random rand = new Random();
        rand.setSeed(System.nanoTime());
        ArrayList<Punto> p = new ArrayList<Punto>();
        for (int i = 0; i < n; i++) {
            int num = rand.nextInt(4000) + 1;
            int den = rand.nextInt(11) + 7;
            double x = num / ((double) den + 0.37);
            double y = (rand.nextInt(4000) + 1) / ((double) (rand.nextInt(11) + 7) + 0.37);
            Punto punto = new Punto(i + 1, x, y);
            p.add(punto);
        }
        return p;
    }

    public void crearFicheroAleatorio(int size) {
        //puntos = new ArrayList<>();

        String fileName = "dataset" + size + ".tsp";
        this.file = new File(this.rutaDelProyecto);
        for (int i = 0; i < 2; i++) {
            file = file.getParentFile();
        }
        file = new File(file.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "TSP" + File.separator + fileName);
        String filePath = file.toString();
        System.out.println(filePath);
        Random r = new Random();
        r.setSeed(System.nanoTime());
        DecimalFormat decimalFormat = new DecimalFormat("#.##########");
        try {
            ArrayList<Punto> puntos;
            if (isPeorCaso) {
                puntos = GenerarPuntosAleatoriosPeor(size);

            } else {
                puntos = GenerarPuntosAleatorios(size);

            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("NAME: " + fileName);
            writer.newLine();
            writer.write("TYPE: TSP");
            writer.newLine();
            writer.write("COMMENT: " + size + " locations");
            writer.newLine();
            writer.write("DIMENSION: " + size);
            writer.newLine();
            writer.write("EDGE_WEIGHT_TYPE: EUC_2D");
            writer.newLine();
            writer.write("NODE_COORD_SECTION");
            writer.newLine();
            for (int i = 0; i < size; i++) {
                writer.write(i + 1 + " " + puntos.get(i).getX() + " " + puntos.get(i).getY());
                writer.newLine();
            }
            //Forzar escritura en el archivo
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        int puntosCalculados = 0;
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
        mejorLinea.setPuntosCalculados(puntosCalculados);
        return mejorLinea;
    }

    public Linea exhaustivoPoda(ArrayList<Punto> puntos) {
        //Declaración de variables
        Linea mejorLinea = new Linea();
        Linea actualLinea;
        Double distanciaMin;
        int puntosCalculados = 0;

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

        mejorLinea.setPuntosCalculados(puntosCalculados);
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

    //****************************************METODOS AUXILIARES PARA LAS DIFERENTES OPCIONES*********************
    public Linea calcularYCrearAlgoritmo(String algoritmoACalcular, ArrayList<Punto> puntos) {
        double tiempoEjecucion = 0;
        double endTime = 0;
        Linea l = null;
        double startTime = System.nanoTime();
        switch (algoritmoACalcular) {
            case "exhaustivo":
                l = exhaustivo(puntos);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
            case "exhaustivoPoda":
                l = exhaustivoPoda(puntos);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
            case "dyv":
                l = divideyvenceras(puntos, 0, puntos.size() - 1);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
            case "dyvMejorado":
                l = dyvMejorado(puntos, 0, puntos.size() - 1);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
        }
        return l;
    }

    public ArrayList<Linea> estudiarUnaEstrategia(String algoritmoSeleccionado) {
        ArrayList<Punto> puntosNuevo;
        ArrayList<Linea> lineas = new ArrayList<Linea>();
        int pos = 0;
        for (int i = 500; i <= 5000; i += 500) {
            if (isPeorCaso) {
                puntosNuevo = GenerarPuntosAleatoriosPeor(i);

            } else {
                puntosNuevo = GenerarPuntosAleatorios(i);

            }
            ArrayList<Punto> puntosOrdenadosX = quicksortX(puntosNuevo, 0, puntosNuevo.size() - 1);
            switch (algoritmoSeleccionado) {
                case "exhaustivo":
                    lineas.add(calcularYCrearAlgoritmo(algoritmoSeleccionado, puntosNuevo));
                    break;
                case "exhaustivoPoda":
                    lineas.add(calcularYCrearAlgoritmo(algoritmoSeleccionado, puntosOrdenadosX));
                    break;
                case "dyv":
                    lineas.add(calcularYCrearAlgoritmo(algoritmoSeleccionado, puntosOrdenadosX));
                    break;
                case "dyvMejorado":
                    lineas.add(calcularYCrearAlgoritmo(algoritmoSeleccionado, puntosOrdenadosX));
                    break;
            }
            pos++;
        }
        for (int i = 0; i < lineas.size(); i++) {
            System.out.println("Tiempo que tarda " + i + ": " + lineas.get(i).getTiempoEjecucion());
        }
        return lineas;
    }

    public ArrayList<Linea> ejecutarAlgoritmos(ArrayList<Punto> puntos) {
        ArrayList<Linea> mejoresLineas = new ArrayList<>();
        ArrayList<Punto> puntosOrdenadosX = quicksortX(puntos, 0, puntos.size() - 1);
        mejoresLineas.add(calcularYCrearAlgoritmo("exhaustivo", puntos));
        mejoresLineas.add(calcularYCrearAlgoritmo("exhaustivoPoda", puntosOrdenadosX));
        mejoresLineas.add(calcularYCrearAlgoritmo("dyv", puntosOrdenadosX));
        mejoresLineas.add(calcularYCrearAlgoritmo("dyvMejorado", puntosOrdenadosX));
        return mejoresLineas;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion, vista = "";
        accion = request.getPathInfo();

        ServletContext context = getServletContext();
        rutaDelProyecto = context.getRealPath("/");

        switch (accion) {
            
            case "/comprobarDatasets": {
                Gson gson = new Gson();
                ArrayList<Linea> mejoresLineas = new ArrayList<>();

                mejoresLineas.addAll(ejecutarAlgoritmos(leerPuntos(buscarRuta("berlin52.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(leerPuntos(buscarRuta("ch130.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(leerPuntos(buscarRuta("ch150.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(leerPuntos(buscarRuta("d493.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(leerPuntos(buscarRuta("d657.tsp"))));

                request.setAttribute("mejoresLineas", mejoresLineas);

                String mejoresLineasJSON = gson.toJson(mejoresLineas);

                request.setAttribute("mejoresLineasJSON", mejoresLineasJSON);
                request.setAttribute("opcionMenuResult", "comprobarDatasets");

                vista = "/result_view.jsp";
            }
            break;
            case "/comprobarEstrategias": {

                request.setAttribute("opcionMenu", "comprobarEstrategias");

                vista = "/intermediate_view.jsp";

            }
            break;

            case "/comprobarEstrategias_result": {
                Gson gson = new Gson();
                String talla = request.getParameter("talla");
                int tallaInt = Integer.parseInt(talla);
                ArrayList<Punto> puntosaux;
                if (isPeorCaso) {
                    puntosaux = GenerarPuntosAleatoriosPeor(tallaInt);
                }else{
                    puntosaux = GenerarPuntosAleatorios(tallaInt);
                }
                ArrayList<Linea> mejoresLineas = ejecutarAlgoritmos(puntosaux);

                request.setAttribute("mejoresLineas", mejoresLineas);

                String tallaJSON = gson.toJson(talla);
                String mejoresLineasJSON = gson.toJson(mejoresLineas);

                request.setAttribute("mejoresLineasJSON", mejoresLineasJSON);
                request.setAttribute("tallaJSON", tallaJSON);

                request.setAttribute("opcionMenuResult", "comprobarEstrategias_result");

                vista = "/result_view.jsp";

            }
            break;

            case "/estudiarUnaEstrategia": {
                request.setAttribute("opcionMenu", "estudiarUnaEstrategia");
                vista = "/intermediate_view.jsp";
            }
            break;

            case "/estudiarUnaEstrategia_result": {
                Gson gson = new Gson();

                String algoritmo = request.getParameter("algoritmo");

                ArrayList<Linea> mejoresLineas = estudiarUnaEstrategia(algoritmo);

                request.setAttribute("mejoresLineas", mejoresLineas);

                String mejoresLineasJSON = gson.toJson(mejoresLineas);

                request.setAttribute("mejoresLineasJSON", mejoresLineasJSON);
                request.setAttribute("algoritmo", algoritmo);

                request.setAttribute("opcionMenuResult", "estudiarUnaEstrategia_result");
                vista = "/result_view.jsp";
            }
            break;

            case "/estudiarDosEstrategias": {

                request.setAttribute("opcionMenu", "estudiarDosEstrategias");

                vista = "/intermediate_view.jsp";
            }
            break;
            case "/estudiarDosEstrategias_result": {
                Gson gson = new Gson();
                String algoritmoPri = request.getParameter("algoritmoPri");
                String algoritmoSeg = request.getParameter("algoritmoSeg");
                String[] algoritmos = {algoritmoPri, algoritmoSeg};

                ArrayList<Linea> mejoresLineas = estudiarUnaEstrategia(algoritmoPri);
                mejoresLineas.addAll(estudiarUnaEstrategia(algoritmoSeg));

                for (int i = 0; i < mejoresLineas.size(); i++) {
                    System.out.println("Lineas sin ordenar: " + mejoresLineas.get(i).getTiempoEjecucion());
                }

                // Crear una lista auxiliar para el reordenamiento
                ArrayList<Linea> listaReordenada = new ArrayList<>();

                // Reordenar el ArrayList según el patrón
                for (int i = 0; i < 10; i++) {
                    listaReordenada.add(mejoresLineas.get(i));          // Elemento i
                    listaReordenada.add(mejoresLineas.get(i + 10));     // Elemento i + 10
                }

                request.setAttribute("mejoresLineas", mejoresLineas);
                String mejoresLineasJSON = gson.toJson(listaReordenada);
                request.setAttribute("mejoresLineasJSON", mejoresLineasJSON);

                request.setAttribute("algoritmosEDE", algoritmos);

                request.setAttribute("opcionMenuResult", "estudiarDosEstrategias_result");

                vista = "/result_view.jsp";
            }
            break;
            case "/compararEstrategias": {

                ArrayList<Linea> mejoresLineas = new ArrayList<>();
                ArrayList<Punto> puntosaux;
                Gson gson = new Gson();

                for (int i = 500; i <= 5000; i += 500) {
                    if (isPeorCaso) {
                        puntosaux = GenerarPuntosAleatoriosPeor(i);
                    }else{
                        puntosaux = GenerarPuntosAleatorios(i);
                    }
                    mejoresLineas.addAll(ejecutarAlgoritmos(puntosaux));
                }

                request.setAttribute("mejoresLineas", mejoresLineas);

                String mejoresLineasJSON = gson.toJson(mejoresLineas);

                request.setAttribute("mejoresLineasJSON", mejoresLineasJSON);

                request.setAttribute("opcionMenuResult", "compararEstrategias");

                vista = "/result_view.jsp";
            }
            break;
            case "/peorCaso": {

                if (isPeorCaso) {
                    request.setAttribute("peorCaso", "OFF");
                    isPeorCaso = false;
                } else {
                    request.setAttribute("peorCaso", "ON");
                    isPeorCaso = true;
                }

                vista = "/index.jsp";
            }
            break;
            case "/ficheroAleatorio": {

                request.setAttribute("opcionMenu", "ficheroAleatorio");

                vista = "/intermediate_view.jsp";
            }
            break;
            case "/ficheroAleatorio_result": {
                Gson gson = new Gson();
                String talla = request.getParameter("talla");
                System.out.println("Talla: " + talla);
                String nombreFichero = "dataset" + talla + ".tsp";
                System.out.println("Nombre fichero: " + nombreFichero);
                int tallaInt = Integer.parseInt(talla);
                crearFicheroAleatorio(tallaInt);

                //Mostrar grafica del fichero creado
                ArrayList<Punto> puntosAleatorio = leerPuntos(buscarRuta(nombreFichero));
                ArrayList<Linea> mejoresLineas = ejecutarAlgoritmos(puntosAleatorio);

                request.setAttribute("nombreFichero", nombreFichero);
                request.setAttribute("mejoresLineas", mejoresLineas);

                String mejoresLineasJSON = gson.toJson(mejoresLineas);

                request.setAttribute("mejoresLineasJSON", mejoresLineasJSON);

                request.setAttribute("opcionMenuResult", "ficheroAleatorio_result");

                vista = "/result_view.jsp";
            }
            break;

            case "/compararEstrategiasFichero": {

                request.setAttribute("opcionMenu", "compararEstrategiasFichero");

                vista = "/intermediate_view.jsp";
            }
            break;

            case "/compararEstrategiasFichero_result": {

                Gson gson = new Gson();
                ArrayList<Linea> mejoresLineas = new ArrayList<>();
                String fichero = request.getParameter("fichero");

                ArrayList<Punto> puntosaux = leerPuntos(buscarRuta(fichero));

                if (!puntosaux.isEmpty()) {
                    mejoresLineas.addAll(ejecutarAlgoritmos(puntosaux));

                    request.setAttribute("mejoresLineas", mejoresLineas);

                    String mejoresLineasJSON = gson.toJson(mejoresLineas);

                    request.setAttribute("fichero", fichero);
                    request.setAttribute("mejoresLineasJSON", mejoresLineasJSON);
                    request.setAttribute("opcionMenuResult", "compararEstrategiasFichero_result");

                    vista = "/result_view.jsp";
                } else {
                    request.setAttribute("opcionMenuResult", "ERRORcompararEstrategiasFichero_result");

                    vista = "/result_view.jsp";
                }

            }
            break;

            case "/verPuntosGrafica": {

                request.setAttribute("opcionMenu", "verPuntosGrafica");

                vista = "/intermediate_view.jsp";
            }
            break;
            
            case "/verPuntosGrafica_result": {
                //Recoge las variables enviadas por la URL
                String fichero = request.getParameter("opcionFichero");
                String algoritmo = request.getParameter("opcionAlgoritmo");

                Gson gson = new Gson();
                Linea mejorLinea = new Linea();
                ArrayList<Punto> puntos = leerPuntos(buscarRuta(fichero));

                if (null != algoritmo) //Comprobar que algoritmo vamos a utilizar
                {
                    switch (algoritmo) {
                        case "exhaustivo":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, puntos);
                            break;
                        case "exhaustivoPoda":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, quicksortX(puntos, 0, puntos.size() - 1));
                            break;
                        case "dyv":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, quicksortX(puntos, 0, puntos.size() - 1));
                            break;
                        case "dyvMejorado":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, quicksortX(puntos, 0, puntos.size() - 1));
                            break;
                        default:
                            break;
                    }
                }

                //Convertimos las variables que vamos a tratar en JS a tipo JSON
                String puntosJSON = gson.toJson(puntos);
                String lineaJSON = gson.toJson(mejorLinea);

                //Mandamos los datos sin convertir para que el h1 del jsp muestre los datos
                request.setAttribute("linea", mejorLinea);
                request.setAttribute("tiempoEjecucion", mejorLinea.getTiempoEjecucion());

                //Mandamos los datos convertidas en JSON para que sean tratadas en el JS
                request.setAttribute("lineaJSON", lineaJSON);
                request.setAttribute("puntosJSON", puntosJSON);

                request.setAttribute("opcionMenuResult", "verPuntosGrafica_result");

                //Indicamos a que vista queremos que nos mande luego de ejecutar todo el codigo anterior
                vista = "/result_view.jsp";
            }
            break;

            case "/index": {
                ejecutarAlgoritmos(GenerarPuntosAleatorios(1000));
                request.setAttribute("peorCaso", "OFF");
                isPeorCaso = false;
                vista = "/index.jsp";
            }
            break;

            case "/volver": {

                request.setAttribute("lineaJSON", null);
                request.setAttribute("puntosJSON", null);
                if (isPeorCaso) {
                    request.setAttribute("peorCaso", "ON");
                }else{
                    request.setAttribute("peorCaso", "OFF");
                }

                vista = "/index.jsp";
            }
            break;

            case "/volverIntermediateView": {
                request.setAttribute("opcionMenu", "compararEstrategiasFichero");

                vista = "/intermediate_view.jsp";
            }
            break;
        }

        try {
            if (!"".equals(vista)) {
                RequestDispatcher rd = request.getRequestDispatcher(vista);
                if (rd != null) {
                    rd.forward(request, response);
                }

            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
