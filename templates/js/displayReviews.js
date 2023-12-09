function fetchReviews(hotelId) {
    fetch('/getReviews', {
            method: 'POST',
            body: "hotelId=" + encodeURIComponent(hotelId),
            headers: {
               'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
        .then(response => response.json())
        .then(data => {
            document.getElementById('averageRating').innerHTML = data.averageRating;
            const reviewsContainer = document.getElementById('reviews');
            reviewsContainer.innerHTML = '';

            data.reviews.forEach(review => {
                const reviewElement = createReviewElement(review, data.username, hotelId);
                reviewsContainer.appendChild(reviewElement);
            });
        })
        .catch(error => console.error('Error fetching reviews:', error));
}


function createReviewElement(review, username, hotelId) {
    const reviewElement = document.createElement('div');
    reviewElement.className = 'review';

    const reviewHeader = document.createElement('div');
    reviewHeader.className = 'reviewHeader';
    reviewHeader.innerHTML = `<span>Review by ${review.user}</span>
                              <span>Submission Date: ${review.date}</span>`;
    reviewElement.appendChild(reviewHeader);

    const reviewTitle = document.createElement('div');
    reviewTitle.id = `reviewTitle_${review.reviewId}`;
    reviewTitle.innerHTML = `<h3>${review.title}</h3>`;
    reviewElement.appendChild(reviewTitle);

    const reviewText = document.createElement('div');
    reviewText.id = `reviewText_${review.reviewId}`;
    reviewText.innerHTML = review.reviewText;
    reviewElement.appendChild(reviewText);

    if (username === review.user) {
        createEditForm(review, hotelId, reviewElement);
        createModal(review, hotelId, reviewElement);
        createDeleteForm(review, hotelId, reviewElement);
    }

    return reviewElement;
}

function createDeleteForm(review, hotelId, reviewElement) {
    // Create the form element
    const deleteForm = document.createElement('form');
    deleteForm.method = 'post';
    deleteForm.action = '/deleteReview';

    // Create and append the hidden inputs
    const reviewIdInput = document.createElement('input');
    reviewIdInput.type = 'hidden';
    reviewIdInput.name = 'reviewId';
    reviewIdInput.value = review.reviewId;
    deleteForm.appendChild(reviewIdInput);

    const hotelIdInput = document.createElement('input');
    hotelIdInput.type = 'hidden';
    hotelIdInput.name = 'hotelId';
    hotelIdInput.value = hotelId;
    deleteForm.appendChild(hotelIdInput);

    // Create the "Delete Review" button
    const deleteButton = document.createElement('button');
    deleteButton.type = 'submit';
    deleteButton.textContent = 'Delete Review';

    // Append the button to the form
    deleteForm.appendChild(deleteButton);

    reviewElement.appendChild(deleteForm);
}


function createModal(review, hotelId, reviewElement) {
    const overlay = document.createElement('div');
    overlay.id = 'overlay';
    overlay.className = 'overlay';
    overlay.addEventListener('click', closeModal); // Attach closeModal function to click event

    // Create the modal element
    const modal = document.createElement('div');
    modal.id = 'myModal';
    modal.className = 'modal';

    // Create the close button
    const closeButton = document.createElement('span');
    closeButton.textContent = '×';
    closeButton.style.cursor = 'pointer';
    closeButton.addEventListener('click', closeModal); // Attach closeModal function to click event
    modal.appendChild(closeButton);

    // Create the "New Title" input
    const editedTitleInput = document.createElement('input');
    editedTitleInput.id = 'editedTitle';
    editedTitleInput.type = 'text';
    editedTitleInput.name = 'reviewTitle';
    editedTitleInput.value = review.title; // Set initial value
    modal.appendChild(document.createTextNode('New Title: '));
    modal.appendChild(editedTitleInput);
    modal.appendChild(document.createElement('br'));

    // Create the "New Review" textarea
    const editedReviewTextarea = document.createElement('textarea');
    editedReviewTextarea.id = 'editedReview';
    editedReviewTextarea.name = 'editedReview';
    editedReviewTextarea.rows = '4';
    editedReviewTextarea.required = true;
    editedReviewTextarea.textContent = 'Hello!'; // Set initial value
    modal.appendChild(document.createTextNode('New Review: '));
    modal.appendChild(editedReviewTextarea);
    modal.appendChild(document.createElement('br'));

    // Create the "Submit Edited Review" button
    const submitButton = document.createElement('button');
    submitButton.type = 'button';
    submitButton.textContent = 'Submit Edited Review';
    submitButton.addEventListener('click', function () {
        submitForm(review.reviewId, hotelId);
    });
    modal.appendChild(submitButton);

    // Append the overlay and modal to the body or another appropriate container
    reviewElement.appendChild(overlay);
    reviewElement.appendChild(modal);

}


function createEditForm(review, hotelId, reviewElement) {
    const editForm = document.createElement('form');
    editForm.id = 'editForm';
    editForm.method = 'post';
    editForm.action = '/editReview';

    // Create and append the hidden inputs
    const reviewIdInput = document.createElement('input');
    reviewIdInput.type = 'hidden';
    reviewIdInput.name = 'reviewId';
    reviewIdInput.value = review.reviewId;
    editForm.appendChild(reviewIdInput);

    const hotelIdInput = document.createElement('input');
    hotelIdInput.type = 'hidden';
    hotelIdInput.name = 'hotelId';
    hotelIdInput.value = hotelId;
    editForm.appendChild(hotelIdInput);

    // Create the "Edit Review" button
    const editButton = document.createElement('button');
    editButton.type = 'button';
    editButton.textContent = 'Edit Review';

    // Attach the openModal function to the button's click event
    editButton.addEventListener('click', function () {
        openModal();
    });

    // Append the button to the form
    editForm.appendChild(editButton);

    // Append the form to the body or another appropriate container
    reviewElement.appendChild(editForm);
}