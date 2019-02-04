package Clientes;

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

    public ListaObjetos(File archivoTemporal) throws IOException {
            this.archivoTemporal=archivoTemporal;
            lista = new ArrayList<>();
            if (archivoTemporal.exists()){
                input = new FileInputStream(archivoTemporal);
                flujoEntrada = new ObjectInputStream(input);
                // Cargo todos los elementos del archivo en la lista
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
    private void reconstruir() throws IOException {
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
    public void clearObjetos() throws IOException {
        lista.clear();
        flujoSalida= new ObjectOutputStream(output);
        flujoSalida.flush();
    }
    public String borrarArchivo() throws IOException {
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

    public void addObjeto(Cliente cl) throws IOException {
        lista.add(cl);
        flujoSalida.writeObject(cl);
    }
    public void removeObjeto(String NIF) throws IOException {
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
    public Cliente getObjeto(String NIF){
        for (Cliente e: lista){
            if (e.getNif().toLowerCase().equals(NIF.toLowerCase())){
                return e;
            }
        }
        return null;
    }

}
