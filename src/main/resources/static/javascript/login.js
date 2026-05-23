const loginForm = document.getElementById("loginForm");

const correoInput = document.getElementById("correo");
const passwordInput = document.getElementById("password");

const messageBox = document.getElementById("messageBox");

const togglePassword = document.getElementById("togglePassword");

document.addEventListener("DOMContentLoaded", () => {

    configurarPassword();

    verificarSesion();

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

function verificarSesion() {

    const params = new URLSearchParams(window.location.search);
    const cambiarCuenta = params.get("cambiarCuenta");

    if (cambiarCuenta === "true") {
        localStorage.removeItem("usuario");
        return;
    }

    const usuario = obtenerUsuarioLogueado();

    if (usuario) {
        if (usuario.rol === "ADMIN") {
            window.location.href = `/admin?lang=${obtenerIdiomaActual()}`;
        } else if (usuario.rol === "CLIENTE") {
            window.location.href = `/catalogo?lang=${obtenerIdiomaActual()}`;
        }
    }
}

loginForm.addEventListener("submit", async (e) => {

    e.preventDefault();

    ocultarMensaje();

    const datos = {
        correo: correoInput.value.trim(),
        password: passwordInput.value.trim()
    };

    try{

        const response = await fetch("/api/auth/login", {

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body:JSON.stringify(datos)

        });

        const data = await response.json();

        if(data.success){

            mostrarMensaje(data.message, "success");

            localStorage.removeItem("usuario");
            localStorage.setItem("usuario", JSON.stringify(data));

            setTimeout(() => {

                if(data.rol === "ADMIN"){
                    window.location.href = `/admin?lang=${obtenerIdiomaActual ? obtenerIdiomaActual() : "es"}`;
                }else{
                    window.location.href = `/catalogo?lang=${obtenerIdiomaActual ? obtenerIdiomaActual() : "es"}`;
                }

            }, 1300);

        }else{

            mostrarMensaje(data.message, "error");

        }

    }catch(error){

        console.error(error);

        mostrarMensaje(
            "Error conectando con el servidor",
            "error"
        );

    }

});

function mostrarMensaje(texto, tipo){

    messageBox.textContent = texto;

    messageBox.className = `message-box ${tipo}`;

}

function ocultarMensaje(){

    messageBox.className = "message-box";

}