const productForm = document.getElementById("productForm");

const pageTitle = document.getElementById("pageTitle");
const saveBtn = document.getElementById("saveBtn");

const messageBox = document.getElementById("messageBox");

const nombreInput = document.getElementById("nombre");
const tallaInput = document.getElementById("talla");
const precioBaseInput = document.getElementById("precioBase");
const idModeloInput = document.getElementById("idModelo");

const imagen1Input = document.getElementById("imagen1");
const imagen2Input = document.getElementById("imagen2");
const imagen3Input = document.getElementById("imagen3");

const preview1 = document.getElementById("preview1");
const preview2 = document.getElementById("preview2");
const preview3 = document.getElementById("preview3");

const params = new URLSearchParams(window.location.search);

const idProducto = params.get("id");

document.addEventListener("DOMContentLoaded", () => {

    protegerPaginaAdmin();

    configurarPreviews();

    if(idProducto){
        prepararModoEdicion(idProducto);
    }

});

/* =========================
   MODO EDICIÓN
========================= */

async function prepararModoEdicion(id){

    pageTitle.textContent =
        "Editar producto";

    saveBtn.innerHTML = `
        <i class="bi bi-pencil-fill"></i>
        Actualizar producto
    `;

    imagen1Input.required = false;
    imagen2Input.required = false;
    imagen3Input.required = false;

    preview1.src =
        `/images/vestidos/${id}-1.jpg`;

    preview2.src =
        `/images/vestidos/${id}-2.jpg`;

    preview3.src =
        `/images/vestidos/${id}-3.jpg`;

    try{

        const response =
            await fetch(`/api/vestidos/${id}`);

        const producto =
            await response.json();

        nombreInput.value =
            producto.nombre;

        tallaInput.value =
            producto.talla;

        precioBaseInput.value =
            producto.precioBase;

        idModeloInput.value =
            convertirModeloAId(
                producto.modelo
            );

    }catch(error){

        console.error(error);

        mostrarMensaje(
            "No se pudo cargar el producto",
            "error"
        );

    }

}

/* =========================
   PREVIEWS
========================= */

function configurarPreviews(){

    configurarPreview(
        imagen1Input,
        preview1
    );

    configurarPreview(
        imagen2Input,
        preview2
    );

    configurarPreview(
        imagen3Input,
        preview3
    );

}

function configurarPreview(input, preview){

    input.addEventListener("change", () => {

        const archivo =
            input.files[0];

        if(!archivo) return;

        const reader =
            new FileReader();

        reader.onload = (e) => {

            preview.src =
                e.target.result;

        };

        reader.readAsDataURL(archivo);

    });

}

/* =========================
   SUBMIT
========================= */

productForm.addEventListener(
    "submit",
    async (e) => {

        e.preventDefault();

        ocultarMensaje();

        if(!validarFormulario()) return;

        const formData =
            new FormData();

        formData.append(
            "nombre",
            nombreInput.value.trim()
        );

        formData.append(
            "talla",
            tallaInput.value.trim()
        );

        formData.append(
            "precioBase",
            precioBaseInput.value
        );

        formData.append(
            "idModelo",
            idModeloInput.value
        );

        if(imagen1Input.files[0]){
            formData.append(
                "imagen1",
                imagen1Input.files[0]
            );
        }

        if(imagen2Input.files[0]){
            formData.append(
                "imagen2",
                imagen2Input.files[0]
            );
        }

        if(imagen3Input.files[0]){
            formData.append(
                "imagen3",
                imagen3Input.files[0]
            );
        }

        if(idProducto){

            actualizarProducto(formData);

        }else{

            crearProducto(formData);

        }

    }
);

/* =========================
   CREAR
========================= */

async function crearProducto(formData){

    try{

        const response =
            await fetch(
                "/api/admin/vestidos/con-imagenes",
                {
                    method:"POST",
                    headers: obtenerHeadersAuth(),
                    body:formData
                }
            );
        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

        const data =
            await response.json();

        if(data.success){

            mostrarMensaje(
                data.message,
                "success"
            );

            productForm.reset();

            resetearPreviews();

        }else{

            mostrarMensaje(
                data.message,
                "error"
            );

        }

    }catch(error){

        console.error(error);

        mostrarMensaje(
            "Error creando el producto",
            "error"
        );

    }

}

/* =========================
   ACTUALIZAR
========================= */

async function actualizarProducto(formData){

    try{

        const response =
            await fetch(
                `/api/admin/vestidos/${idProducto}/con-imagenes`,
                {
                    method:"PUT",
                    headers: obtenerHeadersAuth(),
                    body:formData
                }
            );

        const data =
            await response.json();
        if (manejarRespuestaNoAutorizada(response)) {
            return;
        }

        if(data.success){

            mostrarMensaje(
                data.message,
                "success"
            );

            setTimeout(() => {

                window.location.href =
                    "/ver-productos";

            }, 1200);

        }else{

            mostrarMensaje(
                data.message,
                "error"
            );

        }

    }catch(error){

        console.error(error);

        mostrarMensaje(
            "Error actualizando el producto",
            "error"
        );

    }

}

/* =========================
   VALIDACIONES
========================= */

function validarFormulario(){

    if(!nombreInput.value.trim()){
        mostrarMensaje(
            "Ingresa el nombre del vestido",
            "error"
        );
        return false;
    }

    if(!tallaInput.value.trim()){
        mostrarMensaje(
            "Ingresa la talla del vestido",
            "error"
        );
        return false;
    }
    if(
        !precioBaseInput.value ||
        Number(precioBaseInput.value) <= 0
    ){
        mostrarMensaje(
            "El precio debe ser mayor a cero",
            "error"
        );
        return false;
    }
    if (Number(precioBaseInput.value) % 10000 !== 0) {

        mostrarMensaje(
            "El precio debe ser un valor cerrado, por ejemplo 120000 o 250000",
            "error"
        );

        return false;
    }

    if(!idModeloInput.value){

        mostrarMensaje(
            "Selecciona un modelo",
            "error"
        );

        return false;

    }

    if(!idProducto){

        if(
            !imagen1Input.files[0] ||
            !imagen2Input.files[0] ||
            !imagen3Input.files[0]
        ){

            mostrarMensaje(
                "Debes seleccionar las 3 imágenes",
                "error"
            );

            return false;

        }

    }

    return true;

}

/* =========================
   HELPERS
========================= */

function resetearPreviews(){

    preview1.src =
        "/images/logo.png";

    preview2.src =
        "/images/logo.png";

    preview3.src =
        "/images/logo.png";

}

function mostrarMensaje(texto, tipo){

    messageBox.textContent =
        texto;

    messageBox.className =
        `message-box ${tipo}`;

}

function ocultarMensaje(){

    messageBox.className =
        "message-box";

    messageBox.textContent =
        "";

}

function convertirModeloAId(nombreModelo){

    const modelos = {

        "Tradicional":"TT",
        "Fantasía":"TF",
        "Pintado":"P",
        "Profesional":"TPRO"

    };

    return modelos[nombreModelo] || "";

}