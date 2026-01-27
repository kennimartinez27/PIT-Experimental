package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Rol;
import com.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Boolean existsByUsername(String username);
    
    // Métodos de búsqueda
    List<Usuario> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);
    
    List<Usuario> findByRol(Rol rol);
    
    List<Usuario> findByNombreCompletoContainingIgnoreCaseAndRol(String nombreCompleto, Rol rol);
}
