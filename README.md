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

```Java
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
```
*Creo una clase simple con un método constructor y los getters y setter necesarios para el cliente*

```Java
package ejercicioTarea06;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListaObjetos {
    private List<Cliente> lista;
    private ObjectInputStream flujoEntrada;
    private ObjectOutputStream flujoSalida;
    private File archivoTemporal;
    FileInputStream input;
    FileOutputStream output;

    public ListaObjetos(File archivoTemporal) throws IOException {//Constructor
            this.archivoTemporal=archivoTemporal;
            lista = new ArrayList<>();
            if (archivoTemporal.exists()){
                input = new FileInputStream(archivoTemporal);
                flujoEntrada = new ObjectInputStream(input);
                // Cargo todos los elementos del archivo en la lista para trabajar con ellos
                while (true){
                    try {
                        lista.add((Cliente) flujoEntrada.readObject());
                    }catch (ClassNotFoundException | IOException e){
                        flujoEntrada.close();
                        break;
                    }
                }
            }
            reconstruir();
    }
    private void reconstruir() throws IOException { //Es necesario la reconstuccion completa para evitar errores
        output = new FileOutputStream(archivoTemporal);
        flujoSalida= new ObjectOutputStream(output);
        flujoSalida.flush();
        lista.forEach((cl)->{
            try {
                flujoSalida.writeObject(cl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void clearObjetos() throws IOException { //Método que vacía la lista
        lista.clear();
        flujoSalida= new ObjectOutputStream(output);
        flujoSalida.flush();
    }
    public String borrarArchivo() throws IOException { //Método que elimina el archivo
        flujoSalida.close();
        flujoEntrada.close();
        input.close();
        output.close();
        if(archivoTemporal.delete()){
            return "\n" + archivoTemporal.getName() + " eliminado...";
        }else{
            return "\nError al intentar eliminar " + archivoTemporal;
        }
    }
    public List<Cliente> getLista(){
        return lista;
    }

    public void addObjeto(Cliente cl) throws IOException { //Método para añadir un cliente
        lista.add(cl);
        flujoSalida.writeObject(cl);
    }
    public void removeObjeto(String NIF) throws IOException { //Método para eliminar un cliente
        Cliente remove = null;
        for (Cliente e: lista){
            if (e.getNif().toLowerCase().equals(NIF.toLowerCase())){
                remove = e;
            }
        }
        if (remove != null){
            lista.remove(remove);
        }
        reconstruir();
    }
    public Cliente getObjeto(String NIF){ //Método para obtener un cliente
        for (Cliente e: lista){
            if (e.getNif().toLowerCase().equals(NIF.toLowerCase())){
                return e;
            }
        }
        return null;
    }

}
```
*Creo una clase que facilite el trabajo con el archivo secuancial. Mientras trabajaba con ella me di cuenta que al escribir en el archivo con el modificador APPEND, por alguna razón da error, por lo que leí en foros es un error bastante común en los ficheros secuenciales, por lo q preferí guardar el archivo y reconstrutirlo en el primer acceso para evitar problemas.*

```Java
package ejercicioTarea06;

public class CalculaNIF {

    private static final String letrasNif = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String letrasCif = "ABCDEFGHJKLMNPQRSUVW";
    private static final String letrasNie = "XYZ";
    private static final String digitoControlCif = "JABCDEFGHI";
    private static final String cifLetra = "KPQRSNW";

    //ESTÉ CODIGO ESTÁ ADAPTADO DE UN PROYECTO DE GITHUB, SE MODIFICARON CIERTOS MÉTODOS Y SE DEFINIERON COMO ESTÁTICOS.

    public CalculaNIF() {
    }

    /**
     * Calcula el dígito o letra de control de un documento de identificación
     * del reino de España
     *
     * @param nif documento a calcular
     * @return devuelve el documento con el dígito o letra de control calculado.
     */
    public static String calcularLetra(String nif) {
        nif = nif.toUpperCase();
        String a = nif.substring(0, 1);

        if (letrasCif.contains(a)) {
            return calculaCif(nif);
        } else if (letrasNie.contains(a)) {
            return calculaNie(nif);
        } else {
            return calculaDni(nif);
        }
    }
    /**
     * Valida un documento de identificación del reino de España
     *
     * @param nif documento a validar
     * @return null si es inválido, String si es válido.
     */
    public static String isValido(String nif){
        if (CalculaNIF.isDniValido(nif)) {
            return "\nDNI válido...";
        } else if (CalculaNIF.isCifValido(nif)) {
            return "\nCIF válido...";
        } else if (CalculaNIF.isNieValido(nif)) {
            return "\nNIE válido...";
        }
        return null;
    }

    private static String calculaDni(String dni) {
        String str = completaCeros(dni, 8);

        if(str.length()==9){
            str=str.substring(0,dni.length()-1);
        }
        return str + calculaLetra(str);
    }

    private static String calculaNie(String nie) {
        String str = null;

        if(nie.length()==9){
            nie=nie.substring(0, nie.length()-1);
        }

        if (nie.startsWith("X")) {
            str = nie.replace('X', '0');
        } else if (nie.startsWith("Y")) {
            str = nie.replace('Y', '1');
        } else if (nie.startsWith("Z")) {
            str = nie.replace('Z', '2');
        }

        return nie + calculaLetra(str);
    }

    private static String calculaCif(String cif) {
        return cif + calculaDigitoControl(cif);
    }

    private static int posicionImpar(String str) {
        int aux = Integer.parseInt(str);
        aux = aux * 2;
        aux = (aux / 10) + (aux % 10);

        return aux;
    }

    private static boolean isDniValido(String dni) {
        if (dni.toUpperCase().matches("^\\d{8}["+ letrasNif +"]$")) {
            String aux = dni.substring(0, 8);
            aux = calculaDni(aux);
            return dni.toUpperCase().equals(aux);
        }
        return false;
    }

    private static boolean isNieValido(String nie) {
        if (nie.toUpperCase().matches("^["+letrasNie+"]\\d{7}["+ letrasNif+ "]$")) {
            String aux = nie.toUpperCase().substring(0, 8);
            aux = calculaNie(aux);
            return nie.toUpperCase().equals(aux);
        }
        return false;
    }

    private static boolean isCifValido(String cif) {
        if (cif.toUpperCase().matches("^["+letrasCif+"]\\d{8}$")) {
            String aux = cif.toUpperCase().substring(0, 8);
            aux = calculaCif(aux);
            return cif.toUpperCase().equals(aux);
        }
        return false;
    }


    private static char calculaLetra(String aux) {
        return letrasNif.charAt(Integer.parseInt(aux) % 23);
    }

    private static String calculaDigitoControl(String cif) {
        String str = cif.substring(1, 8);
        String cabecera = cif.substring(0, 1);
        int sumaPar = 0;
        int sumaImpar = 0;
        int sumaTotal;

        for (int i = 1; i < str.length(); i += 2) {
            int aux = Integer.parseInt("" + str.charAt(i));
            sumaPar += aux;
        }

        for (int i = 0; i < str.length(); i += 2) {
            sumaImpar += posicionImpar("" + str.charAt(i));
        }

        sumaTotal = sumaPar + sumaImpar;
        sumaTotal = 10 - (sumaTotal % 10);

        if(sumaTotal==10){
            sumaTotal=0;
        }

        if (cifLetra.contains(cabecera)) {
            str = "" + digitoControlCif.charAt(sumaTotal);
        } else {
            str = "" + sumaTotal;
        }

        return str;
    }

    private static String completaCeros(String str, int num) {
        while (str.length() < num) {
            str = "0" + str;
        }
        return str;
    }
}

```
*He de decir que gran parte de este código para la verificación de un NIF no es mio sino de un proyecto de github el cual fue adaptado para este ejercico, supongo que el objetivo del ejercico era trabajar con ficheros secuenciales, no crear una clase para identificar un NIF válido, si no es así puedo crear mi propio código*

```Java
package ejercicioTarea06;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main (String[] args){
        try {
            String menu = //Menú que se mostrará
                    "\nOpciones disponibles: \n\n" +
                            "1 - Añadir Cliente.\n" +
                            "2 - Listar Clientes.\n" +
                            "3 - Buscar Clientes.\n" +
                            "4 - Borrar Cliente.\n" +
                            "5 - Borrar fichero de clientes completamente.\n" +
                            "0 - Salir de la aplicación.\n";
            String archivotemporal = System.getProperty("java.io.tmpdir") +"/clientes.dat";
            System.out.println("Ruta del archivo temporal: " + archivotemporal);
            ListaObjetos lista = new ListaObjetos(new File(archivotemporal));
            String nif, nombre, telefono, direccion;
            double deuda = 0.0;
            String opcionMenu = "";
            System.out.print(menu);
            opcionMenu = pausa();
            while (!opcionMenu.equals("0")){
                Scanner teclado;
                switch (opcionMenu){ //Switch para las opciones del menú.
                    case "1":
                        boolean valido = false;
                        nif = "";
                        while (!valido) {
                            System.out.print("\nIntroduzca el NIF: ");
                            teclado = new Scanner(System.in);
                            nif = teclado.nextLine();
                            String texto;
                            if ((texto = CalculaNIF.isValido(nif))!= null){
                                valido = true;
                                System.out.println(texto);
                            }else {
                                System.out.println("\nNIF inválido!!");
                            }
                        }
                        System.out.print("\nIntroduzca el Nombre: ");
                        teclado = new Scanner(System.in);
                        nombre = teclado.nextLine();
                        valido = false;
                        telefono = "";
                        while (!valido) { //Fuerza la introducción de un teléfono válido
                            System.out.print("\nIntroduzca el Teléfono: ");
                            teclado = new Scanner(System.in);
                            String tmp = teclado.nextLine();
                            if (tmp.replaceAll(" ","").matches("^\\+?\\d+$")) {
                                valido = true;
                                telefono = tmp;
                            }else{
                                System.out.println("\nFormato de número incorrecto...");
                            }
                        }
                        System.out.print("\nIntroduzca la Dirección: ");
                        teclado = new Scanner(System.in);
                        direccion = teclado.nextLine();
                        valido = false;
                        while (!valido) { //Fuerza la introducción de un número decimal válido
                            System.out.print("\nIntroduzca la deuda: ");
                            teclado = new Scanner(System.in);
                            try {
                                deuda = Double.parseDouble(teclado.nextLine());
                                valido = true;
                            } catch (NumberFormatException ex) {
                                System.out.println("\nFormato de número incorrecto...");
                            }
                        }
                        Cliente tmp = new Cliente(nif, nombre, telefono, direccion, deuda);
                        lista.addObjeto(tmp);
                        opcionMenu = pausa();
                        break;
                    case "2":
                        Cliente cliente = null;
                        for (Cliente x: lista.getLista()){
                            System.out.println(x.toString());
                        }
                        opcionMenu = pausa();
                        break;
                    case "3":
                        valido = false;
                        nif = "";
                        while (!valido) { //Fuerza la intrucción de un NIF válido
                            System.out.print("\nIntroduzca el NIF del cliente a buscar: ");
                            teclado = new Scanner(System.in);
                            nif = teclado.nextLine();
                            String texto;
                            if ((texto = CalculaNIF.isValido(nif))!= null){
                                valido = true;
                                System.out.println(texto);
                            }else {
                                System.out.println("\nNIF inválido!!");
                            }
                        }
                        System.out.println(lista.getObjeto(nif) != null?lista.getObjeto(nif).toString():"No existe ningún cliente con ese NIF...");
                        opcionMenu = pausa();
                        break;
                    case "4":
                        System.out.print("\nIntroduzca el NIF del cliente que desea eliminar: ");
                        teclado = new Scanner(System.in);
                        nif = teclado.nextLine();
                        lista.removeObjeto(nif);
                        opcionMenu = pausa();
                        break;
                    case "5":
                        System.out.println(lista.borrarArchivo());
                        opcionMenu = "0";
                        break;
                    case "m":
                        System.out.print(menu);
                        opcionMenu = pausa();;
                        break;
                    case "0":
                        break;
                    default :
                        System.out.println("Opción inválida."+"\n");
                        opcionMenu = pausa();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String pausa(){//Método simple para crear una pausa
        System.out.print("\nSeleccione una opción (\"m\" para mostrar menú): ");
        return new Scanner(System.in).nextLine().toLowerCase();
    }

}
```
*Creo la clase con el método Main necesaria para la visualización del menú. Dentro de este método se incluyen varios bucles que fuerzan la introducción de un valor válido mediante el método .matches de la clase String que me permite aproximarme a un valor coherente para el campo en cuestión.*
