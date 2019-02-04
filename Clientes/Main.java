package Clientes;

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
            String archivotemporal = System.getProperty("java.io.tmpdir") +"clientes.dat";
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
                        System.out.print("\nIntroduzca el Teléfono: ");
                        teclado = new Scanner(System.in);
                        telefono = teclado.nextLine();
                        System.out.print("\nIntroduzca la Dirección: ");
                        teclado = new Scanner(System.in);
                        direccion = teclado.nextLine();
                        valido = false;
                        while (!valido) {
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
                        while (!valido) {
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
