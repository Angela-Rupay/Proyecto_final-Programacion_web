const I18N = {
    es: {
        navHome: "Inicio",
        navCatalog: "Catálogo",
        navLogin: "Iniciar sesión",
        navRegister: "Registrarse",
        navAdmin: "Panel admin",
        navCart: "Carrito",
        navHistory: "Historial",
        navLogout: "Cerrar sesión",
        loginRequired: "Inicia sesión primero para continuar",
        langEs: "ES",
        langEn: "EN",
        langPt: "PT"
    },
    en: {
        navHome: "Home",
        navCatalog: "Catalog",
        navLogin: "Log in",
        navRegister: "Sign up",
        navAdmin: "Admin panel",
        navCart: "Cart",
        navHistory: "History",
        navLogout: "Log out",
        loginRequired: "Please log in first to continue",
        langEs: "ES",
        langEn: "EN",
        langPt: "PT"
    },
    pt: {
        navHome: "Início",
        navCatalog: "Catálogo",
        navLogin: "Entrar",
        navRegister: "Registrar-se",
        navAdmin: "Painel admin",
        navCart: "Carrinho",
        navHistory: "Histórico",
        navLogout: "Sair",
        loginRequired: "Faça login primeiro para continuar",
        langEs: "ES",
        langEn: "EN",
        langPt: "PT"
    }
};

document.addEventListener("DOMContentLoaded", () => {
    configurarIdiomaDesdeUrl();
    configurarNavbar();

    const messageBox = document.getElementById("messageBox");

    const params = new URLSearchParams(window.location.search);
    const mensajeUrl = params.get("mensaje");

    if (mensajeUrl === "login" && messageBox) {
        messageBox.textContent = traducir("loginRequired");
        messageBox.classList.add("error");
    }
});

function configurarIdiomaDesdeUrl() {
    const params = new URLSearchParams(window.location.search);
    const lang = params.get("lang");

    if (["es", "en", "pt"].includes(lang)) {
        localStorage.setItem("lang", lang);
    }
}

function obtenerIdiomaActual() {
    return localStorage.getItem("lang") || "es";
}

function traducir(clave) {
    const idioma = obtenerIdiomaActual();
    return I18N[idioma]?.[clave] || I18N.es[clave] || clave;
}

function construirUrlConIdioma(lang) {
    const url = new URL(window.location.href);
    url.searchParams.set("lang", lang);
    return url.pathname + url.search;
}

function construirSelectorIdioma() {
    return `
        <div class="language-switch">
            <a href="${construirUrlConIdioma("es")}" class="${obtenerIdiomaActual() === "es" ? "active" : ""}">
                ${traducir("langEs")}
            </a>
            <span>|</span>
            <a href="${construirUrlConIdioma("en")}" class="${obtenerIdiomaActual() === "en" ? "active" : ""}">
                ${traducir("langEn")}
            </a>
            <span>|</span>
            <a href="${construirUrlConIdioma("pt")}" class="${obtenerIdiomaActual() === "pt" ? "active" : ""}">
                ${traducir("langPt")}
            </a>
        </div>
    `;
}

function configurarNavbar() {
    const navbar = document.querySelector(".navbar");

    if (!navbar) return;

    const usuario = JSON.parse(localStorage.getItem("usuario"));

    if (!usuario) {
        navbar.innerHTML = `
            <a href="/?lang=${obtenerIdiomaActual()}">${traducir("navHome")}</a>
            <a href="/catalogo?lang=${obtenerIdiomaActual()}">${traducir("navCatalog")}</a>
            <a href="/login?lang=${obtenerIdiomaActual()}">${traducir("navLogin")}</a>
            <a href="/registro?lang=${obtenerIdiomaActual()}" class="nav-btn">${traducir("navRegister")}</a>
            ${construirSelectorIdioma()}
        `;
        return;
    }

    if (usuario.rol === "ADMIN") {
        navbar.innerHTML = `
            <a href="/?lang=${obtenerIdiomaActual()}">${traducir("navHome")}</a>
            <a href="/catalogo?lang=${obtenerIdiomaActual()}">${traducir("navCatalog")}</a>
            <a href="/admin?lang=${obtenerIdiomaActual()}">${traducir("navAdmin")}</a>
            ${construirSelectorIdioma()}
            <button class="logout-btn" id="logoutBtn">
                <i class="bi bi-box-arrow-right"></i>
                ${traducir("navLogout")}
            </button>
        `;
    } else {
        navbar.innerHTML = `
            <a href="/?lang=${obtenerIdiomaActual()}">${traducir("navHome")}</a>
            <a href="/catalogo?lang=${obtenerIdiomaActual()}">${traducir("navCatalog")}</a>
            <a href="/carrito?lang=${obtenerIdiomaActual()}">${traducir("navCart")}</a>
            <a href="/historial?lang=${obtenerIdiomaActual()}">${traducir("navHistory")}</a>
            ${construirSelectorIdioma()}
            <button class="logout-btn" id="logoutBtn">
                <i class="bi bi-box-arrow-right"></i>
                ${traducir("navLogout")}
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
        sessionStorage.clear();
        window.location.href = `/login?cambiarCuenta=true&lang=${obtenerIdiomaActual()}`;
    });
}

function obtenerUsuarioLogueado() {
    return JSON.parse(localStorage.getItem("usuario"));
}

function protegerPaginaCliente() {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario) {
        window.location.href = `/login?mensaje=login&lang=${obtenerIdiomaActual()}`;
        return;
    }

    if (usuario.rol !== "CLIENTE") {
        window.location.href = `/sin-permisos?lang=${obtenerIdiomaActual()}`;
    }
}

function protegerPaginaAdmin() {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario) {
        window.location.href = `/login?mensaje=login&lang=${obtenerIdiomaActual()}`;
        return;
    }

    if (usuario.rol !== "ADMIN") {
        window.location.href = `/sin-permisos?lang=${obtenerIdiomaActual()}`;
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
        window.location.href = `/login?mensaje=login&lang=${obtenerIdiomaActual()}`;
        return true;
    }

    if (response.status === 403) {
        window.location.href = `/sin-permisos?lang=${obtenerIdiomaActual()}`;
        return true;
    }

    return false;
}