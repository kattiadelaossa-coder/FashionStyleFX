/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author pc
 */
public class CarritoController implements Initializable {

    @FXML
    private ListView<HBox> listaCarrito;
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
        listaCarrito.getItems().clear();

        ColaCarrito temp = new ColaCarrito();

        // Recorrer la cola sin perder los datos
        while (!carrito.colaVacia()) {
            Producto p = carrito.valorFrente();

            // Crear HBox para cada producto
            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPrefHeight(40);

            // Información del producto
            Label lblInfo = new Label(p.getNombre() + " - $" + p.getPrecio());
            lblInfo.setPrefWidth(180);

            // Botón disminuir
            Button btnMenos = new Button("-");
            btnMenos.setStyle("-fx-background-color: #E0E0E0; -fx-min-width: 30;");

            // Cantidad
            Label lblCant = new Label(String.valueOf(p.getCantidad()));
            lblCant.setPrefWidth(30);
            lblCant.setAlignment(Pos.CENTER);

            // Botón aumentar
            Button btnMas = new Button("+");
            btnMas.setStyle("-fx-background-color: #E0E0E0; -fx-min-width: 30;");

            // Subtotal del producto
            double subtotal = p.getPrecio() * p.getCantidad();
            Label lblSubtotal = new Label("$" + subtotal);
            lblSubtotal.setPrefWidth(80);
            lblSubtotal.setStyle("-fx-text-fill: #1E88E5; -fx-font-weight: bold;");

            // Botón eliminar
            Button btnElim = new Button("🗑️");
            btnElim.setStyle("-fx-background-color: transparent; -fx-text-fill: #E53935;");

            // Eventos
            int id = p.getId();
            btnMenos.setOnAction(e -> {
                carrito.disminuirCantidad(id);
                actualizarVista();
            });

            btnMas.setOnAction(e -> {
                carrito.aumentarCantidad(id);
                actualizarVista();
            });

            btnElim.setOnAction(e -> {
                carrito.eliminarProducto(id);
                actualizarVista();
            });

            hbox.getChildren().addAll(lblInfo, btnMenos, lblCant, btnMas, lblSubtotal, btnElim);
            HBox.setHgrow(lblInfo, Priority.ALWAYS);

            listaCarrito.getItems().add(hbox);

            temp.agregar(p);
            carrito.quitar();
        }

        // Restaurar la cola original
        while (!temp.colaVacia()) {
            carrito.agregar(temp.valorFrente());
            temp.quitar();
        }

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
