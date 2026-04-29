/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author pc
 */
public class HistorialController implements Initializable {

    @FXML
    private ListView<String> listaHistorial;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensaje;

    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarHistorial();
        btnVolver.setOnAction(event -> volver());
    }

    private void cargarHistorial() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipoLista = new TypeToken<List<Compra>>() {
            }.getType();
            List<Compra> compras = gson.fromJson(reader, tipoLista);
            reader.close();

            if (compras == null || compras.isEmpty()) {
                lblMensaje.setText("No hay compras registradas");
                return;
            }

            ObservableList<String> items = FXCollections.observableArrayList();
            for (Compra c : compras) {
                String texto = "Pedido #" + c.getId() + " | " + c.getFecha() + " | Total: $" + c.getTotal();
                items.add(texto);
                // Productos (como segundo elemento o tooltip)
                items.add("  └ " + c.getResumenProductos());
            }

            listaHistorial.setItems(items);
            lblMensaje.setText("Total de compras: " + compras.size());

        } catch (Exception e) {
            lblMensaje.setText("Error al cargar historial");
            e.printStackTrace();
        }
    }

    private void volver() {
        btnVolver.getScene().getWindow().hide();
        abrirCatalogo();
    }

    private void abrirCatalogo() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Catalogo.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("FashionStyle - Catálogo");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
