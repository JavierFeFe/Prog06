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
