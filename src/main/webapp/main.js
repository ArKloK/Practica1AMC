var xyValues = [];
var lineaJSON;
var puntosJSON;
var mejorBerlin52JSON, mejorCh130JSON, mejorCh150JSON, mejorD493JSON, mejorD657JSON;
var mejoresAlgoritmosJSON;
var mejoresAlgoritmos = [];
const algoritmos = ["Exhaustivo", "Exhaustivo poda", "Divide y Venceras", "Divide y Venceras mejorado"];
const tam = ["500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"];
const nomArchivos = ["berlin52", "ch130", "ch150", "d493", "d657"];
window.addEventListener('load', function () {
    if (puntosJSON && lineaJSON) {
        // Llamar a la function cuando los datos de la grafica esten disponibles
        cargarGraficaPuntos();
    } else if (mejoresAlgoritmosJSON) {
        cargarGraficaComparar();
    }
});

function redirigirShow() {
    lineaJSON = null;
    puntosJSON = null;
    xyValues = [];
    // Obtiene el valor seleccionado en el desplegable
    var opcionFichero = document.getElementById("ficheros").value;
    var opcionAlgoritmo = document.getElementById("algoritmos").value;

    // Construye la URL del servlet con el parámetro
    var urlDelServlet = "/Practica1AMC/AlgoritmosController/show?opcionFichero=" + opcionFichero + "&opcionAlgoritmo=" + opcionAlgoritmo;

    // Redirige a la URL del servlet
    window.location.href = urlDelServlet;
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
        if (mejoresAlgoritmos[i]) {
            for (var j = 0; j < mejoresAlgoritmos[i].length; j++) {
                var tiempoEjecucion = mejoresAlgoritmos[i][j].tiempoEjecucion;
                console.log("Tiempo de Ejecucion:", tiempoEjecucion);
            }
        }
    }
}

function cargarGraficaComparar() {

    rehacerJSON();

    var barData = {
        //Nombre del archivo / talla
        labels: nomArchivos,
        datasets: [{
                //Nombre del algoritmo
                label: algoritmos[0],
                data: [mejoresAlgoritmos[0][0].tiempoEjecucion, mejoresAlgoritmos[1][0].tiempoEjecucion, mejoresAlgoritmos[2][0].tiempoEjecucion, mejoresAlgoritmos[3][0].tiempoEjecucion, mejoresAlgoritmos[4][0].tiempoEjecucion],
                borderColor: "rgba(75, 192, 192, 1)",
                backgroundColor: "rgba(75, 192, 192, 0.5)"
            },
            {
                label: algoritmos[1],
                data: [mejoresAlgoritmos[0][1].tiempoEjecucion, mejoresAlgoritmos[1][1].tiempoEjecucion, mejoresAlgoritmos[2][1].tiempoEjecucion, mejoresAlgoritmos[3][1].tiempoEjecucion, mejoresAlgoritmos[4][1].tiempoEjecucion],
                borderColor: "rgba(255, 0, 0, 0.6);)",
                backgroundColor: "rgba(255, 0, 0, 0.8);"
            },
            {
                label: algoritmos[2],
                data: [mejoresAlgoritmos[0][2].tiempoEjecucion, mejoresAlgoritmos[1][2].tiempoEjecucion, mejoresAlgoritmos[2][2].tiempoEjecucion, mejoresAlgoritmos[3][2].tiempoEjecucion, mejoresAlgoritmos[4][2].tiempoEjecucion],
                borderColor: "rgba(255, 53, 157, 0.8)",
                backgroundColor: "rgba(255, 124, 192, 0.8)"
            },
            {
                label: algoritmos[3],
                data: [mejoresAlgoritmos[0][3].tiempoEjecucion, mejoresAlgoritmos[1][3].tiempoEjecucion, mejoresAlgoritmos[2][3].tiempoEjecucion, mejoresAlgoritmos[3][3].tiempoEjecucion, mejoresAlgoritmos[4][3].tiempoEjecucion],
                borderColor: "rgba(93, 53, 255, 0.8)",
                backgroundColor: "rgba(163, 140, 255, 0.8)"
            }]
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

