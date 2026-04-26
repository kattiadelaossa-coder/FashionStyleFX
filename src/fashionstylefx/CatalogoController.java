/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class CatalogoController implements Initializable {

    @FXML
    private GridPane gridProductos;
    @FXML
    private Label lblMensaje;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnFiltrarHombres;
    @FXML
    private Button btnFiltrarMujeres;
    @FXML
    private Button btnFiltrarTodos;
    @FXML
private Button btnVerCarrito;

    private static ColaCarrito carrito = new ColaCarrito();

    private List<Producto> listaProductosOriginal;
    private List<Producto> listaProductosFiltrada;
    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";

    public static ColaCarrito getCarrito() {
        return carrito;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarProductos();

        btnFiltrarHombres.setOnAction(event -> filtrarPorCategoria("Hombre"));
        btnFiltrarMujeres.setOnAction(event -> filtrarPorCategoria("Mujer"));
        btnFiltrarTodos.setOnAction(event -> filtrarPorCategoria("Todos"));
        btnCerrarSesion.setOnAction(event -> handleCerrarSesion());
        btnVerCarrito.setOnAction(event -> abrirCarrito());

        filtrarPorCategoria("Todos");
    }

    private void cargarProductos() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_PRODUCTOS);
            Type tipoLista = new TypeToken<List<Producto>>() {
            }.getType();
            listaProductosOriginal = gson.fromJson(reader, tipoLista);
            reader.close();

            for (Producto p : listaProductosOriginal) {
                p.setCantidad(0);
            }

            lblMensaje.setText("Productos cargados: " + listaProductosOriginal.size());
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar productos");
            e.printStackTrace();
        }
    }

    private void filtrarPorCategoria(String categoria) {
        listaProductosFiltrada = new ArrayList<>();

        if (categoria.equals("Todos")) {
            listaProductosFiltrada = new ArrayList<>(listaProductosOriginal);
        } else {
            for (Producto p : listaProductosOriginal) {
                if (p.getCategoria().equals(categoria)) {
                    listaProductosFiltrada.add(p);
                }
            }
        }

        mostrarProductosEnGrid();

        if (listaProductosFiltrada.isEmpty()) {
            lblMensaje.setText("No hay productos en la categoría " + categoria);
        } else {
            lblMensaje.setText("Mostrando " + listaProductosFiltrada.size() + " productos de " + categoria);
        }
    }

    private void mostrarProductosEnGrid() {
        gridProductos.getChildren().clear();

        int fila = 0;
        int columna = 0;
        int maxColumnas = 3;

        for (Producto p : listaProductosFiltrada) {
            VBox tarjeta = crearTarjetaProducto(p);
            gridProductos.add(tarjeta, columna, fila);

            columna++;
            if (columna >= maxColumnas) {
                columna = 0;
                fila++;
            }
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("-fx-padding: 12; -fx-border-color: #E0E0E0; -fx-border-radius: 12; -fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        tarjeta.setPrefWidth(200);

        Label lblNombre = new Label(producto.getNombre());
        lblNombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");
        lblNombre.setPrefWidth(180);

        Label lblPrecio = new Label("$" + producto.getPrecio());
        lblPrecio.setStyle("-fx-text-fill: #1E88E5; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblCategoria = new Label(producto.getCategoria());
        lblCategoria.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");

        Button btnAgregar = new Button("🛒 Agregar");
        btnAgregar.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        btnAgregar.setOnAction(event -> agregarAlCarrito(producto));

        tarjeta.getChildren().addAll(lblNombre, lblPrecio, lblCategoria, btnAgregar);

        return tarjeta;
    }

    private void agregarAlCarrito(Producto producto) {
        // Buscar si el producto ya está en el carrito
        boolean encontrado = false;
        ColaCarrito temp = new ColaCarrito();
        Producto existente = null;

        // Recorrer la cola sin perder los datos
        while (!carrito.colaVacia()) {
            Producto p = carrito.valorFrente();
            if (p.getId() == producto.getId()) {
                encontrado = true;
                existente = p;
            }
            temp.agregar(p);
            carrito.quitar();
        }

        // Restaurar la cola
        while (!temp.colaVacia()) {
            carrito.agregar(temp.valorFrente());
            temp.quitar();
        }

        if (encontrado && existente != null) {
            existente.setCantidad(existente.getCantidad() + 1);
        } else {
            Producto nuevo = new Producto(producto.getId(), producto.getNombre(),
                    producto.getPrecio(), producto.getCategoria(),
                    producto.getImagen());
            nuevo.setCantidad(1);
            carrito.agregar(nuevo);
        }

        lblMensaje.setText("✓ " + producto.getNombre() + " agregado al carrito (Total: " + carrito.getTotalNodos() + " productos)");
    }

    private void handleCerrarSesion() {
        btnCerrarSesion.getScene().getWindow().hide();
        abrirLogin();
    }

    private void abrirLogin() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Login");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirCarrito() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Carrito.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Carrito");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir carrito");
        }
    }
}
