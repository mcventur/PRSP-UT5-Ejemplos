import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solicita al usuario un dni y valida su formato mediante expresión regular
 * con la función matches()
 */
public class E01_ValidacionEntradas {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce tu DNI: ");
        String dni = sc.nextLine();
        //Declaramos el patrón. Admitimos mayúsculas y minúsculas para la letra
        //Como usaremos matcher, buscamos coincidencia total
        Pattern pat = Pattern.compile("[0-9]{8}[A-Za-z]");
        //Creamos el matcher a partir de la expresión regular, pasándole la cadena sobre la que se verifica
        Matcher mat = pat.matcher(dni);
        if(mat.matches()){
            System.out.println("Formato de DNI correcto");
        } else{
            System.err.println("El formato de DNI es incorrecto");
        }
    }
}
