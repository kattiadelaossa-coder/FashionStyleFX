/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author pc
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnRegistrarse;
    @FXML
    private Label lblMensaje;

    private List<Usuario> listaUsuarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUsuariosDesdeJSON();

        btnIniciarSesion.setOnAction(event -> handleIniciarSesion());
        btnRegistrarse.setOnAction(event -> handleRegistrarse());
    }

    private void cargarUsuariosDesdeJSON() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("src/fashionstylefx/usuarios.json");
            Type tipoLista = new TypeToken<List<Usuario>>() {
            }.getType();
            listaUsuarios = gson.fromJson(reader, tipoLista);
            reader.close();
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar usuarios");
            e.printStackTrace();
        }
    }

    private void handleIniciarSesion() {
        String correo = txtCorreo.getText();
        String password = txtPassword.getText();

        if (correo.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }

        // Buscar usuario en la lista
        for (Usuario u : listaUsuarios) {
            if (u.getCorreo().equals(correo) && u.getPassword().equals(password)) {
                lblMensaje.setText("¡Login exitoso! Bienvenido " + u.getNombre());
                return;
            }
        }

        lblMensaje.setText("Correo o contraseña incorrectos");
    }

    private void handleRegistrarse() {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("Registro.fxml"));
        Stage stage = new Stage();
        stage.setTitle("FashionStyle - Registro");
        stage.setScene(new Scene(root));
        stage.show();
        
        // Cerrar ventana de Login
        btnRegistrarse.getScene().getWindow().hide();
    } catch (Exception e) {
        e.printStackTrace();
        lblMensaje.setText("Error al abrir registro");
    }
}
}
