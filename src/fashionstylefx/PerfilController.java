/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fashionstylefx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class PerfilController implements Initializable {

    @FXML
    private Label lblNombre;
    @FXML
    private Label lblCorreo;
    @FXML
    private Label lblTelefono;
    @FXML
    private Label lblMiembroDesde;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Label lblMisCompras;
    @FXML
    private Label lblListaDeseos;
    @FXML
    private Label lblMensaje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUsuarioActual();

        btnEditar.setOnAction(event -> editarPerfil());
        btnCerrarSesion.setOnAction(event -> cerrarSesion());
        lblMisCompras.setOnMouseClicked(event -> abrirHistorial());
        lblListaDeseos.setOnMouseClicked(event -> abrirListaDeseos());
    }

    private void cargarUsuarioActual() {
        Usuario usuarioActual = LoginController.getUsuarioActual();

        if (usuarioActual != null) {
            lblNombre.setText(usuarioActual.getNombre());
            lblCorreo.setText(usuarioActual.getCorreo());
            // Mostrar el teléfono del usuario
            String telefono = usuarioActual.getTelefono();
            if (telefono != null && !telefono.isEmpty()) {
                lblTelefono.setText("Teléfono: " + telefono);
            } else {
                lblTelefono.setText("Teléfono: No registrado");
            }
            lblMiembroDesde.setText("Miembro desde: 20/05/2026");
        } else {
            lblNombre.setText("Usuario no encontrado");
            lblCorreo.setText("Inicie sesión nuevamente");
            lblTelefono.setText("Teléfono: ---");
            lblMiembroDesde.setText("Miembro desde: ---");
        }
    }

    private void editarPerfil() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("EditarPerfil.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Editar Perfil");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();  // Espera a que se cierre para actualizar
            cargarUsuarioActual(); // Recargar datos después de editar
        } catch (Exception e) {
            lblMensaje.setText("Error al abrir editar perfil");
        }
    }

    private void cerrarSesion() {
        try {
            // Cerrar ventana de Perfil
            Stage stagePerfil = (Stage) btnCerrarSesion.getScene().getWindow();
            stagePerfil.close();

            // Buscar y cerrar la ventana del Catálogo
            for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                if (window instanceof Stage) {
                    Stage stage = (Stage) window;
                    if (stage.getTitle() != null && stage.getTitle().equals("FashionStyle - Catálogo")) {
                        stage.close();
                        break;
                    }
                }
            }

            // Abrir Login
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("FashionStyle - Login");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cerrar sesión");
        }
    }

    private void abrirHistorial() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Historial.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("FashionStyle - Historial");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            lblMensaje.setText("Error al abrir historial");
        }
    }

    private void abrirListaDeseos() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("ListaDeseos.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Lista de Deseos");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir lista de deseos");
        }
    }

}
