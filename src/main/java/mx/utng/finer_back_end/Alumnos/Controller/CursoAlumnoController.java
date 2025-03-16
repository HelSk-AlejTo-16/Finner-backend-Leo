package mx.utng.finer_back_end.Alumnos.Controller;

import org.springframework.http.HttpHeaders;  
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Alumnos.Documentos.CertificadoDetalleDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.CursoDetalleAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.PuntuacionAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Services.CursoAlumnoService;
import mx.utng.finer_back_end.Alumnos.Services.PdfGenerationService;

import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/cursos/alumno")
public class CursoAlumnoController {

    @Autowired
    private CursoAlumnoService cursoService;

    @Autowired
    private PdfGenerationService pdfGenerationService;

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
            List<CursoDetalleAlumnoDTO> detalles = cursoService.getCurso(id);

            // Si la lista está vacía, significa que el curso no existe
            if (detalles.isEmpty()) {
                return ResponseEntity.status(404).body("No se encontró ningún curso con el ID " + id);
            }

            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping("/inscripcionCurso/{idCurso}/{idAlumno}")
    public ResponseEntity<Map<String, Object>> inscribirseCurso(@PathVariable Integer idCurso,
            @PathVariable Integer idAlumno) {
        Map<String, Object> response = new HashMap<>();

        Boolean inscripcionCurso = cursoService.inscribirseCurso(idAlumno, idCurso);

        if (inscripcionCurso != null && inscripcionCurso) {
            response.put("mensaje", "Inscripción al curso completada");
            return ResponseEntity.ok(response);
        } else if (inscripcionCurso != null && !inscripcionCurso) {
            response.put("mensaje", "El alumno ya está inscrito en este curso y/o acaba de completar el curso");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("mensaje", "Error al procesar la inscripción");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/resultadoEvaluacion/{idInscricpion}")
    public ResponseEntity<?> verPuntuacion(@PathVariable Integer idInscricpion) {
        try {
            List<PuntuacionAlumnoDTO> puntuacionAlumnoDTOs = cursoService.verPuntuacion(idInscricpion);

            if (puntuacionAlumnoDTOs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron puntuaciones para el alumno con la inscripción: " + idInscricpion);
            }

            return ResponseEntity.ok(puntuacionAlumnoDTOs);

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Solicitud incorrecta: " + e.getMessage());
        }
    }

    @GetMapping("bajaCurso/{idInscripcion}")
    public ResponseEntity<Map<String, Object>> bajaCursoAlumno(@PathVariable Integer idInscripcion) {
        Map<String, Object> response = new HashMap<>();

        try {
            String bajaCurso = cursoService.bajaCursoAlumno(idInscripcion);
            response.put("mensaje", bajaCurso);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", "El ID de inscripción no es válido o no se encuentra en nuestros registros.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar procesar la baja.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Ocurrió un error inesperado.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

@GetMapping("/certificado/{idInscripcion}")
public ResponseEntity<byte[]> generarCertificado(@PathVariable Integer idInscripcion) {
    try {
        CertificadoDetalleDTO certificadoDetalles = cursoService.obtenerDetallesCertificado(idInscripcion);

        if (certificadoDetalles == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("No se encontraron detalles para el certificado del alumno con la inscripción: " + idInscripcion).getBytes());
        }

        System.out.println("Detalles del certificado: ");
        System.out.println("ID Inscripción: " + certificadoDetalles.getIdInscripcion());
        System.out.println("Nombre Alumno: " + certificadoDetalles.getNombreCompletoAlumno());
        System.out.println("Curso: " + certificadoDetalles.getTituloCurso());
        System.out.println("Instructor: " + certificadoDetalles.getNombreInstructor());
        System.out.println("Fecha Inscripción: " + certificadoDetalles.getFechaInscripcion());
        System.out.println("Fecha Generación: " + certificadoDetalles.getFechaGeneracion());

        byte[] pdfContent = pdfGenerationService.generarCertificado(certificadoDetalles);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=certificado_" + idInscripcion + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF) 
                .body(pdfContent); 

    } catch (Exception e) {
        System.err.println("Error al generar el certificado: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Error al generar el certificado: " + e.getMessage()).getBytes());
    }
}



}