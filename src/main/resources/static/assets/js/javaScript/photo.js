let uploadInput = document.getElementById('uploadImageInput');
let preview = document.getElementById('uploadedImage'); 
const removeIcon = document.getElementById('removeIcon');

if(preview.src === ''){
    removeIcon.style.display = 'none';
} else {
    removeIcon.style.display = 'inline-block';
}

function openUploader() {
    uploadInput.click();
}

function uploadHandler() {
    let file = document.querySelector('input[type=file]').files[0];
    let reader = new FileReader();
    const removeIcon = document.getElementById('removeIcon');

    reader.addEventListener("load", function () {
        preview.src = reader.result;
    }, false);

    if (file) {
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            localStorage.setItem('tempImg', reader.result.split(',')[1]);
            removeIcon.style.display = 'inline-block';
        }
    }
}

function removeImage() {

preview.src = '';

// Reset the input element value to clear the selected file

uploadInput.value = '';
removeIcon.style.display = 'none';
}