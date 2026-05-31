/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.google.gson.GsonBuilder;
import javafx.stage.Stage;
public class CarritoController implements Initializable {

    @FXML
    private VBox contenedorProductos;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblEnvio;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnComprar;
    @FXML
    private Button btnSeguirComprando;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Label lblMensaje;

    private ColaCarrito carrito;
    private final double COSTO_ENVIO = 10000;
    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";
    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";
    private int ultimoId = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carrito = CatalogoController.getCarrito();
        cargarUltimoId();
        actualizarVista();

        btnComprar.setOnAction(event -> handleComprar());
        btnSeguirComprando.setOnAction(event -> handleSeguirComprando());
        btnLimpiar.setOnAction(event -> handleLimpiar());
    }

    private void actualizarVista() {
        contenedorProductos.getChildren().clear();

        double subtotal = 0;

        ColaCarrito temp = new ColaCarrito();
        while (!carrito.colaVacia()) {
            Producto p = carrito.valorFrente();
            subtotal += p.getPrecio() * p.getCantidad();

            HBox fila = crearFilaProducto(p);
            contenedorProductos.getChildren().add(fila);

            temp.agregar(p);
            carrito.quitar();
        }

        while (!temp.colaVacia()) {
            carrito.agregar(temp.valorFrente());
            temp.quitar();
        }

        double total = subtotal + COSTO_ENVIO;

        lblSubtotal.setText("$" + subtotal);
        lblEnvio.setText("$" + COSTO_ENVIO);
        lblTotal.setText("$" + total);
    }

    private HBox crearFilaProducto(Producto p) {
    HBox fila = new HBox(10);
    fila.setAlignment(Pos.CENTER_LEFT);
    fila.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 10;");

    Label lblInfo = new Label(p.getNombre());
    lblInfo.setPrefWidth(280);

    Label lblPrecio = new Label("$" + p.getPrecio());
    lblPrecio.setPrefWidth(100);

    HBox cantidadControl = new HBox(5);
    Button btnMenos = new Button("-");
    Label lblCantidad = new Label(String.valueOf(p.getCantidad()));
    Button btnMas = new Button("+");
    cantidadControl.getChildren().addAll(btnMenos, lblCantidad, btnMas);
    cantidadControl.setPrefWidth(120);

    double subtotal = p.getPrecio() * p.getCantidad();
    Label lblSubtotalProducto = new Label("$" + subtotal);
    lblSubtotalProducto.setPrefWidth(120);

    Button btnEliminar = new Button("X");

    int id = p.getId();
    
    btnMenos.setOnAction(e -> {
        carrito.disminuirCantidad(id);
        actualizarVista();
    });
    
    // Validar stock antes de aumentar
    btnMas.setOnAction(e -> {
        // Verificar que no supere el stock disponible
        if (p.getCantidad() + 1 > p.getStock()) {
            mostrarAlertaError("No hay suficiente stock de " + p.getNombre());
            return;
        }
        carrito.aumentarCantidad(id);
        actualizarVista();
    });
    
    btnEliminar.setOnAction(e -> {
        carrito.eliminarProducto(id);
        actualizarVista();
    });

    fila.getChildren().addAll(lblInfo, lblPrecio, cantidadControl, lblSubtotalProducto, btnEliminar);

    return fila;
}
    private void handleComprar() {
    if (carrito.colaVacia()) {
        lblMensaje.setText("El carrito esta vacio");
        return;
    }
    
    // Validar stock
    if (!validarStockCarrito()) {
        return;
    }

    // Preparar lista de productos desde el carrito
    List<Producto> productosComprados = new ArrayList<>();
    while (!carrito.colaVacia()) {
        Producto p = carrito.valorFrente();
        System.out.println("Producto: " + p.getNombre() + ", Cantidad: " + p.getCantidad() + ", Precio: " + p.getPrecio());
        productosComprados.add(p);
        carrito.quitar();
    }

    // Guardar compra
    guardarCompra(productosComprados);

    double total = calcularTotalCarrito(productosComprados);
    lblMensaje.setText("Compra realizada con exito! Total: $" + total);
    actualizarVista();
}

private double calcularTotalCarrito(List<Producto> productos) {
    double total = 0;
    for (Producto p : productos) {
        total += p.getPrecio() * p.getCantidad();
    }
    return total + COSTO_ENVIO;
}

    private void handleSeguirComprando() {
        volverCatalogo();
    }

    private void handleLimpiar() {
        carrito.limpiarCola();
        actualizarVista();
        lblMensaje.setText("Carrito limpiado");
    }

    private void volverCatalogo() {
    try {
        // Cerrar la ventana actual de carrito
        Stage stageActual = (Stage) btnSeguirComprando.getScene().getWindow();
        stageActual.close();
        
        // Abrir el catálogo
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Catalogo.fxml"));
        javafx.scene.Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("FashionStyle - Catálogo");
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void cargarUltimoId() {
        try {
            File archivo = new File(ARCHIVO_COMPRAS);
            if (archivo.exists()) {
                Gson gson = new Gson();
                FileReader reader = new FileReader(ARCHIVO_COMPRAS);
                Type tipoLista = new TypeToken<List<Compra>>() {
                }.getType();
                List<Compra> compras = gson.fromJson(reader, tipoLista);
                reader.close();
                if (compras != null && !compras.isEmpty()) {
                    ultimoId = compras.get(compras.size() - 1).getId();
                }
            }
        } catch (Exception e) {
            ultimoId = 0;
        }
    }

    private void guardarCompra(List<Producto> productos) {
    try {
        String email = LoginController.getUsuarioActual().getCorreo();
        
        // Calcular total CORRECTAMENTE
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecio() * p.getCantidad();
        }
        System.out.println("Total calculado: " + total);
        
        // Actualizar stock
        actualizarStockProductos(productos);
        
        // Cargar TODAS las compras de todos los usuarios para obtener el último ID global
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, List<Compra>> todasLasCompras = new HashMap<>();
        
        File archivo = new File(ARCHIVO_COMPRAS);
        if (archivo.exists()) {
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipoMapa = new TypeToken<Map<String, List<Compra>>>(){}.getType();
            todasLasCompras = gson.fromJson(reader, tipoMapa);
            reader.close();
            if (todasLasCompras == null) todasLasCompras = new HashMap<>();
        }
        
        // Obtener el último ID GLOBAL
        int ultimoIdGlobal = 0;
        for (List<Compra> comprasUsuario : todasLasCompras.values()) {
            for (Compra c : comprasUsuario) {
                if (c.getId() > ultimoIdGlobal) {
                    ultimoIdGlobal = c.getId();
                }
            }
        }
        int nuevoId = ultimoIdGlobal + 1;
        System.out.println("Nuevo ID global: " + nuevoId);
        
        // Obtener compras del usuario actual
        List<Compra> comprasUsuario = todasLasCompras.getOrDefault(email, new ArrayList<>());
        
        // Crear nueva compra con ID global
        String nombreCliente = LoginController.getUsuarioActual().getNombre();
        Compra nuevaCompra = new Compra(nuevoId, total, productos, nombreCliente);
        comprasUsuario.add(nuevaCompra);
        
        // Actualizar y guardar
        todasLasCompras.put(email, comprasUsuario);
        
        FileWriter writer = new FileWriter(ARCHIVO_COMPRAS);
        gson.toJson(todasLasCompras, writer);
        writer.close();
        
        System.out.println("Compra guardada con ID: " + nuevoId + " para: " + email);
        
    } catch (Exception e) {
        lblMensaje.setText("Error al guardar compra");
        e.printStackTrace();
    }
}
    private void actualizarStockProductos(List<Producto> productosComprados) {
    try {      
        // Cargar productos actuales
        Gson gson = new Gson();
        FileReader reader = new FileReader(ARCHIVO_PRODUCTOS);
        Type tipo = new TypeToken<List<Producto>>(){}.getType();
        List<Producto> productos = gson.fromJson(reader, tipo);
        reader.close();
        
        // Actualizar stock
        for (Producto comprado : productosComprados) {
            for (Producto p : productos) {
                if (p.getId() == comprado.getId()) {
                    int nuevoStock = p.getStock() - comprado.getCantidad();
                    p.setStock(nuevoStock);
                    System.out.println("Stock actualizado: " + p.getNombre() + " -> " + nuevoStock);
                    break;
                }
            }
        }
        
        // Guardar cambios
        FileWriter writer = new FileWriter(ARCHIVO_PRODUCTOS);
        gson.toJson(productos, writer);
        writer.close();
        
    } catch (Exception e) {
        System.out.println("Error al actualizar stock: " + e.getMessage());
    }
}

   private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("FashionStyle");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
   
   private boolean validarStockCarrito() {
    ColaCarrito temp = new ColaCarrito();
    boolean stockValido = true;
    StringBuilder productosSinStock = new StringBuilder();
    
    // Recorrer el carrito para verificar stock
    while (!carrito.colaVacia()) {
        Producto p = carrito.valorFrente();
        
        // Verificar si la cantidad en el carrito supera el stock disponible
        if (p.getCantidad() > p.getStock()) {
            if (productosSinStock.length() > 0) {
                productosSinStock.append(", ");
            }
            productosSinStock.append(p.getNombre())
                .append(" (stock: ").append(p.getStock())
                .append(", solicitado: ").append(p.getCantidad()).append(")");
            stockValido = false;
        }
        temp.agregar(p);
        carrito.quitar();
    }
    
    // Restaurar el carrito
    while (!temp.colaVacia()) {
        carrito.agregar(temp.valorFrente());
        temp.quitar();
    }
    
    // Mostrar mensaje de error si no hay stock
    if (!stockValido) {
        mostrarAlertaError("No hay suficiente stock de los siguientes productos:\n" + productosSinStock.toString());
    }
    
    return stockValido;
}
}
