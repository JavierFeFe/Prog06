package Clientes;

import modelo.clientes.ObjetoCliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static String menu = //Menú que se mostrará
            "\nOpciones disponibles: \n\n" +
                    "1 - Añadir Cliente.\n" +
                    "2 - Listar Clientes.\n" +
                    "3 - Buscar Clientes.\n" +
                    "4 - Borrar Cliente.\n" +
                    "5 - Borrar fichero de clientes completamente.\n" +
                    "0 - Salir de la aplicación.\n";
    public static void main (String[] args){

        try {
            String archivotemporal = System.getProperty("java.io.tmpdir") +"clientes.dat";
            System.out.println("Ruta del archivo temporal: " + archivotemporal);
            String nif, nombre, telefono, direccion = "";
            double deuda = 0.0;
            ObjectInputStream flujoEntrada;
            ObjectOutputStream flujoSalida;
            String opcionMenu = "";
            System.out.print(menu);
            opcionMenu = pausa();
            while (!opcionMenu.equals("0")){
                Scanner teclado;
                switch (opcionMenu){ //Switch para las opciones del menú.
                    case "1":
                        System.out.print("\nIntroduzca el NIF: ");
                        teclado = new Scanner(System.in);
                        nif = teclado.nextLine();
                        System.out.print("\nIntroduzca el Nombre: ");
                        teclado = new Scanner(System.in);
                        nombre = teclado.nextLine();
                        System.out.print("\nIntroduzca el Teléfono: ");
                        teclado = new Scanner(System.in);
                        telefono = teclado.nextLine();
                        System.out.print("\nIntroduzca la Dirección: ");
                        teclado = new Scanner(System.in);
                        direccion = teclado.nextLine();
                        System.out.print("\nIntroduzca la deuda: ");
                        teclado = new Scanner(System.in);
                        deuda = Double.parseDouble(teclado.nextLine());
                        Cliente tmp = new Cliente(nif, nombre, telefono, direccion, deuda);
                        List<Cliente> lista = new ArrayList<Cliente>();
                        flujoEntrada = new ObjectInputStream(new FileInputStream(archivotemporal));
                        while (true){
                            try {
                                lista.add((Cliente) flujoEntrada.readObject());
                            }catch (EOFException e){
                                flujoEntrada.close();
                                break;
                            }
                        }
                        lista.add(tmp);
                        flujoSalida= new ObjectOutputStream(new FileOutputStream(archivotemporal));
                        for (Cliente cl: lista){
                            flujoSalida.writeObject(cl);
                        }
                        flujoSalida.close();
                        opcionMenu = pausa();
                        break;
                    case "2":
                        Cliente cliente = null;
                        flujoEntrada = new ObjectInputStream(new FileInputStream(archivotemporal));
                        while (true){
                            try {
                                cliente = (Cliente) flujoEntrada.readObject();
                                System.out.println(cliente.getNif());
                            }catch (EOFException e){
                                flujoEntrada.close();
                                break;
                            }
                        }
                        opcionMenu = pausa();
                        break;
                    case "3":
                        System.out.print("\nIntroduzca el NIF del cliente a buscar: ");
                        teclado = new Scanner(System.in);
                        nif = teclado.nextLine();
                        flujoEntrada = new ObjectInputStream(new FileInputStream(archivotemporal));
                        while (true){
                            try {
                                cliente = (Cliente) flujoEntrada.readObject();
                                if (cliente.getNif().toLowerCase().equals(nif.toLowerCase())){
                                    System.out.println(cliente.toString());
                                }
                            }catch (EOFException e){
                                flujoEntrada.close();
                                break;
                            }
                        }
                        opcionMenu = pausa();
                        break;
                    case "4":
                        System.out.print("\nIntroduzca el NIF del cliente que desea eliminar: ");
                        teclado = new Scanner(System.in);
                        nif = teclado.nextLine();
                        lista = new ArrayList<Cliente>();
                        flujoEntrada = new ObjectInputStream(new FileInputStream(archivotemporal));
                        boolean encontrado = false;
                        while (true){
                            try {
                                cliente = (Cliente) flujoEntrada.readObject();
                                if (!cliente.getNif().toLowerCase().equals(nif.toLowerCase())){
                                    lista.add(cliente);
                                }else{
                                    System.out.println("Eliminando cliente: " + cliente.getNombre());
                                    encontrado = true;
                                }
                            }catch (EOFException e){
                                flujoEntrada.close();
                                break;
                            }
                        }
                        if (encontrado) {
                            flujoSalida= new ObjectOutputStream(new FileOutputStream(archivotemporal));
                            for (Cliente c : lista) {
                                flujoSalida.writeObject(c);
                            }
                            flujoSalida.close();
                        }else{
                            System.out.println("No se encontró el cliente...");
                        }
                        opcionMenu = pausa();
                        break;
                    case "5":
                        flujoSalida= new ObjectOutputStream(new FileOutputStream(archivotemporal));
                        flujoSalida.close();
                        opcionMenu = pausa();;
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static String pausa(){//Método simple para crear una pausa
        System.out.print("\nSeleccione una opción (\"m\" para mostrar menú): ");
        return new Scanner(System.in).nextLine().toLowerCase();
    }

}
