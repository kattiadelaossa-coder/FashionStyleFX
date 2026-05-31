/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author pc
 */
public class HistorialController implements Initializable {

    @FXML
    private VBox contenedorHistorial;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensaje;

    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarHistorial();
        btnVolver.setOnAction(event -> volver());
    }

    private void cargarHistorial() {
        contenedorHistorial.getChildren().clear();

        try {
            String email = LoginController.getUsuarioActual().getCorreo();
            System.out.println("Cargando historial para: " + email);

            // Usar DataStorage para cargar compras del usuario actual
            List<Compra> compras = DataStorage.cargarCompras(email);

            System.out.println("Compras encontradas: " + compras.size());

            if (compras == null || compras.isEmpty()) {
                lblMensaje.setText("No hay compras registradas");
                return;
            }

            for (Compra c : compras) {
                VBox tarjeta = crearTarjetaCompra(c);
                contenedorHistorial.getChildren().add(tarjeta);
            }

            lblMensaje.setText("Total de compras: " + compras.size());

        } catch (Exception e) {
            lblMensaje.setText("Error al cargar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox crearTarjetaCompra(Compra compra) {
        VBox tarjeta = new VBox(10);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 4);");

        // Header de la tarjeta: Pedido #ID y Estado
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblId = new Label("Pedido #" + compra.getId());
        lblId.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblEstado = new Label(compra.getEstado() != null ? compra.getEstado() : "Completado");
        String colorEstado = obtenerColorEstado(compra.getEstado());
        lblEstado.setStyle("-fx-background-color: " + colorEstado + "; -fx-text-fill: white; -fx-padding: 4 12; -fx-background-radius: 12; -fx-font-size: 12px;");

        header.getChildren().addAll(lblId, lblEstado);

        // Fecha y Total
        HBox info = new HBox(30);
        info.setAlignment(Pos.CENTER_LEFT);

        Label lblFecha = new Label("📅 " + compra.getFecha());
        lblFecha.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        Label lblTotal = new Label("💰 Total: $" + compra.getTotal());
        lblTotal.setStyle("-fx-text-fill: #1E88E5; -fx-font-size: 14px; -fx-font-weight: bold;");

        info.getChildren().addAll(lblFecha, lblTotal);

        // Cliente
        Label lblCliente = new Label("👤 Cliente: " + (compra.getCliente() != null ? compra.getCliente() : "Cliente"));
        lblCliente.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        // Productos
        Label lblProductos = new Label("📦 Productos: " + compra.getResumenProductos());
        lblProductos.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px; -fx-wrap-text: true;");
        lblProductos.setMaxWidth(800);

        // Botones
        HBox botones = new HBox(15);
        botones.setAlignment(Pos.CENTER_LEFT);

        Button btnDetalles = new Button("Ver detalles");
        btnDetalles.setStyle("-fx-background-color: transparent; -fx-border-color: #1E88E5; -fx-border-radius: 8; -fx-text-fill: #1E88E5; -fx-padding: 6 15;");
        btnDetalles.setOnAction(e -> verDetalles(compra));

        Button btnComprarNuevo = new Button("Comprar de nuevo");
        btnComprarNuevo.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 6 15; -fx-cursor: hand;");
        btnComprarNuevo.setOnAction(e -> comprarDeNuevo(compra));

        botones.getChildren().addAll(btnDetalles, btnComprarNuevo);

        tarjeta.getChildren().addAll(header, info, lblCliente, lblProductos, botones);

        return tarjeta;
    }

    private String obtenerColorEstado(String estado) {
        if (estado == null) {
            return "#4CAF50";
        }
        switch (estado) {
            case "Entregado":
                return "#4CAF50";
            case "En camino":
                return "#FF9800";
            case "Procesando":
                return "#2196F3";
            default:
                return "#4CAF50";
        }
    }

    private void verDetalles(Compra compra) {
        // Mostrar ventana con detalles
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(compra.getId()).append("\n");
        sb.append("Fecha: ").append(compra.getFecha()).append("\n");
        sb.append("Cliente: ").append(compra.getCliente()).append("\n");
        sb.append("Estado: ").append(compra.getEstado()).append("\n");
        sb.append("Total: $").append(compra.getTotal()).append("\n\n");
        sb.append("Productos:\n");

        if (compra.getProductos() != null) {
            for (Producto p : compra.getProductos()) {
                sb.append("  - ").append(p.getNombre())
                        .append(" x").append(p.getCantidad())
                        .append(" = $").append(p.getPrecio() * p.getCantidad()).append("\n");
            }
        }

        javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setPrefHeight(300);
        textArea.setPrefWidth(400);

        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Detalles del pedido");
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.OK);
        dialog.showAndWait();
    }

    private void comprarDeNuevo(Compra compra) {
        if (compra.getProductos() != null) {
            ColaCarrito carrito = CatalogoController.getCarrito();

            for (Producto p : compra.getProductos()) {
                if (carrito.existeProducto(p.getId())) {
                    carrito.aumentarCantidad(p.getId(), p.getCantidad());
                } else {
                    Producto nuevo = new Producto(p.getId(), p.getNombre(), p.getPrecio(), p.getCategoria(), p.getImagen());
                    nuevo.setCantidad(p.getCantidad());
                    carrito.agregar(nuevo);
                }
            }
            lblMensaje.setText("Productos agregados al carrito");
            abrirCarrito();
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
            lblMensaje.setText("Error al abrir carrito");
        }
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
}
