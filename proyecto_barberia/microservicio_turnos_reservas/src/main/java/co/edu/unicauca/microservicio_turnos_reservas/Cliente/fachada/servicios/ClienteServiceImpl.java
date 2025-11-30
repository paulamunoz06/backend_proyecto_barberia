package co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.accesoADatos.ClienteRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ClienteDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ClienteDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements IClienteService{
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<ClienteDTORespuesta> findAll() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw new ReglaNegocioExcepcion("No se encontro el cliente");
        }
        return clientes.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTORespuesta findById(String id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + id));
        return mapearARespuesta(cliente);
    }

    public String obtenerCorreoPorId(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntidadNoExisteException(
                        "Cliente no encontrado con ID: " + clienteId));

        return cliente.getEmail();
    }

    @Override
    public ClienteDTORespuesta save(ClienteDTOPeticion clienteDTO) {
        if (clienteRepository.existsById(clienteDTO.getId())) {
            throw new ReglaNegocioExcepcion("Ya existe un cliente con ID: " + clienteDTO.getId());
        }

        Cliente cliente = mapearAPersistencia(clienteDTO);
        cliente.setEstado("ACTIVO");

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return mapearARespuesta(clienteGuardado);
    }

    @Override
    public ClienteDTORespuesta update(String id, ClienteDTOPeticion clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + id));

        if (!id.equals(clienteDTO.getId())) {
            throw new RuntimeException("El ID del path no coincide con el ID del cliente");
        }

        clienteExistente.setNombre(clienteDTO.getNombre());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setTelefono(clienteDTO.getTelefono());

        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        return mapearARespuesta(clienteActualizado);
    }

    @Override
    public boolean delete(String id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + id));

        cliente.setEstado("INACTIVO");
        clienteRepository.save(cliente);

        return true;
    }

    public ClienteDTORespuesta mapearARespuesta(Cliente c) {
        ClienteDTORespuesta dto = new ClienteDTORespuesta();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setEmail(c.getEmail());
        dto.setTelefono(c.getTelefono());
        dto.setEstado(c.getEstado());
        return dto;
    }

    private Cliente mapearAPersistencia(ClienteDTOPeticion dto) {
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setEmail(dto.getEmail());
        c.setTelefono(dto.getTelefono());
        c.setEstado(dto.getEstado());
        return c;
    }
}
