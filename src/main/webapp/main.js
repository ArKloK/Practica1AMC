var xyValues = [];
var lineaJSON;
var puntosJSON;

window.addEventListener('load', function () {
    if (puntosJSON && lineaJSON) {
        // Llamar a lafunction cuando los datos de la grafica esten disponibles
        cargarGrafica();
    }
});

function redirigirServlet() {
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

function cargarGrafica() {
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
    var scatterChart = new Chart("grafica", {
        type: 'scatter',
        data: scatterData,
        options: scatterOptions
    });
}