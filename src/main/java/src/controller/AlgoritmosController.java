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
    public File buscarRuta(String nombreFichero){
        this.file = new File(this.rutaDelProyecto);

        for (int i = 0; i < 2; i++) {
            file = file.getParentFile();
        }

        file = new File(file.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "TSP" + File.separator + nombreFichero);
        return file;
    }
    
    public void quicksort(ArrayList<Punto> puntosaux, int primero, int ultimo){
        if (primero< ultimo) {
            Punto pivote = puntosaux.get(ultimo);
            int posicion  = partition(puntosaux, primero, ultimo, pivote);
            quicksort(puntosaux, primero, posicion-1);
            quicksort(puntosaux, posicion+1, ultimo);
        }
    }
    
    public int partition(ArrayList<Punto> puntosaux, int primero, int ultimo, Punto pivote){
        int i = primero;
        int j = primero;
        
        while(i <= ultimo){
            if (puntosaux.get(i).getX() > pivote.getX()) {
                i++;
            }else{
                intercambiar(puntosaux, i, j);
                i++;
                j++;
            }
        }
        
        return j-1;
    }
    
    public void intercambiar(ArrayList<Punto> puntosaux, int i, int j){
        Punto temp = puntosaux.get(i);
        puntosaux.set(i, puntosaux.get(j));
        puntosaux.set(j, temp);
    }
    
    public Linea exhaustivo(String nombreFichero) {
        double mejorCamino = 90000;
        Linea mejorLinea = new Linea();
        leerPuntos(buscarRuta(nombreFichero));
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < this.puntos.size(); i++) {
            for (int j = i + 1; j < this.puntos.size(); j++) {
                Linea l = new Linea(puntos.get(i), puntos.get(j));
                if (l.distancia() < mejorCamino) {
                    mejorCamino = l.distancia();
                    mejorLinea = l;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long tiempoEjecucion = endTime - startTime;

        System.out.println("Tiempo de ejecución: " + tiempoEjecucion + " milisegundos");
        return mejorLinea;
    }
    
    public Linea poda(String nombreFichero){
        buscarRuta(nombreFichero);
        
        Linea mejorLinea = new Linea();
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

        String accion, vista = "";
        accion = request.getPathInfo();

        ServletContext context = getServletContext();
        rutaDelProyecto = context.getRealPath("/");

        switch (accion) {
            case "/show": {
                
                String nombreFichero = request.getParameter("opcion");  
                Gson gson = new Gson();
                Linea mejorLinea;
                mejorLinea = exhaustivo(nombreFichero);
                
                
                System.out.println("Numero de puntos dentro " + puntos.size());

                //Convertimos las variables que vamos a tratar en JS a tipo JSON
                String puntosJSON = gson.toJson(puntos);
                String lineaJSON = gson.toJson(mejorLinea);
                
                //Mandamos la linea sin convertir para que el h1 del jsp muestre los datos
                request.setAttribute("linea", mejorLinea);
                
                //System.out.println("Los puntos mas cercanos son: " + mejorLinea.getP1().getX() + " y " + mejorLinea.getP2().getY());
                request.setAttribute("lineaJSON", lineaJSON);
                request.setAttribute("puntosJSON", puntosJSON);
                
                //Indicamos a que vista queremos que nos mande luego de ejecutar todo el codigo anterior
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
