package clientejb;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.*;

import ec.edu.ups.ppw63.demo63.business.GestionDetalleFacturasRemoto;
import ec.edu.ups.ppw63.demo63.model.DetalleFactura;

public class DetalleFacturaGUI extends JFrame {
	JTextField nombreField, cantidadField, precioField, idField;
	JButton agregarButton, listarButton, actualizarButton, eliminarButton;
	JTextArea listaArea;
	GestionDetalleFacturasRemoto gestionFacturas;

	public DetalleFacturaGUI(GestionDetalleFacturasRemoto gestionFacturas) {
		super("Detalles de Factura");

		this.gestionFacturas = gestionFacturas;

		// Configurar componentes de la interfaz
		nombreField = new JTextField(20);
		cantidadField = new JTextField(5);
		precioField = new JTextField(10);
		idField = new JTextField(10);
		agregarButton = new JButton("Agregar");
		listarButton = new JButton("Listar");
		actualizarButton = new JButton("Actualizar");
		eliminarButton = new JButton("Eliminar");
		listaArea = new JTextArea(15, 30);
		listaArea.setEditable(false); // Para que el área de lista sea solo de lectura

		JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		inputPanel.add(new JLabel("ID:"));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("Nombre:"));
		inputPanel.add(nombreField);
		inputPanel.add(new JLabel("Cantidad:"));
		inputPanel.add(cantidadField);
		inputPanel.add(new JLabel("Precio:"));
		inputPanel.add(precioField);

		JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
		buttonPanel.add(agregarButton);
		buttonPanel.add(listarButton);
		buttonPanel.add(actualizarButton);
		buttonPanel.add(eliminarButton);

		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(inputPanel, BorderLayout.CENTER);
		controlPanel.add(buttonPanel, BorderLayout.EAST);

		JScrollPane scrollPane = new JScrollPane(listaArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Detalles de Factura")); // Añadir un borde con título al
																						// área de lista

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(controlPanel, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// Agregar ActionListeners a los botones
		agregarButton.addActionListener(e -> agregarDetalleFactura());
		listarButton.addActionListener(e -> listarDetallesFactura());
		actualizarButton.addActionListener(e -> actualizarDetalleFactura());
		eliminarButton.addActionListener(e -> eliminarDetalleFactura());

		// Configurar la ventana
		add(mainPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void agregarDetalleFactura() {
		listaArea.setText("");

		// Verificar si todos los campos están llenos
		if (cantidadField.getText().isEmpty() || nombreField.getText().isEmpty() || precioField.getText().isEmpty()) {
			listaArea.append("Por favor, complete todos los campos antes de agregar el detalle de factura.\n");
			return; // Salir del método si algún campo está vacío
		}

		try {
			DetalleFactura detalle = new DetalleFactura();
			detalle.setCantidad(Integer.parseInt(cantidadField.getText()));
			detalle.setNombre(nombreField.getText());
			detalle.setPrecio(Double.parseDouble(precioField.getText()));

			gestionFacturas.guardarDetalles(detalle);
			listaArea.append("Detalle de factura agregado correctamente.\n");
		} catch (NumberFormatException e) {
			listaArea.append("Error: Cantidad o precio ingresado no válido.\n");
		} catch (Exception e) {
			listaArea.append("Error al agregar detalle de factura: " + e.getMessage() + "\n");
		}
	}

	private void listarDetallesFactura() {
		try {
			listaArea.setText("");
			StringBuilder detalles = new StringBuilder();
			detalles.append("Detalles de Factura:\n");
			for (DetalleFactura detalle : gestionFacturas.getDetalles()) {
				detalles.append("ID: ").append(detalle.getCodigo()).append(", Nombre: ").append(detalle.getNombre())
						.append(", Cantidad: ").append(detalle.getCantidad()).append(", Precio: ")
						.append(detalle.getPrecio()).append("\n");
			}
			listaArea.setText(detalles.toString());
		} catch (Exception e) {
			listaArea.append("Error al listar detalles de factura: " + e.getMessage() + "\n");
		}
	}

	private void actualizarDetalleFactura() {
		listaArea.setText("");

		if (cantidadField.getText().isEmpty() || nombreField.getText().isEmpty() || precioField.getText().isEmpty()) {
			listaArea.append("Por favor, complete todos los campos antes de agregar el detalle de factura.\n");
			return; // Salir del método si algún campo está vacío
		}

		DetalleFactura detalle = new DetalleFactura();
		detalle.setCodigo(Integer.parseInt(idField.getText()));
		detalle.setCantidad(Integer.parseInt(cantidadField.getText()));
		detalle.setNombre(nombreField.getText());
		detalle.setPrecio(Double.parseDouble(precioField.getText()));
		try {
			gestionFacturas.actualizarDetalle(detalle);
			listaArea.append("Detalle de factura actualizado correctamente.\n");
		} catch (Exception e) {
			listaArea.append("Error al agregar detalle de factura: " + e.getMessage() + "\n");
		}
	}

	private void eliminarDetalleFactura() {
		listaArea.setText("");

		// Verificar si el campo de código está vacío
		if (!idField.getText().isEmpty() && idField.getText().matches("\\d+")) {
			// Convertir el texto a un valor entero
			int codigo = Integer.parseInt(idField.getText());
			try {
				// Intentar borrar el detalle usando el código
				gestionFacturas.borrarDetalle(codigo);
				listaArea.append("Detalle de factura eliminado correctamente.\n");
			} catch (NumberFormatException e) {
				listaArea.append("Error: Código ingresado no válido.\n");
			} catch (Exception e) {
				listaArea.append("Error al eliminar detalle de factura: " + e.getMessage() + "\n");
			}
		}
	}

	public static void main(String[] args) {
		try {
			// Configuración del contexto JNDI
			final Hashtable<String, String> jndiProperties = new Hashtable<>();
			jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.wildfly.naming.client.WildFlyInitialContextFactory");
			jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:18080");
			jndiProperties.put(Context.SECURITY_PRINCIPAL, "ejb64"); // Reemplaza 'username' con tu nombre de usuario
			jndiProperties.put(Context.SECURITY_CREDENTIALS, "ejb64"); // Reemplaza 'password' con tu contraseña
			jndiProperties.put("jboss.naming.client.ejb.context", "true");

			// Inicialización del contexto
			final Context context = new InitialContext(jndiProperties);

			// Búsqueda del EJB remoto
			GestionDetalleFacturasRemoto gestionFacturas = (GestionDetalleFacturasRemoto) context.lookup(
					"ejb:/demo63/GestionDetalleFacturas!ec.edu.ups.ppw63.demo63.business.GestionDetalleFacturasRemoto");

			// Creación y visualización de la interfaz gráfica
			SwingUtilities.invokeLater(() -> new DetalleFacturaGUI(gestionFacturas));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
