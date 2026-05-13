const registroForm = document.getElementById("registroForm");
const nombreInput = document.getElementById("nombre");
const apellidoInput = document.getElementById("apellido");
const documentoInput = document.getElementById("documento");
const correoInput = document.getElementById("correo");
const telefonoInput = document.getElementById("telefono");
const passwordInput = document.getElementById("password");
const registerBtn = document.getElementById("registerBtn");
const messageBox = document.getElementById("messageBox");
const modalOverlay = document.getElementById("modalOverlay");
const togglePassword = document.getElementById("togglePassword");
const ruleLength = document.getElementById("ruleLength");
const ruleUppercase = document.getElementById("ruleUppercase");
const ruleNumber = document.getElementById("ruleNumber");

document.addEventListener("DOMContentLoaded", () => {
    configurarPassword();
    validarPassword();
    configurarValidaciones();
});

function configurarPassword(){
    togglePassword.addEventListener("click", () => {

        const icon = togglePassword.querySelector("i");

        if(passwordInput.type === "password"){
            passwordInput.type = "text";
            icon.classList.remove("bi-eye-fill");
            icon.classList.add("bi-eye-slash-fill");

        }else{
            passwordInput.type = "password";
            icon.classList.remove("bi-eye-slash-fill");
            icon.classList.add("bi-eye-fill");
        }
    });
}

function validarPassword(){

    passwordInput.addEventListener("input", () => {
        const password = passwordInput.value;
        const tieneLongitud = password.length >= 8;
        const tieneMayuscula = /[A-Z]/.test(password);
        const tieneNumero = /\d/.test(password);
        actualizarRegla(ruleLength, tieneLongitud);
        actualizarRegla(ruleUppercase, tieneMayuscula);
        actualizarRegla(ruleNumber, tieneNumero);
        validarFormulario();
    });
}

function actualizarRegla(regla, valido){

    if(valido){
        regla.classList.add("valid");
    }else{
        regla.classList.remove("valid");
    }

}

function configurarValidaciones(){

    const inputs = [
        nombreInput,
        apellidoInput,
        documentoInput,
        correoInput,
        telefonoInput,
        passwordInput
    ];

    inputs.forEach(input => {
        input.addEventListener("input", validarFormulario);
    });

}

function validarFormulario(){

    const nombreValido =
        nombreInput.value.trim() &&
        !nombreInput.value.includes(" ");

    const apellidoValido =
        apellidoInput.value.trim() &&
        !apellidoInput.value.includes(" ");

    const documentoValido =
        /^\d+$/.test(documentoInput.value.trim());

    const correoValido =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/
            .test(correoInput.value.trim());

    const telefonoValido =
        /^3\d{9}$/
            .test(telefonoInput.value.trim());

    const password = passwordInput.value;

    const passwordValido =
        password.length >= 8 &&
        /[A-Z]/.test(password) &&
        /\d/.test(password);

    registerBtn.disabled = !(
        nombreValido &&
        apellidoValido &&
        documentoValido &&
        correoValido &&
        telefonoValido &&
        passwordValido
    );
}

registroForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    ocultarMensaje();
    const datos = {
        documento: documentoInput.value.trim(),
        nombre: nombreInput.value.trim(),
        apellido: apellidoInput.value.trim(),
        correo: correoInput.value.trim(),
        telefono: telefonoInput.value.trim(),
        password: passwordInput.value.trim()
    };

    try{
        const response = await fetch("/api/auth/registro", {
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(datos)
        });

        const data = await response.json();
        if(data.success){
            modalOverlay.classList.add("active");
            registroForm.reset();
            registerBtn.disabled = true;
        }else{
            mostrarMensaje(data.message);
        }

    }catch(error){
        console.error(error);
        mostrarMensaje(
            "Error conectando con el servidor"
        );
    }
});

function mostrarMensaje(texto){
    messageBox.textContent = texto;
    messageBox.className = "message-box error";
}

function ocultarMensaje(){
    messageBox.className = "message-box";
}