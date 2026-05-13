document.addEventListener("DOMContentLoaded", () => {
    configurarNavbar();
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

    if (!usuario || usuario.rol !== "CLIENTE") {
        window.location.href = "/login";
    }
}

function protegerPaginaAdmin() {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario || usuario.rol !== "ADMIN") {
        window.location.href = "/login";
    }
}