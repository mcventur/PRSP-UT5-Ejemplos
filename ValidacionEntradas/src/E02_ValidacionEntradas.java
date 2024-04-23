import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solicita al usuario un correo electrónico
 * y valida su formato con la función find()
 *
 * Un
 */
public class E02_ValidacionEntradas {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce tu email: ");
        String email = sc.nextLine();
        //Declaramos el patrón
        //Como usaremos find(), sólo verificamos que lleve el patrón especificado
        Pattern pat = Pattern.compile("@");
        Matcher mat = pat.matcher(email);
        if(mat.find()){
            System.out.println("Email correcto");
        } else{
            System.err.println("Email incorrecto");
        }
    }
}
