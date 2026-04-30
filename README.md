# FashionStyleFX - Tienda Virtual

## 📌 Descripción
FashionStyleFX es una tienda virtual de ropa juvenil desarrollada en JavaFX. Permite a los usuarios registrarse, iniciar sesión, explorar productos por categoría, agregar al carrito y ver historial de compras.

## 🛠️ Tecnologías utilizadas
- **Java 23** (JDK)
- **JavaFX 23** (Interfaz gráfica)
- **Gson** (Manejo de archivos JSON)
- **NetBeans 26** (IDE)
- **Scene Builder** (Diseño de interfaces)

## 📁 Estructura del proyecto
FashionStyleFX/
├── src/fashionstylefx/
│ ├── FashionStyleFX.java (Clase principal)
│ ├── LoginController.java (Controlador Login)
│ ├── RegistroController.java (Controlador Registro)
│ ├── CatalogoController.java (Controlador Catálogo)
│ ├── CarritoController.java (Controlador Carrito)
│ ├── HistorialController.java (Controlador Historial)
│ ├── Usuario.java (Modelo)
│ ├── Producto.java (Modelo)
│ ├── Compra.java (Modelo)
│ ├── NodoProducto.java (Nodo para lista doble)
│ ├── ListaDobleCircular.java (Lista doble circular)
│ ├── NodoCarrito.java (Nodo para cola)
│ ├── ColaCarrito.java (Cola FIFO para carrito)
│ ├── usuarios.json (Datos de usuarios)
│ ├── productos.json (Datos de productos)
│ └── compras.json (Historial de compras)
└── README.md

## 🎯 Funcionalidades
### Cliente
- ✅ Login de usuarios (desde JSON)
- ✅ Registro de nuevos usuarios
- ✅ Catálogo de productos con filtros por categoría (Hombres/Mujeres)
- ✅ Carrito de compras con operaciones:
  - Agregar productos
  - Modificar cantidades
  - Eliminar productos individuales
- ✅ Historial de compras
- ✅ Cerrar sesión

### Estructuras de datos implementadas
- ✅ Lista doble circular (para productos en catálogo)
- ✅ Cola FIFO (para carrito de compras)

## 📦 Archivos de datos
- `usuarios.json`: Almacena usuarios registrados
- `productos.json`: Almacena productos disponibles
- `compras.json`: Almacena historial de compras

## 🚀 Cómo ejecutar
1. Clonar el repositorio
2. Abrir en NetBeans 26
3. Configurar JavaFX SDK y Gson en las librerías
4. Configurar VM Options: --module-path "ruta\javafx-sdk-23\lib" --add-modules javafx.controls,javafx.fxml
5. Ejecutar `FashionStyleFX.java`

## 👤 Credenciales de prueba
- **Admin:** admin@fashionstyle.com / admin123

## 📅 Entregas
- Entregable 01: Prototipo en Figma (✅)
- Entregable 02: Avance en JavaFX (✅)
- Entregable 03: Proyecto final (Pendiente)

## 👩‍💻 Autora
[Maria Paternina]
