document.addEventListener("DOMContentLoaded", () => {
    configurarNavbar();
    const messageBox = document.getElementById("messageBox");

    const params = new URLSearchParams(window.location.search);
    const mensajeUrl = params.get("mensaje");

    if (mensajeUrl === "login" && messageBox) {
        messageBox.textContent = "Inicia sesión primero para continuar";
        messageBox.classList.add("error");
    }
});

function configurarNavbar() {
    const navbar = document.querySelector(".navbar");

    if (!navbar) return;

    const usuario = JSON.parse(localStorage.getItem("usuario"));

    if (!usuario) {
        navbar.innerHTML = `
            <a href="/">Inicio</a>
            <a href="/catalogo">Catálogo</a>
            <a href="/login">Iniciar sesión</a>
            <a href="/registro" class="nav-btn">Registrarse</a>
        `;
        return;
    }

    if (usuario.rol === "ADMIN") {
        navbar.innerHTML = `
            <a href="/">Inicio</a>
            <a href="/catalogo">Catálogo</a>
            <a href="/admin">Panel admin</a>
            <button class="logout-btn" id="logoutBtn">
                <i class="bi bi-box-arrow-right"></i>
                Cerrar sesión
            </button>
        `;
    } else {
        navbar.innerHTML = `
            <a href="/">Inicio</a>
            <a href="/catalogo">Catálogo</a>
            <a href="/carrito">Carrito</a>
            <a href="/historial">Historial</a>
            <button class="logout-btn" id="logoutBtn">
                <i class="bi bi-box-arrow-right"></i>
                Cerrar sesión
            </button>
        `;
    }

    configurarLogout();
}

function configurarLogout() {
    const logoutBtn = document.getElementById("logoutBtn");

    if (!logoutBtn) return;

    logoutBtn.addEventListener("click", () => {
        localStorage.removeItem("usuario");
        window.location.href = "/login";
    });
}

function obtenerUsuarioLogueado() {
    return JSON.parse(localStorage.getItem("usuario"));
}

function protegerPaginaCliente() {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario) {
        window.location.href = "/login?mensaje=login";
        return;
    }

    if (usuario.rol !== "CLIENTE") {
        window.location.href = "/sin-permisos";
    }
}


function protegerPaginaAdmin() {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario) {
        window.location.href = "/login?mensaje=login";
        return;
    }

    if (usuario.rol !== "ADMIN") {
        window.location.href = "/sin-permisos";
    }
}

function obtenerToken() {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario || !usuario.token) {
        return null;
    }

    return usuario.token;
}

function obtenerHeadersAuth() {
    const token = obtenerToken();

    if (!token) {
        return {};
    }

    return {
        "Authorization": `Bearer ${token}`
    };
}
function obtenerHeadersJsonAuth() {
    const token = obtenerToken();
    if (!token) {
        return {
            "Content-Type": "application/json"
        };
    }
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    };
}
function manejarRespuestaNoAutorizada(response) {
    if (response.status === 401) {
        localStorage.removeItem("usuario");
        window.location.href = "/login?mensaje=login";
        return true;
    }
    if (response.status === 403) {
        window.location.href = "/sin-permisos";
        return true;
    }
    return false;
}