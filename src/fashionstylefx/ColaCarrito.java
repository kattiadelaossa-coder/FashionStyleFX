/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class ColaCarrito {

    static final int CANTIDAD_MAXIMA = 20; // Número máximo de productos en el carrito
    private int totalNodos;
    private NodoCarrito frente;
    private NodoCarrito finalCola;

    public ColaCarrito() {
        totalNodos = 0;
        frente = null;
        finalCola = null;
    }

    public int getTotalNodos() {
        return totalNodos;
    }

    public NodoCarrito getFrente() {
        return frente;
    }

    public NodoCarrito getFinal() {
        return finalCola;
    }

    public boolean colaLlena() {
        return totalNodos >= CANTIDAD_MAXIMA;
    }

    public boolean colaVacia() {
        return totalNodos == 0;
    }

    // Agregar producto al final de la cola
    public void agregar(Producto producto) {
        if (!colaLlena()) {
            NodoCarrito temp = new NodoCarrito();
            temp.setProducto(producto);

            if (getFrente() == null) {
                frente = temp;
            } else {
                getFinal().setSiguiente(temp);
            }
            finalCola = temp;
            totalNodos = totalNodos + 1;
        } else {
            JOptionPane.showMessageDialog(null, "El carrito está lleno, no puede agregar más productos");
        }
    }

    // Eliminar producto del frente de la cola
    public void quitar() {
        NodoCarrito temp;
        if (!colaVacia()) {
            temp = getFrente();
            frente = getFrente().getSiguiente();
            if (getFrente() == null) {
                finalCola = null;
            }
            temp = null;
            totalNodos = totalNodos - 1;
        }
    }

    // Ver el producto del frente sin eliminarlo
    public Producto valorFrente() {
        if (!colaVacia()) {
            return getFrente().getProducto();
        }
        return null;
    }

    // Ver el producto del final sin eliminarlo
    public Producto valorFinal() {
        if (!colaVacia()) {
            return getFinal().getProducto();
        }
        return null;
    }

    // Limpiar toda la cola
    public void limpiarCola() {
        while (!colaVacia()) {
            quitar();
        }
    }

    // Calcular total del carrito
    public double calcularTotal() {
        double total = 0;
        ColaCarrito temp = copiarCola();
        while (!temp.colaVacia()) {
            Producto p = temp.valorFrente();
            total += p.getPrecio() * p.getCantidad();
            temp.quitar();
        }
        return total;
    }

    // Copiar la cola para no perder los datos originales
    private ColaCarrito copiarCola() {
        ColaCarrito copia = new ColaCarrito();
        ColaCarrito aux = new ColaCarrito();

        while (!this.colaVacia()) {
            Producto p = this.valorFrente();
            aux.agregar(p);
            this.quitar();
        }

        while (!aux.colaVacia()) {
            Producto p = aux.valorFrente();
            this.agregar(p);
            copia.agregar(p);
            aux.quitar();
        }

        return copia;
    }

    // Obtener todos los productos (para mostrar en lista)
    public String[] getListaProductos() {
        String[] lista = new String[totalNodos];
        ColaCarrito temp = copiarCola();
        int i = 0;
        while (!temp.colaVacia()) {
            Producto p = temp.valorFrente();
            lista[i] = p.getNombre() + " x" + p.getCantidad() + " = $" + (p.getPrecio() * p.getCantidad());
            temp.quitar();
            i++;
        }
        return lista;
    }

    // Aumentar cantidad de un producto
    public void aumentarCantidad(int idProducto) {
        ColaCarrito temp = new ColaCarrito();
        while (!this.colaVacia()) {
            Producto p = this.valorFrente();
            if (p.getId() == idProducto) {
                p.setCantidad(p.getCantidad() + 1);
            }
            temp.agregar(p);
            this.quitar();
        }
        while (!temp.colaVacia()) {
            this.agregar(temp.valorFrente());
            temp.quitar();
        }
    }

// Disminuir cantidad de un producto
    public void disminuirCantidad(int idProducto) {
        ColaCarrito temp = new ColaCarrito();
        while (!this.colaVacia()) {
            Producto p = this.valorFrente();
            if (p.getId() == idProducto) {
                if (p.getCantidad() > 1) {
                    p.setCantidad(p.getCantidad() - 1);
                    temp.agregar(p);
                }
                // Si cantidad es 1, no se agrega (se elimina)
            } else {
                temp.agregar(p);
            }
            this.quitar();
        }
        while (!temp.colaVacia()) {
            this.agregar(temp.valorFrente());
            temp.quitar();
        }
    }

// Eliminar producto completamente
    public void eliminarProducto(int idProducto) {
        ColaCarrito temp = new ColaCarrito();
        while (!this.colaVacia()) {
            Producto p = this.valorFrente();
            if (p.getId() != idProducto) {
                temp.agregar(p);
            }
            this.quitar();
        }
        while (!temp.colaVacia()) {
            this.agregar(temp.valorFrente());
            temp.quitar();
        }
    }

// Obtener lista de productos para mostrar con cantidad
    public String[] getListaProductosDetalle() {
        String[] lista = new String[totalNodos];
        ColaCarrito temp = copiarCola();
        int i = 0;
        while (!temp.colaVacia()) {
            Producto p = temp.valorFrente();
            lista[i] = p.getId() + "|" + p.getNombre() + "|" + p.getPrecio() + "|" + p.getCantidad();
            temp.quitar();
            i++;
        }
        return lista;
    }
}
