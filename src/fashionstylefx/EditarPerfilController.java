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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditarPerfilController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblMensaje;

    private Usuario usuarioActual;
    private final String ARCHIVO_USUARIOS = "src/fashionstylefx/usuarios.json";
    private List<Usuario> listaUsuarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioActual = LoginController.getUsuarioActual();
        cargarUsuarios();
        cargarDatosActuales();
        
        btnGuardar.setOnAction(event -> guardarCambios());
        btnCancelar.setOnAction(event -> cerrar());
    }
    
    private void cargarUsuarios() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_USUARIOS);
            Type tipo = new TypeToken<List<Usuario>>(){}.getType();
            listaUsuarios = gson.fromJson(reader, tipo);
            reader.close();
        } catch (Exception e) {
            listaUsuarios = null;
        }
    }
    
    private void cargarDatosActuales() {
        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre());
            String telefono = usuarioActual.getTelefono();
            if (telefono != null && !telefono.isEmpty()) {
                txtTelefono.setText(telefono);
            }
        }
    }
    
    private void guardarCambios() {
        String nuevoNombre = txtNombre.getText().trim();
        String nuevoTelefono = txtTelefono.getText().trim();
        String nuevaPassword = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        
        if (nuevoNombre.isEmpty()) {
            lblMensaje.setText("El nombre no puede estar vacío");
            return;
        }
        
        // Validar contraseña si se ingresó
        if (!nuevaPassword.isEmpty()) {
            if (nuevaPassword.length() < 6) {
                lblMensaje.setText("La contraseña debe tener al menos 6 caracteres");
                return;
            }
            if (!nuevaPassword.equals(confirmPassword)) {
                lblMensaje.setText("Las contraseñas no coinciden");
                return;
            }
        }
        
        // Actualizar en la lista
        for (Usuario u : listaUsuarios) {
            if (u.getCorreo().equals(usuarioActual.getCorreo())) {
                u.setNombre(nuevoNombre);
                u.setTelefono(nuevoTelefono);
                if (!nuevaPassword.isEmpty()) {
                    u.setPassword(nuevaPassword);
                }
                break;
            }
        }
        
        // Guardar en archivo
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(ARCHIVO_USUARIOS);
            gson.toJson(listaUsuarios, writer);
            writer.close();
            
            // Actualizar el usuario actual
            usuarioActual.setNombre(nuevoNombre);
            usuarioActual.setTelefono(nuevoTelefono);
            if (!nuevaPassword.isEmpty()) {
                usuarioActual.setPassword(nuevaPassword);
            }
            
            lblMensaje.setText("¡Perfil actualizado correctamente!");
            
            // Cerrar ventana después de 1 segundo
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() -> cerrar());
                } catch (InterruptedException e) {}
            }).start();
            
        } catch (Exception e) {
            lblMensaje.setText("Error al guardar cambios");
        }
    }
    
    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}