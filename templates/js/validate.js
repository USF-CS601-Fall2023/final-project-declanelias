function validate(event, fetchEndpoint, endpoint) {
    event.preventDefault();

    const username =  document.querySelector('input[name="username"]').value;
    const password =  document.querySelector('input[name="password"]').value;

    const data = {
        username: username,
        password: password
    };

    fetch(fetchEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(res => res.json())
        .then(data => {
            console.log(data);
            if (!data.success) {
                document.getElementById("messageFromServer").innerHTML = data.message
            } else {
                window.location.href = endpoint
            }
        })
        .catch(err => {
            console.log(err);
        });
}