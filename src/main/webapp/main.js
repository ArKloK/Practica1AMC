var xyValues = [];
var lineaJSON;
var puntosJSON;
var mejorBerlin52JSON, mejorCh130JSON, mejorCh150JSON, mejorD493JSON, mejorD657JSON;
var mejoresAlgoritmosJSON;
var mejoresAlgoritmos = [];
var caminosJSON;
var caminos = [];
var leyenda = [];
var leyendaJSON = [];
var algoritmoPri, algoritmoSeg;
var nombreDelArchivo;

window.addEventListener('load', function () {
    if (puntosJSON && lineaJSON) {
        // Llamar a la function cuando los datos de la grafica esten disponibles
        cargarGraficaPuntos();
    } else if (mejoresAlgoritmosJSON) {
        cargarGraficaComparar();
    } else if (caminosJSON) {
        cargarGraficaVoraces();
    }
});

function redirigirComprobarEstrategia(event) {
    event.preventDefault();

    leyenda.push(document.getElementById("talla").value);
    leyendaJSON = JSON.stringify(leyenda);

    localStorage.setItem('leyendaJSON', leyendaJSON);

    var urlDelServlet = "/Practica1AMC/AlgoritmosController/comprobarEstrategias_result?talla=" + leyenda;

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
}

function redirigirVerPuntosGrafica(event) {
    event.preventDefault();

    lineaJSON = null;
    puntosJSON = null;
    xyValues = [];
    // Obtiene el valor seleccionado en el desplegable
    var opcionFichero = document.getElementById("ficheros").value;
    var opcionAlgoritmo = document.getElementById("algoritmos").value;

    // Construye la URL del servlet con el parámetro
    var urlDelServlet = "/Practica1AMC/AlgoritmosController/verPuntosGrafica_result?opcionFichero=" + opcionFichero + "&opcionAlgoritmo=" + opcionAlgoritmo;

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
}

function redirigirEstudiarUnaEstrategia(event) {

    event.preventDefault();

    leyenda.push(document.getElementById("algoritmos").value);
    leyendaJSON = JSON.stringify(leyenda);

    localStorage.setItem('leyendaJSON', leyendaJSON);

    var urlDelServlet = "/Practica1AMC/AlgoritmosController/estudiarUnaEstrategia_result?algoritmo=" + leyenda;

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
}

function redirigirEstudiarDosEstrategias(event) {

    event.preventDefault();

    algoritmoPri = document.getElementById("algoritmoPri").value;
    algoritmoSeg = document.getElementById("algoritmoSeg").value;

    leyenda = [algoritmoPri, algoritmoSeg];
    leyendaJSON = JSON.stringify(leyenda);

    localStorage.setItem('leyendaJSON', leyendaJSON);

    var urlDelServlet = "/Practica1AMC/AlgoritmosController/estudiarDosEstrategias_result?algoritmoPri=" + algoritmoPri + "&algoritmoSeg=" + algoritmoSeg;

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
}

function redirigircompararEstrategiasFichero(event) {
    event.preventDefault();

    leyenda.push(document.getElementById("ficheros").value);

    console.log("Nombre fichero " + document.getElementById("ficheros").value);

    leyendaJSON = JSON.stringify(leyenda);

    localStorage.setItem('leyendaJSON', leyendaJSON);

    var urlDelServlet = "/Practica1AMC/AlgoritmosController/compararEstrategiasFichero_result?fichero=" + leyenda;

//    setTimeout(function () {
//        window.location.href = urlDelServlet;
//    }, 3000); // 3000 ms = 3 segundos

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
}

function redirigirComprobarVoracesFichero(event) {
    event.preventDefault();

    leyenda.push(document.getElementById("ficheros").value);

    leyendaJSON = JSON.stringify(leyenda);

    localStorage.setItem('leyendaJSON', leyendaJSON);

    var urlDelServlet = "/Practica1AMC/AlgoritmosController/comprobarVoracesFichero_result?fichero=" + leyenda;

//    setTimeout(function () {
//        window.location.href = urlDelServlet;
//    }, 3000); // 3000 ms = 3 segundos

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
}

function redirigirComprobarVoraces(event) {
    event.preventDefault();

    leyenda.push(document.getElementById("talla").value);

    leyendaJSON = JSON.stringify(leyenda);

    localStorage.setItem('leyendaJSON', leyendaJSON);

    var urlDelServlet = "/Practica1AMC/AlgoritmosController/comprobarVoraces_result?talla=" + leyenda;

//    setTimeout(function () {
//        window.location.href = urlDelServlet;
//    }, 3000); // 3000 ms = 3 segundos

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
}

//Es necesario rehacer la conversion a JSON para poder acceder a los atributos de cada elemento
function parseJSON(jsonStr) {
    try {
        return JSON.parse(jsonStr);
    } catch (e) {
        console.error("Error al analizar JSON:", e);
        return null;
    }
}

//Rehace la conversion a JSON y guarda el objeto en otro array que será con el que trabajaremos
function rehacerJSON() {
    for (var i = 0; i < mejoresAlgoritmosJSON.length; i++) {
        mejoresAlgoritmos.push(parseJSON(mejoresAlgoritmosJSON[i]));
    }
}

/*function rehacerJSONVoraces() {
 console.log("CAMINOS JSON " + caminosJSON);
 for (var i = 0; i < caminosJSON.length; i++) {
 caminos.push(parseJSON(caminosJSON[i]));
 }
 console.log("CAMINOS " + caminos);
 }*/

function cargarGraficaComparar() {

    rehacerJSON();
    leyenda = localStorage.getItem('leyenda');
    leyendaJSON = localStorage.getItem('leyendaJSON');
    if (leyenda !== null && leyendaJSON !== null) {
        if (leyendaJSON !== 'null') {
            console.log("ENTRA");
            leyenda = JSON.parse(leyendaJSON);
            console.log("Tamano leyenda " + leyenda.length);
            localStorage.setItem('leyendaJSON', 'null');
        }

        if (leyenda !== 'null') {
            for (var i = 0; i < leyenda.length; i++) {
                algoritmos.push(leyenda[i]);
            }
            localStorage.setItem('leyenda', 'null');
        }
    }


    var datos = [], pos = algoritmos.length; // Aquí almacenaremos los objetos de datos

    for (var i = 0; i < algoritmos.length; i++) {
        var dato = {
            label: algoritmos[i],
            data: [],
            borderColor: "",
            backgroundColor: ""
        };

        //La operación [j*pos+k] funciona de la siguiente manera:
        //  j va a representar el número de ficheros que tenemos que recorrer, es decir, si hemos generado 10 fichero aleatorios, la j irá hasta 10.
        //  pos es el número de algoritmos que vamos a comprobar, es decir, si queremos estudiar todos, sería un total de 4.
        //  i se incrementa en el bucle anterior y nos ayudará a que cada vez que entremos en el segundo bucle no nos recoja los mismos j*pos valores.

        for (var j = 0; j < ejeX.length; j++) {
            if (mejoresAlgoritmos[0][j * pos + i] && mejoresAlgoritmos[0][j * pos + i].puntosCalculados !== undefined) {
                dato.data.push(mejoresAlgoritmos[0][j * pos + i].puntosCalculados);
            }
        }

        // Asigna colores de borde y fondo según corresponda
        switch (i) {
            case 0:
                dato.borderColor = "rgba(75, 192, 192, 1)";
                dato.backgroundColor = "rgba(75, 192, 192, 0.5)";
                break;
            case 1:
                dato.borderColor = "rgba(255, 0, 0, 0.6)";
                dato.backgroundColor = "rgba(255, 0, 0, 0.8)";
                break;
            case 2:
                dato.borderColor = "rgba(255, 53, 157, 0.8)";
                dato.backgroundColor = "rgba(255, 124, 192, 0.8)";
                break;
            case 3:
                dato.borderColor = "rgba(93, 53, 255, 0.8)";
                dato.backgroundColor = "rgba(163, 140, 255, 0.8)";
                break;
                // Agrega más casos según sea necesario
        }

        datos.push(dato);
    }


    var barData = {
        labels: ejeX,
        datasets: datos
    };
    var barOptions = {
        scales: {
            y: {
                begginAtZero: true
            }
        }
    };

    var barChart = new Chart("grafica", {
        type: 'bar',
        data: barData,
        options: barOptions
    });
}

function cargarGraficaVoraces() {

    leyenda = localStorage.getItem('leyenda');
    leyendaJSON = localStorage.getItem('leyendaJSON');

    if (leyendaJSON !== 'null') {
        console.log("ENTRA");
        leyenda = JSON.parse(leyendaJSON);
        localStorage.setItem('leyendaJSON', 'null');
    }

    if (leyenda !== 'null') {
        for (var i = 0; i < leyenda.length; i++) {
            algoritmos.push(leyenda[i]);
        }
        localStorage.setItem('leyenda', 'null');
    }

    var datos = [], pos = algoritmos.length; // Aquí almacenaremos los objetos de datos

    for (var i = 0; i < algoritmos.length; i++) {
        var dato = {
            label: algoritmos[i],
            data: [],
            borderColor: "",
            backgroundColor: ""
        };

        //La operación [j*pos+k] funciona de la siguiente manera:
        //  j va a representar el número de ficheros que tenemos que recorrer, es decir, si hemos generado 10 fichero aleatorios, la j irá hasta 10.
        //  pos es el número de algoritmos que vamos a comprobar, es decir, si queremos estudiar todos, sería un total de 4.
        //  i se incrementa en el bucle anterior y nos ayudará a que cada vez que entremos en el segundo bucle no nos recoja los mismos j*pos valores.

        for (var j = 0; j < ejeX.length; j++) {
            if (caminosJSON[j * pos + i] && caminosJSON[j * pos + i].coste !== undefined) {
                dato.data.push(caminosJSON[j * pos + i].coste);
            }
        }

        // Asigna colores de borde y fondo según corresponda
        switch (i) {
            case 0:
                dato.borderColor = "rgba(75, 192, 192, 1)";
                dato.backgroundColor = "rgba(75, 192, 192, 0.5)";
                break;
            case 1:
                dato.borderColor = "rgba(255, 0, 0, 0.6)";
                dato.backgroundColor = "rgba(255, 0, 0, 0.8)";
                break;
            case 2:
                dato.borderColor = "rgba(255, 53, 157, 0.8)";
                dato.backgroundColor = "rgba(255, 124, 192, 0.8)";
                break;
            case 3:
                dato.borderColor = "rgba(93, 53, 255, 0.8)";
                dato.backgroundColor = "rgba(163, 140, 255, 0.8)";
                break;
                // Agrega más casos según sea necesario
        }

        datos.push(dato);
    }


    var barData = {
        labels: ejeX,
        datasets: datos
    };
    var barOptions = {
        scales: {
            y: {
                begginAtZero: true
            }
        }
    };

    var barChart = new Chart("grafica", {
        type: 'bar',
        data: barData,
        options: barOptions
    });
}

function cargarGraficaPuntos() {
    var Linea = [
        {x: lineaJSON.p1.x, y: lineaJSON.p1.y},
        {x: lineaJSON.p2.x, y: lineaJSON.p2.y}

    ];

    for (var i = 0; i < puntosJSON.length; i++) {
        var punto = puntosJSON[i];
        xyValues.push({x: punto.x, y: punto.y});
    }


// Configura los datos para el gráfico de dispersión
    var scatterData = {
        datasets: [{
                label: "Puntos del fichero",
                data: xyValues,
                borderColor: "rgba(75, 192, 192, 1)",
                backgroundColor: "rgba(75, 192, 192, 0.5)"
            }, {
                label: "Puntos mas cercanos",
                data: Linea,
                borderColor: 'red', // Color de la línea
                borderWidth: 1, // Ancho de la línea
                fill: false, // No llenar el área bajo la línea
                type: 'line' // Tipo de gráfico línea
            }]
    };

// Configura las opciones del gráfico
    var scatterOptions = {
        scales: {
            x: {
                type: 'linear',
                position: 'bottom',
                title: {
                    display: true,
                    text: 'Valor X'
                }
            },
            y: {
                type: 'linear',
                position: 'left',
                title: {
                    display: true,
                    text: 'Valor Y'
                }
            }
        }
    };

// Crea el gráfico de dispersión
    var scatterChart = new Chart("graficaPuntos", {
        type: 'scatter',
        data: scatterData,
        options: scatterOptions
    });
}
