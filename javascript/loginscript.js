function showError() {
    const usernameField = document.getElementById('username');
    const passwordField = document.getElementById('password');

    usernameField.classList.remove('border', 'border-gray-300');
    usernameField.classList.add('border', 'border-red-500');

    passwordField.classList.remove('border', 'border-gray-300');
    passwordField.classList.add('border','border-red-500');

    let errorMessage = document.getElementById('error-message');
    if (!errorMessage) {
        errorMessage = document.createElement('p');
        errorMessage.id = 'error-message';
        errorMessage.textContent = "Invalid username or password";
        errorMessage.classList.add('text-red-500', 'mt-2', 'text-semibold');
        document.getElementById('passwordForm').appendChild(errorMessage);
    }
}

function logout() {
    localStorage.removeItem('token'); 
    window.location.href = '/login.html'; 
}


document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = new FormData(this); 
    const data = Object.fromEntries(formData.entries()); 

    fetch('http://localhost:8080/auth/authenticate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data), 
    })
    .then(response => response.json()) 
    // Handle login success and gets the token
    .then(result => {
        if (result.token) {  
           
            localStorage.setItem('token', result.token);
            window.location.href = '/home.html';
        } else {
            showError();
        }
    })
    .catch(error => {
        showError();
        console.error('Error:', error);
        
    });
});

//Phase 2 
const token = localStorage.getItem('token');  


