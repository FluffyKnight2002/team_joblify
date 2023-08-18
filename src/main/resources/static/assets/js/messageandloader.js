// Function to submit form
function actionToVacancy(button) {

    const formId = $(button).data('form-id');
    const warningMessage = $(button).data('warning-message');
    const successMessage = $(button).data('success-message');
    const errorMessage = $(button).data('error-message');

    const csrfToken = document.querySelector('#token').value;
    console.log(csrfToken);
    const metaCsrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");

    console.log(metaCsrfToken);

    // Hide #detailModal if it exists
    let detailModal = $('#detailModal');
    if (detailModal.length) {
        console.log("Detail Modal Have")
        detailModal.modal('hide');
    }
    
    // Hide #detailModal if it exists
    let applyForm = $('#apply-form');
    if (applyForm.length) {
        console.log("Detail Modal Have")
        applyForm.modal('hide');
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
    let formData = {};
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
            if(formId === 'upload-form') {

                console.log("Upload form !!!!")

                const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
                let selectedDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri']; // Set default selected days
                let startTime = '9:00';
                let endTime = '18:00';

                function updateInputValue() {
                    console.log("Mon ~ Fri : ", selectedDays);

                    if (selectedDays.length === 5 && selectedDays.every(day => ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'].includes(day))) {
                        $('#workingDays').val('Mon ~ Fri');
                    } else if (selectedDays.length === 2 && selectedDays.includes('Sun') && selectedDays.includes('Sat')) {
                        $('#workingDays').val('Weekend');
                    } else {
                        let textSelectedDays = daysOfWeek.filter(day => selectedDays.includes(day));
                        $('#workingDays').val(textSelectedDays.join(' ~ '));
                    }

                    console.log("Start Time : ", startTime);
                    console.log("End Time : ",endTime);
                    // Format start and end times
                    const formattedStartTime = formatTime(startTime);
                    const formattedEndTime = formatTime(endTime);

                    // Update workingHours input value
                    $('#workingHours').val(`${formattedStartTime} ~ ${formattedEndTime}`);
                }
            }
            hideMessageModalAfterDelay();
            // To change back form
            if(formId == 'reopen-form'){
                $('#submit-btn')
                    .attr('data-form-id', 'update-form')
                    .attr('data-warning-message', 'Your vacancy will be updated.')
                    .attr('data-success-message', 'Update successful!')
                    .attr('data-error-message', 'Update failed. Please try again.')
                    .html('Update');
                reopenBtn.removeClass('btn-bright');
                reopenBtn.addClass('btn-un-bright');
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
            if(formId === 'upload-form') {

                console.log("Upload form !!!!")

                const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
                let selectedDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri']; // Set default selected days
                let startTime = '9:00';
                let endTime = '17:00';

                function updateInputValue() {
                    console.log("Mon ~ Fri : ", selectedDays);

                    if (selectedDays.length === 5 && selectedDays.every(day => ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'].includes(day))) {
                        $('#workingDays').val('Mon ~ Fri');
                    } else if (selectedDays.length === 2 && selectedDays.includes('Sun') && selectedDays.includes('Sat')) {
                        $('#workingDays').val('Weekend');
                    } else {
                        let textSelectedDays = daysOfWeek.filter(day => selectedDays.includes(day));
                        $('#workingDays').val(textSelectedDays.join(' ~ '));
                    }

                    console.log("Start Time : ", startTime);
                    console.log("End Time : ",endTime);
                    // Format start and end times
                    const formattedStartTime = formatTime(startTime);
                    const formattedEndTime = formatTime(endTime);

                    // Update workingHours input value
                    $('#workingHours').val(`${formattedStartTime} ~ ${formattedEndTime}`);
                }
            }
            // Handle the error response and update the message-con modal
            hideMessageModalAfterDelay();
            $('#message-con').html('<p class="text-white">' + errorMessage + '</p><div class="text-center"><button class="btn btn-sm btn-light" onclick="closeModal()">Close</button></div>'); // Replace with the content you want to show for error
        });
}

// Function to hide the message-con modal after a delay
function hideMessageModalAfterDelay() {

    if($('#detailModal').length > 0) {
        $('#detailModal').modal('hide');
    }

    // Remove .modal-backdrop if it exists
    let modalBackdrop = $('.modal-backdrop');
    if (modalBackdrop.length) {
        modalBackdrop.css("background", "transparent");
    }

    // Reload #table if it exists
    let table = $('#table');
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

function closeModal(formId) {
    let modal = $('#loadMe');
    if (modal.length) {
        modal.modal('hide');
    }
}

$(document).ready(function () {

    let formStatus = false;

    // Function to update formStatus based on input emptiness
    function updateFormStatus() {
        let emptyInputs = $('input[type="text"], input[type="number"], input#post, textarea').filter(function() {
            return $.trim($(this).val()) === '' && $(this).prop('required'); // Only consider required fields
        });

        formStatus = emptyInputs.length === 0;
    }

    function showFeedback(inputElement) {
        inputElement.addClass('is-invalid'); // Apply Bootstrap is-invalid class
        inputElement.css('background-image', 'none');
        // Show feedback message here
        // inputElement.siblings('.feedback-message').text('Field cannot be empty');
    }

    // Function to show feedback and change border color
    function clearFeedback(inputElement) {
        inputElement.removeClass('is-invalid'); // Remove Bootstrap is-invalid class
        inputElement.removeClass('is-valid'); // Remove Bootstrap is-valid class if previously added
        inputElement.css('background-image', 'none');
        // Clear feedback message here
        // Example: inputElement.siblings('.feedback-message').text('');
    }

    // Validate inputs on input change
    function  validate() {
        $('input[type="text"],input[type="number"],input#post, textarea').on('input', function() {
            let inputElement = $(this);
            if ($.trim(inputElement.val()) === '') {
                showFeedback(inputElement);
            } else {
                clearFeedback(inputElement);
                inputElement.addClass('is-valid'); // Apply Bootstrap is-valid class
                inputElement.css('background-image', 'none');
            }
            updateFormStatus(); // Update formStatus
        });
    }
    validate();

    $('#submit-btn').on('click', function (event) {
        event.preventDefault();

        validate();
        updateFormStatus();
        console.log("Form Status : ", formStatus);

        if (!formStatus) {
            // Find empty inputs and add 'is-invalid' class
            $('input[type="text"], input[type="number"], input#post, textarea').each(function() {
                if ($.trim($(this).val()) === '') {
                    $(this).addClass('is-invalid');
                    $(this).css('background-image', 'none');
                }
            });
        } else {
            // Get the formId, successMessage, and errorMessage from the event data attributes
            var formId = $(this).data('form-id');
            var warningMessage = $(this).data('warning-message');
            var successMessage = $(this).data('success-message');
            var errorMessage = $(this).data('error-message');

            console.log("FormId : ",formId);
            console.log("warningMessage : ", warningMessage);
            console.log("SuccessMessage : ",successMessage);
            console.log("ErrorMessage : ",errorMessage);

            // Show the loader and the message-con modal
            $('#message-con').html('' +
                '<div class="loader"></div>' +
                '<div class="loader-txt">' +
                '<h3 class="text-white">Are you sure?</h3>' +
                `<p class="text-center text-white">${warningMessage}</p>` +
                '<div>' +
                `<button type="button" class="btn btn-sm btn-light-danger mx-1" data-form-id="${formId}" 
            data-warning-message="${warningMessage}" data-success-message="${successMessage}" data-error-message="${errorMessage}"
            onclick="actionToVacancy(this)">Sure</button>` +
                `<button class="btn btn-sm btn-light mx-1" onclick="closeModal()"">Cancel</button></div>` +
                '</div>' +
                '</div>');
            $('#loadMe').modal({
                backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
            }).modal('show');
        }
    });

    // Close calendar and time picker containers when clicking outside
    $(document).on('click', function(event) {
        const $target = $(event.target);

        if ($target.is('#submit-btn')) {
            event.stopPropagation();
            return; // Don't execute the rest of the code if the target is the submit button
        }

        if (!$target.closest('#calendar-btn, #calendar, .calendar-day').length) {
            $('#calendar').hide();
        }

        if (!$target.closest('#timePickerBtn, #timePickerContainer').length) {
            $('#timePickerContainer').hide();
        }
    });

});