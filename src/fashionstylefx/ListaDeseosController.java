/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListaDeseosController implements Initializable {

    @FXML
    private GridPane gridDeseos;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensaje;

    private List<Producto> listaDeseos;
    private final String ARCHIVO_DESEOS = "src/fashionstylefx/deseos.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarListaDeseos();
        mostrarProductos();
        btnVolver.setOnAction(event -> volver());
    }

    private void cargarListaDeseos() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_DESEOS);
            Type tipoLista = new TypeToken<List<Producto>>() {
            }.getType();
            listaDeseos = gson.fromJson(reader, tipoLista);
            reader.close();
            if (listaDeseos == null) {
                listaDeseos = new ArrayList<>();
            }
        } catch (Exception e) {
            listaDeseos = new ArrayList<>();
            System.out.println("Error al cargar deseos: " + e.getMessage());
        }
    }

    private void guardarDeseos() {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(ARCHIVO_DESEOS);
            gson.toJson(listaDeseos, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error al guardar deseos: " + e.getMessage());
        }
    }

    private void mostrarProductos() {
        gridDeseos.getChildren().clear();

        int columna = 0;
        int fila = 0;

        for (Producto p : listaDeseos) {
            VBox tarjeta = crearTarjetaProducto(p);
            gridDeseos.add(tarjeta, columna, fila);

            columna++;
            if (columna > 3) {
                columna = 0;
                fila++;
            }
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("-fx-padding: 12; -fx-border-color: #E0E0E0; -fx-border-radius: 12; -fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        tarjeta.setPrefWidth(200);
        tarjeta.setAlignment(Pos.CENTER);

        Label lblNombre = new Label(producto.getNombre());
        lblNombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");
        lblNombre.setPrefWidth(180);

        Label lblPrecio = new Label("$" + producto.getPrecio());
        lblPrecio.setStyle("-fx-text-fill: #1E88E5; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnAgregar = new Button("🛒 Agregar al carrito");
        btnAgregar.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        btnAgregar.setOnAction(e -> agregarAlCarrito(producto));

        Button btnEliminar = new Button("❤️ Eliminar");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-border-color: #E53935; -fx-border-radius: 8; -fx-text-fill: #E53935; -fx-cursor: hand;");
        btnEliminar.setOnAction(e -> eliminarDeDeseos(producto));

        HBox botones = new HBox(10, btnAgregar, btnEliminar);
        botones.setAlignment(Pos.CENTER);

        tarjeta.getChildren().addAll(lblNombre, lblPrecio, botones);

        return tarjeta;
    }

    private void agregarAlCarrito(Producto producto) {
        ColaCarrito carrito = CatalogoController.getCarrito();

        boolean encontrado = false;
        ColaCarrito temp = new ColaCarrito();

        while (!carrito.colaVacia()) {
            Producto p = carrito.valorFrente();
            if (p.getId() == producto.getId()) {
                p.setCantidad(p.getCantidad() + 1);
                encontrado = true;
            }
            temp.agregar(p);
            carrito.quitar();
        }

        while (!temp.colaVacia()) {
            carrito.agregar(temp.valorFrente());
            temp.quitar();
        }

        if (!encontrado) {
            Producto nuevo = new Producto(producto.getId(), producto.getNombre(),
                    producto.getPrecio(), producto.getCategoria(),
                    producto.getImagen());
            nuevo.setCantidad(1);
            carrito.agregar(nuevo);
        }

        lblMensaje.setText("✓ " + producto.getNombre() + " agregado al carrito");
    }

    private void eliminarDeDeseos(Producto producto) {
        listaDeseos.remove(producto);
        guardarDeseos();
        mostrarProductos();
        lblMensaje.setText("✗ " + producto.getNombre() + " eliminado de la lista de deseos");
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
