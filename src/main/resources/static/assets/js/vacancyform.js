$(document).ready(function() {

    // Attach input and change event handlers to the relevant textareas
    $("#requirements, #responsibility, #preferences").on("input change", function() {
        var textarea = this;
        textarea.style.height = "auto"; // Reset the height to allow recalculation
        textarea.style.height = textarea.scrollHeight + "px"; // Set the height based on the scroll height
    });

    // Attach click event handler to the relevant textareas
    $("#requirements, #responsibility, #preferences").on("click", function() {
        var textarea = $(this);

        // Check if the textarea is empty or only contains whitespace
        if (textarea.val().trim() === "") {
            textarea.val("• "); // Add a bullet to the textarea
        }
    });

    // Attach keydown event handler to the relevant fields
    $("#requirements, #responsibility, #preferences").on("keydown", function(e) {

        var keyCode = e.which || e.keyCode;
        // Check if Enter key (keyCode 13) or Tab key (keyCode 9) is pressed
        if (keyCode === 13 || keyCode === 9) {
            e.preventDefault(); // Prevent the default behavior of the key press

            // Determine the key value based on the pressed key
            var keyValue = (keyCode === 13) ? "• " : "\t◦ ";

            // Get the current field value
            var fieldValue = $(this).val();

            // Check if the field is empty or the cursor is at the start of a new line or a tab character is present
            if (fieldValue === "" || fieldValue.endsWith("\n") || fieldValue.endsWith("\t")) {
                // Append the bullet or checkmark to the field
                fieldValue += "• ";
            } else {
                // Append a new line and then the bullet or checkmark to the field
                fieldValue += "\n" + keyValue;
            }

            // Update the field value
            $(this).val(fieldValue);
        }
    });

    // Preview Modal
    // Attach click event handler to the preview button
    $("#preview-btn").on("click", function() {
        // Get form field values
        var title = $("#title").val();
        var post = $("#post").val();
        var department = $("#department").val();
        var type = $("#type").val();
        var experienceLevel = $('#experienceLevel').val();
        var workingDays = $("#workingDays").val();
        var workingHours = $("#workingHours").val();
        var salary = $("#salary").val();
        var address = $("#address").val();
        var description = $("#descriptions").val();
        var requirements = $("#requirements").val();
        var responsibility = $("#responsibility").val();
        var preferences = $("#preferences").val();

        // Set form field values in the preview modal
        $("#preview-title").text(title);
        $("#preview-post-type").text(post + '(' + type + ')');
        $("#preview-department").text(department);
        $("#preview-address").text(address);
        $("#preview-salary").text(salary);
        $("#preview-level").text(experienceLevel);
        $("#preview-description").text(description);
        $("#preview-requirements").text(requirements);
        $("#preview-responsibilities").text(responsibility);
        $("#preview-preferences").text(preferences);
        $("#preview-working-days").text(workingDays);
        $("#preview-working-hours").text(workingHours);
        $("#preview-location").text(address);

        // Open the preview modal
        $("#previewModal").modal("show");

    });
});