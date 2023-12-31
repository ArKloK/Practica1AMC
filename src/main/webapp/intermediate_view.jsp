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
            <form onsubmit="redirigirEstudiarDosEstrategias(event);">
                <select id="algoritmoPri" name="algoritmoPri" title="Primer algoritmo">
                    <option value="exhaustivo">Exhaustivo</option>
                    <option value="exhaustivoPoda">Exhaustivo con poda</option>
                    <option value="dyv">Divide y venceras</option>
                    <option value="dyvMejorado">Divide y venceras Mejorado</option>
                </select>

                <select id="algoritmoSeg" name="algoritmoSeg" title="Segundo Algoritmo">
                    <option value="exhaustivo">Exhaustivo</option>
                    <option value="exhaustivoPoda">Exhaustivo con poda</option>
                    <option value="dyv">Divide y venceras</option>
                    <option value="dyvMejorado">Divide y venceras Mejorado</option>
                </select>
                <input type="submit" value="Aceptar">
            </form>

        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'ficheroAleatorio'}">
            <form action="/Practica1AMC/AlgoritmosController/ficheroAleatorio_result">
                <label for="talla">Introduce una talla: </label>
                <input type="number" id="talla" name="talla"/>
                <br><br>
                <input type="submit" value="Aceptar">
            </form>
        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'compararEstrategiasFichero'}">

            <form onsubmit="redirigircompararEstrategiasFichero(event);" method="post" enctype="multipart/form-data">
                <label for="archivo">Selecciona un archivo .tsp:</label>
                <input type="text" name="ficheros" id="ficheros" accept=".tsp">
                <input type="submit" value="Aceptar">
            </form>

        </c:if>
        <c:if test="${requestScope.opcionMenu eq 'verPuntosGrafica'}">
            <h1>Fichero: </h1>
            <form onsubmit="redirigirVerPuntosGrafica(event)">
                <select id="ficheros" name="ficheros" title="ficheros">
                    <option value="berlin52.tsp">Berlin52</option>
                    <option value="ch130.tsp">Ch130</option>
                    <option value="ch150.tsp">Ch150</option>
                    <option value="d493.tsp">D493</option>
                    <option value="d657.tsp">D657</option>
                </select>
                <select id="algoritmos" name="algoritmos" title="algoritmos">
                    <option value="exhaustivo">Exhaustivo</option>
                    <option value="exhaustivoPoda">Exhaustivo con poda</option>
                    <option value="dyv">Divide y venceras</option>
                    <option value="dyvMejorado">Divide y venceras Mejorado</option>
                </select>
                <input type="submit" value="Aceptar">
            </form>

        </c:if>

        <c:if test="${requestScope.opcionMenu eq 'comprobarVoracesFichero'}">

            <form onsubmit="redirigirComprobarVoracesFichero(event);" method="post" enctype="multipart/form-data">
                <label for="ficheros">Selecciona un archivo .tsp:</label>
                <input type="text" name="ficheros" id="ficheros" accept=".tsp">
                <input type="submit" value="Aceptar">
            </form>

        </c:if>

        <c:if test="${requestScope.opcionMenu eq 'comprobarVoraces'}">

            <form onsubmit="redirigirComprobarVoraces(event);" method="post" enctype="multipart/form-data">
                <label for="talla">Introduce una talla:</label>
                <input type="number" name="talla" id="talla" >
                <input type="submit" value="Aceptar">
            </form>

        </c:if>
        <script src="/Practica1AMC/main.js"></script>
    </body>
</html>

