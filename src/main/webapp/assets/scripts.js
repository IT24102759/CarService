document.getElementById("signup-form").addEventListener("submit", function(event) {
    event.preventDefault();

    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;
    const passwordError = document.getElementById("password-error");

    // Check if passwords match
    if (password !== confirmPassword) {
        passwordError.textContent = "Passwords do not match!";
        passwordError.style.display = "block";
        return;
    } else {
        passwordError.style.display = "none";
    }

    // Simulate successful signup (since this is a student project)
    alert("Account created successfully! (Demo)");
    this.reset(); // Clear form
});