const completarPerfilForm = document.getElementById("completarPerfilForm");
const documentoInput = document.getElementById("documento");
const telefonoInput = document.getElementById("telefono");
const completarBtn = document.getElementById("completarBtn");
const messageBox = document.getElementById("messageBox");
const modalOverlay = document.getElementById("modalOverlay");

const nombreGoogle = document.getElementById("nombreGoogle");
const correoGoogle = document.getElementById("correoGoogle");

const params = new URLSearchParams(window.location.search);

const email = params.get("email");
const googleId = params.get("googleId");
const nombre = params.get("nombre");
const apellido = params.get("apellido");

document.addEventListener("DOMContentLoaded", () => {
    configurarSliderSanjuanero();
    cargarDatosGoogle();
    configurarValidacionesCompletarPerfil();
    permitirSoloNumeros(documentoInput);
    permitirSoloNumeros(telefonoInput);
});

function cargarDatosGoogle() {
    if (!email || !googleId) {
        mostrarMensaje("No se encontró información de Google. Inicia sesión nuevamente.");

        setTimeout(() => {
            window.location.href = "/login";
        }, 2200);

        return;
    }

    const nombreCompleto = `${nombre || ""} ${apellido || ""}`.trim();

    nombreGoogle.textContent = nombreCompleto || "Usuario Google";
    correoGoogle.textContent = email;
}

function configurarValidacionesCompletarPerfil() {
    documentoInput.addEventListener("input", validarFormularioCompletarPerfil);
    telefonoInput.addEventListener("input", validarFormularioCompletarPerfil);
}

function validarFormularioCompletarPerfil() {
    const documentoValido = /^\d{6,12}$/.test(documentoInput.value.trim());
    const telefonoValido = /^3\d{9}$/.test(telefonoInput.value.trim());

    completarBtn.disabled = !(documentoValido && telefonoValido);
}

completarPerfilForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    ocultarMensaje();

    const documento = documentoInput.value.trim();
    const telefono = telefonoInput.value.trim();

    if (!/^\d{6,12}$/.test(documento)) {
        mostrarMensaje("Ingresa una cédula válida. Solo números, mínimo 6 dígitos.");
        return;
    }

    if (!/^3\d{9}$/.test(telefono)) {
        mostrarMensaje("Ingresa un celular colombiano válido. Debe iniciar por 3 y tener 10 dígitos.");
        return;
    }

    const datos = {
        documento: Number(documento),
        telefono: telefono,
        email: email,
        googleId: googleId,
        nombre: nombre || "Usuario",
        apellido: apellido || ""
    };

    try {
        completarBtn.disabled = true;
        completarBtn.innerHTML = `
            <i class="bi bi-hourglass-split"></i>
            Registrando...
        `;

        const response = await fetch("/api/auth/google/completar-perfil", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datos)
        });

        const data = await response.json();

        if (data.success) {
            localStorage.setItem("usuario", JSON.stringify(data));
            modalOverlay.classList.add("active");

            setTimeout(() => {
                window.location.href = "/catalogo";
            }, 1600);

        } else {
            mostrarMensaje(data.message || "No se pudo completar el perfil.");
        }

    } catch (error) {
        console.error(error);
        mostrarMensaje("Error conectando con el servidor.");

    } finally {
        completarBtn.disabled = false;
        completarBtn.innerHTML = `
            <i class="bi bi-check-circle-fill"></i>
            Finalizar registro
        `;
        validarFormularioCompletarPerfil();
    }
});

function mostrarMensaje(texto) {
    messageBox.textContent = texto;
    messageBox.className = "message-box error";
}

function ocultarMensaje() {
    messageBox.className = "message-box";
}

function configurarSliderSanjuanero() {

    const sliderImage = document.getElementById("sliderImage");
    const sliderTitle = document.getElementById("sliderTitle");
    const sliderDots = document.getElementById("sliderDots");

    if (!sliderImage || !sliderTitle || !sliderDots) return;

    const figuras = [
        { imagen: "/images/invitacion.jpg", titulo: "Invitación" },
        { imagen: "/images/ocho.jpg", titulo: "Los ochos" },
        { imagen: "/images/coqueteo.jpg", titulo: "El coqueteo" },
        { imagen: "/images/arrodillada.jpg", titulo: "La arrodillada" },
        { imagen: "/images/levantada-pie.jpg", titulo: "Levantada del pie" },
        { imagen: "/images/arrastrada-ala.jpg", titulo: "La arrastrada del ala" },
        { imagen: "/images/secreto.jpg", titulo: "El secreto" },
        { imagen: "/images/salida-final.jpg", titulo: "Salida final" }
    ];

    let indice = 0;

    sliderDots.innerHTML = "";

    figuras.forEach((_, i) => {
        const dot = document.createElement("span");
        dot.classList.add("slider-dot");

        if (i === 0) {
            dot.classList.add("active");
        }

        sliderDots.appendChild(dot);
    });

    const dots = document.querySelectorAll(".slider-dot");

    function actualizarSlider(nuevoIndice) {
        sliderImage.classList.add("fade-out");

        setTimeout(() => {
            indice = nuevoIndice;

            sliderImage.src = figuras[indice].imagen;
            sliderTitle.textContent = figuras[indice].titulo;

            dots.forEach(dot => dot.classList.remove("active"));
            dots[indice].classList.add("active");

            sliderImage.classList.remove("fade-out");
        }, 450);
    }

    setInterval(() => {
        const siguiente = (indice + 1) % figuras.length;
        actualizarSlider(siguiente);
    }, 3000);
}

function permitirSoloNumeros(input) {
    if (!input) return;

    input.addEventListener("input", () => {
        input.value = input.value.replace(/\D/g, "");
        validarFormularioCompletarPerfil();
    });

    input.addEventListener("paste", (event) => {
        event.preventDefault();

        const textoPegado = (event.clipboardData || window.clipboardData).getData("text");
        input.value += textoPegado.replace(/\D/g, "");
        validarFormularioCompletarPerfil();
    });
}