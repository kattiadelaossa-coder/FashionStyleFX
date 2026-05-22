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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author pc
 */


public class AdminDashboardController implements Initializable {

    @FXML
    private Label lblTotalProductos;
    @FXML
    private Label lblTotalUsuarios;
    @FXML
    private Label lblTotalPedidos;
    @FXML
    private Label lblTotalVentas;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Label lblMensaje;
    @FXML
    private BarChart<String, Number> graficoVentas;

    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";
    private final String ARCHIVO_USUARIOS = "src/fashionstylefx/usuarios.json";
    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstadisticas();
        cargarGraficoVentas();
        btnCerrarSesion.setOnAction(event -> cerrarSesion());
    }
    
    private void cargarEstadisticas() {
        // Productos
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_PRODUCTOS);
            Type tipo = new TypeToken<List<Producto>>(){}.getType();
            List<Producto> productos = gson.fromJson(reader, tipo);
            reader.close();
            lblTotalProductos.setText(String.valueOf(productos.size()));
        } catch (Exception e) {
            lblTotalProductos.setText("0");
            System.out.println("Error al cargar productos: " + e.getMessage());
        }
        
        // Usuarios
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_USUARIOS);
            Type tipo = new TypeToken<List<Usuario>>(){}.getType();
            List<Usuario> usuarios = gson.fromJson(reader, tipo);
            reader.close();
            lblTotalUsuarios.setText(String.valueOf(usuarios.size()));
        } catch (Exception e) {
            lblTotalUsuarios.setText("0");
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
        
        // Pedidos y Ventas
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipo = new TypeToken<List<Compra>>(){}.getType();
            List<Compra> compras = gson.fromJson(reader, tipo);
            reader.close();
            
            if (compras != null && !compras.isEmpty()) {
                lblTotalPedidos.setText(String.valueOf(compras.size()));
                double total = 0;
                for (Compra c : compras) {
                    total += c.getTotal();
                }
                lblTotalVentas.setText("$" + total);
            } else {
                lblTotalPedidos.setText("0");
                lblTotalVentas.setText("$0");
            }
        } catch (Exception e) {
            lblTotalPedidos.setText("0");
            lblTotalVentas.setText("$0");
            System.out.println("Error al cargar compras: " + e.getMessage());
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
    
    private void cargarGraficoVentas() {
    try {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas 2026");
        series.getData().add(new XYChart.Data<>("Enero", 150000));
        series.getData().add(new XYChart.Data<>("Febrero", 200000));
        series.getData().add(new XYChart.Data<>("Marzo", 180000));
        series.getData().add(new XYChart.Data<>("Abril", 250000));
        series.getData().add(new XYChart.Data<>("Mayo", 300000));
        
        graficoVentas.getData().clear();
        graficoVentas.getData().add(series);
    } catch (Exception e) {
        System.out.println("Error al cargar gráfico: " + e.getMessage());
    }
}
}