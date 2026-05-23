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
const barrioInput = document.getElementById("barrio");
const editAddressBtn = document.getElementById("editAddressBtn");
const editNeighborhoodBtn = document.getElementById("editNeighborhoodBtn");

const paymentI18n = {
    es: {
        processing: "Procesando...",
        completeTransaction: "Completar transacción",
        purchaseIncomplete: "No se pudo completar la compra",
        transactionError: "Error completando la transacción",
        cardholderRequired: "Ingresa el nombre del titular",
        cardNumberInvalid: "El número de tarjeta debe tener 16 dígitos",
        expirationInvalid: "La fecha debe estar en formato MM/AA y ser válida",
        cvvInvalid: "El CVV debe tener 3 dígitos",
        addressRequired: "Ingresa la dirección de entrega",
        neighborhoodRequired: "Ingresa el barrio de entrega"
    },
    en: {
        processing: "Processing...",
        completeTransaction: "Complete transaction",
        purchaseIncomplete: "The purchase could not be completed",
        transactionError: "Error completing the transaction",
        cardholderRequired: "Enter the cardholder name",
        cardNumberInvalid: "The card number must have 16 digits",
        expirationInvalid: "The date must be in MM/YY format and be valid",
        cvvInvalid: "The CVV must have 3 digits",
        addressRequired: "Enter the delivery address",
        neighborhoodRequired: "Enter the delivery neighborhood"
    },
    pt: {
        processing: "Processando...",
        completeTransaction: "Finalizar transação",
        purchaseIncomplete: "Não foi possível concluir a compra",
        transactionError: "Erro ao concluir a transação",
        cardholderRequired: "Digite o nome do titular",
        cardNumberInvalid: "O número do cartão deve ter 16 dígitos",
        expirationInvalid: "A data deve estar no formato MM/AA e ser válida",
        cvvInvalid: "O CVV deve ter 3 dígitos",
        addressRequired: "Digite o endereço de entrega",
        neighborhoodRequired: "Digite o bairro de entrega"
    }
};

document.addEventListener("DOMContentLoaded", () => {
    protegerPaginaCliente();
    configurarFormatoTarjeta();
    configurarFormatoFecha();
    cargarTotalCarrito();
    autocompletarDatosEnvio();
    configurarEdicionEnvio();
});

function idiomaActualPago() {
    if (typeof obtenerIdiomaActual === "function") {
        return obtenerIdiomaActual();
    }

    return localStorage.getItem("lang") || "es";
}

function tPago(clave) {
    const idioma = idiomaActualPago();
    return paymentI18n[idioma]?.[clave] || paymentI18n.es[clave] || clave;
}

async function cargarTotalCarrito() {
    const usuario = obtenerUsuarioLogueado();

    try {
        const response = await fetch(`/api/carrito/${usuario.documento}`, {
            headers: obtenerHeadersAuth()
        });

        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

        const items = await response.json();

        if (!items || items.length === 0) {
            window.location.href = `/carrito?lang=${idiomaActualPago()}`;
            return;
        }

        const total = items.reduce((sum, item) => {
            return sum + Number(item.precioBase);
        }, 0);

        summaryTotal.textContent = formatearPrecio(total);

    } catch (error) {
        console.error(error);
        window.location.href = `/carrito?lang=${idiomaActualPago()}`;
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
        direccion: direccionInput.value.trim(),
        barrio: barrioInput.value.trim()
    };

    try {
        payBtn.disabled = true;
        payBtn.innerHTML = `
            <i class="bi bi-hourglass-split"></i>
            ${tPago("processing")}
        `;

        const response = await fetch("/api/pago/procesar", {
            method: "POST",
            headers: obtenerHeadersJsonAuth(),
            body: JSON.stringify(datosPago)
        });

        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

        const data = await response.json();

        if (data.success) {
            actualizarUsuarioLocal(
                direccionInput.value.trim(),
                barrioInput.value.trim()
            );

            successModal.classList.add("active");
        } else {
            mostrarMensaje(data.message || tPago("purchaseIncomplete"));
            restaurarBoton();
        }

    } catch (error) {
        console.error(error);
        mostrarMensaje(tPago("transactionError"));
        restaurarBoton();
    }
});

function validarFormulario() {
    const titular = titularInput.value.trim();
    const tarjeta = tarjetaInput.value.replace(/\s/g, "");
    const fecha = fechaInput.value.trim();
    const cvv = cvvInput.value.trim();
    const direccion = direccionInput.value.trim();
    const barrio = barrioInput.value.trim();

    if (!titular) {
        mostrarMensaje(tPago("cardholderRequired"));
        return false;
    }

    if (!/^\d{16}$/.test(tarjeta)) {
        mostrarMensaje(tPago("cardNumberInvalid"));
        return false;
    }

    if (!validarFecha(fecha)) {
        mostrarMensaje(tPago("expirationInvalid"));
        return false;
    }

    if (!/^\d{3}$/.test(cvv)) {
        mostrarMensaje(tPago("cvvInvalid"));
        return false;
    }

    if (!direccion) {
        mostrarMensaje(tPago("addressRequired"));
        return false;
    }

    if (!barrio) {
        mostrarMensaje(tPago("neighborhoodRequired"));
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
        ${tPago("completeTransaction")}
    `;
}

function formatearPrecio(valor) {
    const idioma = idiomaActualPago();

    const localePorIdioma = {
        es: "es-CO",
        en: "en-US",
        pt: "pt-BR"
    };

    return new Intl.NumberFormat(localePorIdioma[idioma] || "es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0
    }).format(valor);
}

function autocompletarDatosEnvio() {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario) return;

    if (usuario.direccion) {
        direccionInput.value = usuario.direccion;
        direccionInput.readOnly = true;
        direccionInput.classList.add("readonly-field");
    }

    if (usuario.barrio) {
        barrioInput.value = usuario.barrio;
        barrioInput.readOnly = true;
        barrioInput.classList.add("readonly-field");
    }
}

function configurarEdicionEnvio() {
    if (editAddressBtn) {
        editAddressBtn.addEventListener("click", () => {
            direccionInput.readOnly = false;
            direccionInput.classList.remove("readonly-field");
            direccionInput.focus();
        });
    }

    if (editNeighborhoodBtn) {
        editNeighborhoodBtn.addEventListener("click", () => {
            barrioInput.readOnly = false;
            barrioInput.classList.remove("readonly-field");
            barrioInput.focus();
        });
    }
}

function actualizarUsuarioLocal(direccion, barrio) {
    const usuario = obtenerUsuarioLogueado();

    if (!usuario) return;

    usuario.direccion = direccion;
    usuario.barrio = barrio;

    localStorage.setItem("usuario", JSON.stringify(usuario));
}