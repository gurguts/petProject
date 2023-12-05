document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const loginValue = document.getElementById('loginReg').value;
    const passwordValue = document.getElementById('passwordReg').value;

    fetch('/api/v1/reg/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            login: loginValue,
            password: passwordValue
        }),
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
        })
        .then(data => {
            const successMessage = document.getElementById('registrationSuccessMessage');
            successMessage.innerText = data;
            successMessage.style.display = 'block';

            document.getElementById('chk').checked = false;
        })
        .catch(error => {
            const errorMessageElement = document.getElementById('errorRegMessage');
            errorMessageElement.innerText = error.message;
            errorMessageElement.style.display = 'block';
        });
});