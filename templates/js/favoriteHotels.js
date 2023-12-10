function favoriteHotel(id) {

    console.log(id);

    fetch('/addFavorite', {
        method: 'POST',
        body: "id=" + encodeURIComponent(id),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        console.log('Response:', response);
        var url = '/hotel?hotelId=' + id;
        window.location.href = url;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function removeFavoriteHotel(id) {

    console.log(id);

    fetch('/removeFavorite', {
        method: 'POST',
        body: "hotelId=" + encodeURIComponent(id),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        var url = '/hotel?hotelId=' + id;
        window.location.href = url;
        console.log('Response:', response);
    })
    .catch(error => {
        console.error('Error:', error);
    });


}