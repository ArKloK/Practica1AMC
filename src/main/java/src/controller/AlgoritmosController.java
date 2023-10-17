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
import java.util.Comparator;
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

    private ArrayList<Punto> puntos;
    private String rutaDelProyecto;
    private File file;

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
    
    public static ArrayList<Punto> GenerarPuntosAleatoriosPeor(int n){
        Random rand = new Random();
        ArrayList<Punto> p = new ArrayList<Punto>();
        for (int i = 0; i < n; i++) {
            double aux1 = rand.nextInt(1000)+7;
            double y = aux1 / ((double)(i+1)+i*0.100);
            int num = rand.nextInt(3);
            y += ((i % 500) - num * (rand.nextInt(100)));
            double x = 1;
            Punto punto = new Punto(i+1, x, y);
            p.add(punto);
        }
        return p;
    }
    public static ArrayList<Punto> GenerarPuntosAleatorios(int n){
        Random rand = new Random();
        ArrayList<Punto> p = new ArrayList<Punto>();
        for (int i = 0; i < n; i++) {
            int num = rand.nextInt(4000)+1;
            int den = rand.nextInt(11)+7;
            double x = num / ((double) den + 0.37);
            double y = (rand.nextInt(4000)+1) / ((double)(rand.nextInt(11)+7)+0.37);
            Punto punto = new Punto(i+1, x, y);
            p.add(punto);
        }
        return p;
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
    public Linea exhaustivo(String nombreFichero, ArrayList<Punto> puntos) {
        //Declaración de variables
        double mejorCamino = Double.MAX_VALUE;
        Linea mejorLinea = new Linea();
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
        estudiarUnaEstrategia();
        return mejorLinea;
    }

    public Linea exhaustivoPoda(String nombreFichero, ArrayList<Punto> puntos) {
        //Declaración de variables
        Linea mejorLinea;
        Linea actualLinea;
        Double distanciaMin;
        //Asignamos los puntos que vamos a seleccionar en la primera iteración, definimos la linea que une a esos puntos y calculamos la distancia minima
        Punto punto1 = this.puntos.get(0);
        Punto punto2 = this.puntos.get(1);
        actualLinea = new Linea(punto1, punto2);
        distanciaMin = actualLinea.distancia();

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

    //****************************************ACCIONES MENU PRINCIPAL*********************************************
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

        //PARA DARLE FORMATO DE 10 DECIMALES Y PARA QUE NO SALGA , EN VEZ DE .
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.0000000000", symbols);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("NAME: " + fileName);
            writer.newLine();
            writer.write("TYPE: TSP\n");
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
                double coordPrimer = r.nextInt(1000) + r.nextDouble();
                double coordSegun = r.nextInt(1000) + r.nextDouble();
                writer.write((i + 1) + " " + df.format(coordPrimer) + " " + df.format(coordSegun));
                writer.newLine();
            }
            //Forzar escritura en el archivo
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void estudiarUnaEstrategia() {
        int talla = 500;
        ArrayList<Punto> puntosNuevo = new ArrayList<Punto>();
        Random r = new Random();
        r.setSeed(System.nanoTime());
        for (int i = 500; i <= 5000; i += 500) {
                puntosNuevo = GenerarPuntosAleatorios(i);
        }
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
            case "/show": {
                //Recoge las variables enviadas por la URL
                String fichero = request.getParameter("opcionFichero");
                String algoritmo = request.getParameter("opcionAlgoritmo");

                Gson gson = new Gson();
                Linea mejorLinea = null;
                double tiempoEjecucion = 0;
                leerPuntos(buscarRuta(fichero));
                //Comprobar que algoritmo vamos a utilizar
                if ("exhaustivo".equals(algoritmo)) {
                    long startTime = System.nanoTime();
                    mejorLinea = exhaustivo(fichero, puntos);
                    long endTime = System.nanoTime();
                    tiempoEjecucion = endTime - startTime;
                    tiempoEjecucion /= 1000;
                    mejorLinea.setTiempoEjecucion(tiempoEjecucion);
                } else if ("exhaustivopoda".equals(algoritmo)) {
                    quicksortX(puntos, 0, this.puntos.size() - 1);
                    long startTime = System.nanoTime();
                    mejorLinea = exhaustivoPoda(fichero, puntos);
                    long endTime = System.nanoTime();
                    tiempoEjecucion = endTime - startTime;
                    tiempoEjecucion /= 1000;
                    mejorLinea.setTiempoEjecucion(tiempoEjecucion);
                } else if ("divideyvenceras".equals(algoritmo)) {
                    quicksortX(puntos, 0, puntos.size() - 1);
                    long startTime = System.nanoTime();
                    mejorLinea = divideyvenceras(puntos, 0, puntos.size() - 1);
                    long endTime = System.nanoTime();
                    tiempoEjecucion = endTime - startTime;
                    tiempoEjecucion /= 1000;
                    mejorLinea.setTiempoEjecucion(tiempoEjecucion);
                }

                System.out.println("Numero de puntos dentro " + puntos.size());

                //Convertimos las variables que vamos a tratar en JS a tipo JSON
                String puntosJSON = gson.toJson(puntos);
                String lineaJSON = gson.toJson(mejorLinea);

                //Mandamos los datos sin convertir para que el h1 del jsp muestre los datos
                request.setAttribute("linea", mejorLinea);
                request.setAttribute("tiempoEjecucion", mejorLinea.getTiempoEjecucion());

                //Mandamos los datos convertidas en JSON para que sean tratadas en el JS
                request.setAttribute("lineaJSON", lineaJSON);
                request.setAttribute("puntosJSON", puntosJSON);

                //Indicamos a que vista queremos que nos mande luego de ejecutar todo el codigo anterior
                vista = "/index.jsp";
            }
            break;

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
