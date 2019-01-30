package Clientes;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nif, nombre, telefono, direccion;
    private double deuda;

    public Cliente(String nif, String nombre, String telefono, String direccion, double deuda) {
        this.nif = nif;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.deuda = deuda;
    }

    public String getNif() {
        return nif;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getDeuda() {
        return deuda;
    }
    public String toString(){
        return "NIF: " + nif +  " Nombre: " + nombre + " Teléfono: " + telefono + " Dirección: " + direccion + " Deuda: " + deuda;
    }
}
