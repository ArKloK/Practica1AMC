<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="src.model.Linea"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>JSP Page</title>
    </head>
    <body>

        <c:if test="${empty requestScope.puntosJSON && empty requestScope.lineaJSON}">
            <h1>Fichero: </h1>
            <select id="ficheros" name="ficheros" title="ficheros">
                <option value="berlin52.tsp">Berlin52</option>
                <option value="ch130.tsp">Ch130</option>
                <option value="ch150.tsp">Ch150</option>
                <option value="d493.tsp">D493</option>
                <option value="d657.tsp">D657</option>
            </select>
            <button type="button" onclick="redirigirServlet()">Aceptar</button>
            <script>
                function redirigirServlet() {
                    // Obtiene el valor seleccionado en el desplegable
                    var opcionSeleccionada = document.getElementById("ficheros").value;

                    // Construye la URL del servlet con el parámetro
                    var urlDelServlet = "/Practica1AMC/AlgoritmosController/show?opcion=" + opcionSeleccionada;

                    // Redirige a la URL del servlet
                    window.location.href = urlDelServlet;
                }
            </script>
        </c:if>
        <c:if test="${!empty requestScope.puntosJSON && !empty requestScope.lineaJSON}">

            <h1>Los puntos mas cercanos son: ${requestScope.linea.p1.id} y ${requestScope.linea.p2.id}</h1>

            <canvas id="grafica" style="width:100%;max-width:1400px"></canvas>

            <script>
                var puntosJSON = <%= request.getAttribute("puntosJSON")%>;
                var lineaJSON = <%= request.getAttribute("lineaJSON")%>;
            </script>
        </c:if>

        <script src="/Practica1AMC/main.js" type="text/javascript"></script>
    </body>
</html>