package mx.utng.finer_back_end.Administrador.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Administrador.Documentos.CursoDetalleDTO;
import mx.utng.finer_back_end.Administrador.Services.CursoService;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    /**
     * Endpoint para obtener los detalles de un curso por su ID.
     * 
     * Este método recibe un ID de curso como parámetro en la URL y retorna
     * la información detallada del curso en formato JSON.
     *
     * @param id ID del curso que se desea consultar. Debe ser un número entero
     *           positivo.
     * @return ResponseEntity con la información del curso si se encuentra,
     *         o un mensaje de error en caso de problemas.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Devuelve la información del curso en una lista de
     *         `CursoDetalleDTO`.
     *         - `400 Bad Request`: Si el ID proporcionado no es válido (nulo o
     *         menor o igual a 0).
     *         - `404 Not Found`: Si no existe un curso con el ID proporcionado.
     *         - `500 Internal Server Error`: Si ocurre un error inesperado en el
     *         servidor.
     */
    @GetMapping("/detalles/{id}")
    public ResponseEntity<?> obtenerDetalles(@PathVariable Integer id) {
        try {
            List<CursoDetalleDTO> detalles = cursoService.getCurso(id);

            // Si la lista está vacía, significa que el curso no existe
            if (detalles.isEmpty()) {
                return ResponseEntity.status(404).body("No se encontró ningún curso con el ID " + id);
            }

            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
        }
    }

}