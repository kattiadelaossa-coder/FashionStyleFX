/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;
/**
 *
 * @author pc
 */
public class NodoCarrito {

    private Producto producto;
    private NodoCarrito siguiente;

    public NodoCarrito() {
        this.producto = null;
        this.siguiente = null;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setSiguiente(NodoCarrito sig) {
        this.siguiente = sig;
    }

    public Producto getProducto() {
        return producto;
    }

    public NodoCarrito getSiguiente() {
        return siguiente;
    }
}
