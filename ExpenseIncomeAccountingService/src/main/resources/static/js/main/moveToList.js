const button = document.getElementById('listButton');

button.addEventListener('click', function() {
    window.location.href = '/api/v1/main/list';
});