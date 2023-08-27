let techSkillsInput = new Set;
let languageSkillsInput = new Set;
$(document).ready(function () {
  // Add tech skill block
  $("#techSkills").keyup(function (event) {
    if (event.keyCode === 13) {
      var skillValue = $(this).val();
      if ($.trim(skillValue) !== "") {
        techSkillsInput.add(skillValue);
        console.log("techSkillsInput ",techSkillsInput)
        var count = Math.floor(Math.random() * 1000); // Generate a random number
        var skill =
            '<div class="text-white rounded-pill d-inline-flex m-2 pb-1" style="max-height: 24px;background: #184070" id="tech-skill' +
          count +
          '">' +
          '<span class="text-white text-center px-2">' +
          skillValue +
          "</span>" +
          '<span class="rounded-circle col-auto d-inline-block remove-skill remove-tech-skill" data-count="' +
          count +
          '" style="cursor: pointer; transform: translate(-24%, 9%); font-size: 0.9rem"><i class="bx bx-x"></i></span>' +
          "</div>";

        $(".tech-skills-block").append(skill);
        validateTechSkills();
        $(this).val("");
      }
    }
  });

  // Remove tech skill block
  $(document).on("click", ".remove-tech-skill", function () {
    var count = $(this).data("count");
    techSkillsInput.delete($(this).siblings('.text-center').text());
    console.log("techSkillsInput ",techSkillsInput)
    $("#tech-skill" + count).remove();
    validateTechSkills();
  });

  // Add language skill block
  $("#languageSkills").keyup(function (event) {
    if (event.keyCode === 13) {
      var skillValue = $(this).val();
      if ($.trim(skillValue) !== "") {
        languageSkillsInput.add(skillValue);
        console.log("languageSkillsInput ",languageSkillsInput);
        var count = Math.floor(Math.random() * 1000); // Generate a random number
        var skill =
            '<div class="text-white rounded-pill d-inline-flex m-2 pb-1" style="max-height: 24px;background: #184070" id="language-skill' +
            count +
            '">' +
            '<span class="text-white text-center px-2">' +
            skillValue +
            "</span>" +
            '<span class="rounded-circle col-auto d-inline-block remove-skill remove-language-skill" data-count="' +
            count +
            '" style="cursor: pointer; transform: translate(-24%, 9%); font-size: 0.9rem"><i class="bx bx-x"></i></span>' +
            "</div>";

        $(".language-skills-block").append(skill);
        validateLanguageSkills();
        $(this).val("");
      }
    }
  });

  // Remove language skill block
  $(document).on("click", ".remove-language-skill", function () {
    var count = $(this).data("count");
    languageSkillsInput.delete($(this).siblings('.text-center').text());
    console.log("languageSkillsInput ",languageSkillsInput);
    $("#language-skill" + count).remove();
    validateLanguageSkills();
  });

});
