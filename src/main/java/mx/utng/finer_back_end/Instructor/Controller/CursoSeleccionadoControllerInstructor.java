package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Documentos.CursoSeleccionadoDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoSeleccionadoServiceInstructor;

@RestController
@RequestMapping("/api/cursos")
public class CursoSeleccionadoControllerInstructor {

    @Autowired
    private CursoSeleccionadoServiceInstructor cursoSeleccionadoService;

    /**
     * Endpoint para seleccionar un curso específico.
     *
     * @param idCurso identificador del curso a seleccionar.
     * @return ResponseEntity con el estado de la operación y, en caso de éxito, los datos del curso.
     */
    @GetMapping("/seleccionar/{idCurso}")
    public ResponseEntity<?> seleccionarCurso(@PathVariable Integer idCurso) {
        try {
            CursoSeleccionadoDTO resultado = cursoSeleccionadoService.seleccionarCurso(idCurso);

            // Retorna el ResponseEntity según el código devuelto por la función
            if (resultado.getStatusCode() == 200) {
                return ResponseEntity.ok(resultado);
            } else if (resultado.getStatusCode() == 404) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al seleccionar el curso: " + e.getMessage());
        }
    }

}
