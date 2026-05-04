/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author pc
 */

public class RegistroController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Button btnRegistrarse;
    @FXML
    private Label btnVolverLogin;  
    @FXML
    private Label lblMensaje;

    private List<Usuario> listaUsuarios;
    private final String ARCHIVO_USUARIOS = "src/fashionstylefx/usuarios.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUsuarios();

        btnRegistrarse.setOnAction(event -> handleRegistrarse());
        btnVolverLogin.setOnMouseClicked(event -> handleVolverLogin());
    }

    private void cargarUsuarios() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_USUARIOS);
            Type tipoLista = new TypeToken<List<Usuario>>() {
            }.getType();
            listaUsuarios = gson.fromJson(reader, tipoLista);
            reader.close();

            if (listaUsuarios == null) {
                listaUsuarios = new ArrayList<>();
            }
        } catch (Exception e) {
            listaUsuarios = new ArrayList<>();
        }
    }

    private void guardarUsuarios() {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(ARCHIVO_USUARIOS);
            gson.toJson(listaUsuarios, writer);
            writer.close();
        } catch (Exception e) {
            lblMensaje.setText("Error al guardar usuario");
        }
    }

    private void handleRegistrarse() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Validar campos vacíos
        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            lblMensaje.setText("Las contraseñas no coinciden");
            return;
        }

        // Validar contraseña mínima
        if (password.length() < 6) {
            lblMensaje.setText("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        // Validar que el correo no esté registrado
        for (Usuario u : listaUsuarios) {
            if (u.getCorreo().equals(correo)) {
                lblMensaje.setText("El correo ya está registrado");
                return;
            }
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario(correo, password, nombre, "Cliente");
        listaUsuarios.add(nuevoUsuario);
        guardarUsuarios();

        lblMensaje.setText("¡Registro exitoso! Ahora puedes iniciar sesión");

        // Limpiar campos
        limpiarCampos();
    }

    private void handleVolverLogin() {
        // Cerrar esta ventana
        btnVolverLogin.getScene().getWindow().hide();
        // Abrir Login nuevamente
        abrirLogin();
    }

    private void abrirLogin() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Login");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
