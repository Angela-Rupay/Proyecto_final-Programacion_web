const paymentForm = document.getElementById("paymentForm");
const titularInput = document.getElementById("titular");
const tarjetaInput = document.getElementById("tarjeta");
const fechaInput = document.getElementById("fecha");
const cvvInput = document.getElementById("cvv");
const direccionInput = document.getElementById("direccion");
const messageBox = document.getElementById("messageBox");
const successModal = document.getElementById("successModal");
const summaryTotal = document.getElementById("summaryTotal");
const payBtn = document.getElementById("payBtn");

document.addEventListener("DOMContentLoaded", () => {
    protegerPaginaCliente();
    configurarFormatoTarjeta();
    configurarFormatoFecha();
    cargarTotalCarrito();
});

async function cargarTotalCarrito() {
    const usuario = obtenerUsuarioLogueado();

    try {
        const response = await fetch(`/api/carrito/${usuario.documento}`);
        const items = await response.json();

        if (!items || items.length === 0) {
            window.location.href = "/carrito";
            return;
        }

        const total = items.reduce((sum, item) => {
            return sum + Number(item.precioBase);
        }, 0);

        summaryTotal.textContent = formatearPrecio(total);

    } catch (error) {
        console.error(error);
        window.location.href = "/carrito";
    }
}

function configurarFormatoTarjeta() {
    tarjetaInput.addEventListener("input", () => {
        let valor = tarjetaInput.value.replace(/\D/g, "");
        valor = valor.substring(0, 16);
        tarjetaInput.value = valor.replace(/(.{4})/g, "$1 ").trim();
    });
}

function configurarFormatoFecha() {
    fechaInput.addEventListener("input", () => {
        let valor = fechaInput.value.replace(/\D/g, "");
        valor = valor.substring(0, 4);

        if (valor.length >= 3) {
            valor = valor.substring(0, 2) + "/" + valor.substring(2);
        }

        fechaInput.value = valor;
    });
}

paymentForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    ocultarMensaje();

    if (!validarFormulario()) return;

    const usuario = obtenerUsuarioLogueado();
    const datosPago = {
        documento: usuario.documento,
        titular: titularInput.value.trim(),
        numeroTarjeta: tarjetaInput.value.replace(/\s/g, ""),
        fechaVencimiento: fechaInput.value.trim(),
        cvv: cvvInput.value.trim(),
        direccion: direccionInput.value.trim()
    };

    try {
        payBtn.disabled = true;
        payBtn.innerHTML = `
            <i class="bi bi-hourglass-split"></i>
            Procesando...
        `;

        const response = await fetch("/api/pago/procesar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datosPago)
        });

        const data = await response.json();

        if (data.success) {
            successModal.classList.add("active");
        } else {
            mostrarMensaje(data.message || "No se pudo completar la compra");
            restaurarBoton();
        }

    } catch (error) {
        console.error(error);
        mostrarMensaje("Error completando la transacción");
        restaurarBoton();
    }
});

function validarFormulario() {
    const titular = titularInput.value.trim();
    const tarjeta = tarjetaInput.value.replace(/\s/g, "");
    const fecha = fechaInput.value.trim();
    const cvv = cvvInput.value.trim();
    const direccion = direccionInput.value.trim();

    if (!titular) {
        mostrarMensaje("Ingresa el nombre del titular");
        return false;
    }

    if (!/^\d{16}$/.test(tarjeta)) {
        mostrarMensaje("El número de tarjeta debe tener 16 dígitos");
        return false;
    }

    if (!validarFecha(fecha)) {
        mostrarMensaje("La fecha debe estar en formato MM/AA y ser válida");
        return false;
    }

    if (!/^\d{3}$/.test(cvv)) {
        mostrarMensaje("El CVV debe tener 3 dígitos");
        return false;
    }

    if (!direccion) {
        mostrarMensaje("Ingresa la dirección de entrega");
        return false;
    }

    return true;
}

function validarFecha(fecha) {
    if (!/^\d{2}\/\d{2}$/.test(fecha)) return false;

    const [mesTexto, anioTexto] = fecha.split("/");
    const mes = Number(mesTexto);
    const anio = Number("20" + anioTexto);

    if (mes < 1 || mes > 12) return false;

    const ahora = new Date();
    const anioActual = ahora.getFullYear();
    const mesActual = ahora.getMonth() + 1;

    if (anio < anioActual) return false;
    if (anio === anioActual && mes < mesActual) return false;

    return true;
}

function mostrarMensaje(texto) {
    messageBox.textContent = texto;
    messageBox.className = "message-box error";
}

function ocultarMensaje() {
    messageBox.className = "message-box";
    messageBox.textContent = "";
}

function restaurarBoton() {
    payBtn.disabled = false;
    payBtn.innerHTML = `
        <i class="bi bi-check-circle-fill"></i>
        Completar transacción
    `;
}

function formatearPrecio(valor) {
    return new Intl.NumberFormat("es-CO", {
        style:"currency",
        currency:"COP",
        minimumFractionDigits:0
    }).format(valor);
}

