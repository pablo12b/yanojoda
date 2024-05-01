package clienteejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import business.GestionClientesRemoto;
import model.Cliente;

public class Main {

    public static void main(String[] args) {

        try {
            final Hashtable<String, String> jndiProperties = new Hashtable<>();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            jndiProperties.put("jboss.naming.client.ejb.context", "true");

            final Context context = new InitialContext(jndiProperties);
            GestionClientesRemoto gestionClientes = (GestionClientesRemoto) context.lookup("ejb:/demo63/GestionClientesRemotoImpl!ec.edu.ups.ppw63.demo63.business.GestionClientesRemoto");

            // Uso del EJB
            Cliente cliente = new Cliente();
            cliente.setDni("0101010101");
            cliente.setNombre("Juan Perez");
            gestionClientes.guardarClientes(cliente);
            System.out.println("Cliente guardado!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}