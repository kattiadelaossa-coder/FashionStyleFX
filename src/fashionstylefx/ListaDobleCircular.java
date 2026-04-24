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

    // NUEVO: Obtener el siguiente producto a partir de uno dado
    public Producto getSiguiente(Producto actual) {
        if (cabeza == null || actual == null) {
            return null;
        }

        NodoProducto temp = cabeza;
        do {
            if (temp.getProducto().getId() == actual.getId()) {
                return temp.getSiguiente().getProducto();
            }
            temp = temp.getSiguiente();
        } while (temp != cabeza);

        return null;
    }

    // NUEVO: Obtener el anterior producto a partir de uno dado
    public Producto getAnterior(Producto actual) {
        if (cabeza == null || actual == null) {
            return null;
        }

        NodoProducto temp = cabeza;
        do {
            if (temp.getProducto().getId() == actual.getId()) {
                return temp.getAnterior().getProducto();
            }
            temp = temp.getSiguiente();
        } while (temp != cabeza);

        return null;
    }

    // NUEVO: Eliminar un producto por ID
    public boolean eliminar(int id) {
        if (cabeza == null) {
            return false;
        }

        NodoProducto actual = cabeza;
        do {
            if (actual.getProducto().getId() == id) {
                if (tamaño == 1) {
                    cabeza = null;
                } else {
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                    if (actual == cabeza) {
                        cabeza = actual.getSiguiente();
                    }
                }
                tamaño--;
                return true;
            }
            actual = actual.getSiguiente();
        } while (actual != cabeza);

        return false;
    }
}
