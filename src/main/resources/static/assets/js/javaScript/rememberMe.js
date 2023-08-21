function updateCookieExpiration(cookieName, newExpiration) {
    const existingCookie = getCookie(cookieName);
    
    if (existingCookie) {
        document.cookie = `${cookieName}=${existingCookie}; expires=${newExpiration}; path=/`;
        console.log('Cookie expiration updated.');
    } else {
        console.log('Cookie not found.');
    }
}

function getCookie(cookieName) {
    let name = cookieName + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    console.log(decodedCookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return null;
}

// Calculate the expiration date (3 months from now)
const currentDate = new Date();
const threeMonthsLater = new Date(currentDate);
threeMonthsLater.setMonth(threeMonthsLater.getMonth() + 3);
const newExpiration = threeMonthsLater.toUTCString();

// Call the function with the name of the specific cookie you want to update and the new expiration date
updateCookieExpiration('remember-me-cookie', newExpiration);