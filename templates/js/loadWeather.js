function loadWeather(lat, lng) {

    fetch('/getWeather', {
        method: 'POST',
        body: "lat=" + encodeURIComponent(lat) + "&lng=" + encodeURIComponent(lng),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(res => res.json())
    .then(data => {
        let str = "Temperature: " + data.temperature + "ºC<br> Windspeed: " + data.windspeed + " MPH";
        document.getElementById("weather").innerHTML = str;
    })
    .catch(error => {
            console.error('Error:', error);
    });
}