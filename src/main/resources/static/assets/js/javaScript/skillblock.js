let techSkillsInput = [];
let languageSkillsInput = [];
$(document).ready(function () {
  // Add tech skill block
  $("#techSkills").keyup(function (event) {
    if (event.keyCode === 13) {
      var skillValue = $(this).val();
      // Split the skillValue string into an array using commas as the delimiter
      var skillsArray = skillValue.split(',');

      // Loop through each skill in the skillsArray
      for (var i = 0; i < skillsArray.length; i++) {
        var individualSkill = $.trim(skillsArray[i]);
        if (individualSkill !== "") {
          // Add the individual skill to the techSkillsInput array
          techSkillsInput.push(individualSkill);

          var count = Math.floor(Math.random() * 1000); // Generate a random number
          var skillHTML =
              '<div class="text-white rounded-pill d-inline-flex m-2 pb-1" style="max-height: 24px;background: #184070" id="tech-skill' +
              count +
              '">' +
              '<span class="text-white text-center px-2">' +
              individualSkill +
              "</span>" +
              '<span class="rounded-circle col-auto d-inline-block remove-skill remove-tech-skill" data-count="' +
              count +
              '" style="cursor: pointer; transform: translate(-24%, 9%); font-size: 0.9rem"><i class="bx bx-x"></i></span>' +
              "</div>";

          $(".tech-skills-block").append(skillHTML);
        }
      }

      validateTechSkills();
      $(this).val("");
    }
  });

  $(document).on("click", ".remove-tech-skill", function () {
    var count = $(this).data("count");
    var skillText = $(this).siblings('.text-center').text().trim();

    // Find the index of the skillText in the techSkillsInput array
    var index = techSkillsInput.indexOf(skillText);
    if (index !== -1) {
      // Remove the skill from the techSkillsInput array
      techSkillsInput.splice(index, 1);
    }

    console.log("techSkillsInput ", techSkillsInput);
    $("#tech-skill" + count).remove();
    validateTechSkills();
  });

  // Add language skill block
  $("#languageSkills").keyup(function (event) {
    if (event.keyCode === 13) {
      var skillValue = $(this).val();
      // Split the skillValue string into an array using commas as the delimiter
      var skillsArray = skillValue.split(',');

      // Loop through each skill in the skillsArray
      for (var i = 0; i < skillsArray.length; i++) {
        var individualSkill = $.trim(skillsArray[i]);
        if (individualSkill !== "") {
          // Add the individual skill to the techSkillsInput array
          languageSkillsInput.push(individualSkill);

          var count = Math.floor(Math.random() * 1000); // Generate a random number
          var skillHTML =
              '<div class="text-white rounded-pill d-inline-flex m-2 pb-1" style="max-height: 24px;background: #184070" id="language-skill' +
              count +
              '">' +
              '<span class="text-white text-center px-2">' +
              individualSkill +
              "</span>" +
              '<span class="rounded-circle col-auto d-inline-block remove-skill remove-language-skill" data-count="' +
              count +
              '" style="cursor: pointer; transform: translate(-24%, 9%); font-size: 0.9rem"><i class="bx bx-x"></i></span>' +
              "</div>";

          $(".language-skills-block").append(skillHTML);
        }
      }

      validateLanguageSkills();
      $(this).val("");
    }
  });


  // Remove language skill block
  $(document).on("click", ".remove-language-skill", function () {
    var count = $(this).data("count");
    var skillText = $(this).siblings('.text-center').text().trim();

    // Find the index of the skillText in the techSkillsInput array
    var index = languageSkillsInput.indexOf(skillText);
    if (index !== -1) {
      // Remove the skill from the techSkillsInput array
      languageSkillsInput.splice(index, 1);
    }

    console.log("languageSkillsInput ", languageSkillsInput);
    $("#language-skill" + count).remove();
    validateLanguageSkills();
  });

});
