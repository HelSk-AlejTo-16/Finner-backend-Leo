package mx.utng.finer_back_end.AlumnosInstructor.Controller;

import mx.utng.finer_back_end.AlumnosInstructor.Services.AlumnoInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumnos-instructor")
public class AlumnoInstructorController {

    @Autowired
    private AlumnoInstructorService alumnoInstructorService;

    /**
     * Endpoint para verificar si el correo es válido.
     * @param correo Correo a verificar.
     * @return true si el correo es válido, false si no lo es.
     */
    @GetMapping("/verificar-correo")
    public boolean verificarCorreo(@RequestParam String correo) {
        return alumnoInstructorService.verificarCorreo(correo);
    }
}
