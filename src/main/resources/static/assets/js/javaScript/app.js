// Wait for the page to load
document.addEventListener("DOMContentLoaded", function (event) {
  // Show the loading animation
  document.getElementById("loading-animation").classList.add("show");

  // Hide the loading animation after a short delay (replace 2000 with the desired delay in milliseconds)
  setTimeout(function () {
    document.getElementById("loading-animation").classList.remove("show");
  }, 1000);
});

function convertToLakhs(decimalValue) {
  const lakhsValue = parseFloat(decimalValue) / 100000;
  const formattedValue = lakhsValue.toFixed(6).replace(/\.?0+$/, ''); // Remove trailing zeros
  return `${formattedValue} Lakhs ( Nego )`;
}

function reconvertToString(input) {
  // Replace underscores with spaces and convert to title case
  if (input === "ON_SITE") {
    return "On-site";
  }
  return input.split('_').map(word => word.charAt(0) + word.slice(1).toLowerCase()).join(' ');
}

function timeAgo(time) {
  const currentTime = new Date();
  const inputTime = new Date(time);
  const timeDifferenceInSeconds = Math.floor((currentTime - inputTime) / 1000);
  console.log(time)

  // Define time units in seconds
  const minute = 60;
  const hour = 60 * minute;
  const day = 24 * hour;
  const week = 7 * day;
  const month = 30 * day;

  if (timeDifferenceInSeconds < minute) {
    console.log("Just now")
    return 'Just now';
  } else if (timeDifferenceInSeconds < hour) {
    const minutesAgo = Math.floor(timeDifferenceInSeconds / minute);
    console.log(minutesAgo)
    return `${minutesAgo} minute${minutesAgo > 1 ? 's' : ''} ago`;
  } else if (timeDifferenceInSeconds < day) {
    const hoursAgo = Math.floor(timeDifferenceInSeconds / hour);
    console.log(hoursAgo)
    return `${hoursAgo} hour${hoursAgo > 1 ? 's' : ''} ago`;
  } else if (timeDifferenceInSeconds < week) {
    const daysAgo = Math.floor(timeDifferenceInSeconds / day);
    console.log(daysAgo)
    return `${daysAgo} day${daysAgo > 1 ? 's' : ''} ago`;
  } else if (timeDifferenceInSeconds == week) {
    const weeksAgo = Math.floor(timeDifferenceInSeconds / week);
    console.log(weeksAgo)
    return `${weeksAgo} week${weeksAgo > 1 ? 's' : ''} ago`;
  } else {
    // Display the date in the format: 'MMM DD YYYY'
    const formattedDate = inputTime.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
    console.log(formattedDate)
    return formattedDate;
  }
}

function changeTimeFormat(time) {

  // Parse the date string to a JavaScript Date object
  var date = new Date(time);

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

  // Format the date as "Dayth Month Year" (e.g., "27th Jul")
  var formattedDate = day + suffix + " " + monthNames[date.getMonth()];
  return formattedDate;
}
