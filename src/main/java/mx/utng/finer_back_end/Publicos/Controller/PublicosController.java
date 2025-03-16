package mx.utng.finer_back_end.Publicos.Controller;

import mx.utng.finer_back_end.Publicos.Services.EmailService;
import mx.utng.finer_back_end.Publicos.Services.PublicosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/token")
public class PublicosController {
    
    private static final SecureRandom random = new SecureRandom();
    private final PublicosService usuarioService;
    private final EmailService emailService;

    public PublicosController(PublicosService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    /**
     * - Genera el token de 6 dígitos numéricos
     * @return regresa el código de 6 dígitos
     */
    private String generarTokenNumerico() {
        return String.valueOf(100000 + random.nextInt(900000));
    }

    /**
     * Obtiene el correo del usuario según su ID y envía el token.
     * @param idUsuario ID del usuario.
     * @return Mensaje de confirmación o error.
     */
    @GetMapping("/enviar/{idUsuario}")
    public ResponseEntity<String> enviarToken(@PathVariable Long idUsuario) {
        String correoUsuario = usuarioService.obtenerCorreoPorId(idUsuario);

        if (correoUsuario == null || correoUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body("No se encontró el usuario con ID: " + idUsuario);
        }

        String token = generarTokenNumerico();
        boolean enviado = emailService.mandarTokenNumerico(correoUsuario, token);

        if (enviado) {
            return ResponseEntity.ok("Token enviado a " + correoUsuario);
        } else {
            return ResponseEntity.status(500).body("Error al enviar el token.");
        }
    }
}
