// Text area session start
// Attach input and change event handlers to the relevant textareas
$("#requirements, #responsibilities, #preferences").on("input change", function() {
    var textarea = this;
    textarea.style.height = "auto"; // Reset the height to allow recalculation
    textarea.style.height = textarea.scrollHeight + "px"; // Set the height based on the scroll height
});

// Attach click event handler to the relevant textareas
$("#requirements, #responsibilities, #preferences").on("click", function() {
    var textarea = $(this);

    // Check if the textarea is empty or only contains whitespace
    if (textarea.val().trim() === "") {
        textarea.val("•  "); // Add a bullet to the textarea
    }
});
$("#requirements, #responsibilities, #preferences").keydown(function(event) {
    var bullet = (event.which === 13) ? "\n•  " : "\n\t◦  ";
    var cursorPos = (event.which === 13) ? 4 : 5;
    if (event.which === 13 || event.which == 9) {
        event.preventDefault();
        var textarea = $(this)[0];
        var startPos = textarea.selectionStart;
        var endPos = textarea.selectionEnd;
        textarea.value = textarea.value.substring(0, startPos) + bullet + textarea.value.substring(endPos);
        textarea.selectionStart = textarea.selectionEnd = startPos + cursorPos;
    }
});

function handleBulletButton(textareaId, buttonId, bulletChar, indent, bulletToRemove) {
    $(buttonId).click(function() {
        var textarea = $(textareaId)[0];
        var selectedText = textarea.value.substring(textarea.selectionStart, textarea.selectionEnd);
        var lines = selectedText.split('\n');
        var bulletList = '';

        for (var i = 0; i < lines.length; i++) {
            var line = lines[i].trim();

            if (line !== '') {
                // Add bullet and indent
                bulletList += indent + bulletChar + ' ' + line;
            } else {
                // Preserve empty lines
                bulletList += line;
            }

            if (i < lines.length - 1) {
                bulletList += '\n';
            }
        }

        var startPos = textarea.selectionStart;
        var endPos = textarea.selectionEnd;

        textarea.value = textarea.value.substring(0, startPos) + bulletList + textarea.value.substring(endPos);
    });
}

function handleRemoveBulletButton(textareaId, buttonId, bulletChars) {
    $(buttonId).click(function() {
        var textarea = $(textareaId)[0];
        var selectedText = textarea.value.substring(textarea.selectionStart, textarea.selectionEnd);
        var lines = selectedText.split('\n');
        var bulletList = '';

        for (var i = 0; i < lines.length; i++) {
            var line = lines[i].trim();

            if (line.startsWith(bulletChars.bullet)) {
                // Remove bullet
                bulletList += line.substring(bulletChars.bullet.length);
            }else if (line.startsWith(bulletChars.subBullet)) {
                // Remove sub-bullet
                bulletList += line.substring(bulletChars.subBullet.length+1);
            } else {
                // Preserve lines
                bulletList += line;
            }

            if (i < lines.length - 1) {
                bulletList += '\n';
            }
        }

        var startPos = textarea.selectionStart;
        var endPos = textarea.selectionEnd;

        textarea.value = textarea.value.substring(0, startPos) + bulletList + textarea.value.substring(endPos);
    });
}

handleBulletButton('#responsibilities', '#setBulletButton1', '•  ', '');
handleBulletButton('#responsibilities', '#addSubListButton1', '◦  ', '\t');
handleRemoveBulletButton('#responsibilities', '#removeBulletButton1', { bullet: '•  ', subBullet: '◦  ' });

handleBulletButton('#requirements', '#setBulletButton2', '•  ', '');
handleBulletButton('#requirements', '#addSubListButton2', '◦  ', '\t');
handleRemoveBulletButton('#requirements', '#removeBulletButton2', { bullet: '•  ', subBullet: '◦  ' });

handleBulletButton('#preferences', '#setBulletButton3', '•  ', '');
handleBulletButton('#preferences', '#addSubListButton3', '◦  ', '\t');
handleRemoveBulletButton('#preferences', '#removeBulletButton3', { bullet: '•  ', subBullet: '◦  ' });
// Set bullet list end

// Text area session end