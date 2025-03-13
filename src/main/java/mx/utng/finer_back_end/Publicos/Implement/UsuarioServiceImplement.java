package mx.utng.finer_back_end.Publicos.Implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mx.utng.finer_back_end.Documentos.UsuarioDocumento;
import mx.utng.finer_back_end.Publicos.Dao.UsuarioDao;
import mx.utng.finer_back_end.Publicos.Services.UsuarioServices;

@Service
public class UsuarioServiceImplement implements UsuarioServices {
    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    @Transactional
    public List<UsuarioDocumento> findAll() {
        return (List<UsuarioDocumento>) usuarioDao.findAll();
    }

    @Override
    @Transactional
    public UsuarioDocumento findById(String id) {
        Long usuarioId = Long.parseLong(id);
        Optional<UsuarioDocumento> usuarioOpt = usuarioDao.findById(usuarioId);
        return usuarioOpt.orElse(null); // Devuelve null si no se encuentra el usuario
    }

    @Override
    @Transactional
    public UsuarioDocumento save(UsuarioDocumento usuario) {
        return usuarioDao.save(usuario);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Long usuarioId = Long.parseLong(id);
        usuarioDao.deleteById(usuarioId);
    }

    @Override
    @Transactional
    public UsuarioDocumento update(UsuarioDocumento usuario) {
        return usuarioDao.save(usuario);
    }

    @Override
    public void actualizarPerfilAlumno(Integer idUsuario, String nombre, String apellidoPaterno,
                                        String apellidoMaterno, String nombreUsuario, 
                                        String correo, String contrasenia) {
        usuarioDao.actualizarPerfilAlumno(idUsuario, nombre, apellidoPaterno, apellidoMaterno,
                                           nombreUsuario, correo, contrasenia);
    }
}
