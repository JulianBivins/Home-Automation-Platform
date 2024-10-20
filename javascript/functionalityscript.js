
// document.addEventListener('DOMContentLoaded', () => {
//     hideForms(); 
// });

// function hideForms() {
    
//     if (formContainer.classList.contains('card-functionality')){
//         formContainer.classList.remove('card-functionality');
//     } else {
//         formContainer.classList.add('card-functionality');
//     }

//     if (groupForm.classList.contains('flex')){
//         groupForm.classList.remove('flex');
//         groupForm.classList.add('hidden');
//     } else {
//         groupForm.classList.remove('hidden');
//         groupForm.classList.add('flex');
//     }
//     if (ruleForm.classList.contains('flex')){
//         ruleForm.classList.remove('flex');
//         ruleForm.classList.add('hidden');
//     } else {
//         ruleForm.classList.remove('hidden');
//         ruleForm.classList.add('flex');
//     }

//     if (deviceForm.classList.contains('flex')){
//         deviceForm.classList.remove('flex');
//         deviceForm.classList.add('hidden');
//     } else {
//         deviceForm.classList.remove('hidden');
//         deviceForm.classList.add('flex');
//     }

// }


const groupForm = document.getElementById('groupForm');
const ruleForm = document.getElementById('ruleForm');
const deviceForm = document.getElementById('deviceForm');
const homeDashboard = document.getElementById('homeDashboard');
const formContainer = document.getElementById('formContainer');


document.addEventListener('DOMContentLoaded', () => {
    hideForms();
    setupDeviceBehaviourAssociation();

    updateSelectOptionsGroup();
    updateSelectOptionsRules();
    updateSelectOptionsDevice();
});


function hideForms() {
    console.log('hideForms() is called');

    [groupForm, ruleForm, deviceForm, homeDashboard ].forEach(form => {
        // if (form === ruleForm) {
        //     fetchDevices();
        // }
        form.classList.remove('flex');
        form.classList.add('hidden');
    });

    formContainer.classList.add('hidden');
}


const cardIcons = document.querySelectorAll('.card-icon');

cardIcons.forEach(element => {
    element.addEventListener('click', () => {
        cardIcons.forEach(icon => {
            if (icon !== element) {
                icon.classList.remove('shift-right');
            }
        });
        element.classList.toggle('shift-right');

        if (element.classList.contains('shift-right')) { // the active one
            hideForms();
            if (element === document.getElementById('groupsIcon')) {
                fetchRules();
                formContainer.classList.remove('hidden');
                groupForm.classList.remove('hidden');
            } else if (element === document.getElementById('rulesIcon')) {
                fetchGroups();
                fetchDevices();
                formContainer.classList.remove('hidden');
                ruleForm.classList.remove('hidden');
            } else if (element === document.getElementById('devicesIcon')) {
                fetchRules();
                formContainer.classList.remove('hidden');
                deviceForm.classList.remove('hidden');
            }else if (element === document.getElementById('homeIcon')) {
                fetchGroups();
                fetchDevices();
                populateGroupsInTableHtml();
                populateDevicesInTableHtml();
                populateRulesInTableHtml();
                homeDashboard.classList.remove('hidden');
            }
        } else {
            hideForms();
        }
    });
});


//Group
const groupNameInput = document.getElementById('groupName');
const groupSelects = document.querySelectorAll('.groupname-select');

function updateSelectOptionsGroup() {
    const groupName = groupNameInput.value;

    groupSelects.forEach(select => {
        const firstOption = select.options[0];
        firstOption.textContent = `Associate Rule to ${groupName}`;
    });
}

groupNameInput.addEventListener('input', updateSelectOptionsGroup);

//Rule
const ruleNameInput = document.getElementById('ruleName');
const ruleSelects = document.querySelectorAll('.rulename-select');

function updateSelectOptionsRules() {
    const ruleName = ruleNameInput.value;

    ruleSelects.forEach(select => {
        const firstOption = select.options[0];
        if (select.id.startsWith('groupSelector')){
            firstOption.textContent = `Associate Group to ${ruleName}`;
        } else if (select.id.startsWith('deviceSelector')) {
            firstOption.textContent = `Associate Device to ${ruleName}`;
        }
    });

    const eventSelector = document.getElementById('eventSelector');
    if (eventSelector) {
        const firstOption = eventSelector.options[0];
        firstOption.textContent = `Associate Event to ${ruleName}`;
    }
}

ruleNameInput.addEventListener('input', updateSelectOptionsRules);


//device
const deviceNameInput = document.getElementById('deviceName');
const deviceSelects = document.querySelectorAll('.devicename-select');

function updateSelectOptionsDevice() {
    const deviceName = deviceNameInput.value;

    deviceSelects.forEach(select => {
        const firstOption = select.options[0];
        if (select.id === 'deviceTypeSelector'){
            firstOption.textContent = `Associate a Type to ${deviceName}`;
        } else if (select.id.startsWith('ruleSelector')) {
            firstOption.textContent = `Associate Rule to ${deviceName}`;
        }
    });
}

deviceNameInput.addEventListener('input', updateSelectOptionsDevice);

// Displaying Device to Corresponding behaviour
function setupDeviceBehaviourAssociation() {
    const deviceSelects = document.querySelectorAll('.device-select');

    deviceSelects.forEach(deviceSelect => {
        deviceSelect.addEventListener('change', () => {
            const selectedDeviceName = deviceSelect.textContent.substring(19);
            const behaviourSelectId = deviceSelect.getAttribute('behaviour-select-id');
            const behaviourSelect = document.getElementById(behaviourSelectId);

            if (behaviourSelect) {
                const firstOption = behaviourSelect.options[0];
                firstOption.textContent = `Associate Behaviour to ${selectedDeviceName}`;
            }
        });
    });
}

// Add new device selector in Rule Form
function addNewDeviceSelector() {
    const deviceSelectorsContainer = document.getElementById('deviceSelectorsContainer');
    const deviceCount = deviceSelectorsContainer.querySelectorAll('.device-select').length + 1;

    const deviceDiv = document.createElement('div');
    deviceDiv.className = 'flex mb-10';

    const deviceSelect = document.createElement('select');
    deviceSelect.className = 'form-field device-select device-populate';
    deviceSelect.name = `devices${deviceCount}`;
    deviceSelect.id = `deviceSelector${deviceCount}`;
    deviceSelect.setAttribute('behaviour-select-id', `deviceBehaviourSelector${deviceCount}`);

    const deviceOptionPlaceholder = document.createElement('option');
    deviceOptionPlaceholder.value = '';
    deviceOptionPlaceholder.textContent = 'Associate Device to ';
    deviceSelect.appendChild(deviceOptionPlaceholder);

    const deviceOptions = ['placeholder1', 'placeholder2', 'placeholder3'];
    deviceOptions.forEach(optionValue => {
        const option = document.createElement('option');
        option.value = optionValue;
        option.textContent = optionValue;
        deviceSelect.appendChild(option);
    });

    deviceDiv.appendChild(deviceSelect);

    //Behaviour Selector
    const behaviourDiv = document.createElement('div');
    behaviourDiv.className = 'flex mb-10';

    const behaviourSelect = document.createElement('select');
    behaviourSelect.className = 'form-field behaviour-select';
    behaviourSelect.name = `deviceBehaviour${deviceCount}`;
    behaviourSelect.id = `deviceBehaviourSelector${deviceCount}`;


    const behaviourOptionPlaceholder = document.createElement('option');
    behaviourOptionPlaceholder.value = '';
    behaviourOptionPlaceholder.textContent = 'Associate Behaviour to';
    behaviourSelect.appendChild(behaviourOptionPlaceholder);

    const behaviourOptions = ['placeholder1', 'placeholder2', 'placeholder3'];
    behaviourOptions.forEach(optionValue => {
        const option = document.createElement('option');
        option.value = optionValue;
        option.textContent = optionValue;
        behaviourSelect.appendChild(option);
    });

    behaviourDiv.appendChild(behaviourSelect);

    // Appendending to Container
    deviceSelectorsContainer.appendChild(deviceDiv);
    deviceSelectorsContainer.appendChild(behaviourDiv);


    deviceSelect.addEventListener('change', () => {
        const selectedDeviceName = deviceSelect.value;
        const firstOption = behaviourSelect.options[0];
        firstOption.textContent = `Associate Behaviour to ${selectedDeviceName}`;
    });

}


const addDeviceButton = document.getElementById('addDevice');
addDeviceButton.addEventListener('click', (event) => {
    event.preventDefault();
    addNewDeviceSelector();
    fetchDevices();
});


const removeDeviceButton = document.getElementById('removeDevice');
removeDeviceButton.addEventListener('click', (event) => {
    event.preventDefault();
    removeLastDeviceSelector();
});

function removeLastDeviceSelector() {
    const deviceSelectorsContainer = document.getElementById('deviceSelectorsContainer');
    const deviceSelects = deviceSelectorsContainer.querySelectorAll('.device-select');

    if (deviceSelects.length > 1) {
        deviceSelectorsContainer.removeChild(deviceSelectorsContainer.lastChild); // Remove behaviourDiv
        deviceSelectorsContainer.removeChild(deviceSelectorsContainer.lastChild); // Remove deviceDiv
    } else {
        alert('Cannot remove the last device selector.');
    }
}

//home dashboard tabs
const tabs = document.querySelectorAll('[data-tab-target]');
const activeClass = 'bg-indigo-200';
//first class as default
tabs[0].classList.add(activeClass);
document.querySelector('#tab1').classList.remove('hidden');


tabs.forEach(tab => {
    tab.addEventListener('click', () => {
        const targetContent = document.querySelector(tab.dataset.tabTarget)
        // console.log(targetContent);

        document.querySelectorAll('.hometab-content').forEach(content => content.classList.add('hidden'));
        
        targetContent.classList.remove('hidden');

        document.querySelectorAll('.bg-indigo-200').forEach(activeTab => activeTab.classList.remove(activeClass));
        tab.classList.add(activeClass);
    });
});


const updateForm = document.getElementById('updateForm');
updateForm.addEventListener('submit', function(event) {
    event.preventDefault();
    fetchCurrentUser();

    const formData = new FormData(this); 
    const data = Object.fromEntries(formData.entries());
    
    const trimmedData = {
        username: data.username.trim()
    };


    if (data.username !== trimmedData.username) {
        console.log('Trailing spaces removed from inputs.');
    }

    fetch(`http://localhost:8080/users/${localStorage.getItem('userId')}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(trimmedData), 
    })
    .then(response => response.json()) 
    .then(result => {
        console.log(result);
        updateForm.reset();
        alert("Your username change was succesfull. Please log in again to further use the website")
        window.location.href = '/public/index.html';

    })
    .catch(error => {
        console.error('Error:', error);
        
    });



});

