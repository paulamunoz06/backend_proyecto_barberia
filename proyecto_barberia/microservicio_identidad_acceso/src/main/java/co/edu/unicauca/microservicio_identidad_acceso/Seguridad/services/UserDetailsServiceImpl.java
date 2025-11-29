package co.edu.unicauca.microservicio_identidad_acceso.Seguridad.services;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.accesoADatos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No se econtró un usuario con el id: " + username));
    return UserDetailsImpl.build(user);
  }

}
