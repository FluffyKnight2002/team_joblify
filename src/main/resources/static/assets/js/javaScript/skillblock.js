$(document).ready(function () {
  // Add skill block
  $("#skill").keyup(function (event) {
    if (event.keyCode === 13) {
      var skillValue = $(this).val();
      if ($.trim(skillValue) !== "") {
        var count = Math.floor(Math.random() * 1000); // Generate a random number
        var skill =
          '<div class="bg-light rounded-pill row w-auto m-2" id="skill' +
          count +
          '">' +
          '<span class="default-font col-8 d-inline-block text-center">' +
          skillValue +
          "</span>" +
          '<span class="rounded-circle fs-4 col-4 d-inline-block d-flex justify-content-center align-content-center remove-skill" data-count="' +
          count +
          '" style="cursor: pointer;"><i class="bx bx-x"></i></span>' +
          "</div>";

        $(".skill-block").append(skill);
        $(this).val("");
      }
    }
  });

  // Remove skill block
  $(document).on("click", ".remove-skill", function () {
    var count = $(this).data("count");
    $("#skill" + count).remove();
  });
});
