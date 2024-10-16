const groupForm = document.getElementById('groupForm');
const ruleForm = document.getElementById('ruleForm');
const deviceForm = document.getElementById('deviceForm');
const formContainer = document.getElementById('formContainer');

const loaded = document.addEventListener('DOMContentLoaded', () => {
    hideForms(); 
});

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

function hideForms() {

    [groupForm, ruleForm, deviceForm].forEach(form => {
        form.classList.remove('flex');
        form.classList.add('hidden');
    });

    formContainer.classList.remove('flex');
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

        // ------------
        if (element.classList.contains('shift-right')) { //the active one 
            hideForms();

            if (element === document.getElementById('groupsIcon')) {
                formContainer.classList.remove('hidden');
                formContainer.classList.add('flex');
                groupForm.classList.remove('hidden');
                groupForm.classList.add('flex');
            } else if (element === document.getElementById('rulesIcon')) {
                formContainer.classList.remove('hidden');
                formContainer.classList.add('flex');
                ruleForm.classList.remove('hidden');
                ruleForm.classList.add('flex');
            } else if (element === document.getElementById('devicesIcon')) {
                formContainer.classList.remove('hidden');
                formContainer.classList.add('flex');
                deviceForm.classList.remove('hidden');
                deviceForm.classList.add('flex');
            }
        } else {

            hideForms();
        }
    });
});