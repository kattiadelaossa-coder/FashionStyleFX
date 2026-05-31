/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ListaDeseosController implements Initializable {

    @FXML
    private GridPane gridDeseos;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensaje;

    private List<Producto> listaDeseos;
    private String emailUsuario;
    private final String ARCHIVO_DESEOS = "src/fashionstylefx/deseos.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailUsuario = LoginController.getUsuarioActual().getCorreo();
        cargarListaDeseos();
        mostrarProductos();
        btnVolver.setOnAction(event -> volver());
    }

    private void cargarListaDeseos() {
    try {
        String email = LoginController.getUsuarioActual().getCorreo();
        listaDeseos = DataStorage.cargarDeseos(email);
        
        // ========== ACTUALIZAR STOCK DESDE productos.json ==========
        Gson gson = new Gson();
        FileReader reader = new FileReader("src/fashionstylefx/productos.json");
        Type tipo = new TypeToken<List<Producto>>(){}.getType();
        List<Producto> productosActuales = gson.fromJson(reader, tipo);
        reader.close();
        
        // Actualizar el stock de cada producto en la lista de deseos
        for (Producto deseado : listaDeseos) {
            for (Producto p : productosActuales) {
                if (deseado.getId() == p.getId()) {
                    deseado.setStock(p.getStock());
                    System.out.println("Stock actualizado para " + deseado.getNombre() + ": " + p.getStock());
                    break;
                }
            }
        }
        // ===========================================================
        
        if (listaDeseos == null) listaDeseos = new ArrayList<>();
    } catch (Exception e) {
        listaDeseos = new ArrayList<>();
    }
}

    private void guardarDeseos() {
        String email = LoginController.getUsuarioActual().getCorreo();
        DataStorage.guardarDeseos(email, listaDeseos);
    }

    private void mostrarProductos() {
        gridDeseos.getChildren().clear();

        int columna = 0;
        int fila = 0;

        for (Producto p : listaDeseos) {
            VBox tarjeta = crearTarjetaProducto(p);
            gridDeseos.add(tarjeta, columna, fila);

            columna++;
            if (columna > 3) {
                columna = 0;
                fila++;
            }
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("-fx-padding: 12; -fx-border-color: #E0E0E0; -fx-border-radius: 12; -fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        tarjeta.setPrefWidth(200);
        tarjeta.setAlignment(Pos.CENTER);

        // ========== AGREGAR IMAGEN ==========
        javafx.scene.image.ImageView imagen = new javafx.scene.image.ImageView();
        try {
            String rutaImagen = "imagenes/" + producto.getImagen();
            java.io.InputStream inputStream = getClass().getResourceAsStream(rutaImagen);
            if (inputStream != null) {
                javafx.scene.image.Image img = new javafx.scene.image.Image(inputStream);
                imagen.setImage(img);
                imagen.setFitWidth(180);
                imagen.setFitHeight(180);
                imagen.setPreserveRatio(true);
            } else {
                imagen.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-radius: 8;");
                imagen.setFitWidth(180);
                imagen.setFitHeight(180);
            }
        } catch (Exception e) {
            imagen.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-radius: 8;");
            imagen.setFitWidth(180);
            imagen.setFitHeight(180);
        }
        // ===================================

        Label lblNombre = new Label(producto.getNombre());
        lblNombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");
        lblNombre.setPrefWidth(180);

        Label lblPrecio = new Label("$" + producto.getPrecio());
        lblPrecio.setStyle("-fx-text-fill: #1E88E5; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnAgregar = new Button("🛒 Agregar al carrito");
        btnAgregar.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        btnAgregar.setOnAction(e -> agregarAlCarrito(producto));

        Button btnEliminar = new Button("❤Eliminar");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-border-color: #E53935; -fx-border-radius: 8; -fx-text-fill: #E53935; -fx-cursor: hand;");
        btnEliminar.setOnAction(e -> eliminarDeDeseos(producto));

        VBox botones = new VBox(8);
        botones.setAlignment(Pos.CENTER);
        btnAgregar.setPrefWidth(160);
        btnEliminar.setPrefWidth(160);
        botones.getChildren().addAll(btnAgregar, btnEliminar);

        // Agregar la imagen primero
        tarjeta.getChildren().addAll(imagen, lblNombre, lblPrecio, botones);

        return tarjeta;
    }

    private void agregarAlCarrito(Producto producto) {
    // Verificar stock disponible
    if (producto.getStock() <= 0) {
        mostrarAlertaError("No hay stock disponible de " + producto.getNombre());
        return;
    }
    
    ColaCarrito carrito = CatalogoController.getCarrito();
    boolean encontrado = false;
    ColaCarrito temp = new ColaCarrito();
    Producto existente = null;

    while (!carrito.colaVacia()) {
        Producto p = carrito.valorFrente();
        if (p.getId() == producto.getId()) {
            // Verificar que no supere el stock
            int nuevaCantidad = p.getCantidad() + 1;
            if (nuevaCantidad > producto.getStock()) {
                mostrarAlertaError("No hay suficiente stock de " + producto.getNombre() + 
                    ". Stock disponible: " + producto.getStock());
                return;
            }
            p.setCantidad(nuevaCantidad);
            encontrado = true;
            existente = p;
        }
        temp.agregar(p);
        carrito.quitar();
    }

    while (!temp.colaVacia()) {
        carrito.agregar(temp.valorFrente());
        temp.quitar();
    }

    if (!encontrado) {
        // Verificar que haya al menos 1 unidad de stock
        if (producto.getStock() < 1) {
            mostrarAlertaError("No hay stock disponible de " + producto.getNombre());
            return;
        }
        Producto nuevo = new Producto(producto.getId(), producto.getNombre(),
                producto.getPrecio(), producto.getCategoria(),
                producto.getImagen());
        nuevo.setCantidad(1);
        nuevo.setStock(producto.getStock());
        carrito.agregar(nuevo);
    }

    mostrarAlertaExito(producto.getNombre() + " agregado al carrito");
}
    private void eliminarDeDeseos(Producto producto) {
        listaDeseos.remove(producto);
        guardarDeseos();
        mostrarProductos();
        lblMensaje.setText("✗ " + producto.getNombre() + " eliminado de la lista de deseos");
    }

    private void mostrarAlertaExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FashionStyle");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void volver() {
        btnVolver.getScene().getWindow().hide();
        abrirCatalogo();
    }

    private void abrirCatalogo() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Catalogo.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Catálogo");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("FashionStyle");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
