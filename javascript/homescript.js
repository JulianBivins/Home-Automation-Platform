
const token = localStorage.getItem('token');
const refreshButton = document.getElementById('refresh-btn');
const temp1 = document.getElementById('temp-1');
const temp2 = document.getElementById('temp-2');
const energyPercent = document.getElementById('energy-percent');
const precipitationPercent = document.getElementById('precipitation-percent');
const energyDot = document.getElementById('energy-dot');
const precipitationDot = document.getElementById('precipitation-dot');


document.addEventListener('DOMContentLoaded', () => { 
    ceckTocken();  
    updateAllValues();
});

refreshButton.addEventListener('click', updateAllValues());
 


function ceckTocken () {
    if (token) {
        console.log('Token retrieved:', token);



    } else {
        console.log('There seems to have been a mistake. No token has been found. Redirecting to login.');
        window.location.href = '/public/index.html'; 
    }
}

document.getElementById('LogOut').addEventListener('click', logout);

function logout() {
    localStorage.removeItem('token');
    window.location.href = '/public/index.html'; 
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}


function updateAllValues() {
    updateTemperatures();
    updateEnergyPrecipitation();
}


//This is only for testing. Planning to add real weather data
function updateTemperatures() {
        const temp1Value = getRandomInt(10, 35); 
        const temp2Value = getRandomInt(10, 35);

        if (temp1Value > temp2Value) {
            const temporary = temp1Value;
            temp1Value = temp2Value; 
            temp2Value = temporary;
        }else if (temp1Value === temp2Value) {
            temp1Value--; 
        }
        
        temp1.textContent = `${temp1Value}°C`;
        temp2.textContent = `${temp2Value}°C`;
}
function updateEnergyPrecipitation() {
        const energyPercentValue = getRandomInt(0, 95);
        const precipitationPercentValue = getRandomInt(0, 95);

        energyPercent.textContent = `${energyPercentValue}%`;
        precipitationPercent.textContent = `${precipitationPercentValue}%`;

        if (energyDot) {
            energyDot.style.left = `${energyPercentValue}%`;
        }

        if (precipitationDot) {
            precipitationDot.style.left = `${precipitationPercentValue}%`;
        }
    }


    

