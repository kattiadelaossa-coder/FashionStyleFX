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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
            Type tipoLista = new TypeToken<List<Usuario>>(){}.getType();
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
        
        for (Usuario u : listaUsuarios) {
            if (u.getCorreo().equals(correo) && u.getPassword().equals(password)) {
                lblMensaje.setText("¡Login exitoso! Bienvenido " + u.getNombre());
                abrirCatalogo();
                return;
            }
        }
        
        lblMensaje.setText("Correo o contraseña incorrectos");
    }
    
    private void handleRegistrarse() {
        abrirRegistro();
    }
    
    private void handleOlvideContrasena() {
        lblMensaje.setText("Función en desarrollo. Contacte al administrador.");
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
            Parent root = FXMLLoader.load(getClass().getResource("Catalogo.fxml"));
            Stage stage = new Stage();
            stage.setTitle("FashionStyle - Catálogo");
            stage.setScene(new Scene(root));
            stage.show();
            
            btnIniciarSesion.getScene().getWindow().hide();
        } catch (Exception e) {
            lblMensaje.setText("Error al abrir catálogo");
        }
    }
}