package mx.utng.finer_back_end.Publicos.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

@Repository
public interface UsuarioDao extends JpaRepository<UsuarioDocumento, Long> {

    // Usamos @Query con una consulta nativa para llamar al procedimiento
    @Query(value = "SELECT actualizar_perfil_alumno(:p_id_usuario, :p_nombre, :p_apellido_paterno, :p_apellido_materno, :p_nombre_usuario, :p_correo, :p_contrasenia)", nativeQuery = true)
    void actualizarPerfilAlumno(
        Integer p_id_usuario, 
        String p_nombre, 
        String p_apellido_paterno, 
        String p_apellido_materno, 
        String p_nombre_usuario, 
        String p_correo, 
        String p_contrasenia
    );
}

