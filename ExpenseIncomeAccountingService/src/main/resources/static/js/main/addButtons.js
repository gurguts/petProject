document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('income-form');
    document.getElementById('income-form').addEventListener('submit', function (event) {
        event.preventDefault();

        const incomeAmount = document.getElementById('incomeAmount').value;
        const incomeDate = document.getElementById('incomeDate').value;
        const incomeDescription = document.getElementById('incomeDescription').value;
        const incomeLogin = localStorage.getItem('loginValue');
        const incomeType = 'INCOME';

        fetch('http://localhost:8081/api/v1/mm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                amount: incomeAmount,
                date: incomeDate,
                description: incomeDescription,
                login: incomeLogin,
                type: incomeType
            }),
        })
            .then(response => response.json())
            .then(() => {
                form.reset();
                fetchBalance();
                updateChart()
            })
            .catch(error => console.error('Error:', error));
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('expense-form');
    document.getElementById('expense-form').addEventListener('submit', function (event) {
        event.preventDefault();

        const expenseAmount = document.getElementById('expenseAmount').value;
        const expenseDate = document.getElementById('expenseDate').value;
        const expenseDescription = document.getElementById('expenseDescription').value;
        const expenseLogin = localStorage.getItem('loginValue');
        const expenseType = 'EXPENSE';

        fetch('http://localhost:8081/api/v1/mm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                amount: expenseAmount,
                date: expenseDate,
                description: expenseDescription,
                login: expenseLogin,
                type: expenseType
            }),
        })
            .then(response => response.json())
            .then(() => {
                form.reset();
                fetchBalance();
                updateChart()
            })
            .catch(error => console.error('Error:', error));
    });
});