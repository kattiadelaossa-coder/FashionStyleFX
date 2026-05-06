/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carrito = CatalogoController.getCarrito();
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
        btnMas.setOnAction(e -> {
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
        
        double total = carrito.calcularTotal() + COSTO_ENVIO;
        lblMensaje.setText("Compra realizada con exito! Total: $" + total);
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
            stage.setTitle("FashionStyle - Catalogo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}