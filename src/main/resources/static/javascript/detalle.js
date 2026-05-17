const vestidoNombre = document.getElementById("vestidoNombre");
const modeloBadge = document.getElementById("modeloBadge");
const tallaBadge = document.getElementById("tallaBadge");
const vestidoPrecio = document.getElementById("vestidoPrecio");

const mainImage = document.getElementById("mainImage");
const thumbnails = document.querySelectorAll(".thumbnail");

const addToCartBtn = document.getElementById("addToCartBtn");

const params = new URLSearchParams(window.location.search);
const idVestido = params.get("id");

document.addEventListener("DOMContentLoaded", () => {
    if (!idVestido) {
        window.location.href = "/catalogo";
        return;
    }

    cargarDetalleVestido();
    configurarGaleria();
    configurarBotonCarrito();
});

async function cargarDetalleVestido() {
    try {
        const response = await fetch(`/api/vestidos/${idVestido}`);

        if (!response.ok) {
            throw new Error("No se encontró el vestido");
        }

        const vestido = await response.json();

        vestidoNombre.textContent = vestido.nombre;
        modeloBadge.textContent = vestido.modelo;
        tallaBadge.textContent = `Talla ${vestido.talla}`;
        vestidoPrecio.textContent = formatearPrecio(vestido.precioBase);

        cargarColores(vestido.colores);
        cargarImagenes(idVestido);

    } catch (error) {
        console.error(error);

        vestidoNombre.textContent = "Vestido no encontrado";
        modeloBadge.textContent = "Sin modelo";
        tallaBadge.textContent = "Sin talla";
        vestidoPrecio.textContent = "$0";

        addToCartBtn.disabled = true;
        addToCartBtn.innerHTML = `
            <i class="bi bi-exclamation-triangle-fill"></i>
            No disponible
        `;
    }
}

function cargarImagenes(id) {
    const imagenes = [
        `/images/vestidos/${id}-1.jpg`,
        `/images/vestidos/${id}-2.jpg`,
        `/images/vestidos/${id}-3.jpg`
    ];

    mainImage.src = imagenes[0];
    mainImage.onerror = () => {
        mainImage.src = "/images/logo.png";
    };

    thumbnails.forEach((thumbnail, index) => {
        thumbnail.src = imagenes[index];

        thumbnail.onerror = () => {
            thumbnail.src = "/images/logo.png";
        };
    });
}

function configurarGaleria() {
    thumbnails.forEach(thumbnail => {
        thumbnail.addEventListener("click", () => {
            mainImage.src = thumbnail.src;

            thumbnails.forEach(t => t.classList.remove("active"));
            thumbnail.classList.add("active");
        });
    });
}

function configurarBotonCarrito() {

    const usuario = obtenerUsuarioLogueado();

    if (usuario && usuario.rol === "ADMIN") {
        addToCartBtn.disabled = true;

        addToCartBtn.innerHTML = `
            <i class="bi bi-shield-lock-fill"></i>
            Solo clientes pueden comprar
        `;

        addToCartBtn.classList.add("disabled");
        return;
    }

    addToCartBtn.addEventListener("click", async () => {

        const usuario = obtenerUsuarioLogueado();

        if (!usuario) {
            window.location.href = "/login?mensaje=login";
            return;
        }

        if (usuario.rol !== "CLIENTE") {
            window.location.href = "/sin-permisos";
            return;
        }

        try {
            const response = await fetch("/api/carrito/agregar", {
                method: "POST",
                headers: obtenerHeadersJsonAuth(),
                body: JSON.stringify({
                    documento: usuario.documento,
                    idVestido: Number(idVestido)
                })
            });

            if (manejarRespuestaNoAutorizada(response)) {
                return;
            }

            const data = await response.json();

            if (data.success) {
                addToCartBtn.innerHTML = `
                    <i class="bi bi-check-circle-fill"></i>
                    Agregado al carrito
                `;

                setTimeout(() => {
                    window.location.href = "/carrito";
                }, 900);

            } else {
                alert(data.message);
            }

        } catch (error) {
            console.error(error);
            alert("Error agregando al carrito");
        }
    });
}
function formatearPrecio(valor) {
    return new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0
    }).format(valor);
}