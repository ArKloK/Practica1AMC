<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="src.model.Linea"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Intermediate view</title>
    </head>
    <body>

        <c:if test="${requestScope.opcionMenu eq 'comprobarEstrategias'}">

            <form onsubmit="redirigirComprobarEstrategia(event);">
                <label for="talla">Introduce una talla: </label>
                <input type="number" id="talla" name="talla"/>
                <br><br>
                <input type="submit" value="Aceptar">
            </form>

        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'estudiarUnaEstrategia'}">

            <form onsubmit="redirigirEstudiarUnaEstrategia(event);">
                <select id="algoritmos" name="algoritmos" title="algoritmos">
                    <option value="exhaustivo">Exhaustivo</option>
                    <option value="exhaustivoPoda">Exhaustivo con poda</option>
                    <option value="dyv">Divide y venceras</option>
                    <option value="dyvMejorado">Divide y venceras Mejorado</option>
                </select>
                <input type="submit" value="Aceptar">
            </form>

        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'estudiarDosEstrategias'}">

        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'ficheroAleatorio'}">

        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'compararEstrategiasAleatorio'}">

        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'verPuntosGrafica'}">
            <h1>Fichero: </h1>
            <select id="ficheros" name="ficheros" title="ficheros">
                <option value="berlin52.tsp">Berlin52</option>
                <option value="ch130.tsp">Ch130</option>
                <option value="ch150.tsp">Ch150</option>
                <option value="d493.tsp">D493</option>
                <option value="d657.tsp">D657</option>
            </select>
            <select id="algoritmos" name="algoritmos" title="algoritmos">
                <option value="exhaustivo">Exhaustivo</option>
                <option value="exhaustivopoda">Exhaustivo con poda</option>
                <option value="divideyvenceras">Divide y venceras</option>
                <option value="dyvmejorado">Divide y venceras Mejorado</option>
            </select>
            <button type="button" onclick="redirigirShow()">Aceptar</button>
        </c:if>

        <script src="/Practica1AMC/main.js"></script>
    </body>
</html>

