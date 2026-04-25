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

    private List<Producto> listaProductosOriginal;
    private List<Producto> listaProductosFiltrada;
    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarProductos();

        // Configurar eventos de los botones
        btnFiltrarHombres.setOnAction(event -> filtrarPorCategoria("Hombre"));
        btnFiltrarMujeres.setOnAction(event -> filtrarPorCategoria("Mujer"));
        btnFiltrarTodos.setOnAction(event -> filtrarPorCategoria("Todos"));
        btnCerrarSesion.setOnAction(event -> handleCerrarSesion());

        // Mostrar todos los productos al inicio
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
        // Limpiar el grid antes de mostrar nuevos productos
        gridProductos.getChildren().clear();

        int fila = 0;
        int columna = 0;
        int maxColumnas = 3; // 3 columnas por fila

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

        tarjeta.getChildren().addAll(lblNombre, lblPrecio, lblCategoria);

        return tarjeta;
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
}
