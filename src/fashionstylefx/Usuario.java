/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

public class Usuario {

    private String correo;
    private String password;
    private String nombre;
    private String rol;
    private String telefono;  // ← NUEVO CAMPO

    public Usuario() {
    }

    public Usuario(String correo, String password, String nombre, String rol) {
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.telefono = "";
    }

    // Constructor completo con teléfono
    public Usuario(String correo, String password, String nombre, String rol, String telefono) {
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.telefono = telefono;
    }

    // Getters y Setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }  // ← NUEVO

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }  // ← NUEVO
}
