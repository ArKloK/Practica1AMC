package src.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import src.model.Algoritmos;
import src.model.Fichero;
import src.model.GeneradorPuntos;
import src.model.Linea;
import src.model.Punto;

/**
 *
 * @author Usuario
 */
@WebServlet(name = "AlgoritmosController", urlPatterns = {"/AlgoritmosController/*"})
public class AlgoritmosController extends HttpServlet {

    private boolean isPeorCaso;
    Algoritmos algoritmos;
    Fichero fichero;
    GeneradorPuntos generadorPuntos;

    public AlgoritmosController() {
        algoritmos = new Algoritmos();
        fichero = new Fichero();
        generadorPuntos = new GeneradorPuntos();
    }

    //****************************************METODOS AUXILIARES PARA LAS DIFERENTES OPCIONES*********************
    public Linea calcularYCrearAlgoritmo(String algoritmoACalcular, ArrayList<Punto> puntos) {
        double tiempoEjecucion = 0;
        double endTime = 0;
        Linea l = new Linea();
        double startTime = System.nanoTime();
        algoritmos.setPuntosCalculados(0);
        switch (algoritmoACalcular) {
            case "exhaustivo":
                l = algoritmos.exhaustivo(puntos);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
            case "exhaustivoPoda":
                l = algoritmos.exhaustivoPoda(puntos);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
            case "dyv":
                l = algoritmos.divideyvenceras(puntos, 0, puntos.size() - 1);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
            case "dyvMejorado":
                l = algoritmos.dyvMejorado(puntos, 0, puntos.size() - 1);
                endTime = System.nanoTime();
                tiempoEjecucion = endTime - startTime;
                tiempoEjecucion /= 1000000;
                l.setTiempoEjecucion(tiempoEjecucion);
                break;
        }
        l.setPuntosCalculados(algoritmos.getPuntosCalculados());
        return l;
    }

    public ArrayList<Linea> estudiarUnaEstrategia(String algoritmoSeleccionado) {
        ArrayList<Punto> puntosNuevo;
        ArrayList<Linea> lineas = new ArrayList<Linea>();
        int pos = 0;
        for (int i = 500; i <= 5000; i += 500) {
            if (isPeorCaso) {
                puntosNuevo = generadorPuntos.GenerarPuntosAleatoriosPeor(i);

            } else {
                puntosNuevo = generadorPuntos.GenerarPuntosAleatorios(i);

            }
            ArrayList<Punto> puntosOrdenadosX = algoritmos.quicksortX(puntosNuevo, 0, puntosNuevo.size() - 1);
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
        return lineas;
    }

    public ArrayList<Linea> ejecutarAlgoritmos(ArrayList<Punto> puntos) {
        ArrayList<Linea> mejoresLineas = new ArrayList<>();
        ArrayList<Punto> puntosOrdenadosX = algoritmos.quicksortX(puntos, 0, puntos.size() - 1);
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
        fichero.setRutaDelProyecto(context.getRealPath("/"));
        generadorPuntos.setFichero(fichero);

        switch (accion) {

            case "/comprobarDatasets": {
                Gson gson = new Gson();
                ArrayList<Linea> mejoresLineas = new ArrayList<>();

                mejoresLineas.addAll(ejecutarAlgoritmos(fichero.leerPuntos(fichero.buscarRuta("berlin52.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(fichero.leerPuntos(fichero.buscarRuta("ch130.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(fichero.leerPuntos(fichero.buscarRuta("ch150.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(fichero.leerPuntos(fichero.buscarRuta("d493.tsp"))));

                mejoresLineas.addAll(ejecutarAlgoritmos(fichero.leerPuntos(fichero.buscarRuta("d657.tsp"))));

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
                    puntosaux = generadorPuntos.GenerarPuntosAleatoriosPeor(tallaInt);
                } else {
                    puntosaux = generadorPuntos.GenerarPuntosAleatorios(tallaInt);
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
                        puntosaux = generadorPuntos.GenerarPuntosAleatoriosPeor(i);
                    } else {
                        puntosaux = generadorPuntos.GenerarPuntosAleatorios(i);
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
                String nombreFichero = "dataset" + talla + ".tsp";
                int tallaInt = Integer.parseInt(talla);
                generadorPuntos.crearFicheroAleatorio(tallaInt);
                fichero = generadorPuntos.getFichero();

                //Mostrar grafica del fichero creado
                ArrayList<Punto> puntosAleatorio = fichero.leerPuntos(fichero.buscarRuta(nombreFichero));
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
                String nombreFichero = request.getParameter("fichero");

                ArrayList<Punto> puntosaux = fichero.leerPuntos(fichero.buscarRuta(nombreFichero));

                if (!puntosaux.isEmpty()) {
                    mejoresLineas.addAll(ejecutarAlgoritmos(puntosaux));

                    request.setAttribute("mejoresLineas", mejoresLineas);

                    String mejoresLineasJSON = gson.toJson(mejoresLineas);

                    request.setAttribute("fichero", nombreFichero);
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
                String nombreFichero = request.getParameter("opcionFichero");
                String algoritmo = request.getParameter("opcionAlgoritmo");

                Gson gson = new Gson();
                Linea mejorLinea = new Linea();
                ArrayList<Punto> puntos = fichero.leerPuntos(fichero.buscarRuta(nombreFichero));

                if (null != algoritmo) //Comprobar que algoritmo vamos a utilizar
                {
                    switch (algoritmo) {
                        case "exhaustivo":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, puntos);
                            break;
                        case "exhaustivoPoda":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, algoritmos.quicksortX(puntos, 0, puntos.size() - 1));
                            break;
                        case "dyv":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, algoritmos.quicksortX(puntos, 0, puntos.size() - 1));
                            break;
                        case "dyvMejorado":
                            mejorLinea = calcularYCrearAlgoritmo(algoritmo, algoritmos.quicksortX(puntos, 0, puntos.size() - 1));
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

            case "/dijkstra": {
                ArrayList<Punto> puntos = fichero.leerPuntos(fichero.buscarRuta("berlin52.tsp"));
                double pesos[][] = new double[puntos.size()][puntos.size()];

                for (int i = 0; i < puntos.size(); i++) {
                    for (int j = 0; j < puntos.size(); j++) {
                        if (i != j) {
                            Linea l = new Linea(puntos.get(i), puntos.get(j));
                            pesos[i][j] = l.distancia();
                        } else {
                            pesos[i][j] = 0;
                        }
                    }
                }

                double distancia[] = algoritmos.vorazUnidireccional(puntos.get(0), pesos, puntos);

                System.out.println("Los puntos visitados son: ");
                for (int i = 0; i < distancia.length; i++) {
                    System.out.println(distancia[i] + " ");
                }
            }
            break;

            case "/index": {
                ejecutarAlgoritmos(generadorPuntos.GenerarPuntosAleatorios(1000));
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
                } else {
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
