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
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class CatalogoController implements Initializable {

    @FXML
    private VBox tarjetaProducto;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblCategoria;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Label lblMensaje;

    private ListaDobleCircular listaCircular;
    private Producto productoActual;
    private int indiceActual;

    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarProductosEnListaCircular();

        if (listaCircular != null && !listaCircular.estaVacia()) {
            indiceActual = 0;
            productoActual = listaCircular.get(indiceActual);
            mostrarProductoActual();
        }

        btnSiguiente.setOnAction(event -> handleSiguiente());
        btnAnterior.setOnAction(event -> handleAnterior());
        btnCerrarSesion.setOnAction(event -> handleCerrarSesion());
    }

    private void cargarProductosEnListaCircular() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_PRODUCTOS);
            Type tipoLista = new TypeToken<List<Producto>>() {
            }.getType();
            List<Producto> productos = gson.fromJson(reader, tipoLista);
            reader.close();

            listaCircular = new ListaDobleCircular();
            for (Producto p : productos) {
                listaCircular.agregar(p);
            }

            lblMensaje.setText("Productos cargados: " + listaCircular.getTamaño());
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar productos");
            e.printStackTrace();
        }
    }

    private void mostrarProductoActual() {
        if (productoActual != null) {
            lblNombre.setText(productoActual.getNombre());
            lblPrecio.setText("$" + productoActual.getPrecio());
            lblCategoria.setText(productoActual.getCategoria());
        }
    }

    private void handleSiguiente() {
        if (listaCircular != null && !listaCircular.estaVacia()) {
            indiceActual = (indiceActual + 1) % listaCircular.getTamaño();
            productoActual = listaCircular.get(indiceActual);
            mostrarProductoActual();
        }
    }

    private void handleAnterior() {
        if (listaCircular != null && !listaCircular.estaVacia()) {
            indiceActual = (indiceActual - 1 + listaCircular.getTamaño()) % listaCircular.getTamaño();
            productoActual = listaCircular.get(indiceActual);
            mostrarProductoActual();
        }
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
