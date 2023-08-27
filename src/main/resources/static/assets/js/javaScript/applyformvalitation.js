const name = $('#name');
const dob = $('#dob');
const phone = $('#phone');
const email = $("#email");
const education = $("#education");
const techSkill = $("#techSkills");
const languageSkill = $("#languageSkills");
const resume = $("#resume");
$(document).ready(function () {
   $('.feedback-message').hide();
});

function validateFormStatus(event) {

    validateName();
    validateDob();
    validatePhone();
    validateEmail();
    validateTechSkills();
    validateLanguageSkills();
    validateResume();
    validateForm();

}

function validateForm() {

    invalidInputs = $('.is-invalid');

    formStatus = invalidInputs.length === 0;
}

function validateName() {

    let nameValue = $.trim(name.val());

    if(nameValue.length < 3) {
        showFeedback(name,'Enter your real name please');
    }else {
        changeFeedback(name);
    }
}

function validateDob() {

    let dobValue = $.trim(dob.val());
    // Check if the input is empty
    if (dobValue === "") {
        showFeedback(dob, 'Date of birth is required');
        return; // Exit the function early
    }

    let dobDate = new Date(dobValue);
    let thirteenYearsAgo = new Date();

    thirteenYearsAgo.setFullYear(thirteenYearsAgo.getFullYear() - 13);

    if (dobDate > thirteenYearsAgo) {
        showFeedback(dob, 'Your age must be greater than 13');
    } else {
        changeFeedback(dob);
    }
}

function validatePhone() {

    let phoneValue = $.trim(phone.val());

    const myanmarPhonePattern = /^[0-9]{9,11}$/;

    if (!myanmarPhonePattern.test(phoneValue)) {
        showFeedback(phone, 'Please enter a valid Myanmar phone number');
    } else {
        changeFeedback(phone);
    }

}

function validateEmail() {

    let emailValue = $.trim(email.val());

    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

    if (!emailPattern.test(emailValue)) {
        // Email is not in the correct format
        showFeedback(email, 'Please enter a valid email address');
    } else {
        changeFeedback(email);
    }

}

function validateTechSkills() {

    if(techSkillsInput.size === 0 ) {
        showFeedback(techSkill, 'One skill must be added');
    }else {
        changeFeedback(techSkill);
    }

}

function validateLanguageSkills() {

    console.log(languageSkillsInput.size)
    if(languageSkillsInput.size === 0 ) {
        showFeedback(languageSkill, 'One skill must be added');
    }else {
        changeFeedback(languageSkill);
    }

}

function validateResume() {
    let resumeInput = resume[0]; // Get the input element
    let resumeValue = resumeInput.files[0]; // Get the selected file

    if (!resumeValue) {
        // No file selected, show error
        resume.removeClass('is-invalid');
        resume.addClass('is-invalid');
        resume.closest('div').find('.feedback-message').css('display', 'block');
    } else {
        resume.removeClass('is-invalid');
        resume.addClass('is-valid');
        resume.closest('div').find('.feedback-message').css('display', 'none');
    }
    resume.css('background-image', 'none');
}

function showFeedback(inputElement,message) {
    inputElement.addClass('is-invalid'); // Apply Bootstrap is-invalid class
    inputElement.css('background-image', 'none');
    console.log("Show feed back : " ,inputElement.closest('div').find('.custom-feedback-message'));
    inputElement.closest('.form-floating').find('.custom-feedback-message').text(message);
    inputElement.closest('.form-floating').find('.custom-feedback-message').css('display','block'); // Show feedback message

}

// Function to show feedback and change border color
function changeFeedback(inputElement) {
    console.log("Input element : ",inputElement);
    inputElement.removeClass('is-invalid'); // Remove Bootstrap is-invalid class
    inputElement.addClass('is-valid'); // Remove Bootstrap is-valid class if previously added
    inputElement.css('background-image', 'none');
    if(inputElement.closest('.form-floating').find('.custom-feedback-message').css('display','block')) {
        inputElement.closest('.form-floating').find('.custom-feedback-message').text('');
        inputElement.closest('.form-floating').find('.custom-feedback-message').css('display','none')
    }
    inputElement.closest('.mb-3').find('.feedback-message').css('display','none');
}

// Helper function to check if the file extension is valid
function isValidFileExtension(fileName) {
    const validExtensions = ['.pdf', '.doc', '.docx'];
    const fileExtension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
    return validExtensions.includes(fileExtension);
}