/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author pc
 */

public class AdminDashboardController implements Initializable {

    @FXML
    private Label lblAdminNombre;
    @FXML
    private Label lblTotalProductos;
    @FXML
    private Label lblTotalUsuarios;
    @FXML
    private Label lblTotalPedidos;
    @FXML
    private Label lblTotalVentas;
    @FXML
    private Button btnGestionarProductos;
    @FXML
    private Button btnGestionarUsuarios;
    @FXML
    private Button btnGestionarPedidos;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Label lblMensaje;

    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";
    private final String ARCHIVO_USUARIOS = "src/fashionstylefx/usuarios.json";
    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstadisticas();

        btnCerrarSesion.setOnAction(event -> cerrarSesion());
        btnGestionarProductos.setOnAction(event -> gestionarProductos());
        btnGestionarUsuarios.setOnAction(event -> gestionarUsuarios());
        btnGestionarPedidos.setOnAction(event -> gestionarPedidos());
    }

    private void cargarEstadisticas() {
        try {
            Gson gson = new Gson();

            // Productos
            FileReader readerProductos = new FileReader(ARCHIVO_PRODUCTOS);
            Type tipoProductos = new TypeToken<List<Producto>>() {
            }.getType();
            List<Producto> productos = gson.fromJson(readerProductos, tipoProductos);
            readerProductos.close();
            lblTotalProductos.setText(String.valueOf(productos.size()));

            // Usuarios
            FileReader readerUsuarios = new FileReader(ARCHIVO_USUARIOS);
            Type tipoUsuarios = new TypeToken<List<Usuario>>() {
            }.getType();
            List<Usuario> usuarios = gson.fromJson(readerUsuarios, tipoUsuarios);
            readerUsuarios.close();
            lblTotalUsuarios.setText(String.valueOf(usuarios.size()));

            // Pedidos y Ventas
            try {
                FileReader readerCompras = new FileReader(ARCHIVO_COMPRAS);
                Type tipoCompras = new TypeToken<List<Compra>>() {
                }.getType();
                List<Compra> compras = gson.fromJson(readerCompras, tipoCompras);
                readerCompras.close();

                if (compras != null) {
                    lblTotalPedidos.setText(String.valueOf(compras.size()));
                    double totalVentas = 0;
                    for (Compra c : compras) {
                        totalVentas += c.getTotal();
                    }
                    lblTotalVentas.setText("$" + totalVentas);
                }
            } catch (Exception e) {
                lblTotalPedidos.setText("0");
                lblTotalVentas.setText("$0");
            }

        } catch (Exception e) {
            lblMensaje.setText("Error al cargar estadísticas");
            e.printStackTrace();
        }
    }

    private void cerrarSesion() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("FashionStyle - Login");
        } catch (Exception e) {
            lblMensaje.setText("Error al cerrar sesión");
        }
    }

    private void gestionarProductos() {
        lblMensaje.setText("Gestión de productos en desarrollo");
    }

    private void gestionarUsuarios() {
        lblMensaje.setText("Gestión de usuarios en desarrollo");
    }

    private void gestionarPedidos() {
        lblMensaje.setText("Gestión de pedidos en desarrollo");
    }
}
