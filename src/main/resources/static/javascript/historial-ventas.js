const salesBody = document.getElementById("salesBody");
const tableSection = document.getElementById("tableSection");
const emptySales = document.getElementById("emptySales");

const modalOverlay = document.getElementById("modalOverlay");
const closeModal = document.getElementById("closeModal");
const detailsContainer = document.getElementById("detailsContainer");
const modalTitle = document.getElementById("modalTitle");

document.addEventListener("DOMContentLoaded", () => {
    protegerPaginaAdmin();
    cargarVentas();
    configurarModal();
});

async function cargarVentas() {
    try {
        const response = await fetch("/api/ventas", {
            headers: obtenerHeadersAuth()
        });

        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

        if (!response.ok) {
            throw new Error("Error consultando ventas");
        }

        const ventas = await response.json();

        renderizarVentas(ventas);

    } catch (error) {
        console.error(error);
        mostrarVacio();
    }
}

function renderizarVentas(ventas) {
    salesBody.innerHTML = "";

    if (!ventas || ventas.length === 0) {
        mostrarVacio();
        return;
    }

    tableSection.style.display = "block";
    emptySales.style.display = "none";

    ventas.forEach(venta => {
        const fila = document.createElement("tr");

        fila.innerHTML = `
            <td><strong>#${venta.idVenta}</strong></td>

            <td>${venta.nombreCliente}</td>

            <td>${venta.documentoCliente}</td>

            <td>
                <i class="bi bi-calendar-event"></i>
                ${formatearFecha(venta.fechaCompra)}
            </td>

            <td class="total">
                ${formatearPrecio(venta.total)}
            </td>

            <td>
                <button class="details-btn"
                        onclick="verDetalles(${venta.idVenta})">
                    <i class="bi bi-eye-fill"></i>
                    Ver detalles
                </button>
            </td>
        `;

        salesBody.appendChild(fila);
    });
}

async function verDetalles(idVenta) {
    try {
        const response = await fetch(`/api/ventas/${idVenta}`, {
            headers: obtenerHeadersAuth()
        });

        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

        if (!response.ok) {
            throw new Error("Error consultando detalle");
        }

        const detalles = await response.json();

        detailsContainer.innerHTML = "";
        modalTitle.textContent = `Venta #${idVenta}`;

        if (!detalles || detalles.length === 0) {
            detailsContainer.innerHTML = `
                <div class="detail-card">
                    <div>
                        <h3>Sin detalles registrados</h3>
                        <p>No se encontraron productos asociados a esta venta.</p>
                    </div>
                </div>
            `;

            modalOverlay.classList.add("active");
            return;
        }

        detalles.forEach(detalle => {
            const card = document.createElement("div");
            card.classList.add("detail-card");

            card.innerHTML = `
                <div>
                    <h3>
                        <i class="bi bi-bag-heart-fill"></i>
                        ${detalle.vestido}
                    </h3>

                    <p>
                        <strong>Talla:</strong>
                        ${detalle.talla}
                    </p>
                </div>

                <p class="detail-price">
                    ${formatearPrecio(detalle.subtotal)}
                </p>
            `;

            detailsContainer.appendChild(card);
        });

        modalOverlay.classList.add("active");

    } catch (error) {
        console.error(error);
        alert("No se pudo cargar el detalle de la venta");
    }
}

function configurarModal() {
    closeModal.addEventListener("click", cerrarModal);

    modalOverlay.addEventListener("click", (e) => {
        if (e.target === modalOverlay) {
            cerrarModal();
        }
    });
}

function cerrarModal() {
    modalOverlay.classList.remove("active");
}

function mostrarVacio() {
    tableSection.style.display = "none";
    emptySales.style.display = "block";
}

function formatearPrecio(valor) {
    return new Intl.NumberFormat("es-CO", {
        style:"currency",
        currency:"COP",
        minimumFractionDigits:0
    }).format(valor);
}

function formatearFecha(fecha) {
    return new Date(fecha).toLocaleString("es-CO", {
        dateStyle:"medium",
        timeStyle:"short"
    });
}