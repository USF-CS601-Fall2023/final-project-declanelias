function favoriteHotel(id) {

    fetch('/favoriteHotel', {
        method: 'POST',
        body: "id=" + encodeURIComponent(id),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        console.log('Response:', response);
    })
    .catch(error => {
            console.error('Error:', error);
    });
}