const token = localStorage.getItem('token');
console.log("Token:" + token)

function fetchCurrentUser() {
    if (!token) {
        alert("There is no token available");
        return;
    }

    fetch(`http://localhost:8080/users/me`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to fetch user: ${response.status} ${response.statusText}`);
        }
        return response.json(); 
    })
    .then(user => {
        console.log("Current User:", user);
        alert("Fetching Succesfull");
        // const userData = JSON.stringify(user, null, 2);
        if (user.userId !== null) {
            localStorage.setItem('userId', user.userId);
            localStorage.setItem('userPassword', user.password);
            console.log("User ID saved to localStorage:", user.userId);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("An error occurred while fetching user data.");
    });
}

let userDevices = [];
function fetchDevices() {
    if (!token) {
        alert("There is no token available. Please log in.");
        return;
    }

    fetch(`http://localhost:8080/util/devices`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to fetch devices: ${response.status} ${response.statusText}`);
        }
        return response.json(); 
    })
    .then(devices => {
        console.log("Current devices:", devices);
        alert("Fetching Successful");
        userDevices = devices;
        populateDevicesInHtml();
    })
    .catch(error => {
        console.error('Error:', error);
        alert("An error occurred while fetching devices.");
    });
}

function populateDevicesInHtml() {
    const selects = document.querySelectorAll('.device-populate');
    selects.forEach(select => {
        select.innerHTML = '<option value="">Associate Device to </option>';

        userDevices.forEach(device => {
            const option = document.createElement('option');
            option.value = device.deviceId;  
            option.textContent = device.name; 
            select.appendChild(option);
        });
    });
}

let userGroups = [];
function fetchGroups() {
    if (!token) {
        alert("There is no token available. Please log in.");
        return;
    }

    fetch(`http://localhost:8080/util/groups`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to fetch groups: ${response.status} ${response.statusText}`);
        }
        return response.json(); 
    })
    .then(groups => {
        console.log("Current groups:", groups);
        alert("Fetching Successful");
        userGroups = groups;
        populateGroupsInHtml();
    })
    .catch(error => {
        console.error('Error:', error);
        alert("An error occurred while fetching groups.");
    });
}

function populateGroupsInHtml() {
    const selects = document.querySelectorAll('.group-populate');
    selects.forEach(select => {
        select.innerHTML = '<option value="">Associate Group to </option>';

        userGroups.forEach(group => {
            const option = document.createElement('option');
            option.value = group.groupId;  
            option.textContent = group.name; 
            select.appendChild(option);
        });
    });
}

function populateGroupsInHtml() {
    const selects = document.querySelectorAll('.group-populate');
    selects.forEach(select => {
        select.innerHTML = '<option value="">Associate Group to </option>';

        userGroups.forEach(group => {
            const option = document.createElement('option');
            option.value = group.groupId;  
            option.textContent = group.name; 
            select.appendChild(option);
        });
    });
}

let userRules = [];
function fetchRules() {
    if (!token) {
        alert("There is no token available. Please log in.");
        return;
    }

    fetch(`http://localhost:8080/util/rules`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to fetch rules: ${response.status} ${response.statusText}`);
        }
        return response.json(); 
    })
    .then(rules => {
        console.log("Current rules:", rules);
        alert("Fetching Successful");
        userRules = rules;
        populateRulesInHtml();
    })
    .catch(error => {
        console.error('Error:', error);
        alert("An error occurred while fetching rules.");
    });
}

function populateRulesInHtml() {
    const selects = document.querySelectorAll('.rule-populate');
    selects.forEach(select => {
        select.innerHTML = '<option value="">Associate Rule to </option>';

        userRules.forEach(rule => {
            const option = document.createElement('option');
            option.value = rule.ruleId;  
            option.textContent = rule.ruleName; 
            select.appendChild(option);
        });
    });
}

document.getElementById('tempUser').addEventListener('click', () => {
    fetchCurrentUser();
});
document.getElementById('tempDevices').addEventListener('click', () => {
    fetchDevices();
});
document.getElementById('tempGroups').addEventListener('click', () => {
    fetchGroups();
});
document.getElementById('tempRules').addEventListener('click', () => {
    fetchRules();
});

// document.addEventListener('DOMContentLoaded', () => { 
//     fetchCurrentUser();
// });

function getDeviceById(id) {
    if (!token) {
        alert("There is no token available. Please log in.");
        return Promise.reject("No token available");
    }

    return fetch(`http://localhost:8080/devices/${id}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to fetch device with ID ${id}: ${response.status} ${response.statusText}`);
        }
        return response.json(); 
    })
    .then(device => {
        console.log("Current device:", device);
        alert("Fetching Successful");
        return device; 
    })
    .catch(error => {
        console.error(`Error fetching device with ID ${id}:`, error);
        alert(`An error occurred while fetching device with ID ${id}.`);
        return null;
    });
}

function getGroupById(id) {
    if (!token) {
        alert("There is no token available. Please log in.");
        return Promise.reject("No token available"); 
    }

    return fetch(`http://localhost:8080/groups/${id}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to fetch group with ID ${id}: ${response.status} ${response.statusText}`);
        }
        return response.json(); 
    })
    .then(group => {
        console.log("Current group:", group);
        alert("Fetching Successful");
        return group; 
    })
    .catch(error => {
        console.error(`Error fetching group with ID ${id}:`, error);
        alert(`An error occurred while fetching group with ID ${id}.`);
        return null;
    });
}

function getRuleById(id) {
    if (!token) {
        alert("There is no token available. Please log in.");
        return Promise.reject("No token available"); 
    }

    return fetch(`http://localhost:8080/rules/${id}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to fetch rule with ID ${id}: ${response.status} ${response.statusText}`);
        }
        return response.json(); 
    })
    .then(rule => {
        console.log("Current rule:", rule);
        alert("Fetching Successful");
        return rule; 
    })
    .catch(error => {
        console.error(`Error fetching group with ID ${id}:`, error);
        alert(`An error occurred while fetching group with ID ${id}.`);
        return null;
    });
}

function populateGroupsInTableHtml() {
    const tableBody = document.getElementById('groups-table-body');

    tableBody.innerHTML = '';

    userGroups.forEach(group => {
        const row = document.createElement('tr');
        row.classList.add('border-b', 'hover:bg-gray-100');

        const idCell = document.createElement('td');
        idCell.classList.add('py-3', 'px-6');
        idCell.textContent = group.groupId;

        const nameCell = document.createElement('td');
        nameCell.classList.add('py-3', 'px-6');
        nameCell.textContent = group.name;


        row.appendChild(idCell);
        row.appendChild(nameCell);

        tableBody.appendChild(row);
    });
}
function populateDevicesInTableHtml() {
    const tableBody = document.getElementById('devices-table-body');

    tableBody.innerHTML = '';

    userDevices.forEach(device => {
        const row = document.createElement('tr');
        row.classList.add('border-b', 'hover:bg-gray-100');

        const idCell = document.createElement('td');
        idCell.classList.add('py-3', 'px-6');
        idCell.textContent = device.deviceId;

        const nameCell = document.createElement('td');
        nameCell.classList.add('py-3', 'px-6');
        nameCell.textContent = device.name;

        const typeCell = document.createElement('td');
        typeCell.classList.add('py-3', 'px-6');
        typeCell.textContent = device.type;


        row.appendChild(idCell);
        row.appendChild(nameCell);
        row.appendChild(typeCell);

        tableBody.appendChild(row);
    });
}
function populateRulesInTableHtml() {
    const tableBody = document.getElementById('rules-table-body');

    tableBody.innerHTML = '';

    userRules.forEach(rule => {
        const row = document.createElement('tr');
        row.classList.add('border-b', 'hover:bg-gray-100');

        const idCell = document.createElement('td');
        idCell.classList.add('py-3', 'px-6');
        idCell.textContent = rule.ruleId;

        const nameCell = document.createElement('td');
        nameCell.classList.add('py-3', 'px-6');
        nameCell.textContent = rule.ruleName;

        const descriptionCell = document.createElement('td');
        descriptionCell.classList.add('py-3', 'px-6');
        descriptionCell.textContent = rule.description;

        const eventCell = document.createElement('td');
        eventCell.classList.add('py-3', 'px-6');
        eventCell.textContent = rule.event;

        const groupCell = document.createElement('td');
        groupCell.classList.add('py-3', 'px-6');
        groupCell.textContent = rule.groupDto.name;


        row.appendChild(idCell);
        row.appendChild(nameCell);
        row.appendChild(descriptionCell);
        row.appendChild(eventCell);
        row.appendChild(groupCell);

        tableBody.appendChild(row);
    });
}
