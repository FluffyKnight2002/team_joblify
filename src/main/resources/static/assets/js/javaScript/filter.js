function applyFilter() {
  // Get selected values
  const sortByRecent = document.getElementById("sortRecent").checked;
  const sortByRelevant = document.getElementById("sortRelevant").checked;
  const dateAny = document.getElementById("dateAny").checked;
  const dateWeek = document.getElementById("dateWeek").checked;
  const date24Hours = document.getElementById("date24Hours").checked;
  const dateMonth = document.getElementById("dateMonth").checked;
  const position = document.getElementById("positionInput").value;

  // Apply filter logic based on the selected values
  // Replace this with your actual filter implementation

  console.log("Sort By Recent:", sortByRecent);
  console.log("Sort By Relevant:", sortByRelevant);
  console.log("Date Any:", dateAny);
  console.log("Date Week:", dateWeek);
  console.log("Date 24 Hours:", date24Hours);
  console.log("Date Month:", dateMonth);
  console.log("Position:", position);
}

function resetFilter() {
  // Reset all filter options to their default values
  document.getElementById("sortRelevant").checked = true;
  document.getElementById("dateAny").checked = true;
  document.getElementById("positionInput").value = "";
}

// Function to open the filter
function openFilter() {
  document.getElementById("filterOffcanvas").classList.add("show");
  document.body.classList.add("offcanvas-open");
}

// Function to close the filter
function closeFilter() {
  document.getElementById("filterOffcanvas").classList.remove("show");
  document.body.classList.remove("offcanvas-open");

  // Wait for the transition to complete before removing the offcanvas class
  setTimeout(function() {
    document.getElementById("filterOffcanvas").classList.remove("offcanvas-transition");
  }, 300); // 300 milliseconds = transition duration
}
