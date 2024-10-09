// src/main/resources/static/js/scripts.js
function openNav() {
    document.getElementById("mySidebar").style.width = "250px";
}

function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
}

function formatPhoneNumber(element) {
    let value = element.value.replace(/\D/g, '');
    if (value.length > 7) {
        value = value.replace(/(\d{3})(\d{4})(\d+)/, "$1-$2-$3");
    } else if (value.length > 3) {
        value = value.replace(/(\d{3})(\d+)/, "$1-$2");
    }
    element.value = value;
}
