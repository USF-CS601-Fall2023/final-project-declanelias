function saveLink(link) {

    console.log(link);

    fetch('/saveLink', {
        method: 'POST',
        body: "link=" + encodeURIComponent(link),
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