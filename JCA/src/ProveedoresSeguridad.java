import java.security.Provider;
import java.security.Security;
import java.util.Set;

/**
 * Muestra los proveedores de seguridad incluidos por defecto en el JDK utilizado
 */
public class ProveedoresSeguridad {
    public static void main(String[] args) {
        printProviders();


        proveedorPorDefectoServicio("DES");
    }

    private static void proveedorPorDefectoServicio(String servicio) {
        Provider defaultProvider = Security.getProvider(servicio);
        if (defaultProvider != null) {
            System.out.println("Proveedor predeterminado para " +  servicio + ": " + defaultProvider.getName());
        } else {
            System.out.println("No hay un proveedor predeterminado registrado para " + servicio);
        }
    }

    private static void printProviders() {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println("Nombre: "  + provider.getName());
            System.out.println("Información:\n" + provider.getInfo());

            Set<Provider.Service> serviceList = provider.getServices();
            for (Provider.Service service : serviceList)
            {
                System.out.println("Tipo de Servicio: " + service.getType() + " Algoritmo " + service.getAlgorithm());
            }
        }
    }
}
