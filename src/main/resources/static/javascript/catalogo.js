const productsGrid = document.getElementById("productsGrid");
const emptyMessage = document.getElementById("emptyMessage");
const productCount = document.getElementById("productCount");
const catalogTitle = document.getElementById("catalogTitle");
const modelInfo = document.getElementById("modelInfo");
const filterButtons = document.querySelectorAll(".filter-btn");

const modelDescriptions = {
    todos: {
        title: "Todos los vestidos",
        text: "Visualiza todos los diseños disponibles actualmente."
    },
    TT: {
        title: "Modelo Tradicional",
        text: "Diseños clásicos inspirados en la esencia del Sanjuanero Huilense."
    },
    TF: {
        title: "Modelo Fantasía",
        text: "Vestidos llamativos, coloridos y elegantes para destacar en escena."
    },
    P: {
        title: "Modelo Pintado",
        text: "Piezas con detalles artísticos y acabados llenos de expresión cultural."
    },
    TPRO: {
        title: "Modelo Profesional",
        text: "Trajes elaborados para concursos, presentaciones y eventos especiales."
    }
};

document.addEventListener("DOMContentLoaded", () => {
    cargarVestidos();
    configurarFiltros();
});

function configurarFiltros() {
    filterButtons.forEach(button => {
        button.addEventListener("click", () => {
            filterButtons.forEach(btn => btn.classList.remove("active"));
            button.classList.add("active");

            const modelo = button.dataset.modelo;

            actualizarInfoModelo(modelo);

            if (modelo === "todos") {
                cargarVestidos();
            } else {
                cargarVestidosPorModelo(modelo);
            }
        });
    });
}

async function cargarVestidos() {
    try {
        const response = await fetch("/api/vestidos");
        const vestidos = await response.json();

        catalogTitle.textContent = "Vestidos disponibles";
        renderizarVestidos(vestidos);

    } catch (error) {
        console.error("Error cargando vestidos:", error);
        mostrarError();
    }
}

async function cargarVestidosPorModelo(idModelo) {
    try {
        const response = await fetch(`/api/vestidos/modelo/${idModelo}`);
        const vestidos = await response.json();

        catalogTitle.textContent = modelDescriptions[idModelo].title;
        renderizarVestidos(vestidos);

    } catch (error) {
        console.error("Error cargando vestidos por modelo:", error);
        mostrarError();
    }
}

function renderizarVestidos(vestidos) {
    productsGrid.innerHTML = "";

    productCount.textContent = `${vestidos.length} producto${vestidos.length !== 1 ? "s" : ""}`;

    if (vestidos.length === 0) {
        emptyMessage.style.display = "block";
        return;
    }

    emptyMessage.style.display = "none";

    vestidos.forEach(vestido => {
        const card = document.createElement("article");
        card.classList.add("product-card");

        card.innerHTML = `
            <div class="product-img-container">
                <img src="/images/vestidos/vestido-${vestido.idVestido}.jpg"
                     alt="${vestido.nombre}"
                     onerror="this.src='/images/logo.png'">
            </div>

            <div class="product-info">
                <h3>${vestido.nombre}</h3>

                <div class="product-details">
                    <span class="badge">Talla ${vestido.talla}</span>
                    <span class="badge">${vestido.modelo.nombreModelo}</span>
                </div>

                <p class="price">${formatearPrecio(vestido.precioBase)}</p>

                <a href="/detalle?id=${vestido.idVestido}" class="details-btn">
                    Ver detalles
                </a>
            </div>
        `;

        productsGrid.appendChild(card);
    });
}

function actualizarInfoModelo(modelo) {
    const info = modelDescriptions[modelo];

    modelInfo.innerHTML = `
        <h3>${info.title}</h3>
        <p>${info.text}</p>
    `;
}

function formatearPrecio(valor) {
    return new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0
    }).format(valor);
}

function mostrarError() {
    productsGrid.innerHTML = "";
    productCount.textContent = "0 productos";
    emptyMessage.style.display = "block";
    emptyMessage.innerHTML = `
        <i class="bi bi-exclamation-triangle-fill"></i>
        <h3>No se pudo cargar el catálogo</h3>
        <p>Verifica que el servidor esté funcionando correctamente.</p>
    `;
}
