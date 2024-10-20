
// console.log(token);





function deviceFormSubmission() {
    const deviceForm = document.getElementById("deviceForm");
    deviceForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const formData = new FormData(this);
        const data = Object.fromEntries(formData.entries());

        const deviceType = data.deviceType;
        const deviceName = data.deviceName;

        const deviceData = {
            name: deviceName,
            type: deviceType,
        };

        console.log("Device Data:", JSON.stringify(deviceData, null, 2));

        sendDeviceData(deviceData);
    });

    function sendDeviceData(data) {
        const token = localStorage.getItem('token');
        fetch("http://localhost:8080/devices/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(data),
        })
            .then((response) => response.json())
            .then((result) => {
                console.log("Response:", result);
                deviceForm.reset();
            })
            .catch((error) => {
                console.error("Error:", error);
                console.log("The submitted JSON was: \n" + data)
            });
    }
}

function groupFormSubmission() {
    const groupForm = document.getElementById("groupForm");
    groupForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const formData = new FormData(this);
        const data = Object.fromEntries(formData.entries());

        const groupData = {
            name: data.groupName,
        };

        console.log("Group Data:", JSON.stringify(groupData, null, 2));
        sendGroupData(groupData);
    });

    function sendGroupData(data) {
        fetch("http://localhost:8080/groups/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(data),
        })
            .then((response) => response.json())
            .then((result) => {
                console.log(" Response:", result);
                groupForm.reset();
            })
            .catch((error) => {
                console.error("Error:", error);
                console.log("The submitted JSON was: \n" + data)
            });
    }
}

function ruleFormSubmission() {
    const ruleForm = document.getElementById("ruleForm");
    ruleForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const form = event.target;
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        const eventSelected = data.event.toUpperCase();

        const groupId = data.groups || null;
        let groupDto = null;

        if (groupId) {
            groupDto = await getGroupById(groupId); 
            if (!groupDto) {
                alert("Failed to fetch the selected group. Please try again.");
                return; 
            }
        }

        const ruleData = {
                ruleName: data.ruleName,
                description: data.ruleDescription,
                event: eventSelected,
                groupDto: groupDto,
                deviceDtos: [],
                deviceBehaviours: {},
            };


        const deviceSelectors = form.querySelectorAll(".device-select"); 

        const deviceFetchPromises = Array.from(deviceSelectors).map(async (deviceSelect) => { // -- Convert NodeList to Array and map
            const deviceId = deviceSelect.value;
            const behaviourSelectId = deviceSelect.getAttribute("behaviour-select-id");
            const behaviourSelect = form.querySelector(`#${behaviourSelectId}`);
            const behavior = behaviourSelect ? behaviourSelect.value : null;

            if (deviceId) {  
                const deviceDto = await getDeviceById(deviceId); 
                if (deviceDto) { 
                    ruleData.deviceDtos.push(deviceDto); 

                    const behaviorValue = behavior ? behavior.toUpperCase() : null; 
                    ruleData.deviceBehaviours[deviceId] = behaviorValue; 
                } else {
                    alert("Failed to fetch the selected device. Please try again.");
                }
            }
        });

        try {
            await Promise.all(deviceFetchPromises); 
            console.log("Rule Data:", JSON.stringify(ruleData, null, 2));

            sendRuleData(ruleData);
        } catch (error) { 
            console.error("Error during rule form submission:", error);
            alert("An error occurred while processing the rule form. Please try again.");
        }
    });

    function sendRuleData(data) {
        fetch("http://localhost:8080/rules/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(data),
        })
            .then((response) => response.json())
            .then((result) => {
                console.log("Response:", result);
                alert("Rule created successfully!");
                ruleForm.reset();
            })
            .catch((error) => {
                console.error("Error:", error);
                console.log("The submitted JSON was: \n" + data)
            });
    }
}

function attachFormSubmitListeners() {
    const deviceForm = document.getElementById("deviceForm");
    const groupForm = document.getElementById("groupForm");
    const ruleForm = document.getElementById("ruleForm");

    deviceForm.addEventListener("submit", deviceFormSubmission());

    groupForm.addEventListener("submit", groupFormSubmission());

    ruleForm.addEventListener("submit", ruleFormSubmission()); 
}

document.addEventListener('DOMContentLoaded', () => {
    attachFormSubmitListeners();
});
