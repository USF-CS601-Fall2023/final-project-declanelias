function openModal() {
    document.getElementById("overlay").style.display = "block";
    document.getElementById("myModal").style.display = "block";
}

function closeModal() {
    document.getElementById("overlay").style.display = "none";
    document.getElementById("myModal").style.display = "none";
}

function submitForm(reviewId, hotelId) {
    var editedReview = document.getElementById("editedReview").value;
    var editedTitle = document.getElementById("editedTitle").value;

    var formData =  "reviewId=" + encodeURIComponent(reviewId) +
                    "&hotelId=" + encodeURIComponent(hotelId) +
                    "&editedReview=" + encodeURIComponent(editedReview) +
                    "&editedTitle=" + encodeURIComponent(editedTitle);


    console.log(formData);

    fetch('/editReview', {
        method: 'POST',
        body: formData,
        headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        console.log('Response:', response);
        closeModal();
        document.getElementById("reviewText_" + reviewId).innerHTML = editedReview;
        document.getElementById("reviewTitle_" + reviewId).innerHTML = editedTitle;
    })
    .catch(error => {
        // Handle errors
        console.error('Error:', error);
    });
}