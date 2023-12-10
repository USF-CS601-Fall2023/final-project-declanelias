const reviewsPerPage = 5;
let data;
let hotelId;
let currentPage;

function fetchReviews(id) {
    hotelId = id;
    fetch('/getReviews', {
        method: 'POST',
        body: "hotelId=" + encodeURIComponent(hotelId),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => response.json())
    .then(receivedData => {
        data = receivedData;
        displayReviews(1);
        // Add pagination controls
        addPaginationControls(data.reviews.length);
    })
    .catch(error => console.error('Error fetching reviews:', error));
}

function displayReviews(page) {
    previousPage = currentPage;
    currentPage = page;
    document.getElementById('averageRating').innerHTML = data.averageRating;

    const reviewsContainer = document.getElementById('reviews');
    reviewsContainer.innerHTML = '';

    const startIndex = (currentPage - 1) * reviewsPerPage;
    const endIndex = startIndex + reviewsPerPage;
    const paginatedReviews = data.reviews.slice(startIndex, endIndex);

    paginatedReviews.forEach(review => {
        const reviewElement = createReviewElement(review, data.username, hotelId);
        reviewsContainer.appendChild(reviewElement);
    });
}

function addPaginationControls(totalReviews) {
    const numPages = Math.ceil(totalReviews / reviewsPerPage);
    const paginationContainer = document.getElementById('pagination');

    paginationContainer.innerHTML = ''; // Clear existing pagination

    const paginationList = document.createElement('ul');
    paginationList.classList.add('pagination'); // Add Bootstrap pagination class

    for (let i = 1; i <= numPages; i++) {
        const pageItem = document.createElement('li');
        const pageLink = document.createElement('a');
        pageLink.textContent = i;
        pageLink.addEventListener('click', function () {
            updateActivePage(i);
            displayReviews(i);
        });

        pageItem.appendChild(pageLink);

        if (i === currentPage) {
            pageItem.classList.add('active');
        }

        paginationList.appendChild(pageItem);
    }

    paginationContainer.appendChild(paginationList);
}

function updateActivePage(newPage) {
    const paginationContainer = document.getElementById('pagination');
    const pages = paginationContainer.querySelectorAll('.pagination li');

    pages.forEach(page => {
        page.classList.remove('active');
    });

    const currentPageItem = paginationContainer.querySelector(`.pagination li:nth-child(${newPage})`);
    currentPageItem.classList.add('active');
}



function createReviewElement(review, username, hotelId) {
    const reviewElement = document.createElement('div');
    reviewElement.className = 'review';

    const reviewHeader = document.createElement('div');
    reviewHeader.className = 'reviewHeader';
    reviewHeader.innerHTML = `<span>Review by <b>${review.user}</b></span> <br>
                              <span>${review.date}</span>`;
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
    const deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.textContent = 'Delete Review';
    deleteButton.addEventListener('click', function () {
        deleteReview(review, hotelId);
    });

    reviewElement.appendChild(deleteButton);
}

function deleteReview(review, hotelId) {
    fetch('/deleteReview', {
        method: 'POST',
        body: "reviewId=" + encodeURIComponent(review.reviewId) + "&hotelId=" + encodeURIComponent(hotelId),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(res => {
        fetchReviews(hotelId);
    })
    .catch(error => {
        console.error('Error:', error);
    });
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
    editedReviewTextarea.textContent = review.reviewText;
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
        document.getElementById("reviewText_" + reviewId).innerHTML = editedReview ;
        document.getElementById("reviewTitle_" + reviewId).innerHTML = "<h3>" + editedTitle + "</h3>";
    })
    .catch(error => {
        // Handle errors
        console.error('Error:', error);
    });
}

function addReview(hotelId) {
    var title = document.getElementById("title").value;
    var rating = document.getElementById("rating").value;
    var review = document.getElementById("review").value;

    var formData =  "hotelId=" + encodeURIComponent(hotelId) +
                    "&title=" + encodeURIComponent(title) +
                    "&review=" + encodeURIComponent(review) +
                    "&rating=" + encodeURIComponent(rating);

    fetch('/addReview', {
        method: 'POST',
        body: formData,
        headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        fetchReviews(hotelId);
    })
    .catch(error => {
        console.error('Error:', error);
    });

}