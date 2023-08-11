$('#submit-btn').on('click', function (event) {
    event.preventDefault();
    // Get the formId, successMessage, and errorMessage from the event data attributes
    var formId = $(this).data('form-id');
    var successMessage = $(this).data('success-message');
    var errorMessage = $(this).data('error-message');

    console.log("FormId : ",formId);
    console.log("SuccessMessage : ",successMessage);
    console.log("ErrorMessage : ",errorMessage);

    const csrfToken = document.querySelector('#token').value;
    console.log(csrfToken);
    const metaCsrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");

    console.log(metaCsrfToken);

    // Hide #detailModal if it exists
    var detailModal = $('#detailModal');
    if (detailModal.length) {
        console.log("Detail Modal Have")
        detailModal.modal('hide');
    }

    // Show the loader and the message-con modal
    $('#message-con').html('<div class="loader"></div><div class="loader-txt"><p class="text-white">Processing...</p></div>');
    $('#loadMe').modal({
        backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
    }).modal('show');
    console.warn(inputsToDisable);
    // console.log("Result : " ,inputsToDisable != undefined);
    if (inputsToDisable.length != 0) {
        inputsToDisable.prop('disabled', false);
        reopenModeWarn.hide();
    }

    // Serialize the form data to JSON manually
    var formData = {};
    $('#' + formId).serializeArray().forEach(item => {
        formData[item.name] = item.value;
    });

    console.log("FormData : " ,JSON.stringify(formData))

    console.log("FormData : " ,JSON.stringify(formData))
    console.log("Url : ", $('#' + formId).attr('action'));

    // Submit the form using AJAX
    fetch($('#' + formId).attr('action'), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-Token': metaCsrfToken
        },
        body: JSON.stringify(formData),// Pass the form data as JSON in the 'body' property
    })
        .then(
            // Clear the form data after the fetch call is completed
            $('#' + formId)[0].reset()
        )
        .then(response => response.json())
        .then(data => {
            if (data === true) {
                // Handle the success response and update the message-con modal for success
                $('#message-con').html('<p class="text-white">' + successMessage + '</p><div class="text-center"><button class="btn btn-sm btn-light" onclick="closeModal()">Okay</button></div>');
            } else {
                // Handle the response and update the message-con modal for error
                $('#message-con').html('<p class="text-white">' + errorMessage + '</p><div class="text-center"><button class="btn btn-sm btn-light" onclick="closeModal()">Close</button></div>');
            }
            hideMessageModalAfterDelay();
            // To change back form
            if(formId == 'reopen-form'){
                $('#submit-btn')
                    .attr('data-form-id', 'update-form')
                    .attr('data-success-message', 'Update successful!')
                    .attr('data-error-message', 'Update failed. Please try again.')
                    .html('Update');
                reopenModeWarn.hide();
                $('#reopen-form')
                    .attr('id', 'update-form')
                    .attr('action', 'update-vacancy');
            }

            console.log("After that....")
            console.log("FormId : ",formId);
            console.log("SuccessMessage : ",successMessage);
            console.log("ErrorMessage : ",errorMessage);
        })
        .catch(error => {
            // Handle the error response and update the message-con modal
            $('#message-con').html('<p class="text-white">' + errorMessage + '</p><div class="text-center"><button class="btn btn-sm btn-light" onclick="closeModal()">Close</button></div>'); // Replace with the content you want to show for error
            hideMessageModalAfterDelay();
        });
});

// Function to hide the message-con modal after a delay
function hideMessageModalAfterDelay() {
    // Remove .modal-backdrop if it exists
    var modalBackdrop = $('.modal-backdrop');
    if (modalBackdrop.length) {
        modalBackdrop.remove();
    }

    // Reload #table if it exists
    var table = $('#table');
    if (table.length) {
        console.log("table reloaded")
        table.DataTable().ajax.reload(null, false);
    }

    // Scroll to the specific section after the fetch call is completed
    const elementToScrollTo = $('#app');
    $('html, body').animate({
        scrollTop: elementToScrollTo.offset().top
    }, 500); // Adjust the animation speed as needed (800 milliseconds in this case)
}

function closeModal() {
    var modal = $('#loadMe');
    if (modal.length) {
        modal.modal('hide');
    }
}