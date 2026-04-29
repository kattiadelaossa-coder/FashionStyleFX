/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pc
 */
public class Compra {

    private int id;
    private String fecha;
    private double total;
    private List<Producto> productos;

    public Compra() {
        this.productos = new ArrayList<>();
    }

    public Compra(int id, double total, List<Producto> productos) {
        this.id = id;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.total = total;
        this.productos = productos;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    // Método para obtener resumen de productos
    public String getResumenProductos() {
        StringBuilder sb = new StringBuilder();
        for (Producto p : productos) {
            sb.append(p.getNombre()).append(" x").append(p.getCantidad()).append(", ");
        }
        String resumen = sb.toString();
        return resumen.length() > 0 ? resumen.substring(0, resumen.length() - 2) : "";
    }
}
