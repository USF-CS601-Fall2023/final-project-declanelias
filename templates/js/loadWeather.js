function loadWeather(lat, lng) {

    fetch('/getWeather', {
        method: 'POST',
        body: "lat=" + encodeURIComponent(lat) + "&lng=" + encodeURIComponent(lng),
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