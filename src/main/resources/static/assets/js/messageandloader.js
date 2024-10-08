// Function to submit form
function actionToVacancy(button) {

    const formId = $(button).data('form-id');
    const warningMessage = $(button).data('warning-message');
    const successMessage = $(button).data('success-message');
    const errorMessage = $(button).data('error-message');
    // console.log(csrfToken);
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const metaCsrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");

    console.log(metaCsrfToken);

    // Hide #detailModal if it exists
    let detailModal = $('#detailModal');
    if (detailModal.length) {
        console.log("Detail Modal Have")
        detailModal.modal('hide');
    }

    // Show the loader and the message-con modal
    $('#message-con').html('<div class="loader"></div><div class="loader-txt"><p class="text-white">Processing...</p></div>');
    $('#loadMe').modal({
        backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
    }).modal('show');

    let formData;

    if ($('form#job-apply').length > 0) {
        const formElement = document.querySelector('form#job-apply'); // Select the form using plain JavaScript
        console.log("Form job-apply exit",formElement);
        formData = new FormData(formElement); // Use the form itself
        console.log("Form Data :", formData);
    } else {
        // Serialize the form data to JSON manually
        formData = {};
        $('#' + formId).serializeArray().forEach(item => {
            formData[item.name] = item.value;
        });
    }

    console.log("Url : ", $('#' + formId).attr('action'));

    let bodyData = JSON.stringify(formData);

    if ($('form#job-apply').length > 0) {
        $('#tech-skills-input').val(Array.from(techSkillsInput));
        $('#language-skills-input').val(Array.from(languageSkillsInput));
        formData.append('techSkillsString', $('#tech-skills-input').val());
        formData.append('languageSkillsString', $('#language-skills-input').val());
        bodyData = formData;
    }

    console.log("Body Data :",bodyData);

    // Submit the form using AJAX
    if($('form#job-apply').length > 0) {
        fetch($('#' + formId).attr('action'), {
            method: 'POST',
            headers: {
                'X-XSRF-Token': metaCsrfToken
            },
            body: bodyData,// Pass the form data as JSON in the 'body' property
        })
            .then(
                // Clear the form data after the fetch call is completed
                $('#' + formId)[0].reset()
            )
            .then(response => response.json())
            .then(data => {

                makeAfterRequestSend(formId, data, successMessage, errorMessage);
            })
            .catch(error => {
                renderCatch(formId, errorMessage);
            });
        if(formId === 'reopen-form'){
            console.log("REOPEN FORM EXIT")
            $('#submit-btn')
                .attr('data-form-id', 'update-form')
                .attr('data-warning-message', 'Your vacancy will be updated.')
                .attr('data-success-message', 'Update successful!')
                .attr('data-error-message', 'Update failed. Please try again.')
                .html('Update');
            reopenBtn.removeClass('btn-bright');
            reopenBtn.addClass('btn-un-bright');
            // reopenModeWarn.hide();
            $('#reopen-form')
                .attr('id', 'update-form')
                .attr('action', 'update-vacancy');
        }

    }else {
        fetch($('#' + formId).attr('action'), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-Token': metaCsrfToken
            },
            body: bodyData,// Pass the form data as JSON in the 'body' property
        })
            .then(
                // Clear the form data after the fetch call is completed
                $('#' + formId)[0].reset()
            )
            .then(response => response.json())
            .then(data => {
                makeAfterRequestSend(formId,data,successMessage,errorMessage);
            })
            .catch(error => {
                renderCatch(formId,errorMessage);
            });
        if(formId === 'reopen-form'){
            console.log("REOPEN FORM EXIT")
            $('#submit-btn')
                .attr('data-form-id', 'update-form')
                .attr('data-warning-message', 'Your vacancy will be updated.')
                .attr('data-success-message', 'Update successful!')
                .attr('data-error-message', 'Update failed. Please try again.')
                .html('Update');
            reopenBtn.removeClass('btn-bright');
            reopenBtn.addClass('btn-un-bright');
            // reopenModeWarn.hide();
            $('#reopen-form')
                .attr('id', 'update-form')
                .attr('action', 'update-vacancy');
        }
    }
}

function makeAfterRequestSend(formId, data,successMessage,errorMessage) {
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

    console.log("After that....")
    console.log("FormId : ",$('#submit-btn').attr('data-form-id'));
    console.log("SuccessMessage : ",$('#submit-btn').attr('data-success-message'));
    console.log("ErrorMessage : ",$('#submit-btn').attr('data-error-message'));

}

function renderCatch(formId,errorMessage) {
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
}

// Function to hide the message-con modal after a delay
function hideMessageModalAfterDelay() {

    if(typeof techSkillsInput  != undefined) {
        techSkillsInput = new Set;
        $('.tech-skills-block').empty();
    }

    if(typeof languageSkillsInput != undefined) {
        languageSkillsInput = new Set;
        $('.language-skills-block').empty();
    }

    if($('form#job-apply').length > 0) {
        $('#dob').removeClass('is-valid');
        $('#phone').removeClass('is-valid');
        $('#email').removeClass('is-valid');
        $('#resume').removeClass('is-valid');
    }

    if($('#detailModal').length > 0) {
        $('#detailModal').modal('hide');
    }

    if($('#apply-form').length > 0) {
        $('#apply-form').modal('hide');
    }

    $('input[type="text"],input[type="number"],input#post, textarea').each(function() {
        // console.log("Descriptions  :", $('#descriptions'))
        let inputElement = $(this);
        inputElement.removeClass('is-valid'); // Apply Bootstrap is-valid class
        inputElement.css('background-image', 'none');
    });

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

    if($('form#job-apply').length === 0) {
        // Scroll to the specific section after the fetch call is completed
        const elementToScrollTo = $('#app');
        $('html, body').animate({
            scrollTop: elementToScrollTo.offset().top
        }, 500); // Adjust the animation speed as needed (800 milliseconds in this case)
    }

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
            if ($(this).attr('name') === 'salary') {
                console.log("It was salary")
                return false; // Skip this input
            }

            return $(this).prop('required') && ($.trim($(this).val()) === '' || ($.trim($(this).val()) <= 0));
        });

        emptyInputs.each(function () {
            if($(this).val() === '' || $(this).val() === '0') {
                $(this).closest('.mb-3').find('.feedback-message').css('display','block'); // Show feedback message;
            }else {
                $(this).closest('.mb-3').find('.feedback-message').css('display','none');
            }
        });

        formStatus = emptyInputs.length === 0;

        console.log("Emtpy Inputs ", emptyInputs)
        console.log("Emtpy Length ", emptyInputs.length)

        if($('form#job-apply').length > 0) {
            console.log("JOB APPLY")
            validateFormStatus();
        }
        if ($('form#update-form').length > 0 && formStatus) {
            formStatus = vacancyInfoSameOrNot();
        }
    }

    function vacancyInfoSameOrNot() {
        console.log("Update Form");
        const formElement = document.querySelector('form#update-form');
        console.log("Form Element :",formElement)
        const formElementData = new FormData(formElement);

        console.log("FormDataString ", JSON.stringify(formElementData));
        console.log("CurrentDataString", JSON.stringify(currentData)); // Make sure to stringify currentData

        // Compare the JSON s trings
        if (isDataSame(formElementData, currentData)) {
            // Display a notification using iziToast.js
            iziToast.show({
                title: '<i class="bi bi-exclamation-triangle-fill"></i>',
                message: 'No changes were made. Vacancy information are still the same.',
                position: 'topCenter',
                timeout: 3000,
                backgroundColor: 'rgb(255,191,140)',
                progressBarColor: 'red', // Set the progress bar color to red
                theme: 'dark', // Optionally, you can set the theme to 'dark' to ensure the text color is visible on the red background
                onClosed: function () {
                }
            });
            return false;
        }

        return true;

        function isDataSame(formElement, currentData) {
            // Assuming currentData is already parsed from JSON
            console.log(parseInt(formElement.get('post')));
            console.log(currentData.post)
            console.log(reconvertToString(formElement.get('type')).toLowerCase());
            console.log(currentData.type.toLowerCase())
            console.log(reconvertToString(formElement.get('lvl')).toLowerCase())
            console.log(currentData.lvl.toLowerCase())
            console.log(formElement.get('salary'));
            console.log(currentData.salary)
            console.log(reconvertToString(formElement.get('onSiteOrRemote')).toLowerCase())
            console.log(currentData.onSiteOrRemote.toLowerCase())
            console.log(formElement.get('descriptions'))
            console.log(currentData.descriptions)
            console.log(formElement.get('responsibilities'))
            console.log(currentData.responsibilities)
            console.log(formElement.get('requirements'))
            console.log(currentData.requirements)
            console.log(formElement.get('preferences'))
            console.log(currentData.preferences)
            console.log(formElement.get('address'))
            console.log(currentData.address)
            console.log(formElement.get('note'))
            console.log(currentData.note)

            let salary = formElement.get('salary') === '' ? 0 : formElement.get('salary');

            if (
                parseInt(formElement.get('post')) === currentData.post &&
                reconvertToString(formElement.get('type')).toLowerCase() === currentData.type.toLowerCase() &&
                reconvertToString(formElement.get('lvl')).toLowerCase() === currentData.lvl.toLowerCase() &&
                parseInt(salary) === currentData.salary &&
                reconvertToString(formElement.get('onSiteOrRemote')).toLowerCase() === currentData.onSiteOrRemote.toLowerCase() &&
                formElement.get('descriptions') === currentData.descriptions &&
                areStringsEqual(formElement.get('responsibilities'), currentData.responsibilities) &&
                areStringsEqual(formElement.get('requirements'), currentData.requirements) &&
                areStringsEqual(formElement.get('preferences'), currentData.preferences) &&
                areStringsEqual(formElement.get('address'), currentData.address) &&
                areStringsEqual(formElement.get('note'), currentData.note)
            ) {
                return true;
            }
            return false;
        }
    }

    function areStringsEqual(str1, str2) {
        // Remove tabs and whitespace before comparison
        str1 = str1.replace(/\s/g, '');
        str2 = str2.replace(/\s/g, '');

        return str1 === str2;
    }

    function showFeedback(inputElement) {
        inputElement.addClass('is-invalid'); // Apply Bootstrap is-invalid class
        inputElement.css('background-image', 'none');
        console.log("Show feed back : " ,inputElement.closest('.mb-3').find('.feedback-message'));
        inputElement.closest('.mb-3').find('.feedback-message').css('display','block'); // Show feedback message
        // Show feedback message here
        // inputElement.siblings('.feedback-message').text('Field cannot be empty');
    }

    // Function to show feedback and change border color
    function clearFeedback(inputElement) {
        inputElement.removeClass('is-invalid'); // Remove Bootstrap is-invalid class
        inputElement.removeClass('is-valid'); // Remove Bootstrap is-valid class if previously added
        inputElement.css('background-image', 'none');
        inputElement.closest('.mb-3').find('.feedback-message').css('display','none');
    }

    // Validate inputs on input change
    function  validate() {
        $('input[type="text"],input[type="number"],input#post, textarea').on('input', function() {
            // console.log("Descriptions  :", $('#descriptions'))
            let inputElement = $(this);
            if (inputElement.attr('name') != 'salary') {

                if ($.trim(inputElement.val()) === '' || $.trim(inputElement.val()) <= '0') {
                    showFeedback(inputElement);
                } else {
                    clearFeedback(inputElement);
                    inputElement.addClass('is-valid'); // Apply Bootstrap is-valid class
                    inputElement.css('background-image', 'none');
                }
            }
            if (inputElement.val() === '0' || (inputElement.val().startsWith('0') || inputElement.val().startsWith(' ') && !inputElement.val().match(/\.\d+/))) {
                // Display the placeholder value and remove the leading '0'
                inputElement.val(inputElement.attr('placeholder') || '');
            }
            // updateFormStatus(); // Update formStatus
        });
    }

    validate();

    $('#submit-btn').on('click', function (event) {
        event.preventDefault();

        // validate();
        // if(formId === 'job-apply') {
        //     validateForm();
        // }else {
            updateFormStatus();
        // }
            console.log("Form Status : ", formStatus);

        if (!formStatus) {
            // Find empty inputs and add 'is-invalid' class
            $('input[type="text"], input[type="number"], input#post, textarea').each(function() {
                if($(this).attr('name') != 'salary') {
                    if ($.trim($(this).val()) === '' && $(this).prop('required')) {
                        $(this).addClass('is-invalid');
                        $(this).css('background-image', 'none');
                    }
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
                `<button type="button" class="btn btn-sm btn-light mx-1" data-form-id="${formId}" 
            data-warning-message="${warningMessage}" data-success-message="${successMessage}" data-error-message="${errorMessage}"
            onclick="actionToVacancy(this)">Sure</button>` +
                `<button class="btn btn-sm btn-secondary mx-1" onclick="closeModal()"">Cancel</button></div>` +
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