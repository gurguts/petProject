window.onload = function () {
    const successMessage = localStorage.getItem('registrationSuccessMessage');
    if (successMessage) {
        document.getElementById('registrationSuccessMessage').innerText = successMessage;

        localStorage.removeItem('registrationSuccessMessage');
    }
}