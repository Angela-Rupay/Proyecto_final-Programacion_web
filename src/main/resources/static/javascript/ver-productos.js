const productsGrid = document.getElementById("productsGrid");

document.addEventListener("DOMContentLoaded", () => {
    protegerPaginaAdmin();
    cargarProductos();
});

async function cargarProductos() {
    try {
        const response = await fetch("/api/admin/vestidos");
        const productos = await response.json();

        renderizarProductos(productos);

    } catch (error) {
        console.error(error);
        alert("Error cargando productos");
    }
}

function renderizarProductos(productos) {
    productsGrid.innerHTML = "";

    if (productos.length === 0) {
        productsGrid.innerHTML = `
            <div class="empty-products">
                <i class="bi bi-bag-x-fill"></i>
                <h2>No hay productos registrados</h2>
                <p>Crea un nuevo vestido para comenzar a llenar el catálogo.</p>
            </div>
        `;
        return;
    }

    productos.forEach(producto => {
        const activo = producto.activo;

        const card = document.createElement("article");
        card.classList.add("product-card");

        card.innerHTML = `
            <div class="product-image">
                <img src="/images/vestidos/${producto.idVestido}-1.jpg"
                     alt="${producto.nombre}"
                     onerror="this.src='/images/logo.png'">
            </div>

            <div class="product-content">
                <h2>${producto.nombre}</h2>

                <div class="badges">
                    <span class="badge model">
                        ${producto.modelo.nombreModelo}
                    </span>

                    <span class="badge ${activo ? "active" : "inactive"}">
                        ${activo ? "Disponible" : "No disponible"}
                    </span>
                </div>

                <p class="price">
                    ${formatearPrecio(producto.precioBase)}
                </p>

                <div class="buttons">
                    <button class="edit-btn"
                            onclick="editarProducto(${producto.idVestido})">
                        <i class="bi bi-pencil-fill"></i>
                        Editar
                    </button>

                    <button class="toggle-btn ${activo ? "disable" : "enable"}"
                            onclick="cambiarEstado(${producto.idVestido}, ${activo})">
                        <i class="bi ${activo ? "bi-x-circle-fill" : "bi-check-circle-fill"}"></i>
                        ${activo ? "Desactivar" : "Activar"}
                    </button>
                </div>
            </div>
        `;

        productsGrid.appendChild(card);
    });
}

function editarProducto(idProducto) {
    window.location.href = `/crear-producto?id=${idProducto}`;
}

async function cambiarEstado(idProducto, activoActual) {
    const endpoint = activoActual
        ? `/api/admin/vestidos/${idProducto}/desactivar`
        : `/api/admin/vestidos/${idProducto}/activar`;

    try {
        const response = await fetch(endpoint, {
            method: "PATCH"
        });

        const data = await response.json();

        if (data.success) {
            cargarProductos();
        } else {
            alert(data.message);
        }

    } catch (error) {
        console.error(error);
        alert("Error cambiando el estado del producto");
    }
}

function formatearPrecio(valor) {
    return new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0
    }).format(valor);
}