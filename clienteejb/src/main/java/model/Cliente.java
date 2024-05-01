package model;

public class Cliente {

    private int codigo;

    private String dni;

    private String nombre;

    private String direccion;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Cliente [codigo=" + codigo + ", dni=" + dni + ", nombre=" + nombre + ", direccion=" + direccion + "]";
    }

}
