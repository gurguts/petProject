document.addEventListener('DOMContentLoaded', function () {
    fetchBalance();
});

function fetchBalance() {
    const login = localStorage.getItem('loginValue');
    fetch('http://localhost:8081/api/v1/counting/balance/' + encodeURIComponent(login))
        .then(response => response.json())
        .then(balance => {
            const balanceElement = document.getElementById('balance');
            balanceElement.innerText = "BALANCE: " + balance;

            if (balance < 0) {
                balanceElement.style.color = 'red';
            } else {
                balanceElement.style.color = 'green';
            }
        })
        .catch(error => console.error('Error:', error));
}