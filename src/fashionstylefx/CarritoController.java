/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CarritoController implements Initializable {

    @FXML
    private ListView<HBox> listaCarrito;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnComprar;
    @FXML
    private Button btnSeguirComprando;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Label lblMensaje;

    private ColaCarrito carrito;
    private final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";
    private int ultimoId = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carrito = CatalogoController.getCarrito();
        cargarUltimoId();
        actualizarVista();

        btnComprar.setOnAction(event -> handleComprar());
        btnSeguirComprando.setOnAction(event -> handleSeguirComprando());
        btnLimpiar.setOnAction(event -> handleLimpiar());
    }

    private void actualizarVista() {
        listaCarrito.getItems().clear();

        ColaCarrito temp = new ColaCarrito();

        while (!carrito.colaVacia()) {
            Producto p = carrito.valorFrente();

            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPrefHeight(40);

            Label lblInfo = new Label(p.getNombre() + " - $" + p.getPrecio());
            lblInfo.setPrefWidth(180);

            Button btnMenos = new Button("-");
            btnMenos.setStyle("-fx-background-color: #E0E0E0; -fx-min-width: 30;");

            Label lblCant = new Label(String.valueOf(p.getCantidad()));
            lblCant.setPrefWidth(30);
            lblCant.setAlignment(Pos.CENTER);

            Button btnMas = new Button("+");
            btnMas.setStyle("-fx-background-color: #E0E0E0; -fx-min-width: 30;");

            double subtotal = p.getPrecio() * p.getCantidad();
            Label lblSubtotal = new Label("$" + subtotal);
            lblSubtotal.setPrefWidth(80);
            lblSubtotal.setStyle("-fx-text-fill: #1E88E5; -fx-font-weight: bold;");

            Button btnElim = new Button("🗑️");
            btnElim.setStyle("-fx-background-color: transparent; -fx-text-fill: #E53935;");

            int id = p.getId();
            btnMenos.setOnAction(e -> {
                carrito.disminuirCantidad(id);
                actualizarVista();
            });

            btnMas.setOnAction(e -> {
                carrito.aumentarCantidad(id);
                actualizarVista();
            });

            btnElim.setOnAction(e -> {
                carrito.eliminarProducto(id);
                actualizarVista();
            });

            hbox.getChildren().addAll(lblInfo, btnMenos, lblCant, btnMas, lblSubtotal, btnElim);
            HBox.setHgrow(lblInfo, Priority.ALWAYS);

            listaCarrito.getItems().add(hbox);

            temp.agregar(p);
            carrito.quitar();
        }

        while (!temp.colaVacia()) {
            carrito.agregar(temp.valorFrente());
            temp.quitar();
        }

        lblTotal.setText("Total: $" + carrito.calcularTotal());
    }

    private void handleComprar() {
        if (carrito.colaVacia()) {
            lblMensaje.setText("El carrito está vacío");
            return;
        }

        List<Producto> productosComprados = new ArrayList<>();
        while (!carrito.colaVacia()) {
            productosComprados.add(carrito.valorFrente());
            carrito.quitar();
        }

        guardarCompra(productosComprados);
        lblMensaje.setText("¡Compra realizada con éxito!");
        actualizarVista();
    }

    private void handleSeguirComprando() {
        volverCatalogo();
    }

    private void handleLimpiar() {
        carrito.limpiarCola();
        actualizarVista();
        lblMensaje.setText("Carrito limpiado");
    }

    private void volverCatalogo() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Catalogo.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnSeguirComprando.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("FashionStyle - Catálogo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarUltimoId() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipoLista = new TypeToken<List<Compra>>() {
            }.getType();
            List<Compra> compras = gson.fromJson(reader, tipoLista);
            reader.close();

            if (compras != null && !compras.isEmpty()) {
                ultimoId = compras.get(compras.size() - 1).getId();
            }
        } catch (Exception e) {
            ultimoId = 0;
        }
    }

    private void guardarCompra(List<Producto> productos) {
        try {
            double total = 0;
            for (Producto p : productos) {
                total += p.getPrecio() * p.getCantidad();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Compra> compras = new ArrayList<>();

            try {
                FileReader reader = new FileReader(ARCHIVO_COMPRAS);
                Type tipoLista = new TypeToken<List<Compra>>() {
                }.getType();
                compras = gson.fromJson(reader, tipoLista);
                reader.close();
                if (compras == null) {
                    compras = new ArrayList<>();
                }
                if (!compras.isEmpty()) {
                    ultimoId = compras.get(compras.size() - 1).getId();
                }
            } catch (Exception e) {
                compras = new ArrayList<>();
            }

            ultimoId++;
            Compra nuevaCompra = new Compra(ultimoId, total, productos);
            compras.add(nuevaCompra);

            FileWriter writer = new FileWriter(ARCHIVO_COMPRAS);
            gson.toJson(compras, writer);
            writer.close();

        } catch (Exception e) {
            lblMensaje.setText("Error al guardar compra");
            e.printStackTrace();
        }
    }
}
