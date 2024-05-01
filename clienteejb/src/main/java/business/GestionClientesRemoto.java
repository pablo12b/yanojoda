package business;

import java.util.List;

import model.Cliente;

public interface GestionClientesRemoto {
    public void guardarClientes (Cliente cliente);
    public void actualizarCliente(Cliente cliente) throws Exception;
    public Cliente getClientePorCedula(String cedula) throws Exception;
    public void borrarCliente (int codigo);
    public List <Cliente> getClientes();
}