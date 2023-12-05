function handleRowClick(event) {
    document.querySelectorAll('#dataTable tr').forEach(function (row) {
        row.classList.remove('highlighted');
    });

    const clickedRow = event.currentTarget;
    clickedRow.classList.add('highlighted');

    const row = event.currentTarget;
    const itemId = row.getAttribute('data-id');

    const deleteButtonContainer = document.getElementById('actionButtonsContainer');
    const deleteButton = document.getElementById('deleteButton');
    deleteButtonContainer.style.display = 'block';
    deleteButton.onclick = function () {

        fetch(`http://localhost:8081/api/v1/mm/${itemId}`, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) {

                clickedRow.remove();
                deleteButtonContainer.style.display = 'none';
            } else {
                console.error('Error deleting item:', itemId);
            }
        }).catch(error => {
            console.error('Error:', error);
        });
    };

    const actionButtonsContainer = document.getElementById('actionButtonsContainer');
    actionButtonsContainer.style.display = 'block';

    const changeButton = document.getElementById('changeButton');
    changeButton.onclick = function () {

        const editFormContainer = document.getElementById('editFormContainer');
        editFormContainer.style.display = 'block';

        const cells = clickedRow.querySelectorAll('td');
        document.getElementById('editDescription').value = cells[1].textContent;
        document.getElementById('editAmount').value = cells[2].textContent;
        document.getElementById('editDate').value = cells[3].textContent;
        document.getElementById('editType').value = cells[4].textContent.toLowerCase();

        document.getElementById('submitEdit').onclick = function () {
            const updatedData = {
                login: localStorage.getItem('loginValue'),
                description: document.getElementById('editDescription').value,
                amount: document.getElementById('editAmount').value,
                date: document.getElementById('editDate').value,
                type: document.getElementById('editType').value.toUpperCase()
            };

            fetch(`http://localhost:8081/api/v1/mm/${itemId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatedData)
            }).then(response => {
                if (response.ok) {
                    loadDataFunction();
                } else {
                    console.error('Error updating item:', itemId);
                }
            }).catch(error => {
                console.error('Error:', error);
            });

            editFormContainer.style.display = 'none';
        };
    };
}