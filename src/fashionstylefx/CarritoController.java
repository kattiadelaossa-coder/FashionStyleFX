/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CarritoController implements Initializable {

    @FXML
    private VBox contenedorProductos;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblEnvio;
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
    private final double COSTO_ENVIO = 10000;
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
        contenedorProductos.getChildren().clear();
        
        double subtotal = 0;
        
        ColaCarrito temp = new ColaCarrito();
        while (!carrito.colaVacia()) {
            Producto p = carrito.valorFrente();
            subtotal += p.getPrecio() * p.getCantidad();
            
            HBox fila = crearFilaProducto(p);
            contenedorProductos.getChildren().add(fila);
            
            temp.agregar(p);
            carrito.quitar();
        }
        
        while (!temp.colaVacia()) {
            carrito.agregar(temp.valorFrente());
            temp.quitar();
        }
        
        double total = subtotal + COSTO_ENVIO;
        
        lblSubtotal.setText("$" + subtotal);
        lblEnvio.setText("$" + COSTO_ENVIO);
        lblTotal.setText("$" + total);
    }
    
    private HBox crearFilaProducto(Producto p) {
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 10;");
        
        Label lblInfo = new Label(p.getNombre());
        lblInfo.setPrefWidth(280);
        
        Label lblPrecio = new Label("$" + p.getPrecio());
        lblPrecio.setPrefWidth(100);
        
        HBox cantidadControl = new HBox(5);
        Button btnMenos = new Button("-");
        Label lblCantidad = new Label(String.valueOf(p.getCantidad()));
        Button btnMas = new Button("+");
        cantidadControl.getChildren().addAll(btnMenos, lblCantidad, btnMas);
        cantidadControl.setPrefWidth(120);
        
        double subtotal = p.getPrecio() * p.getCantidad();
        Label lblSubtotalProducto = new Label("$" + subtotal);
        lblSubtotalProducto.setPrefWidth(120);
        
        Button btnEliminar = new Button("X");
        
        int id = p.getId();
        btnMenos.setOnAction(e -> {
            carrito.disminuirCantidad(id);
            actualizarVista();
        });
        btnMas.setOnAction(e -> {
            carrito.aumentarCantidad(id);
            actualizarVista();
        });
        btnEliminar.setOnAction(e -> {
            carrito.eliminarProducto(id);
            actualizarVista();
        });
        
        fila.getChildren().addAll(lblInfo, lblPrecio, cantidadControl, lblSubtotalProducto, btnEliminar);
        
        return fila;
    }

    private void handleComprar() {
        if (carrito.colaVacia()) {
            lblMensaje.setText("El carrito esta vacio");
            return;
        }
        
        // Preparar lista de productos desde el carrito
        List<Producto> productosComprados = new ArrayList<>();
        while (!carrito.colaVacia()) {
            productosComprados.add(carrito.valorFrente());
            carrito.quitar();
        }
        
        // Guardar compra
        guardarCompra(productosComprados);
        
        double total = carrito.calcularTotal() + COSTO_ENVIO;
        lblMensaje.setText("Compra realizada con exito! Total: $" + total);
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
            stage.setTitle("FashionStyle - Catalogo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarUltimoId() {
        try {
            File archivo = new File(ARCHIVO_COMPRAS);
            if (archivo.exists()) {
                Gson gson = new Gson();
                FileReader reader = new FileReader(ARCHIVO_COMPRAS);
                Type tipoLista = new TypeToken<List<Compra>>() {}.getType();
                List<Compra> compras = gson.fromJson(reader, tipoLista);
                reader.close();
                if (compras != null && !compras.isEmpty()) {
                    ultimoId = compras.get(compras.size() - 1).getId();
                }
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

            // Leer compras existentes
            File archivo = new File(ARCHIVO_COMPRAS);
            if (archivo.exists()) {
                try {
                    FileReader reader = new FileReader(ARCHIVO_COMPRAS);
                    Type tipoLista = new TypeToken<List<Compra>>() {}.getType();
                    compras = gson.fromJson(reader, tipoLista);
                    reader.close();
                    if (compras == null) compras = new ArrayList<>();
                    if (!compras.isEmpty()) {
                        ultimoId = compras.get(compras.size() - 1).getId();
                    }
                } catch (Exception e) {
                    compras = new ArrayList<>();
                }
            }

            ultimoId++;
            
            // Obtener el nombre del cliente logueado
            String nombreCliente = "Cliente";
            if (LoginController.getUsuarioActual() != null) {
                nombreCliente = LoginController.getUsuarioActual().getNombre();
            }
            
            Compra nuevaCompra = new Compra(ultimoId, total, productos, nombreCliente);
            compras.add(nuevaCompra);

            FileWriter writer = new FileWriter(ARCHIVO_COMPRAS);
            gson.toJson(compras, writer);
            writer.close();
            
            System.out.println("Compra guardada con ID: " + ultimoId + " Cliente: " + nombreCliente);

        } catch (Exception e) {
            lblMensaje.setText("Error al guardar compra");
            e.printStackTrace();
        }
    }
}