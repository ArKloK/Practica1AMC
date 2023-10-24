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
                    //Obtener la variable de sesión
                    ArrayList<Linea> mejorBerlin52 = (ArrayList<Linea>) request.getAttribute("mejorBerlin52");
                    ArrayList<Linea> mejorCh130 = (ArrayList<Linea>) request.getAttribute("mejorCh130");
                    ArrayList<Linea> mejorCh150 = (ArrayList<Linea>) request.getAttribute("mejorCh150");
                    ArrayList<Linea> mejorD493 = (ArrayList<Linea>) request.getAttribute("mejorD493");
                    ArrayList<Linea> mejorD657 = (ArrayList<Linea>) request.getAttribute("mejorD657");

                    // Recorremos la lista y mostramos sus elementos
                    out.println("<h2>Berlin52</h2>");
                    out.println("<ul>");
                    for (Linea elemento : mejorBerlin52) {
                        out.println("<li> Distancia: " + elemento.getDistanciaEntrePuntos() + "   Puntos calculados: " + elemento.getPuntosCalculados() + "   Tiempo: " + elemento.getTiempoEjecucion() + "</li>");
                    }
                    out.println("</ul>");

                    out.println("<h2>CH130</h2>");
                    out.println("<ul>");
                    for (Linea elemento : mejorCh130) {
                        out.println("<li> Distancia: " + elemento.getDistanciaEntrePuntos() + "   Puntos calculados: " + elemento.getPuntosCalculados() + "   Tiempo: " + elemento.getTiempoEjecucion() + "</li>");
                    }
                    out.println("</ul>");

                    out.println("<h2>CH150</h2>");
                    out.println("<ul>");
                    for (Linea elemento : mejorCh150) {
                        out.println("<li> Distancia: " + elemento.getDistanciaEntrePuntos() + "   Puntos calculados: " + elemento.getPuntosCalculados() + "   Tiempo: " + elemento.getTiempoEjecucion() + "</li>");
                    }
                    out.println("</ul>");

                    out.println("<h2>D493</h2>");
                    out.println("<ul>");
                    for (Linea elemento : mejorD493) {
                        out.println("<li> Distancia: " + elemento.getDistanciaEntrePuntos() + "   Puntos calculados: " + elemento.getPuntosCalculados() + "   Tiempo: " + elemento.getTiempoEjecucion() + "</li>");
                    }
                    out.println("</ul>");

                    out.println("<h2>D657</h2>");
                    out.println("<ul>");
                    for (Linea elemento : mejorD657) {
                        out.println("<li> Distancia: " + elemento.getDistanciaEntrePuntos() + "   Puntos calculados: " + elemento.getPuntosCalculados() + "   Tiempo: " + elemento.getTiempoEjecucion() + "</li>");
                    }
                    out.println("</ul>");
                %> 
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
            <script>

                var mejoresAlgoritmosJSON = [];

                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejorBerlin52JSON")%>');
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejorCh130JSON")%>');
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejorCh150JSON")%>');
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejorD493JSON")%>');
                mejoresAlgoritmosJSON.push('<%= request.getAttribute("mejorD657JSON")%>');

                // Muestra el contenido del array en la consola (para fines de depuración)
            </script>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'comprobarEstrategias_result'}">
            <h1>Comprobar Estrategias </h1>
            <!-- <canvas id="grafica" style="width:100%;max-width:1400px"></canvas> -->

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
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'estudiarUnaEstrategia_result'}">
            <h1>Estudiar una estrategia</h1>
            <%
                ArrayList<Linea> mejoresLineas = (ArrayList<Linea>) request.getAttribute("mejorLineas");
                int talla = 500;
                out.println("<ul>");
                for (Linea linea : mejoresLineas) {
                    out.println("<li>Talla: " + talla + "   Tiempo: " + linea.getTiempoEjecucion() + "</li>");
                    talla += 500;
                }
                out.println("</ul>");
            %>
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
        </c:if>  

        <c:if test="${requestScope.opcionMenuResult eq 'peorCaso'}">
            <h1>Peor Caso</h1>
            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>
        </c:if>

        <c:if test="${requestScope.opcionMenuResult eq 'verPuntosGrafica'}">
            <h1>Los puntos mas cercanos son: ${requestScope.linea.p1.id} y ${requestScope.linea.p2.id}</h1>
            <canvas id="graficaPuntos" style="width:100%;max-width:1400px"></canvas>

            <h2>Tiempo de ejecución del algoritmo (en µs): ${requestScope.tiempoEjecucion}</h2>

            <button type="button" onclick="window.location.href = '/Practica1AMC/AlgoritmosController/volver'">Volver</button>

            <script>
                var puntosJSON = <%= request.getAttribute("puntosJSON")%>;
                var lineaJSON = <%= request.getAttribute("lineaJSON")%>;
            </script>
        </c:if>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>
        <script src="/Practica1AMC/main.js"></script>

    </body>
</html>