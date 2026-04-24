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

    private List<Producto> listaProductos;
    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarProductos();
        mostrarProductos();

        if (btnCerrarSesion != null) {
            btnCerrarSesion.setOnAction(event -> handleCerrarSesion());
        }
    }

    private void cargarProductos() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_PRODUCTOS);
            Type tipoLista = new TypeToken<List<Producto>>() {
            }.getType();
            listaProductos = gson.fromJson(reader, tipoLista);
            reader.close();
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar productos");
            e.printStackTrace();
        }
    }

    private void mostrarProductos() {
        int fila = 0;
        int columna = 0;

        for (Producto p : listaProductos) {
            VBox tarjeta = crearTarjetaProducto(p);
            gridProductos.add(tarjeta, columna, fila);

            columna++;
            if (columna == 3) { // 3 columnas por fila
                columna = 0;
                fila++;
            }
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
        VBox tarjeta = new VBox(10);
        tarjeta.setStyle("-fx-padding: 15; -fx-border-color: #E0E0E0; -fx-border-radius: 12; -fx-background-color: white; -fx-background-radius: 12;");

        Label lblNombre = new Label(producto.getNombre());
        lblNombre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblPrecio = new Label("$" + producto.getPrecio());
        lblPrecio.setStyle("-fx-text-fill: #1E88E5; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblCategoria = new Label(producto.getCategoria());
        lblCategoria.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        Button btnAgregar = new Button("Agregar al carrito");
        btnAgregar.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-background-radius: 8;");

        tarjeta.getChildren().addAll(lblNombre, lblPrecio, lblCategoria, btnAgregar);

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
