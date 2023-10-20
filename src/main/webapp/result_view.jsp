<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="src.model.Linea"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>JSP Page</title>
    </head>
    <body>
        <c:if test="${requestScope.opcionMenu eq 'comprobarDatasets'}">
            <h1>Comprobar Datasets </h1>
        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'comprobarEstrategias_result'}">
            <h1>Comprobar Estrategias </h1>
            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>
        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'peorCaso'}">
            <h1>Peor Caso</h1>
        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'verPuntosGrafica'}">
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