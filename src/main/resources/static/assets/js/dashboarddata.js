$(document).ready(function () {
    fetchVacancyPostCount();
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

