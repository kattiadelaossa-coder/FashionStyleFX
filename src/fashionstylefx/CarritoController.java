/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author pc
 */

public class CarritoController implements Initializable {

    @FXML
    private ListView<String> listaCarrito;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carrito = CatalogoController.getCarrito();
        actualizarVista();

        btnComprar.setOnAction(event -> handleComprar());
        btnSeguirComprando.setOnAction(event -> handleSeguirComprando());
        btnLimpiar.setOnAction(event -> handleLimpiar());
    }

    private void actualizarVista() {
        String[] productos = carrito.getListaProductos();
        ObservableList<String> items = FXCollections.observableArrayList(productos);
        listaCarrito.setItems(items);
        lblTotal.setText("Total: $" + carrito.calcularTotal());
    }

    private void handleComprar() {
        if (carrito.colaVacia()) {
            lblMensaje.setText("El carrito está vacío");
            return;
        }

        lblMensaje.setText("¡Compra realizada con éxito! Total: $" + carrito.calcularTotal());
        carrito.limpiarCola();
        actualizarVista();
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
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Catalogo.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnSeguirComprando.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("FashionStyle - Catálogo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
