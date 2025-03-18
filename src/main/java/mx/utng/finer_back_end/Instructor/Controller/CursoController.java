package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Services.CursoService;


@RestController
@RequestMapping("/api/curso")
public class CursoController {
    
    @Autowired
    private CursoService cursoService;

    /**
     * Este método se encargar de hacer un registro dentro de la tabla Cursos utilizando la función registrar_curso.
     * Recibirá los datos desde frontend y los insertará dentro de la base de datos.
     * 
     * @param idUsuarioInstructor /int/
     * @param idUsuarioAdministrador /int/
     * @param tituloCurso /String/ Titulo que recibirá el curso
     * @param descripcion /String/ Descripción del curso
     * @return Respuesta con el mensaje de éxito o error.
     */
    @PostMapping("/crear-curso")
    public ResponseEntity<String> crearCurso(
                                            @RequestParam int idUsuarioInstructor,
                                            @RequestParam int idUsuarioAdministrador,
                                             @RequestParam String tituloCurso,  
                                             @RequestParam String descripcion){
        try{
            ResponseEntity<String> mensaje = cursoService.registrarCursos(idUsuarioInstructor, idUsuarioAdministrador,
            tituloCurso,descripcion);
            return mensaje;

        }catch(Exception e){
        return ResponseEntity.status(500).body("Error de conexión: "+ e.getMessage());


        }
        
    }


}
