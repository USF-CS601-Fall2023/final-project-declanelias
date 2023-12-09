function removeFavorite(id) {
    fetch('/removeFavorite', {
            method: 'POST',
            body: "hotelId=" + encodeURIComponent(id),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
        .then(response => {
            var idString = "hotel_" + id;
            console.log(idString);
            document.getElementById(idString).remove();
        })
        .catch(error => {
                console.error('Error:', error);
        });

}