document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = new FormData(this); 
    const data = Object.fromEntries(formData.entries()); 

    const trimmedData = {
        username: data.username.trim(),
        password: data.password.trim(),
    };


    if (data.username !== trimmedData.username || data.password !== trimmedData.password) {
        console.log('Trailing spaces removed from inputs.');
    }
        

    fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(trimmedData),
    })
    .then(response => response.json())
    .then(result => {
        console.log('Success:', result);
        window.location.href = '/public/index.html';
    })
    .catch(error => {
        console.error('Error:', error);
        
    });
});