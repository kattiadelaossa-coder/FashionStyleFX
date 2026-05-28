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

public class GestionProductosController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtCategoria;
    @FXML
    private TextField txtImagen;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnVolver;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, Integer> colId;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private TableColumn<Producto, String> colCategoria;
    @FXML
    private TableColumn<Producto, String> colImagen;
    @FXML
    private TableColumn<Producto, Void> colAcciones;
    @FXML
    private Label lblMensaje;

    private ObservableList<Producto> productosList;
    private final String ARCHIVO_PRODUCTOS = "src/fashionstylefx/productos.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProductos();
        
        btnAgregar.setOnAction(event -> agregarProducto());
        btnActualizar.setOnAction(event -> actualizarProducto());
        btnLimpiar.setOnAction(event -> limpiarCampos());
        btnVolver.setOnAction(event -> volver());
        
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                cargarProductoEnFormulario(selected);
            }
        });
    }
    
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        
        // Botón eliminar en cada fila
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEliminar.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-background-radius: 8;");
                btnEliminar.setOnAction(event -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    eliminarProducto(p);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });
    }
    
    private void cargarProductos() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_PRODUCTOS);
            Type tipo = new TypeToken<List<Producto>>(){}.getType();
            List<Producto> productos = gson.fromJson(reader, tipo);
            reader.close();
            
            productosList = FXCollections.observableArrayList(productos);
            tablaProductos.setItems(productosList);
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar productos");
        }
    }
    
    private void guardarProductos() {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(ARCHIVO_PRODUCTOS);
            gson.toJson(productosList, writer);
            writer.close();
        } catch (Exception e) {
            lblMensaje.setText("Error al guardar productos");
        }
    }
    
    private void agregarProducto() {
        if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            lblMensaje.setText("Complete nombre y precio");
            return;
        }
        
        int nuevoId = productosList.size() + 1;
        String nombre = txtNombre.getText();
        double precio = Double.parseDouble(txtPrecio.getText());
        String categoria = txtCategoria.getText();
        String imagen = txtImagen.getText().isEmpty() ? "producto.png" : txtImagen.getText();
        
        Producto nuevo = new Producto(nuevoId, nombre, precio, categoria, imagen);
        productosList.add(nuevo);
        guardarProductos();
        limpiarCampos();
        lblMensaje.setText("Producto agregado correctamente");
    }
    
    private void actualizarProducto() {
        Producto selected = tablaProductos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMensaje.setText("Seleccione un producto para actualizar");
            return;
        }
        
        selected.setNombre(txtNombre.getText());
        selected.setPrecio(Double.parseDouble(txtPrecio.getText()));
        selected.setCategoria(txtCategoria.getText());
        selected.setImagen(txtImagen.getText());
        
        tablaProductos.refresh();
        guardarProductos();
        limpiarCampos();
        lblMensaje.setText("Producto actualizado correctamente");
    }
    
    private void eliminarProducto(Producto producto) {
        productosList.remove(producto);
        guardarProductos();
        lblMensaje.setText("Producto eliminado correctamente");
    }
    
    private void cargarProductoEnFormulario(Producto p) {
        txtId.setText(String.valueOf(p.getId()));
        txtNombre.setText(p.getNombre());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtCategoria.setText(p.getCategoria());
        txtImagen.setText(p.getImagen());
    }
    
    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtCategoria.clear();
        txtImagen.clear();
        tablaProductos.getSelectionModel().clearSelection();
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