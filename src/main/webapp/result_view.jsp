<%@page import="src.model.Punto"%>
<%@page import="src.model.Camino"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="src.model.Linea"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Result view</title>
    </head>
    <body>
        <c:if test="${requestScope.opcionMenuResult eq 'comprobarDatasets'}">
            <h1>Comprobar Datasets </h1>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>
                <%
                    ArrayList<Linea> mejoresLineasCD = (ArrayList<Linea>) request.getAttribute("mejoresLineas");
                    int contadorCD = 0, contadorFichero = 0; //Esta variable nos servirá para saber cuando cerrar los <ul>
                    String[] ficherosCD = {"Berlin52", "Ch130", "Ch150", "D493", "D657"};

                    // Recorremos la lista y mostramos sus elementos
                    for (int i = 0; i < mejoresLineasCD.size(); i++) {
                        if (i % 4 == 0 || i == 0) {
                            out.println("<h2>" + ficherosCD[contadorFichero] + "</h2>");
                            out.println("<ul>");
                            contadorCD = 0;
                            contadorFichero++;
                        }

                        out.println("<li> Distancia: " + mejoresLineasCD.get(i).getDistanciaEntrePuntos() + "   Puntos calculados: " + mejoresLineasCD.get(i).getPuntosCalculados() + "   Tiempo: " + mejoresLineasCD.get(i).getTiempoEjecucion() + "</li>");
                        contadorCD++;

                        if (contadorCD == 4) {
                            out.println("</ul>");
                        }
                    }
                %> 
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
            <script>
                //Declaramos las variables que serán utilizadas para rellenar la gráfica en el js
                const ejeX = ["berlin52", "ch130", "ch150", "d493", "d657"];
                const algoritmos = ["Exhaustivo", "Exhaustivo poda", "Divide y Venceras", "Divide y Venceras mejorado"];
                var mejoresAlgoritmosJSON = [];
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejoresLineasJSON")%>');
            </script>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'comprobarEstrategias_result'}">
            <h1>Comprobar Estrategias </h1>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <%
                ArrayList<Linea> mejoresLineasComprobarEstrategias = (ArrayList<Linea>) request.getAttribute("mejoresLineas");

                // Recorremos la lista y mostramos sus elementos
                out.println("<h2>Fichero con talla " + request.getParameter("talla") + "</h2>");
                out.println("<ul>");
                for (Linea elemento : mejoresLineasComprobarEstrategias) {
                    out.println("<li> Distancia: " + elemento.getDistanciaEntrePuntos() + "   Puntos calculados: " + elemento.getPuntosCalculados() + "   Tiempo: " + elemento.getTiempoEjecucion() + "</li>");
                }
                out.println("</ul>");
            %>

            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                //Declaramos las variables que serán utilizadas para rellenar la gráfica en el js
                const ejeX = ["Exhaustivo", "Exhaustivo poda", "Divide y Venceras", "Divide y Venceras mejorado"];
                const algoritmos = [];
                var mejoresAlgoritmosJSON = [];
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejoresLineasJSON")%>');
            </script>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'estudiarUnaEstrategia_result'}">
            <h1>Estudiar una estrategia</h1>

            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <%
                ArrayList<Linea> mejoresLineasEstudiarUnaEstrategia = (ArrayList<Linea>) request.getAttribute("mejoresLineas");
                int tallaUnaEstrategia = 500;

                out.println("<h2>" + request.getAttribute("algoritmo") + "</h2>");
                out.println("<ul>");
                for (Linea linea : mejoresLineasEstudiarUnaEstrategia) {
                    out.println("<li>Talla: " + tallaUnaEstrategia + "   Tiempo: " + linea.getTiempoEjecucion() + "</li>");
                    tallaUnaEstrategia += 500;
                }
                out.println("</ul>");
            %>
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                //Declaramos las variables que serán utilizadas para rellenar la gráfica en el js
                const ejeX = ["500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"];
                const algoritmos = [];
                var mejoresAlgoritmosJSON = [];
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejoresLineasJSON")%>');
            </script>
        </c:if>  
        <c:if test="${requestScope.opcionMenuResult eq 'estudiarDosEstrategias_result'}">
            <h1>Estudiar dos estrategias</h1>

            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <%
                ArrayList<Linea> mejoresLineasDosEstrategias = (ArrayList<Linea>) request.getAttribute("mejoresLineas");
                int contadorEDE = 0, contadorFicheroEDE = 0; //Esta variable nos servirá para saber cuando cerrar los <ul>
                String[] ficherosEDE = (String[]) request.getAttribute("algoritmosEDE");
                String[] talla = {"500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"};

                // Recorremos la lista y mostramos sus elementos
                for (int i = 0; i < mejoresLineasDosEstrategias.size(); i++) {
                    if (i % 10 == 0 || i == 0) {
                        out.println("<h2>" + ficherosEDE[contadorFicheroEDE] + "</h2>");
                        out.println("<div style='display:flex'>");
                        out.println("<ul>");
                        contadorEDE = 0;
                        contadorFicheroEDE++;
                    }

                    out.println("<li>Talla:" + talla[contadorEDE] + " Distancia: " + mejoresLineasDosEstrategias.get(i).getDistanciaEntrePuntos() + "   Puntos calculados: " + mejoresLineasDosEstrategias.get(i).getPuntosCalculados() + "   Tiempo: " + mejoresLineasDosEstrategias.get(i).getTiempoEjecucion() + "</li>");
                    contadorEDE++;

                    if (contadorEDE == 10) {
                        out.println("</ul>");
                        out.println("</div>");
                    }
                }
            %>
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                //Declaramos las variables que serán utilizadas para rellenar la gráfica en el js
                const ejeX = ["500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"];
                const algoritmos = [];
                var mejoresAlgoritmosJSON = [];
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejoresLineasJSON")%>');
            </script>
        </c:if>
        <c:if test="${requestScope.opcionMenuResult eq 'ficheroAleatorio_result'}">
            <h1>Fichero aleatorio creado</h1>

            <%
                ArrayList<Linea> mejoresLineasFicheroAleatorio = (ArrayList<Linea>) request.getAttribute("mejoresLineas");

                // Recorremos la lista y mostramos sus elementos
                out.println("<h2>Fichero con nombre " + request.getAttribute("nombreFichero") + "</h2>");
                out.println("<ul>");
                for (Linea elemento : mejoresLineasFicheroAleatorio) {
                    out.println("<li> Distancia: " + elemento.getDistanciaEntrePuntos() + "   Puntos calculados: " + elemento.getPuntosCalculados() + "   Tiempo: " + elemento.getTiempoEjecucion() + "</li>");
                }
                out.println("</ul>");
            %>

            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
        </c:if>
        <c:if test="${requestScope.opcionMenuResult eq 'peorCaso'}">
            <h1>Peor Caso</h1>
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'compararEstrategias'}">
            <h1>Comparar Estrategias</h1>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <%
                ArrayList<Linea> mejoresLineasCE = (ArrayList<Linea>) request.getAttribute("mejoresLineas");
                int contadorCE = 0; //Esta variable nos servirá para saber cuando cerrar los <ul>

                // Recorremos la lista y mostramos sus elementos
                for (int i = 0; i < mejoresLineasCE.size(); i++) {
                    if (i % 4 == 0 || i == 0) {
                        out.println("<h2>Talla " + (i * 125 + 500) + "</h2>");
                        out.println("<ul>");
                        contadorCE = 0;
                    }

                    out.println("<li> Tiempo: " + mejoresLineasCE.get(i).getTiempoEjecucion() + "</li>");
                    contadorCE++;

                    if (contadorCE == 4) {
                        out.println("</ul>");
                    }
                }
            %>

            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                const ejeX = ["500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"];
                const algoritmos = ["Exhaustivo", "Exhaustivo poda", "Divide y Venceras", "Divide y Venceras mejorado"];
                var mejoresAlgoritmosJSON = [];
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejoresLineasJSON")%>');
            </script>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'compararEstrategiasFichero_result'}">
            <h2>Comprobar Estrategias de un fichero concreto</h2>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <%
                ArrayList<Linea> mejoresLineasCEF = (ArrayList<Linea>) request.getAttribute("mejoresLineas");

                out.println("<h2>Fichero: " + request.getAttribute("fichero") + "</h2>");
                out.println("<ul>");

                // Recorremos la lista y mostramos sus elementos
                for (int i = 0; i < mejoresLineasCEF.size(); i++) {
                    out.println("<li> Tiempo: " + mejoresLineasCEF.get(i).getTiempoEjecucion() + "</li>");
                }

                out.println("</ul>");
            %>

            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                const ejeX = ["Exhaustivo", "Exhaustivo poda", "Divide y Venceras", "Divide y Venceras mejorado"];
                const algoritmos = [];
                var mejoresAlgoritmosJSON = [];
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejoresLineasJSON")%>');
            </script>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'ERRORcompararEstrategiasFichero_result'}">
            <h1>ERROR, EL FICHERO INTRODUCIDO NO EXISTE, POR FAVOR INTRODUZCA OTRO FICHERO</h1>

            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'verPuntosGrafica_result'}">
            <h1>Los puntos mas cercanos son: ${requestScope.linea.p1.id} y ${requestScope.linea.p2.id}</h1>
            <canvas id="graficaPuntos" style="width:100%;max-width:1400px"></canvas>

            <h2>Tiempo de ejecución del algoritmo (en µs): ${requestScope.tiempoEjecucion}</h2>

            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                var puntosJSON = <%= request.getAttribute("puntosJSON")%>;
                var lineaJSON = <%= request.getAttribute("lineaJSON")%>;
            </script>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'comprobarVoraces_resultMEM'}">
            <h1>Comprobar Voraces (Ficheros) </h1>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>
                <%
                    ArrayList<Camino> caminos = (ArrayList<Camino>) request.getAttribute("caminos");
                    int contadorCV = 0, contadorFicheroCV = 0; //Esta variable nos servirá para saber cuando cerrar los <ul>
                    String[] ficherosCV = {"Berlin52", "Ch130", "Ch150", "D493", "D657"};

                    // Recorremos la lista y mostramos sus elementos
                    for (int i = 0; i < caminos.size(); i++) {
                        if (i % 2 == 0 || i == 0) {
                            out.println("<h2>" + ficherosCV[contadorFicheroCV] + "</h2>");
                            out.println("<ul>");
                            contadorCV = 0;
                            contadorFicheroCV++;
                        }

                        out.println("<li> Coste " + caminos.get(i).getCoste() + "</li>");
                        contadorCV++;

                        if (contadorCV == 2) {
                            out.println("</ul>");
                        }
                    }
                %> 
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
            <script>
                //Declaramos las variables que serán utilizadas para rellenar la gráfica en el js
                const ejeX = ["berlin52", "ch130", "ch150", "d493", "d657"];
                const algoritmos = ["Voraz Unidireccional", "Voraz Bidireccional"];
                var caminosJSON = [];
                caminosJSON = <%= request.getAttribute("caminosJSON")%>;
            </script>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'comprobarVoracesFichero_result'}">
            <h1>Comprobar Voraces TSP Concreto</h1>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <%
                ArrayList<Camino> caminosCVF = (ArrayList<Camino>) request.getAttribute("caminos");

                out.println("<h2>Fichero: " + request.getAttribute("fichero") + "</h2>");
                out.println("<ul>");

                // Recorremos la lista y mostramos sus elementos
                for (int i = 0; i < caminosCVF.size(); i++) {
                    if (i == 0) {
                        out.println("<li> Coste Unidireccional: " + caminosCVF.get(i).getCoste() + "</li>");
                    } else {
                        out.println("<li> Coste Bidireccional: " + caminosCVF.get(i).getCoste() + "</li>");
                    }
                }

                out.println("</ul>");

                out.println("<h3>Recorrido de la ruta usando Voraz Unidirecional</h3>");
                for (Punto punto : caminosCVF.get(0).getPuntos()) {
                    out.print(punto.getId() + " ");
                }
                out.println("<h3>Recorrido de la ruta usando Voraz Bidirecional</h3>");
                for (Punto punto : caminosCVF.get(1).getPuntos()) {
                    out.print(punto.getId() + " ");
                }
            %>
            <br><br>
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                //Declaramos las variables que serán utilizadas para rellenar la gráfica en el js
                const ejeX = ["Voraz Unidireccional", "Voraz Bidireccional"];
                const algoritmos = [];
                var caminosJSON = [];
                caminosJSON = <%= request.getAttribute("caminosJSON")%>;
            </script>

        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'comprobarVoraces_result'}">
            <h1>Comprobar Voraces</h1>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <%
                ArrayList<Camino> caminosCV = (ArrayList<Camino>) request.getAttribute("caminos");

                out.println("<h2>Talla " + request.getAttribute("talla") + "</h2>");
                out.println("<ul>");

                // Recorremos la lista y mostramos sus elementos
                for (int i = 0; i < caminosCV.size(); i++) {
                    if (i == 0) {
                        out.println("<li> Coste Unidireccional: " + caminosCV.get(i).getCoste() + "</li>");
                    } else {
                        out.println("<li> Coste Bidireccional: " + caminosCV.get(i).getCoste() + "</li>");
                    }
                }

                out.println("</ul>");

                out.println("<h3>Recorrido de la ruta usando Voraz Unidirecional</h3>");
                for (Punto punto : caminosCV.get(0).getPuntos()) {
                    out.print(punto.getId() + " ");
                }
                out.println("<h3>Recorrido de la ruta usando Voraz Bidirecional</h3>");
                for (Punto punto : caminosCV.get(1).getPuntos()) {
                    out.print(punto.getId() + " ");
                }
            %>
            <br><br>
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                //Declaramos las variables que serán utilizadas para rellenar la gráfica en el js
                const ejeX = ["Voraz Unidireccional", "Voraz Bidireccional"];
                const algoritmos = [];
                var caminosJSON = [];
                caminosJSON = <%= request.getAttribute("caminosJSON")%>;
            </script>

        </c:if>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>
        <script src="/Practica1AMC/main.js"></script>

    </body>
</html>