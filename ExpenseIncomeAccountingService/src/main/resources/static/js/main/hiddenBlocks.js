document.getElementById('addIncomeBtn').addEventListener('click', function() {
    const form = document.getElementById('incomeForm');
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
});


document.getElementById('addExpenseBtn').addEventListener('click', function() {
    const form = document.getElementById('expenseForm');
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
});
