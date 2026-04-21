/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar eventos
        btnIniciarSesion.setOnAction(event -> handleIniciarSesion());
        btnRegistrarse.setOnAction(event -> handleRegistrarse());
    }

    private void handleIniciarSesion() {
        String correo = txtCorreo.getText();
        String password = txtPassword.getText();

        if (correo.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }

        // Validación temporal (después vendrá desde JSON)
        if (correo.equals("admin@fashionstyle.com") && password.equals("admin123")) {
            lblMensaje.setText("¡Login exitoso!");
        } else {
            lblMensaje.setText("Correo o contraseña incorrectos");
        }
    }

    private void handleRegistrarse() {
        lblMensaje.setText("Abrir pantalla de registro");
    }
}
