var xyValues = [];
var lineaJSON;
var puntosJSON;
var mejorBerlin52JSON, mejorCh130JSON, mejorCh150JSON, mejorD493JSON, mejorD657JSON;
const algoritmos = ["Exhaustivo", "Exhaustivo poda", "Divide y Venceras", "Divide y Venceras mejorado"];
const tam = ["500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"];
const nomArchivos = ["berlin52", "ch130", "ch150", "d493", "d657"];
window.addEventListener('load', function () {
    if (puntosJSON && lineaJSON) {
        // Llamar a la function cuando los datos de la grafica esten disponibles
        cargarGraficaPuntos();
    } else if (mejorBerlin52JSON && mejorCh130JSON && mejorCh150JSON && mejorD493JSON && mejorD657JSON) {
        console.log(mejorBerlin52JSON);
        console.log(mejorCh130JSON);
        console.log(mejorCh150JSON);
        console.log(mejorD493JSON);
        console.log(mejorD657JSON);
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

function cargarGraficaComparar() {
    const ficherosJuntos = [
        mejorBerlin52JSON,
        mejorCh130JSON,
        mejorCh150JSON,
        mejorD493JSON,
        mejorD657JSON
    ];
    var barData = {
        //Nombre del archivo / talla
        labels: nomArchivos,
        datasets: [{
                //Nombre del algoritmo
                label: algoritmos[0],
                data: [ficherosJuntos[0][0].tiempoEjecucion, ficherosJuntos[1][0].tiempoEjecucion, ficherosJuntos[2][0].tiempoEjecucion, ficherosJuntos[3][0].tiempoEjecucion, ficherosJuntos[4][0].tiempoEjecucion],
                borderColor: "rgba(75, 192, 192, 1)",
                backgroundColor: "rgba(75, 192, 192, 0.5)"
            },
            {
                label: algoritmos[1],
                data: [ficherosJuntos[0][1].tiempoEjecucion, ficherosJuntos[1][1].tiempoEjecucion, ficherosJuntos[2][1].tiempoEjecucion, ficherosJuntos[3][1].tiempoEjecucion, ficherosJuntos[4][1].tiempoEjecucion],
                borderColor: "rgba(255, 0, 0, 0.6);)",
                backgroundColor: "rgba(255, 0, 0, 0.8);"
            },
            {
                label: algoritmos[2],
                data: [ficherosJuntos[0][2].tiempoEjecucion, ficherosJuntos[1][2].tiempoEjecucion, ficherosJuntos[2][2].tiempoEjecucion, ficherosJuntos[3][2].tiempoEjecucion, ficherosJuntos[4][2].tiempoEjecucion],
                borderColor: "rgba(255, 53, 157, 0.8)",
                backgroundColor: "rgba(255, 124, 192, 0.8)"
            },
            {
                label: algoritmos[3],
                data: [ficherosJuntos[0][3].tiempoEjecucion, ficherosJuntos[1][3].tiempoEjecucion, ficherosJuntos[2][3].tiempoEjecucion, ficherosJuntos[3][3].tiempoEjecucion, ficherosJuntos[4][3].tiempoEjecucion],
                borderColor: "rgba(93, 53, 255, 0.8)",
                backgroundColor: "rgba(163, 140, 255, 0.8)"
            }]
    }
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

