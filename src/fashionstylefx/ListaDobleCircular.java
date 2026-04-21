/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

/**
 *
 * @author pc
 */
public class ListaDobleCircular {

    private NodoProducto cabeza;
    private int tamaño;

    public ListaDobleCircular() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    public void agregar(Producto producto) {
        NodoProducto nuevo = new NodoProducto(producto);

        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.setSiguiente(cabeza);
            cabeza.setAnterior(cabeza);
        } else {
            NodoProducto ultimo = cabeza.getAnterior();
            ultimo.setSiguiente(nuevo);
            nuevo.setAnterior(ultimo);
            nuevo.setSiguiente(cabeza);
            cabeza.setAnterior(nuevo);
        }
        tamaño++;
    }

    public Producto get(int index) {
        if (index < 0 || index >= tamaño) {
            return null;
        }

        NodoProducto actual = cabeza;
        for (int i = 0; i < index; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getProducto();
    }

    public int getTamaño() {
        return tamaño;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }
}
