package mx.utng.finer_back_end.Publicos.Services;

import org.springframework.stereotype.Service;
import mx.utng.finer_back_end.Publicos.Repository.PublicosRepository;

@Service
public class PublicosService {

    private final PublicosRepository usuarioRepository;

    public PublicosService(PublicosRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene el correo electrónico del usuario por su ID.
     * @param idUsuario ID del usuario.
     * @return Correo del usuario o null si no se encuentra.
     */
    public String obtenerCorreoPorId(Long idUsuario) {
        return usuarioRepository.findCorreoById(idUsuario);
    }
}
