/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package src.controller;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    private ArrayList<Punto> puntos;
    private String rutaDelProyecto;
    private File file;
    private double tiempoEjecucion;

    public AlgoritmosController() {
        puntos = new ArrayList<>();
    }

    //****************************************MANEJO DE FICHERO**********************************************
    public void leerPuntos(File archivo) {
        if (archivo.exists()) {
            try {
                puntos = new ArrayList<>();
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

    }

    public File buscarRuta(String nombreFichero) {
        this.file = new File(this.rutaDelProyecto);

        for (int i = 0; i < 2; i++) {
            file = file.getParentFile();
        }

        file = new File(file.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "TSP" + File.separator + nombreFichero);
        return file;
    }

    //****************************************ALGORITMOS DE ORDENACIÓN**********************************************
    public void quicksortX(ArrayList<Punto> puntosaux, int primero, int ultimo) {
        if (primero < ultimo) {
            Punto pivote = puntosaux.get(ultimo);
            int posicion = partitionX(puntosaux, primero, ultimo, pivote);
            quicksortX(puntosaux, primero, posicion - 1);
            quicksortX(puntosaux, posicion + 1, ultimo);
        }
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

    public void quicksortY(ArrayList<Punto> puntosaux, int primero, int ultimo) {
        if (primero < ultimo) {
            Punto pivote = puntosaux.get(ultimo);
            int posicion = partitionY(puntosaux, primero, ultimo, pivote);
            quicksortY(puntosaux, primero, posicion - 1);
            quicksortY(puntosaux, posicion + 1, ultimo);
        }
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
    public Linea exhaustivo(String nombreFichero) {
        //Declaración de variables
        double mejorCamino = 90000;
        Linea mejorLinea = new Linea();
        tiempoEjecucion = 0;

        //Leemos el fichero
        leerPuntos(buscarRuta(nombreFichero));

        //Empezamos la busqueda
        long startTime = System.nanoTime();
        for (int i = 0; i < this.puntos.size(); i++) {
            for (int j = i + 1; j < this.puntos.size(); j++) {
                Linea l = new Linea(puntos.get(i), puntos.get(j));
                if (l.distancia() < mejorCamino) {
                    mejorCamino = l.distancia();
                    mejorLinea = l;
                }
            }
        }
        long endTime = System.nanoTime();
        tiempoEjecucion = endTime - startTime;
        tiempoEjecucion /= 1000;

        System.out.println("Tiempo de ejecución: " + tiempoEjecucion + " nanosegundos");
        return mejorLinea;
    }

    public Linea exhaustivoPoda(String nombreFichero) {
        //Declaración de variables
        Linea mejorLinea;
        Linea actualLinea;
        Double distanciaMin;
        tiempoEjecucion = 0;

        //Leemos fichero y ordenamos los puntos por su coordenada x
        leerPuntos(buscarRuta(nombreFichero));
        quicksortX(this.puntos, 0, this.puntos.size() - 1);

        //Asignamos los puntos que vamos a seleccionar en la primera iteración, definimos la linea que une a esos puntos y calculamos la distancia minima
        Punto punto1 = this.puntos.get(0);
        Punto punto2 = this.puntos.get(1);
        actualLinea = new Linea(punto1, punto2);
        distanciaMin = actualLinea.distancia();

        long startTime = System.nanoTime();
        for (int i = 0; i < this.puntos.size() - 1; i++) {
            Punto puntoBase = this.puntos.get(i);
            for (int j = i + 1; j < this.puntos.size(); j++) {
                Punto puntoActual = this.puntos.get(j);
                actualLinea = new Linea(puntoBase, puntoActual);

                //Comprobamos si la distancia minima tiene que ser actualizada
                if (actualLinea.distancia() < distanciaMin) {
                    distanciaMin = actualLinea.distancia();
                    punto1 = puntoBase;
                    punto2 = puntoActual;
                }

                //Poda
                if (Math.abs(puntoBase.getX() - puntoActual.getX()) >= distanciaMin) {
                    break;
                }
            }
        }
        mejorLinea = new Linea(punto1, punto2);
        long endTime = System.nanoTime();
        tiempoEjecucion = endTime - startTime;
        tiempoEjecucion /= 1000;

        System.out.println("Tiempo de ejecución: " + tiempoEjecucion + " nanosegundos");
        return mejorLinea;
    }

    public Linea divideyvenceras(ArrayList<Punto> puntos, int izquierda, int derecha) {
        Linea mejorLinea = null;
        double distanciaMin = Double.MAX_VALUE;

        if (derecha - izquierda <= 2) {
            // Caso base: pocos puntos, calcular por fuerza bruta
            for (int i = izquierda; i <= derecha; i++) {
                for (int j = i + 1; j <= derecha; j++) {
                    Linea actualLinea = new Linea(puntos.get(i), puntos.get(j));
                    double distancia = actualLinea.distancia();
                    if (distancia < distanciaMin) {
                        distanciaMin = distancia;
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
        Linea lineaDerecha = divideyvenceras(puntos, medio + 1, derecha);

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
        quicksortY(puntosEnRango, 0, puntosEnRango.size() - 1);

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

    public Linea dyvMejorado(List<Punto> puntosOrdenadosX, List<Punto> puntosOrdenadosY) {
        int n = puntosOrdenadosX.size();

        if (n <= 3) {
            double distanciaMin = Double.POSITIVE_INFINITY;
            Linea mejorLinea = null;
            // Caso base: Fuerza bruta para un número pequeño de puntos
            for (int i = 0; i <= n - 1; i++) {
                for (int j = i + 1; j <= n; j++) {
                    Linea actualLinea = new Linea(puntos.get(i), puntos.get(j));
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
        int mitad = n / 2;
        Punto puntoMedio = puntosOrdenadosX.get(mitad);

        List<Punto> puntosIzquierdaX = puntosOrdenadosX.subList(0, mitad);
        List<Punto> puntosDerechaX = puntosOrdenadosX.subList(mitad, n);

        ArrayList<Punto> puntosIzquierdaY = new ArrayList<>();
        ArrayList<Punto> puntosDerechaY = new ArrayList<>();

        double distanciaMinima = Double.POSITIVE_INFINITY;
        Linea mejorLinea = null;

        for (Punto punto : puntosOrdenadosY) {
            if (punto.getX() <= puntoMedio.getX()) {
                puntosIzquierdaY.add(punto);
            } else {
                puntosDerechaY.add(punto);
            }
        }

        Linea lineaIzquierda = dyvMejorado(puntosIzquierdaX, puntosIzquierdaY);
        Linea lineaDerecha = dyvMejorado(puntosDerechaX, puntosDerechaY);

        if (lineaIzquierda.distancia() < lineaDerecha.distancia()) {
            distanciaMinima = lineaIzquierda.distancia();
            mejorLinea = lineaIzquierda;
        } else {
            distanciaMinima = lineaDerecha.distancia();
            mejorLinea = lineaDerecha;
        }

        List<Punto> puntosEnRango = new ArrayList<>();
        for (Punto punto : puntosOrdenadosY) {
            if (Math.abs(punto.getX() - puntoMedio.getX()) < distanciaMinima) {
                puntosEnRango.add(punto);
            }
        }

        for (int i = 0; i < puntosEnRango.size() - 1; i++) {
            for (int j = i + 1; j < puntosEnRango.size() && (puntosEnRango.get(j).getY() - puntosEnRango.get(i).getY()) < distanciaMinima; j++) {
                Linea linea = new Linea(puntosEnRango.get(i), puntosEnRango.get(j));
                double distancia = linea.distancia();
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    mejorLinea = new Linea(puntosEnRango.get(i), puntosEnRango.get(j));
                }
            }
        }

        return mejorLinea;
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

        String accion, vista = "", opcionMenu;
        accion = request.getPathInfo();

        ServletContext context = getServletContext();
        rutaDelProyecto = context.getRealPath("/");

        switch (accion) {
            case "/show": {

                //Recoge las variables enviadas por la URL
                String fichero = request.getParameter("opcionFichero");
                String algoritmo = request.getParameter("opcionAlgoritmo");

                Gson gson = new Gson();
                Linea mejorLinea = null;

                if (null != algoritmo)
                {
                    //Comprobar que algoritmo vamos a utilizar
                    switch (algoritmo) {
                        case "exhaustivo":
                            mejorLinea = exhaustivo(fichero);
                            break;
                        case "exhaustivopoda":
                            mejorLinea = exhaustivoPoda(fichero);
                            break;
                        case "divideyvenceras":
                            leerPuntos(buscarRuta(fichero));
                            quicksortX(puntos, 0, puntos.size() - 1);
                            mejorLinea = divideyvenceras(puntos, 0, puntos.size() - 1);
                            break;
                        case "dyvmejorado":
                            leerPuntos(buscarRuta(fichero));
                            quicksortX(puntos, 0, puntos.size() - 1);
                            List<Punto> puntosOrdenadosX = puntos.subList(0, puntos.size());
                            quicksortY(puntos, 0, puntos.size() - 1);
                            List<Punto> puntosOrdenadosY = puntos.subList(0, puntos.size());
                            mejorLinea = dyvMejorado(puntosOrdenadosX, puntosOrdenadosY);
                            break;
                        default:
                            break;
                    }
                }

                System.out.println("Numero de puntos dentro " + puntos.size());

                //Convertimos las variables que vamos a tratar en JS a tipo JSON
                String puntosJSON = gson.toJson(puntos);
                String lineaJSON = gson.toJson(mejorLinea);

                //Mandamos los datos sin convertir para que el h1 del jsp muestre los datos
                request.setAttribute("linea", mejorLinea);
                request.setAttribute("tiempoEjecucion", tiempoEjecucion);

                //Mandamos los datos convertidas en JSON para que sean tratadas en el JS
                request.setAttribute("lineaJSON", lineaJSON);
                request.setAttribute("puntosJSON", puntosJSON);

                request.setAttribute("opcionMenu", "verPuntosGrafica");
                
                //Indicamos a que vista queremos que nos mande luego de ejecutar todo el codigo anterior
                vista = "/result_view.jsp";
            }
            break;

            case "/comprobarDatasets":{
                
                request.setAttribute("opcionMenu", "comprobarDatasets");
                
                vista = "/result_view.jsp";
            }break;
            case "/comprobarEstrategias":{
                
                request.setAttribute("opcionMenu", "comprobarEstrategias");
                
                vista = "/intermediate_view.jsp";
                
            }break;
            
            case "/comprobarEstrategias_result":{
                
                request.setAttribute("opcionMenu", "comprobarEstrategias_result");
                
                vista = "/result_view.jsp";
                
            }break;
            
            case "/estudiarUnaEstrategia":{
                
                request.setAttribute("opcionMenu", "estudiarUnaEstrategia");
                
                vista = "/intermediate_view.jsp";
            }break;
            case "/estudiarDosEstrategias":{
                
                request.setAttribute("opcionMenu", "estudiarDosEstrategias");
                
                vista = "/intermediate_view.jsp";
            }break;
            case "/compararEstrategias":{
                
                request.setAttribute("opcionMenu", "compararEstrategias");
                
                vista = "/result_view.jsp";
            }break;
            case "/peorCaso":{
                
                request.setAttribute("opcionMenu", "peorCaso");
                
                vista = "/result_view.jsp";
            }break;
            case "/ficheroAleatorio":{
                
                request.setAttribute("opcionMenu", "ficheroAleatorio");
                
                vista = "/intermediate_view.jsp";
            }break;
            case "/compararEstrategiasAleatorio":{
                
                request.setAttribute("opcionMenu", "compararEstrategiasAleatorio");
                
                vista = "/intermediate_view.jsp";
            }break;
            
            case "/verPuntosGrafica":{
                
                request.setAttribute("opcionMenu", "verPuntosGrafica");
                
                vista = "/intermediate_view.jsp";
            }break;
            
            case "/volver": {

                request.setAttribute("lineaJSON", null);
                request.setAttribute("puntosJSON", null);

                vista = "/index.jsp";
            }
            break;
        }

        try {
            if (!"".equals(vista)) {
                RequestDispatcher rd = request.getRequestDispatcher(vista);
                rd.forward(request, response);
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
