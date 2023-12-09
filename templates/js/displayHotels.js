function displayHotels() {

    var word = document.getElementById("search").value;

    fetch('/hotelSearch', {
        method: 'POST',
        body: "word=" + encodeURIComponent(word),
        headers: {
           'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(res => res.json())
    .then(data => {
        const hotelsContainer = document.getElementById('hotels');
        hotelsContainer.innerHTML = '';
        data.hotels.forEach(hotel => {
            const hotelElement = createHotelElement(hotel);
            hotelsContainer.appendChild(hotelElement);
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function createHotelElement(hotel) {
    const hotelElement = document.createElement('a');
    hotelElement.href = "/hotel?hotelId=" + hotel.hotelId;
    hotelElement.className = 'list-group-item';
    hotelElement.textContent = hotel.name;
    return hotelElement;
}