function clearHistory() {
    fetch('/history', {
            method: 'POST'
        })
        .then(response => {
            document.getElementById("links").innerHTML = "";
        })
        .catch(error => {
                console.error('Error:', error);
        });
}