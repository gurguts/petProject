document.getElementById('editType').addEventListener('change', function () {
    const type = this.value;
    const descriptionSelect = document.getElementById('editDescription');
    descriptionSelect.innerHTML = '';

    if (type === 'income') {

        const incomeOptions = [
            {value: 'salary', text: 'Salary'},
            {value: 'freelance', text: 'Freelance'},
            {value: 'investment', text: 'Investment'},
            {value: 'business', text: 'Business'},
            {value: 'benefits', text: 'Benefits'},
            {value: 'donations', text: 'Donations'},
            {value: 'rent', text: 'Rent'}
        ];
        incomeOptions.forEach(function (option) {
            const opt = document.createElement('option');
            opt.value = option.value;
            opt.textContent = option.text;
            descriptionSelect.appendChild(opt);
        });
    } else if (type === 'expense') {

        const expenseOptions = [
            {value: 'bills', text: 'Bills'},
            {value: 'groceries', text: 'Groceries'},
            {value: 'entertainment', text: 'Entertainment'},
            {value: 'transport', text: 'Transport'},
            {value: 'housing', text: 'Housing'},
            {value: 'medicine', text: 'Medicine'},
            {value: 'education', text: 'Education'},
            {value: 'cloth', text: 'Cloth'},
            {value: 'present', text: 'Present'},
            {value: 'insurance', text: 'Insurance'}
        ];
        expenseOptions.forEach(function (option) {
            const opt = document.createElement('option');
            opt.value = option.value;
            opt.textContent = option.text;
            descriptionSelect.appendChild(opt);
        });
    }
});