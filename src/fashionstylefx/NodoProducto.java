/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

/**
 *
 * @author pc
 */
public class NodoProducto {

    private Producto producto;
    private NodoProducto siguiente;
    private NodoProducto anterior;

    public NodoProducto(Producto producto) {
        this.producto = producto;
        this.siguiente = null;
        this.anterior = null;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public NodoProducto getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoProducto siguiente) {
        this.siguiente = siguiente;
    }

    public NodoProducto getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoProducto anterior) {
        this.anterior = anterior;
    }
}
