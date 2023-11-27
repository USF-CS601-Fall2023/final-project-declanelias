 function updatePage(event) {
    event.preventDefault();
        var form = document.getElementById('loginForm');
        console.log(form);
        const formData = new FormData(form);
        console.log(formData);
        fetch('/login', {
            method: 'POST',
            body: formData,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
        .then(res => res.json())
        .then(data => {
            console.log(data);
            document.getElementById("messageFromServer").innerHTML = data.message;
        })
        .catch(err => {
            console.log(err);
        });
}