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
    private String cliente;
    private String estado;
    private List<Producto> productos;

    public Compra() {
        this.productos = new ArrayList<>();
    }

    public Compra(int id, double total, List<Producto> productos, String cliente) {
        this.id = id;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.total = total;
        this.productos = productos;
        this.cliente = cliente;
        this.estado = "Procesando";
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getResumenProductos() {
        StringBuilder sb = new StringBuilder();
        for (Producto p : productos) {
            sb.append(p.getNombre()).append(" x").append(p.getCantidad()).append(", ");
        }
        String resumen = sb.toString();
        return resumen.length() > 0 ? resumen.substring(0, resumen.length() - 2) : "";
    }
}
