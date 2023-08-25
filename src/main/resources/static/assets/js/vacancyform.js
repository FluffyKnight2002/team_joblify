let inputsToDisable = [];

// Initialize Bootstrap tooltips
let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
});
$(document).ready(function() {

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
    // Attach click event handler to the preview button
    $("#preview-btn").on("click", function() {
        // Get form field values
        var title = $("#title").val();
        var post = $("#post").val();
        var department = $("#department").val();
        var type = $("#type").val();
        var lvl = $('#lvl').val();
        var workingDays = $("#workingDays").val();
        var workingHours = $("#workingHours").val();
        var salary = $("#salary").val();
        var address = $("#address").val();
        var description = $("#descriptions").val();
        var requirements = $("#requirements").val();
        var responsibilities = $("#responsibilities").val();
        var preferences = $("#preferences").val();
        var onSiteOrRemote = $("#onSiteOrRemote").val();

        // Set form field values in the preview modal with replaced newlines and tabs
        $("#preview-title").text(title);
        $("#preview-post-type").text(post + '(' + reconvertToString(type) + ')');
        $("#preview-department").text(department);
        $("#preview-address").text(address);
        $("#preview-salary").text(salary);
        $("#preview-level").text(reconvertToString(lvl));
        $("#preview-on-site-or-remote").text(reconvertToString(onSiteOrRemote));
        $("#preview-description").text(description);
        $("#preview-requirements").val(requirements);
        $("#preview-responsibilities").val(responsibilities);
        $("#preview-preferences").val(preferences);
        $("#preview-working-days").text(workingDays);
        $("#preview-working-hours").text(workingHours);
        $("#preview-location").text(address);
        $("#preview-type").text(reconvertToString(type));

        // Adjust textarea height in the preview modal
        $("#previewModal").on("shown.bs.modal", function() {
            $('.preview').each(function() {
                this.style.height = "auto";
                this.style.height = this.scrollHeight + "px";
            });
        });

        // Open the preview modal
        $("#previewModal").modal("show");
    });
    // Preview Modal end

    const $calendar = $('#calendar').hide();
    const $workingDaysInput = $('#workingDays');
    const $timePickerBtn = $('#timePickerBtn');
    const $timePickerContainer = $('#timePickerContainer').hide();
    const $startTimePicker = $('#startTimePicker');
    const $endTimePicker = $('#endTimePicker');
    const $workingHoursInput = $('#workingHours'); // Add this line

    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    let selectedDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri']; // Set default selected days
    let startTime = '9:00';
    let endTime = '17:00';

    $('#calendar-btn').on('click', function() {

        $calendar.toggle();
    });

    function updateInputValue() {
        console.log("Mon ~ Fri : ", selectedDays);

        if (selectedDays.length === 5 && selectedDays.every(day => ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'].includes(day))) {
            $workingDaysInput.val('Mon ~ Fri');
        } else if (selectedDays.length === 2 && selectedDays.includes('Sun') && selectedDays.includes('Sat')) {
            $workingDaysInput.val('Weekend');
        } else {
            let textSelectedDays = daysOfWeek.filter(day => selectedDays.includes(day));
            $workingDaysInput.val(textSelectedDays.join(' ~ '));
        }

        console.log("Start Time : ", startTime);
        console.log("End Time : ",endTime);
        // Format start and end times
        const formattedStartTime = formatTime(startTime);
        const formattedEndTime = formatTime(endTime);

        // Update workingHours input value
        $workingHoursInput.val(`${formattedStartTime} ~ ${formattedEndTime}`);
    }

    $endTimePicker.on('change', function() {
        endTime = $(this).val(); // Update the endTime value from the time picker
        updateInputValue();
    });

    function updateCalendar() {
        $calendar.empty();

        daysOfWeek.forEach(day => {
            const $dayElement = $('<div>', {
                text: day,
                class: 'calendar-day'
            });

            if (selectedDays.includes(day)) {
                $dayElement.addClass('selected');
            }

            $dayElement.on('click', () => {
                if (selectedDays.length === 1 && selectedDays.includes(day)) {
                    // If there's only one selected day, prevent unselecting it
                    return;
                }

                if (selectedDays.includes(day)) {
                    selectedDays = selectedDays.filter(selectedDay => selectedDay !== day);
                } else {
                    selectedDays.push(day);
                }

                updateInputValue();
                updateCalendar();
            });

            $calendar.append($dayElement);
        });
    }

    $timePickerBtn.on('click', function() {
        $timePickerContainer.toggle();
    });

    $startTimePicker.on('change', function() {
        startTime = $(this).val();
        updateInputValue();
    });

    $endTimePicker.on('change', function() {
        endTime = $(this).val();
        updateInputValue();
    });

    updateCalendar();
    updateInputValue();
});

// Attach input event handler to the salary input
$('#salary').on('input', function() {
    const inputValue = $(this).val();

    // Remove all non-numeric characters using regex
    const numericValue = inputValue.replace(/\D/g, '');

    // Update the input value with the cleaned numeric value
    $(this).val(numericValue);
});

function formatTime(time) {
    const [hours, minutes, period] = time.split(/[:\s]/);
    const numericHours = parseInt(hours); // Convert hours to a number

    if (numericHours === 12) {
        return `12:${minutes} PM`;
    } else if (numericHours === 0 || numericHours === 24) {
        return `12:${minutes} AM`;
    } else if (numericHours > 12) {
        return `${numericHours - 12}:${minutes} PM`;
    } else {
        return `${numericHours}:${minutes} AM`;
    }
}

// Enum convert and reconvert

function convertToLakhs(decimalValue) {
    const lakhsValue = parseFloat(decimalValue) / 100000;
    const formattedValue = lakhsValue.toFixed(6).replace(/\.?0+$/, ''); // Remove trailing zeros
    return `${formattedValue} Lakhs`;
}
function convertToEnumFormat(input) {
    console.log(input)
    // Replace spaces with underscores and convert to uppercase
    if(input == "On-site") {
        return "ON_SITE";
    }
    return input.replaceAll(" ", "_").toUpperCase();
}

function reconvertToString(input) {
    // Replace underscores with spaces and convert to title case
    if (input === "ON_SITE") {
        return "On-site";
    }
    return input.split('_').map(word => word.charAt(0) + word.slice(1).toLowerCase()).join(' ');
}

// Change time format
function changeTimeFormat(time) {
    var dateString = time;

    // Parse the date string to a JavaScript Date object
    var date = new Date(dateString);

    // Array to map month numbers to month names
    var monthNames = [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    // Get the day of the month
    var day = date.getDate();

    // Determine the suffix for the day (st, nd, rd, or th)
    var suffix;
    if (day >= 11 && day <= 13) {
        suffix = "th";
    } else {
        switch (day % 10) {
            case 1: suffix = "st"; break;
            case 2: suffix = "nd"; break;
            case 3: suffix = "rd"; break;
            default: suffix = "th";
        }
    }

    // Format the date as "Dayth Month Year" (e.g., "27th Jul 2023")
    var formattedDate = day + suffix + " " + monthNames[date.getMonth()] + " " + date.getFullYear();
    return formattedDate;
}