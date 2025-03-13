package mx.utng.finer_back_end.Publicos.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;
import mx.utng.finer_back_end.Publicos.Services.UsuarioServices;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServices usuarioServices;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDocumento>> getAllUsuarios() {
        List<UsuarioDocumento> usuarios = usuarioServices.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDocumento> getUsuarioById(@PathVariable String id) {
        UsuarioDocumento usuario = usuarioServices.findById(id);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioDocumento> createUsuario(@RequestBody UsuarioDocumento usuario) {
        UsuarioDocumento createdUsuario = usuarioServices.save(usuario);
        return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDocumento> updateUsuario(@PathVariable String id, @RequestBody UsuarioDocumento usuario) {
        usuario.setId(Long.parseLong(id));
        UsuarioDocumento updatedUsuario = usuarioServices.update(usuario);
        return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String id) {
        usuarioServices.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/perfil/{id}")
    public void actualizarPerfilAlumno(@PathVariable Integer id,
                                       @RequestParam String nombre,
                                       @RequestParam String apellidoPaterno,
                                       @RequestParam String apellidoMaterno,
                                       @RequestParam String nombreUsuario,
                                       @RequestParam String correo,
                                       @RequestParam String contrasenia) {
        usuarioServices.actualizarPerfilAlumno(id, nombre, apellidoPaterno, apellidoMaterno, 
                                              nombreUsuario, correo, contrasenia);
    }
}
