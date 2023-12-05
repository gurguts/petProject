
document.addEventListener("DOMContentLoaded", function() {
    const logoutButton = document.getElementById("logoutButton");
    if (logoutButton) {
        logoutButton.addEventListener("click", function() {
            fetch('http://localhost:8080/api/v1/auth/logout', {
                method: 'GET',
                credentials: 'include'
            }).then(() => {
                window.location.href = 'http://localhost:8080/';
            });
        });
    } else {
        console.error("Element with id='"+logoutButton+"' was not found.");
    }
});