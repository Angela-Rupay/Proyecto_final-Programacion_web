const productsGrid = document.getElementById("productsGrid");
const emptyMessage = document.getElementById("emptyMessage");
const productCount = document.getElementById("productCount");
const catalogTitle = document.getElementById("catalogTitle");
const modelInfo = document.getElementById("modelInfo");
const filterButtons = document.querySelectorAll(".filter-btn");
const tallaFilter = document.getElementById("tallaFilter");

let modeloSeleccionado = "todos";
let tallaSeleccionada = "todos";

const catalogI18n = {
    es: {
        availableDresses: "Vestidos disponibles",
        sizeDresses: "Vestidos talla {size}",
        sizeLabel: "Talla {size}",
        detailsButton: "Ver detalles",
        productSingular: "producto",
        productPlural: "productos",
        errorTitle: "No se pudo cargar el catálogo",
        errorText: "Verifica que el servidor esté funcionando correctamente.",
        imageAlt: "Imagen del vestido {name}",
        modelDescriptions: {
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
        }
    },

    en: {
        availableDresses: "Available dresses",
        sizeDresses: "Size {size} dresses",
        sizeLabel: "Size {size}",
        detailsButton: "See details",
        productSingular: "product",
        productPlural: "products",
        errorTitle: "The catalog could not be loaded",
        errorText: "Check that the server is running correctly.",
        imageAlt: "Image of dress {name}",
        modelDescriptions: {
            todos: {
                title: "All dresses",
                text: "View all currently available designs."
            },
            TT: {
                title: "Traditional Model",
                text: "Classic designs inspired by the essence of the Huilense Sanjuanero."
            },
            TF: {
                title: "Fantasy Model",
                text: "Eye-catching, colorful, and elegant dresses made to stand out on stage."
            },
            P: {
                title: "Painted Model",
                text: "Pieces with artistic details and finishes full of cultural expression."
            },
            TPRO: {
                title: "Professional Model",
                text: "Dresses designed for contests, performances, and special events."
            }
        }
    },

    pt: {
        availableDresses: "Vestidos disponíveis",
        sizeDresses: "Vestidos tamanho {size}",
        sizeLabel: "Tamanho {size}",
        detailsButton: "Ver detalhes",
        productSingular: "produto",
        productPlural: "produtos",
        errorTitle: "Não foi possível carregar o catálogo",
        errorText: "Verifique se o servidor está funcionando corretamente.",
        imageAlt: "Imagem do vestido {name}",
        modelDescriptions: {
            todos: {
                title: "Todos os vestidos",
                text: "Veja todos os designs disponíveis atualmente."
            },
            TT: {
                title: "Modelo Tradicional",
                text: "Designs clássicos inspirados na essência do Sanjuanero Huilense."
            },
            TF: {
                title: "Modelo Fantasia",
                text: "Vestidos chamativos, coloridos e elegantes para se destacar no palco."
            },
            P: {
                title: "Modelo Pintado",
                text: "Peças com detalhes artísticos e acabamentos cheios de expressão cultural."
            },
            TPRO: {
                title: "Modelo Profissional",
                text: "Trajes elaborados para concursos, apresentações e eventos especiais."
            }
        }
    }
};

document.addEventListener("DOMContentLoaded", () => {
    cargarVestidos();
    configurarFiltros();
});

function idiomaActualCatalogo() {
    if (typeof obtenerIdiomaActual === "function") {
        return obtenerIdiomaActual();
    }

    return localStorage.getItem("lang") || "es";
}

function tCatalogo(clave) {
    const idioma = idiomaActualCatalogo();
    return catalogI18n[idioma]?.[clave] || catalogI18n.es[clave] || clave;
}

function reemplazarVariables(texto, variables) {
    let resultado = texto;

    Object.keys(variables).forEach(clave => {
        resultado = resultado.replace(`{${clave}}`, variables[clave]);
    });

    return resultado;
}

function obtenerDescripcionModelo(modelo) {
    const idioma = idiomaActualCatalogo();
    return catalogI18n[idioma]?.modelDescriptions?.[modelo]
        || catalogI18n.es.modelDescriptions[modelo];
}

function construirUrlDetalle(idVestido) {
    const lang = idiomaActualCatalogo();
    return `/detalle?id=${idVestido}&lang=${lang}`;
}

function configurarFiltros() {
    filterButtons.forEach(button => {
        button.addEventListener("click", () => {
            filterButtons.forEach(btn => btn.classList.remove("active"));
            button.classList.add("active");

            modeloSeleccionado = button.dataset.modelo;

            actualizarInfoModelo(modeloSeleccionado);
            cargarVestidosFiltrados();
        });
    });

    tallaFilter.addEventListener("change", () => {
        tallaSeleccionada = tallaFilter.value;
        cargarVestidosFiltrados();
    });
}

async function cargarVestidosFiltrados() {
    try {
        let url = "/api/vestidos";

        if (modeloSeleccionado !== "todos" && tallaSeleccionada !== "todos") {
            url = `/api/vestidos/modelo/${modeloSeleccionado}/talla/${tallaSeleccionada}`;

            const infoModelo = obtenerDescripcionModelo(modeloSeleccionado);
            const textoTalla = reemplazarVariables(tCatalogo("sizeLabel"), {
                size: tallaSeleccionada
            });

            catalogTitle.textContent = `${infoModelo.title} - ${textoTalla}`;

        } else if (modeloSeleccionado !== "todos") {
            url = `/api/vestidos/modelo/${modeloSeleccionado}`;

            const infoModelo = obtenerDescripcionModelo(modeloSeleccionado);
            catalogTitle.textContent = infoModelo.title;

        } else if (tallaSeleccionada !== "todos") {
            url = `/api/vestidos/talla/${tallaSeleccionada}`;

            catalogTitle.textContent = reemplazarVariables(tCatalogo("sizeDresses"), {
                size: tallaSeleccionada
            });

        } else {
            url = "/api/vestidos";
            catalogTitle.textContent = tCatalogo("availableDresses");
        }

        const response = await fetch(url);

        if (manejarRespuestaNoAutorizada && manejarRespuestaNoAutorizada(response)) {
            return;
        }

        const vestidos = await response.json();

        renderizarVestidos(vestidos);

    } catch (error) {
        console.error("Error cargando vestidos filtrados:", error);
        mostrarError();
    }
}

async function cargarVestidos() {
    try {
        const response = await fetch("/api/vestidos");

        if (manejarRespuestaNoAutorizada && manejarRespuestaNoAutorizada(response)) {
            return;
        }

        const vestidos = await response.json();

        catalogTitle.textContent = tCatalogo("availableDresses");
        actualizarInfoModelo("todos");
        renderizarVestidos(vestidos);

    } catch (error) {
        console.error("Error cargando vestidos:", error);
        mostrarError();
    }
}

function renderizarVestidos(vestidos) {
    productsGrid.innerHTML = "";

    const cantidad = vestidos.length;
    const palabraProducto = cantidad === 1
        ? tCatalogo("productSingular")
        : tCatalogo("productPlural");

    productCount.textContent = `${cantidad} ${palabraProducto}`;

    if (vestidos.length === 0) {
        emptyMessage.style.display = "block";
        return;
    }

    emptyMessage.style.display = "none";

    vestidos.forEach(vestido => {
        const card = document.createElement("article");
        card.classList.add("product-card");

        const altImagen = reemplazarVariables(tCatalogo("imageAlt"), {
            name: vestido.nombre
        });

        const textoTalla = reemplazarVariables(tCatalogo("sizeLabel"), {
            size: vestido.talla
        });

        card.innerHTML = `
            <div class="product-img-container">
               <img src="/images/vestidos/${vestido.idVestido}-1.jpg"
                 alt="${altImagen}"
                 onerror="this.src='/images/logo.png'">
            </div>

            <div class="product-info">
                <h3>${vestido.nombre}</h3>

                <div class="product-details">
                    <span class="badge">${textoTalla}</span>
                    <span class="badge">${vestido.nombreModelo}</span>
                </div>

                <p class="price">${formatearPrecio(vestido.precioBase)}</p>

                <a href="${construirUrlDetalle(vestido.idVestido)}" class="details-btn">
                    ${tCatalogo("detailsButton")}
                </a>
            </div>
        `;

        productsGrid.appendChild(card);
    });
}

function actualizarInfoModelo(modelo) {
    const info = obtenerDescripcionModelo(modelo);

    modelInfo.innerHTML = `
        <h3>${info.title}</h3>
        <p>${info.text}</p>
    `;
}

function formatearPrecio(valor) {
    const idioma = idiomaActualCatalogo();

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

function mostrarError() {
    productsGrid.innerHTML = "";
    productCount.textContent = `0 ${tCatalogo("productPlural")}`;
    emptyMessage.style.display = "block";
    emptyMessage.innerHTML = `
        <i class="bi bi-exclamation-triangle-fill"></i>
        <h3>${tCatalogo("errorTitle")}</h3>
        <p>${tCatalogo("errorText")}</p>
    `;
}