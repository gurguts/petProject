document.addEventListener("DOMContentLoaded", function () {
    loadDataFunction();
});

function loadDataFunction() {
    const dataTableBody = document.querySelector("#dataTable tbody");
    dataTableBody.innerHTML = "";
    const login = localStorage.getItem('loginValue');

    fetch("http://localhost:8081/api/v1/mm/" + encodeURIComponent(login), {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(data => {

            data.forEach(function (item) {

                const row = document.createElement("tr");
                row.setAttribute('data-id', item.id);

                if (item.type === 'INCOME') {
                    row.classList.add('income-row');
                } else if (item.type === 'EXPENSE') {
                    row.classList.add('expense-row');
                }

                row.innerHTML = `
                    <td>${item.id}</td>
                    <td>${item.description}</td>
                    <td>${item.amount}</td>
                    <td>${item.date}</td>
                    <td>${item.type}</td>
                `;
                row.addEventListener('click', handleRowClick);
                dataTableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error("Error loading data: " + error);
        });
}
