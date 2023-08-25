// Get references to elements
let uploadInput = document.getElementById('uploadImageInput');
let preview = document.getElementById('uploadedImage');
const removeIcon = document.getElementById('removeIcon');

// // Initialize the display of remove icon based on image presence
// document.addEventListener('DOMContentLoaded', function() {
//     console.log(preview.src);
//     if (preview.src.startsWith('http://localhost:8080/')) {
//         removeIcon.style.display = 'none';
//     } else {
//         removeIcon.style.display = 'inline-block';
//     }
// });

// Function to open the file uploader
function openUploader() {
    uploadInput.click();
}

// Function to handle image upload
function uploadHandler() {
    let file = uploadInput.files[0];
    let reader = new FileReader();

    reader.addEventListener("load", function() {
        preview.src = reader.result;
    }, false);

    if (file) {
        reader.readAsDataURL(file);
        reader.onloadend = function() {
            localStorage.setItem('tempImg', reader.result.split(',')[1]);
            removeIcon.style.display = 'inline-block';
        };
    }
}

// Function to remove the uploaded image
function removeImage() {
    preview.src = '';

    // Reset the input element value to clear the selected file
    uploadInput.value = '';
    removeIcon.style.display = 'none';
}
