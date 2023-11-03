<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="src.model.Linea"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menú Principal</title>
        <link href="/Practica1AMC/styles.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <h1 style="text-align: center">Practica 1 AMC *Antonio Toro y Carlos Camacho*</h1>
        <h2 style="text-align: center">Menú Principal</h2>
        <h2 style="text-align: right; padding-right: 10%">Peor Caso: ${requestScope.peorCaso}</h2>
        <ol class="centered-list">
            <li><a href="/Practica1AMC/AlgoritmosController/comprobarDatasets">COMPROBAR TODOS LOS DATASET (FICHEROS)</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/comprobarEstrategias">COMPROBAR TODAS LAS ESTRATEGIAS</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/estudiarUnaEstrategia">ESTUDIAR 1 ESTRATEGIA</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/estudiarDosEstrategias">ESTUDIAR 2 ESTRATEGIAS</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/compararEstrategias">COMPARAR TODAS LAS ESTRATEGIAS</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/peorCaso">ACTIVAR/DESACTIVAR PEOR CASO</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/ficheroAleatorio">CREAR FICHERO TSP ALEATORIO</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/compararEstrategiasFichero">COMPARAR TODAS LAS ESTRATEGIAS DE UN FICHERO TSP CONCRETO</a></li>
            <li><a href="/Practica1AMC/AlgoritmosController/verPuntosGrafica">VER PUNTOS EN LA GRÁFICA</a></li>
        </ol>
    </body>
</html>
