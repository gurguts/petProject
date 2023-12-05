document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const loginValue = document.getElementById('login').value;
    const passwordValue = document.getElementById('password').value;

    fetch('/api/v1/auth/login', {
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
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.token) {
                document.cookie = `authToken=${data.token};max-age=3600;path=/;`;
                loadMainPage();
            } else {
                throw new Error('Token was not received');
            }
        })
        .catch(error => {
            console.error('Error:', error);

            const errorMessageDiv = document.getElementById('errorMessage');
            errorMessageDiv.textContent = error.message;
            errorMessageDiv.style.display = 'block';
        });
});

function loadMainPage() {
    window.location.href = 'http://localhost:8081/api/v1/main';
}