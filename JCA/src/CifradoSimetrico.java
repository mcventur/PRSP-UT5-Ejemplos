import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;


/**
 * Ejemplo de Cifrado y descifrado de un fichero usando una clave simétrica
 * con el algoritmo <a href="https://es.wikipedia.org/wiki/Data_Encryption_Standard">DES</a>
 */
public class CifradoSimetrico {

    private final String FICHERO_ORIGINAL = "fichero";
    private final String FICHERO_CIFRADO = "fichero.cifrado";
    private final String FICHERO_DESCIFRADO = "fichero.descifrado";
    //Clave de cifrado simétrica
    private SecretKey clave;

    /**
     * Genera la clave de cifrado y borra los ficheros cifrado y descifrado
     */
    public CifradoSimetrico() {
        generarClave();
        limpiarFicheros();
    }

    public static void main(String[] args) {
        System.out.println("1 - Generamos la clave");
        CifradoSimetrico cif = new CifradoSimetrico();
        //CIFRADO
        System.out.println("2 - Cifrando fichero");
        cif.encriptarFichero();
        //DESCIFRADO
        System.out.println("3 - Descifrando fichero");
        cif.desencriptarFichero();
    }

    /**
     * Elimina los ficheros de cifrado y descifrado
     */
    private void limpiarFicheros() {
        File cifrado = new File(FICHERO_CIFRADO);
        if(cifrado.exists()) cifrado.delete();

        File descifrado = new File(FICHERO_DESCIFRADO);
        if(descifrado.exists()) descifrado.delete();
    }


    /**
     * Genera una clave simétrica usando el algoritmo DES
     *
     * @return la {@link SecretKey SecretKey}  generada
     * @see <a href="https://es.wikipedia.org/wiki/Data_Encryption_Standard">DES</a>
     */
    private void generarClave() {
        try {
            //Indicamos al motor qué algoritmo usaremos. En este caso, DES
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);
            clave = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void encriptarFichero() {
        //Las clases que cifran y descifran en Java trabajan siempre con objetos byte[]
        //Podemos convertirlos a String nosotros mismos para ver los resultados

        //Buffer de 1k = 1000 bytes. Uno para el contenido plano y otro para el cifrado
        byte[] bufferPlano = new byte[1000];
        byte[] bufferCifrado;
        int bytesLeidos;


        //Inicializamos los dos flujos: lectura del fichero original y escritura del fichero cifrado
        try (FileInputStream lecturaFicheroPlano = new FileInputStream(FICHERO_ORIGINAL);
             FileOutputStream escrituraFicheroCifrado = new FileOutputStream(FICHERO_CIFRADO);
        ) {
            //Declaramos el cifrador, indicando el algoritmo a usar. Al no indicar proveedor se usará el preferido (SUN)
            Cipher cifrador = Cipher.getInstance("DES");
            //Iniciamos el cifrador con la clave en modo encriptar
            cifrador.init(Cipher.ENCRYPT_MODE, clave);
            System.out.println("Clave generada: " + clave.toString());

            //Vamos leyendo bytes de 1k en 1k
            while ((bytesLeidos = lecturaFicheroPlano.read(bufferPlano)) != -1) {
                //Ciframos los bytes leídos. IMPORTANTE indicar los bytesLeidos para no escribir caracteres nulos
                bufferCifrado = cifrador.update(bufferPlano, 0, bytesLeidos);
                //Los escribimos en el fichero cifrado
                escrituraFicheroCifrado.write(bufferCifrado);
            }
            //Con cifrados por bloques, debemos llamar al final a doFinal para resetear el Cifrador a su estado original,
            //procesar cualquier dato restante, y liberar recursos
            bufferCifrado = cifrador.doFinal();
            escrituraFicheroCifrado.write(bufferCifrado);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void desencriptarFichero() {
        int bytesLeidos;
        //Buffer para almacenar el contenido descifrado y el cifrado original
        byte[] bufferPlano;
        byte[] bufferCifrado = new byte[1000];

        //Declaramos los streams de lectura del fichero cifrado y escritura del descifrado
        try (FileInputStream entradaCifrado = new FileInputStream(FICHERO_CIFRADO);
             FileOutputStream salidaPlano = new FileOutputStream(FICHERO_DESCIFRADO)) {
            //Declaramos e iniciamos el cifrador en modo descifrar
            Cipher cifrador = Cipher.getInstance("DES");
            cifrador.init(Cipher.DECRYPT_MODE, clave);
            System.out.println("Clave generada: " + clave.toString());

            //Vamos leyendo bytes de 1k en 1k
            while ((bytesLeidos = entradaCifrado.read(bufferCifrado)) != -1) {
                //DesCiframos los bytes leídos.
                //IMPORTANTE: Se debe indicar la cantidad de bytesLeidos, ya que la última porción puede no rellenar el buffer completo
                bufferPlano = cifrador.update(bufferCifrado, 0, bytesLeidos);
                //Los escribimos en el fichero descifrado
                salidaPlano.write(bufferPlano);
            }

            //Terminamos cualquier posible operación de descifrado pendiente y reseteamos el cipher
            bufferPlano = cifrador.doFinal();
            salidaPlano.write(bufferPlano);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
