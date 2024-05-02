package clientejb;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.*;
import ec.edu.ups.ppw63.demo63.business.GestionClientesRemoto;
import ec.edu.ups.ppw63.demo63.model.Cliente;

public class ClienteGUI extends JFrame {
    JTextField dniField, nombreField, idField;
    JButton agregarButton, listarButton, actualizarButton, eliminarButton;
    JTextArea listaArea;
    GestionClientesRemoto gestionClientes;

    public ClienteGUI(GestionClientesRemoto gestionClientes) {
        super("Cliente CRUD");

        this.gestionClientes = gestionClientes;

        // Configurar componentes de la interfaz
        dniField = new JTextField(10);
        idField = new JTextField(10);
        nombreField = new JTextField(20);
        agregarButton = new JButton("Agregar");
        listarButton = new JButton("Listar");
        actualizarButton = new JButton("Actualizar");
        eliminarButton = new JButton("Eliminar");
        listaArea = new JTextArea(10, 30);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("DNI:"));
        inputPanel.add(dniField);
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreField);
        inputPanel.add(agregarButton);
        inputPanel.add(listarButton);
        inputPanel.add(actualizarButton);
        inputPanel.add(eliminarButton);

        JScrollPane scrollPane = new JScrollPane(listaArea);

        JPanel mainPanel = new JPanel();
        mainPanel.add(inputPanel);
        mainPanel.add(scrollPane);

        // Agregar ActionListener a los botones
        agregarButton.addActionListener(e -> agregarCliente());
        listarButton.addActionListener(e -> listarCliente());
        actualizarButton.addActionListener(e -> actualizarCliente());
        eliminarButton.addActionListener(e -> eliminarCliente());

        // Configurar la ventana
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    private void agregarCliente() {
    	listaArea.setText("");
        Cliente cliente = new Cliente();
        cliente.setCodigo(Integer.parseInt(idField.getText()));
        cliente.setDni(dniField.getText());
        cliente.setNombre(nombreField.getText());
        try {
            gestionClientes.guardarClientes(cliente);
            listaArea.setText("Cliente agregado correctamente.");
        } catch (Exception ex) {
            listaArea.setText("Error al agregar el cliente: " + ex.getMessage());
        }
    }
    
    private void listarCliente() {
    	listaArea.setText("");
        try {
        	StringBuilder clientes = new StringBuilder();
        	clientes.append("Detalles de Factura:\n");
            for (Cliente cliente : gestionClientes.getClientes()) {
            	clientes.append("ID: ").append(cliente.getCodigo())
                        .append(", DNI: ").append(cliente.getDni())
                        .append(", Nombre: ").append(cliente.getNombre())
                        .append("\n");
            }
            listaArea.setText(clientes.toString());
        } catch (Exception ex) {
            listaArea.setText("Error al listar clientes: " + ex.getMessage());
        }
    }
    
    private void actualizarCliente() {
    	listaArea.setText("");
    	Cliente cliente = new Cliente();
    	cliente.setCodigo(Integer.parseInt(idField.getText()));
        cliente.setDni(dniField.getText());
        cliente.setNombre(nombreField.getText());
        try {
            gestionClientes.actualizarCliente(cliente);
            listaArea.setText("Cliente agregado correctamente.");
        } catch (Exception ex) {
            listaArea.setText("Error al agregar el cliente: " + ex.getMessage());
        }
    }
    
    private void eliminarCliente() {
    	listaArea.setText("");
        // Lógica para eliminar un cliente
    	Cliente cliente = new Cliente();
    	cliente.setCodigo(Integer.parseInt(idField.getText()));
        try {
            gestionClientes.borrarCliente(cliente.getCodigo());
            listaArea.setText("Cliente agregado correctamente.");
        } catch (Exception ex) {
            listaArea.setText("Error al agregar el cliente: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        try {
            // Configuración del contexto JNDI
            final Hashtable<String, String> jndiProperties = new Hashtable<>();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:18080");
            jndiProperties.put(Context.SECURITY_PRINCIPAL, "ejb64"); // Reemplaza 'username' con tu nombre de usuario
            jndiProperties.put(Context.SECURITY_CREDENTIALS, "ejb64"); // Reemplaza 'password' con tu contraseña
            jndiProperties.put("jboss.naming.client.ejb.context", "true");

            // Inicialización del contexto
            final Context context = new InitialContext(jndiProperties);

            // Búsqueda del EJB remoto
            GestionClientesRemoto gestionClientes = (GestionClientesRemoto) context.lookup("ejb:/demo63/GestionClientes!ec.edu.ups.ppw63.demo63.business.GestionClientesRemoto");

            // Creación y visualización de la interfaz gráfica
            SwingUtilities.invokeLater(() -> new ClienteGUI(gestionClientes));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
