function clearHistory() {
    fetch('/history', {
            method: 'POST'
        })
        .then(response => {
            document.getElementById("tableBody").innerHTML = "";
        })
        .catch(error => {
                console.error('Error:', error);
        });
}