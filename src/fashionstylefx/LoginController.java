/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Label lblRegistrarse;
    @FXML
    private Label lblOlvideContrasena;
    @FXML
    private Label lblMensaje;

    private List<Usuario> listaUsuarios;
    private final String ARCHIVO_USUARIOS = "src/fashionstylefx/usuarios.json";

    // Variable estática para almacenar el usuario actual
    private static Usuario usuarioActual;

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUsuarios();

        btnIniciarSesion.setOnAction(event -> handleIniciarSesion());
        lblRegistrarse.setOnMouseClicked(event -> handleRegistrarse());
        lblOlvideContrasena.setOnMouseClicked(event -> handleOlvideContrasena());
    }

    private void cargarUsuarios() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_USUARIOS);
            Type tipoLista = new TypeToken<List<Usuario>>() {
            }.getType();
            listaUsuarios = gson.fromJson(reader, tipoLista);
            reader.close();
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar usuarios");
        }
    }

    private void handleIniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String password = txtPassword.getText();

        if (correo.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }

        // Buscar si el usuario existe
        Usuario usuarioEncontrado = null;
        for (Usuario u : listaUsuarios) {
            if (u.getCorreo().equals(correo)) {
                usuarioEncontrado = u;
                break;
            }
        }

        // Si el usuario no existe
        if (usuarioEncontrado == null) {
            lblMensaje.setText("El usuario no existe. Por favor, regístrese.");
            return;
        }

        // Si la contraseña es incorrecta
        if (!usuarioEncontrado.getPassword().equals(password)) {
            lblMensaje.setText("Contraseña incorrecta. Intente nuevamente.");
            return;
        }

        // Login exitoso
        lblMensaje.setText("¡Login exitoso! Bienvenido " + usuarioEncontrado.getNombre());
        usuarioActual = usuarioEncontrado;

        if (usuarioEncontrado.getRol() != null && usuarioEncontrado.getRol().equals("Admin")) {
            abrirAdminDashboard();
        } else {
            abrirCatalogo();
        }
    }

    private void handleRegistrarse() {
        abrirRegistro();
    }

    private void handleOlvideContrasena() {
    // Crear un diálogo personalizado
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Recuperar contraseña");
    dialog.setHeaderText("¿Olvidaste tu contraseña?");
    dialog.setContentText("Ingresa tu correo electrónico:");
    
    Optional<String> result = dialog.showAndWait();
    
    result.ifPresent(correo -> {
        // Buscar el usuario por correo
        Usuario usuario = buscarUsuarioPorCorreo(correo);
        
        if (usuario != null) {
            // Mostrar la contraseña (o enviar por correo)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Recuperar contraseña");
            alert.setHeaderText("Contraseña encontrada");
            alert.setContentText("La contraseña para " + correo + " es: " + usuario.getPassword());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Usuario no encontrado");
            alert.setContentText("No existe una cuenta asociada a: " + correo);
            alert.showAndWait();
        }
    });
}

private Usuario buscarUsuarioPorCorreo(String correo) {
    for (Usuario u : listaUsuarios) {
        if (u.getCorreo().equals(correo)) {
            return u;
        }
    }
    return null;
}
    private void abrirRegistro() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Registro.fxml"));
            Stage stage = new Stage();
            stage.setTitle("FashionStyle - Registro");
            stage.setScene(new Scene(root));
            stage.show();

            btnIniciarSesion.getScene().getWindow().hide();
        } catch (Exception e) {
            lblMensaje.setText("Error al abrir registro");
        }
    }

    private void abrirCatalogo() {
        try {
            URL url = getClass().getResource("/fashionstylefx/Catalogo.fxml");
            if (url == null) {
                url = getClass().getResource("Catalogo.fxml");
            }

            if (url == null) {
                lblMensaje.setText("Error: No se encuentra Catalogo.fxml");
                return;
            }

            Parent root = FXMLLoader.load(url);
            Stage stage = new Stage();
            stage.setTitle("FashionStyle - Catálogo");
            stage.setScene(new Scene(root));
            stage.show();

            btnIniciarSesion.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir catálogo: " + e.getMessage());
        }
    }

    private void abrirAdminDashboard() {
        try {
            System.out.println("=== ABRIENDO ADMIN DASHBOARD ===");
            Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
            Stage stage = new Stage();
            stage.setTitle("FashionStyle - Admin Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
            btnIniciarSesion.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir admin dashboard: " + e.getMessage());
        }
    }
}
