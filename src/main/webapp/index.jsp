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

        <c:if test="${!empty requestScope.linea}">
            <h1>Los puntos mas cercanos son: ${requestScope.linea.p1.id} y ${requestScope.linea.p2.id}</h1>
        </c:if>
        <canvas id="grafica" style="width:100%;max-width:700px"></canvas>
        <script src="/Practica1AMC/main.js" type="text/javascript"></script>
    </body>
</html>
