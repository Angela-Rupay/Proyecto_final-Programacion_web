const productsGrid = document.getElementById("productsGrid");

document.addEventListener("DOMContentLoaded", () => {
    protegerPaginaAdmin();
    cargarProductos();
});

async function cargarProductos() {
    try {
        const response = await fetch("/api/admin/vestidos", {
            headers: obtenerHeadersAuth()
        });

        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

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
        const vendido = producto.vendido;

        const textoEstado = vendido
            ? "Vendido"
            : activo
                ? "Disponible"
                : "No disponible";

        const claseEstado = vendido
            ? "sold"
            : activo
                ? "active"
                : "inactive";

        const botonEliminar = vendido
            ? `
                <button class="delete-btn disabled" disabled>
                    <i class="bi bi-lock-fill"></i>
                    No eliminable
                </button>
            `
            : `
                <button class="delete-btn"
                        onclick="eliminarProducto(${producto.idVestido})">
                    <i class="bi bi-trash-fill"></i>
                    Eliminar
                </button>
            `;
        const botonEditar = vendido
            ? `
        <button class="edit-btn disabled" disabled>
            <i class="bi bi-lock-fill"></i>
            No editable
        </button>
    `
            : `
        <button class="edit-btn"
                onclick="editarProducto(${producto.idVestido})">
            <i class="bi bi-pencil-fill"></i>
            Editar
        </button>
    `;

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
                        ${producto.nombreModelo}
                    </span>

                    <span class="badge ${claseEstado}">
                        ${textoEstado}
                    </span>
                </div>

                <p class="price">
                    ${formatearPrecio(producto.precioBase)}
                </p>
                
                <div class="buttons">
                  ${botonEditar}
                  ${botonEliminar}
                  </div>

               

                </div>
            </div>
        `;

        productsGrid.appendChild(card);
    });
}

function editarProducto(idProducto) {
    window.location.href = `/crear-producto?id=${idProducto}`;
}

async function eliminarProducto(idProducto) {
    const confirmar = confirm("¿Seguro que deseas eliminar este vestido? Esta acción solo aplica a productos no vendidos.");

    if (!confirmar) {
        return;
    }

    try {
        const response = await fetch(`/api/admin/vestidos/${idProducto}`, {
            method: "DELETE",
            headers: obtenerHeadersAuth()
        });

        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

        const data = await response.json();

        if (data.success) {
            cargarProductos();
        } else {
            alert(data.message);
        }

    } catch (error) {
        console.error(error);
        alert("Error eliminando el producto");
    }
}

function formatearPrecio(valor) {
    return new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0
    }).format(valor);
}