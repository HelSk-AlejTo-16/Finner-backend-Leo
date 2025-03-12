package mx.utng.finer_back_end.Publicos.Services;

import java.util.List;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

public interface UsuarioServices {
    public List<UsuarioDocumento> findAll();

    public UsuarioDocumento findById(String id);

    public UsuarioDocumento save(UsuarioDocumento usuario);

    public void deleteById(String id);

    public UsuarioDocumento update(UsuarioDocumento usuario);

    public void actualizarPerfilAlumno(Integer idUsuario, String nombre, String apellidoPaterno,
                                        String apellidoMaterno, String nombreUsuario, 
                                        String correo, String contrasenia);
}
