function openUploader() {
    document.getElementById('uploadImage').click();
}

function uploadHandler() {
    let file = document.querySelector('input[type=file]').files[0];
    let reader = new FileReader();
    let preview = document.getElementById('uploadedImage');

    reader.addEventListener("load", function () {
        preview.src = reader.result;
    }, false);

    if (file) {
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            localStorage.setItem('tempImg', reader.result.split(',')[1]);
        }
    }
}

function removeImage() {
let preview = document.getElementById('uploadedImage'); 
preview.src = '';

// Reset the input element value to clear the selected file
let uploadInput = document.getElementById('uploadImage');
uploadInput.value = '';
}