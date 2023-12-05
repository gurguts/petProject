let myChart = null;

function buildChart(diagramData) {
    const months = diagramData.map(data => formatMonth(data.date));
    const balances = diagramData.map(data => data.balance);
    const backgroundColors = balances.map(balance => balance >= 0 ? 'green' : 'red');

    const ctx = document.getElementById('myChart').getContext('2d');

    if (myChart) {
        myChart.destroy();
    }
    myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: '',
                data: balances,
                backgroundColor: backgroundColors,
                borderColor: backgroundColors,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function formatMonth(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const formattedMonth = month < 10 ? `0${month}` : month;
    return `${year}-${formattedMonth}`;
}

function updateChart() {
    if (!myChart) {
        console.error('Chart not created yet');
        return;
    }
    const login = localStorage.getItem('loginValue');
    fetch(`http://localhost:8081/api/v1/counting/diagram/` + encodeURIComponent(login))
        .then(response => response.json())
        .then(data => {
            myChart.data.labels = data.map(data => formatMonth(data.date));
            myChart.data.datasets[0].data = data.map(data => data.balance);

            myChart.data.datasets[0].backgroundColor = data.map(data => data.balance >= 0 ? 'green' : 'red');
            myChart.data.datasets[0].borderColor = data.map(data => data.balance >= 0 ? 'green' : 'red');

            myChart.update();
        })
        .catch(error => console.error('Ошибка:', error));
}

function fetchDiagramData() {
    const login = localStorage.getItem('loginValue');
    fetch(`http://localhost:8081/api/v1/counting/diagram/` + encodeURIComponent(login))
        .then(response => response.json())
        .then(data => {
            buildChart(data);
        })
        .catch(error => console.error('Error:', error));
}

document.addEventListener("DOMContentLoaded", function () {
    fetchDiagramData();
});