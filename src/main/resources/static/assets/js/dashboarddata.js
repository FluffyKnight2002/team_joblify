$(document).ready(function () {
    fetchVacancyPostCount();
    fetchUserCount();
    fetchApplicantsCount();
    fetchHiredCount();
});

async function fetchVacancyPostCount() {
    try {
        const response = await fetch(`vacancy/count`);
        if (!response.ok) {
            throw new Error('Failed to fetch vacancy count');
        }
        const vacancyCount = await response.json();
        $('#vacancy-post-count').text(vacancyCount);
    } catch (error) {
        console.error('Error fetching vacancy count:', error);
    }
}

async function fetchUserCount() {
    try {
        const response = await fetch(`user-count`);
        if (!response.ok) {
            throw new Error('Failed to fetch vacancy count');
        }
        const userCount = await response.json();
        $('#user-count').text(userCount);
    } catch (error) {
        console.error('Error fetching vacancy count:', error);
    }
}

async function fetchApplicantsCount() {
    try {
        const response = await fetch(`applicant-count`);
        if (!response.ok) {
            throw new Error('Failed to fetch vacancy count');
        }
        const applicantsCount = await response.json();
        $('#applicant-count').text(applicantsCount);
    } catch (error) {
        console.error('Error fetching vacancy count:', error);
    }
}

async function fetchHiredCount() {
    try {
        const response = await fetch(`vacancy/count`);
        if (!response.ok) {
            throw new Error('Failed to fetch vacancy count');
        }
        const hiredCount = await response.json();
        $('#hired-count').text(hiredCount);
    } catch (error) {
        console.error('Error fetching vacancy count:', error);
    }
}

