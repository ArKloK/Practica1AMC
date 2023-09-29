var xyValues = [];
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
            label:"Puntos mas cercanos",
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