const cartBody = document.getElementById("cartBody");
const totalPrice = document.getElementById("totalPrice");
const tableSection = document.getElementById("tableSection");
const emptyCart = document.getElementById("emptyCart");
const buyBtn = document.getElementById("buyBtn");

document.addEventListener("DOMContentLoaded", () => {
    protegerPaginaCliente();
    cargarCarrito();
    configurarCompra();
});

async function cargarCarrito() {
    const usuario = obtenerUsuarioLogueado();

    try {
        const response = await fetch(`/api/carrito/${usuario.documento}`);
        const items = await response.json();

        console.log("Items carrito:", items);

        renderizarCarrito(items);

    } catch (error) {
        console.error(error);
        mostrarCarritoVacio();
    }
}

function renderizarCarrito(items) {
    cartBody.innerHTML = "";

    if (!items || items.length === 0) {
        mostrarCarritoVacio();
        return;
    }

    tableSection.style.display = "grid";
    emptyCart.style.display = "none";

    let total = 0;

    items.forEach(item => {
        total += Number(item.vestido.precioBase);

        const fila = document.createElement("tr");

        fila.innerHTML = `
            <td>
                <div class="product-info">
                    <img src="/images/vestidos/${item.vestido.idVestido}-1.jpg"
                         alt="${item.vestido.nombre}"
                         onerror="this.src='/images/logo.png'">

                    <div>
                        <p class="product-name">
                            ${item.vestido.nombre}
                        </p>
                    </div>
                </div>
            </td>

            <td>${item.vestido.modelo ? item.vestido.modelo.nombreModelo : "Sin modelo"}</td>

            <td>${item.vestido.talla}</td>

            <td class="price">
                ${formatearPrecio(item.vestido.precioBase)}
            </td>

            <td>
                <button class="remove-btn"
                        title="Eliminar vestido"
                        onclick="eliminarItem(${item.idCarritoItem})">
                    <i class="bi bi-x-lg"></i>
                </button>
            </td>
        `;

        cartBody.appendChild(fila);
    });

    totalPrice.textContent = formatearPrecio(total);
}

async function eliminarItem(idItem) {
    try {
        const response = await fetch(`/api/carrito/${idItem}`, {
            method: "DELETE"
        });

        const data = await response.json();

        if (data.success) {
            cargarCarrito();
        } else {
            alert(data.message);
        }

    } catch (error) {
        console.error(error);
        alert("Error eliminando el producto del carrito");
    }
}

function configurarCompra() {
    buyBtn.addEventListener("click", () => {
        window.location.href = "/pago";
    });
}

function mostrarCarritoVacio() {
    tableSection.style.display = "none";
    emptyCart.style.display = "block";
    totalPrice.textContent = formatearPrecio(0);
}

function formatearPrecio(valor) {
    return new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0
    }).format(valor);
}