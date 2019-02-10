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
