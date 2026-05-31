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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    @FXML
    private TableView<Compra> tablaPedidos;
    @FXML
    private TableColumn<Compra, Integer> colId;
    @FXML
    private TableColumn<Compra, String> colCliente;
    @FXML
    private TableColumn<Compra, String> colFecha;
    @FXML
    private TableColumn<Compra, Double> colTotal;
    @FXML
    private TableColumn<Compra, String> colEstado;

    // Botones de gestión
    @FXML
    private Button btnGestionarProductos;
    @FXML
    private Button btnGestionarUsuarios;
    @FXML
    private Button btnGestionarPedidos;

    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";
    private final String ARCHIVO_USUARIOS = "src/fashionstylefx/usuarios.json";
    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstadisticas();
        cargarGraficoVentas();
        cargarTablaPedidos();
        btnCerrarSesion.setOnAction(event -> cerrarSesion());

        // Eventos de los botones de gestión
        btnGestionarProductos.setOnAction(event -> abrirGestionProductos());
        btnGestionarUsuarios.setOnAction(event -> abrirGestionUsuarios());
        btnGestionarPedidos.setOnAction(event -> abrirGestionPedidos());
    }

    private void cargarTablaPedidos() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipoMapa = new TypeToken<Map<String, List<Compra>>>() {
            }.getType();
            Map<String, List<Compra>> todasLasCompras = gson.fromJson(reader, tipoMapa);
            reader.close();

            List<Compra> todosLosPedidos = new ArrayList<>();

            if (todasLasCompras != null) {
                for (Map.Entry<String, List<Compra>> entry : todasLasCompras.entrySet()) {
                    List<Compra> comprasUsuario = entry.getValue();
                    if (comprasUsuario != null) {
                        todosLosPedidos.addAll(comprasUsuario);
                    }
                }
            }

            // Ordenar por ID descendente (más reciente primero)
            todosLosPedidos.sort((a, b) -> Integer.compare(b.getId(), a.getId()));

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
            colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

            tablaPedidos.getItems().setAll(todosLosPedidos);

        } catch (Exception e) {
            System.out.println("Error al cargar pedidos: " + e.getMessage());
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

    private void cargarEstadisticas() {
        // Productos
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_PRODUCTOS);
            Type tipo = new TypeToken<List<Producto>>() {
            }.getType();
            List<Producto> productos = gson.fromJson(reader, tipo);
            reader.close();
            lblTotalProductos.setText(String.valueOf(productos.size()));
        } catch (Exception e) {
            lblTotalProductos.setText("0");
        }

        // Usuarios
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_USUARIOS);
            Type tipo = new TypeToken<List<Usuario>>() {
            }.getType();
            List<Usuario> usuarios = gson.fromJson(reader, tipo);
            reader.close();
            lblTotalUsuarios.setText(String.valueOf(usuarios.size()));
        } catch (Exception e) {
            lblTotalUsuarios.setText("0");
        }

        // Pedidos y Ventas - LEER DE TODOS LOS USUARIOS (nueva estructura)
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipoMapa = new TypeToken<Map<String, List<Compra>>>() {
            }.getType();
            Map<String, List<Compra>> todasLasCompras = gson.fromJson(reader, tipoMapa);
            reader.close();

            int totalPedidos = 0;
            double totalVentas = 0;

            if (todasLasCompras != null) {
                for (Map.Entry<String, List<Compra>> entry : todasLasCompras.entrySet()) {
                    List<Compra> comprasUsuario = entry.getValue();
                    if (comprasUsuario != null) {
                        totalPedidos += comprasUsuario.size();
                        for (Compra c : comprasUsuario) {
                            totalVentas += c.getTotal();
                        }
                    }
                }
            }

            lblTotalPedidos.setText(String.valueOf(totalPedidos));
            lblTotalVentas.setText("$" + totalVentas);

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

   private void abrirGestionProductos() {
    try {
        Stage stageActual = (Stage) btnGestionarProductos.getScene().getWindow();
        stageActual.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("GestionProductos.fxml"));
        Stage stage = new Stage();
        stage.setTitle("FashionStyle - Gestionar Productos");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (Exception e) {
        lblMensaje.setText("Error al abrir gestión de productos");
    }
}

    private void abrirGestionUsuarios() {
    try {
        // Cerrar Admin Dashboard
        Stage stageActual = (Stage) btnGestionarUsuarios.getScene().getWindow();
        stageActual.close();
        
        // Abrir Gestión de Usuarios
        Parent root = FXMLLoader.load(getClass().getResource("GestionUsuarios.fxml"));
        Stage stage = new Stage();
        stage.setTitle("FashionStyle - Gestionar Usuarios");
        stage.setScene(new Scene(root));
        stage.show();
        
    } catch (Exception e) {
        lblMensaje.setText("Error al abrir gestión de usuarios");
        e.printStackTrace();
    }
}

    private void abrirGestionPedidos() {
    try {
        // Cerrar Admin Dashboard
        Stage stageActual = (Stage) btnGestionarPedidos.getScene().getWindow();
        stageActual.close();
        
        // Abrir Gestión de Pedidos
        Parent root = FXMLLoader.load(getClass().getResource("GestionPedidos.fxml"));
        Stage stage = new Stage();
        stage.setTitle("FashionStyle - Gestionar Pedidos");
        stage.setScene(new Scene(root));
        stage.show();
        
    } catch (Exception e) {
        lblMensaje.setText("Error al abrir gestión de pedidos");
        e.printStackTrace();
    }
}
}
