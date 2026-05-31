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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestionUsuariosController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtRol;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnVolver;
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableColumn<Usuario, String> colCorreo;
    @FXML
    private TableColumn<Usuario, String> colTelefono;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private TableColumn<Usuario, Void> colAcciones;
    @FXML
    private Label lblMensaje;

    private ObservableList<Usuario> usuariosList;
    private final String ARCHIVO_USUARIOS = "src/fashionstylefx/usuarios.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarUsuarios();
        
        btnAgregar.setOnAction(event -> agregarUsuario());
        btnActualizar.setOnAction(event -> actualizarUsuario());
        btnLimpiar.setOnAction(event -> limpiarCampos());
        btnVolver.setOnAction(event -> volver());
        
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                cargarUsuarioEnFormulario(selected);
            }
        });
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEliminar.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-background-radius: 8;");
                btnEliminar.setOnAction(event -> {
                    Usuario u = getTableView().getItems().get(getIndex());
                    eliminarUsuario(u);
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
    
    private void cargarUsuarios() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_USUARIOS);
            Type tipo = new TypeToken<List<Usuario>>(){}.getType();
            List<Usuario> usuarios = gson.fromJson(reader, tipo);
            reader.close();
            
            usuariosList = FXCollections.observableArrayList(usuarios);
            tablaUsuarios.setItems(usuariosList);
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar usuarios");
        }
    }
    
    private void guardarUsuarios() {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(ARCHIVO_USUARIOS);
            gson.toJson(usuariosList, writer);
            writer.close();
        } catch (Exception e) {
            lblMensaje.setText("Error al guardar usuarios");
        }
    }
    
    private void agregarUsuario() {
        if (txtNombre.getText().isEmpty() || txtCorreo.getText().isEmpty()) {
            lblMensaje.setText("Complete nombre y correo");
            return;
        }
        
        // Verificar si el correo ya existe
        for (Usuario u : usuariosList) {
            if (u.getCorreo().equals(txtCorreo.getText())) {
                lblMensaje.setText("El correo ya está registrado");
                return;
            }
        }
        
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String telefono = txtTelefono.getText();
        String rol = txtRol.getText().isEmpty() ? "Cliente" : txtRol.getText();
        String password = "123456"; // Contraseña por defecto
        
        Usuario nuevo = new Usuario(correo, password, nombre, rol, telefono);
        usuariosList.add(nuevo);
        guardarUsuarios();
        limpiarCampos();
        lblMensaje.setText("Usuario agregado correctamente");
    }
    
    private void actualizarUsuario() {
        Usuario selected = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMensaje.setText("Seleccione un usuario para actualizar");
            return;
        }
        
        selected.setNombre(txtNombre.getText());
        selected.setCorreo(txtCorreo.getText());
        selected.setTelefono(txtTelefono.getText());
        selected.setRol(txtRol.getText());
        
        tablaUsuarios.refresh();
        guardarUsuarios();
        limpiarCampos();
        lblMensaje.setText("Usuario actualizado correctamente");
    }
    
    private void eliminarUsuario(Usuario usuario) {
        usuariosList.remove(usuario);
        guardarUsuarios();
        lblMensaje.setText("Usuario eliminado correctamente");
    }
    
    private void cargarUsuarioEnFormulario(Usuario u) {
        txtNombre.setText(u.getNombre());
        txtCorreo.setText(u.getCorreo());
        txtTelefono.setText(u.getTelefono());
        txtRol.setText(u.getRol());
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtRol.clear();
        tablaUsuarios.getSelectionModel().clearSelection();
    }
    
   private void volver() {
    try {
        // Cerrar ventana actual de Gestión de Usuarios
        Stage stageActual = (Stage) btnVolver.getScene().getWindow();
        stageActual.close();
        
        // Abrir Admin Dashboard
        Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
        Stage stage = new Stage();
        stage.setTitle("FashionStyle - Admin");
        stage.setScene(new Scene(root));
        stage.show();
        
    } catch (Exception e) {
        lblMensaje.setText("Error al volver");
        e.printStackTrace();
    }
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