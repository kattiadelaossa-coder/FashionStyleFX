/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GestionPedidosController implements Initializable {

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
    @FXML
    private TableColumn<Compra, Void> colAcciones;
    @FXML
    private ComboBox<String> cbFiltroEstado;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnLimpiarFiltro;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensaje;

    private ObservableList<Compra> pedidosList;
    private ObservableList<Compra> pedidosOriginal;
    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarPedidos();
        configurarFiltros();
        
        btnFiltrar.setOnAction(event -> filtrarPedidos());
        btnLimpiarFiltro.setOnAction(event -> limpiarFiltro());
        btnVolver.setOnAction(event -> volver());
    }
    
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        // Botón para cambiar estado
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> cbEstado = new ComboBox<>();
            {
                cbEstado.getItems().addAll("Procesando", "En camino", "Entregado");
                cbEstado.setOnAction(event -> {
                    Compra compra = getTableView().getItems().get(getIndex());
                    String nuevoEstado = cbEstado.getValue();
                    if (nuevoEstado != null) {
                        cambiarEstado(compra, nuevoEstado);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Compra compra = getTableView().getItems().get(getIndex());
                    cbEstado.setValue(compra.getEstado() != null ? compra.getEstado() : "Procesando");
                    setGraphic(cbEstado);
                }
            }
        });
    }
    
    private void configurarFiltros() {
        cbFiltroEstado.getItems().addAll("Todos", "Procesando", "En camino", "Entregado");
        cbFiltroEstado.setValue("Todos");
    }
    
    private void cargarPedidos() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipo = new TypeToken<List<Compra>>(){}.getType();
            List<Compra> pedidos = gson.fromJson(reader, tipo);
            reader.close();
            
            if (pedidos == null) pedidos = new java.util.ArrayList<>();
            
            // Asegurar que cada pedido tenga estado
            for (Compra c : pedidos) {
                if (c.getEstado() == null || c.getEstado().isEmpty()) {
                    c.setEstado("Procesando");
                }
            }
            
            pedidosOriginal = FXCollections.observableArrayList(pedidos);
            pedidosList = FXCollections.observableArrayList(pedidos);
            tablaPedidos.setItems(pedidosList);
            
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar pedidos");
        }
    }
    
    private void guardarPedidos() {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(ARCHIVO_COMPRAS);
            gson.toJson(pedidosOriginal, writer);
            writer.close();
        } catch (Exception e) {
            lblMensaje.setText("Error al guardar cambios");
        }
    }
    
    private void cambiarEstado(Compra compra, String nuevoEstado) {
        compra.setEstado(nuevoEstado);
        
        // Actualizar en la lista original
        for (int i = 0; i < pedidosOriginal.size(); i++) {
            if (pedidosOriginal.get(i).getId() == compra.getId()) {
                pedidosOriginal.set(i, compra);
                break;
            }
        }
        
        guardarPedidos();
        
        // Refrescar la vista según el filtro actual
        String filtroActual = cbFiltroEstado.getValue();
        if ("Todos".equals(filtroActual)) {
            refrescarTabla();
        } else {
            filtrarPedidos();
        }
        
        lblMensaje.setText("Pedido #" + compra.getId() + " actualizado a: " + nuevoEstado);
    }
    
    private void filtrarPedidos() {
        String filtro = cbFiltroEstado.getValue();
        if (filtro == null || "Todos".equals(filtro)) {
            limpiarFiltro();
            return;
        }
        
        List<Compra> filtrados = pedidosOriginal.stream()
                .filter(p -> filtro.equals(p.getEstado()))
                .collect(Collectors.toList());
        
        pedidosList.setAll(filtrados);
        tablaPedidos.setItems(pedidosList);
        lblMensaje.setText("Mostrando pedidos con estado: " + filtro);
    }
    
    private void limpiarFiltro() {
        cbFiltroEstado.setValue("Todos");
        pedidosList.setAll(pedidosOriginal);
        tablaPedidos.setItems(pedidosList);
        lblMensaje.setText("Mostrando todos los pedidos");
    }
    
    private void refrescarTabla() {
        pedidosList.setAll(pedidosOriginal);
        tablaPedidos.refresh();
    }
    
    private void volver() {
        btnVolver.getScene().getWindow().hide();
        abrirAdminDashboard();
    }
    
    private void abrirAdminDashboard() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Admin");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            lblMensaje.setText("Error al volver");
        }
    }
}