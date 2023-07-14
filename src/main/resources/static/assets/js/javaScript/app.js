// Wait for the page to load
document.addEventListener("DOMContentLoaded", function (event) {
  // Show the loading animation
  document.getElementById("loading-animation").classList.add("show");

  // Hide the loading animation after a short delay (replace 2000 with the desired delay in milliseconds)
  setTimeout(function () {
    document.getElementById("loading-animation").classList.remove("show");
  }, 1000);
});
