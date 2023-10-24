var xyValues = [];
var lineaJSON;
var puntosJSON;
var mejorBerlin52JSON, mejorCh130JSON, mejorCh150JSON, mejorD493JSON, mejorD657JSON;
var mejoresAlgoritmosJSON;
var mejoresAlgoritmos = [];
//const tam = ["500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"]; Este dato será cargado en el jsp correspondiente
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

var tiempoEjecucion = [];

//Rehace la conversion a JSON y guarda el objeto en otro array que será con el que trabajaremos
function rehacerJSON() {
    for (var i = 0; i < mejoresAlgoritmosJSON.length; i++) {
        mejoresAlgoritmos.push(parseJSON(mejoresAlgoritmosJSON[i]));
    }
}

function cargarGraficaComparar() {

    rehacerJSON();

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

        for (var j = 0; j < nomArchivos.length; j++) {
            if (mejoresAlgoritmos[0][j * pos + i] && mejoresAlgoritmos[0][j * pos + i].tiempoEjecucion !== undefined) {
                dato.data.push(mejoresAlgoritmos[0][j * pos + i].tiempoEjecucion);
                console.log("DATOS QUE VA INTRODUCIENDO EN " + i + " " + (j * pos + 1) + ": " + mejoresAlgoritmos[0][j * pos + i].tiempoEjecucion);
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
        //Nombre del archivo / talla
        labels: nomArchivos,
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

