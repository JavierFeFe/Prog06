# Tarea para PROG06.
## Detalles de la tarea de esta unidad.
### Enunciado.
Se trata de hacer una aplicación en Java que gestione los clientes de una empresa. Esos datos, se almacenarán en un fichero serializado, denominado clientes.dat.

Los datos que se almacenarán sobre cada cliente son:

* NIF.
* Nombre.
* Teléfono.
* Dirección.
* Deuda. 
 
Mediante un menú se podrán realizar determinadas operaciones:

* Añadir cliente. Esta opción pedirá los datos del cliente y añadirá el registro correspondiente en el fichero.
* Listar clientes. Recorrerá el fichero mostrando los clientes almacenados en el mismo.
* Buscar clientes. Pedirá al usuario el nif del cliente a buscar, y comprobará si existe en el fichero.
* Borrar cliente. Pedirá al usuario el nif del cliente a borrar, y si existe, lo borrará del fichero.
* Borrar fichero de clientes completamente. Elimina del disco el fichero clientes.dat
* Salir de la aplicación.

Elabora el programa y un documento con un procesador de texto. El documento debe ser de tipo ".doc" (Microsoft Word) o de tipo ".odt" (OpenOffice.org). Debe tener tamaño de página A4, estilo de letra Times New Roman, tamaño 12 e interlineado normal.

En el documento escribirás un informe sobre todas las consideraciones oportunas que se necesiten para entender cómo has realizado la tarea.

´´´ Java
package ejercicioTarea06;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nif, nombre, telefono, direccion;
    private double deuda;

    public Cliente(String nif, String nombre, String telefono, String direccion, double deuda) {//Costructor
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
    public String toString(){ //Devulve la información completa del cliente
        return "\nNIF: " + nif +  "\nNombre: " + nombre + "\nTeléfono: " + telefono + "\nDirección: " + direccion + "\nDeuda: " + deuda;
    }
}
´´´
