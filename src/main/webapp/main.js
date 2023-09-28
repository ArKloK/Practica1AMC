//const ctx = document.getElementById('grafica');

var xyValues = [
    {x: 50, y: 7},
    {x: 60, y: 8},
    {x: 70, y: 8},
    {x: 80, y: 9},
    {x: 90, y: 9},
    {x: 100, y: 9},
    {x: 110, y: 10},
    {x: 120, y: 11},
    {x: 130, y: 14},
    {x: 140, y: 14},
    {x: 150, y: 15}
];

var puntoLinea = [
    {x: 50, y: 7},
    {x: 60, y: 8}
];

new Chart("grafica", {
    type: "scatter",
    data: {
        datasets: [{
                pointRadius: 3,
                pointBackgroundColor: "red",
                data: xyValues
            }, {
                data: puntoLinea,
                borderColor: 'blue', // Color de la línea
                borderWidth: 1, // Ancho de la línea
                fill: false, // No llenar el área bajo la línea
                type: 'line' // Tipo de gráfico línea
            }]
    },
    options: {
        legend: {display: false},
        scales: {
            xAxes: [{ticks: {min: 40, max: 160}}],
            yAxes: [{ticks: {min: 6, max: 16}}],
        }
    }
});


