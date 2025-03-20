package mx.utng.finer_back_end.Administrador.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Administrador.DTO.CategoriaDTO;
import mx.utng.finer_back_end.Administrador.Services.AdministradorService;

@RestController
@RequestMapping("/api/administrador")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/eliminarCursoAlumno")
    public ResponseEntity<Map<String, Object>> eliminarCursoAlumno(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String matricula = (String) obj.get("matricula");
            Integer idCurso = Integer.parseInt(obj.get("idCurso").toString());
            
            if (matricula == null || matricula.isEmpty() || idCurso == null) {
                response.put("mensaje", "La matrícula y el ID del curso son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            String resultado = administradorService.eliminarAlumnoCurso(matricula, idCurso);
            
            if (resultado.equals("El alumno no está inscrito en este curso.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.equals("Alumno eliminado exitosamente del curso.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar eliminar al alumno del curso");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID del curso debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/rechazarCurso")
    public ResponseEntity<Map<String, Object>> rechazarCurso(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long idSolicitudCurso = Long.parseLong(obj.get("idSolicitudCurso").toString());
            String correoInstructor = (String) obj.get("correoInstructor");
            String motivoRechazo = (String) obj.get("motivoRechazo");
            String tituloCurso = (String) obj.get("tituloCurso");
            
            if (idSolicitudCurso == null || correoInstructor == null || correoInstructor.isEmpty() || 
                motivoRechazo == null || motivoRechazo.isEmpty() || tituloCurso == null || tituloCurso.isEmpty()) {
                response.put("mensaje", "El ID de la solicitud, el correo del instructor, el título del curso y el motivo del rechazo son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            String resultado = administradorService.rechazarCurso(idSolicitudCurso, correoInstructor, motivoRechazo, tituloCurso);
            
            if (resultado.equals("No se encontraron datos")) {
                response.put("mensaje", "No se encontró la solicitud de curso con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.equals("Rechazado")) {
                response.put("mensaje", "Solicitud de curso rechazada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar rechazar el curso");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la solicitud de curso debe ser un número válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/crearCategoria")
    public ResponseEntity<Map<String, Object>> crearCategoria(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer idUsuarioInstructor = Integer.parseInt(obj.get("idUsuarioInstructor").toString());
            Integer idUsuarioAdmin = Integer.parseInt(obj.get("idUsuarioAdmin").toString());
            String nombreCategoria = (String) obj.get("nombreCategoria");
            String descripcion = (String) obj.get("descripcion");
            
            if (idUsuarioInstructor == null || idUsuarioAdmin == null || 
                nombreCategoria == null || nombreCategoria.isEmpty()) {
                response.put("mensaje", "El ID del instructor, ID del administrador y nombre de la categoría son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            String resultado = administradorService.crearCategoria(idUsuarioInstructor, idUsuarioAdmin, nombreCategoria, descripcion);
            
            if (resultado.contains("enviada correctamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar crear la solicitud de categoría");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "Los IDs deben ser números enteros válidos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/modificarCategoria/{id}")
    public ResponseEntity<Map<String, Object>> modificarCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO categoriaDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (id == null || categoriaDTO.getDescripcion() == null || categoriaDTO.getDescripcion().isEmpty()) {
                response.put("mensaje", "El ID de la categoría y la descripción son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            String resultado = administradorService.modificarCategoriaDescripcion(id, categoriaDTO.getDescripcion());
            
            if (resultado.contains("Error: La categoría no existe")) {
                response.put("mensaje", "No se encontró la categoría con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.contains("actualizada exitosamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar modificar la categoría");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la categoría debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/borrarCategoria/{id}")
    public ResponseEntity<Map<String, Object>> borrarCategoria(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (id == null) {
                response.put("mensaje", "El ID de la categoría es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (id == 0) {
                response.put("mensaje", "No se puede eliminar la categoría predeterminada 'Sin elegir'");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Boolean resultado = administradorService.eliminarCategoria(id);
            
            if (resultado) {
                response.put("mensaje", "Categoría eliminada correctamente");
                return ResponseEntity.ok(response);
            } else {
                Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM categoria WHERE id_categoria = ?", 
                    Integer.class, 
                    id
                );
                
                if (count != null && count == 0) {
                    response.put("mensaje", "No se encontró la categoría con el ID proporcionado");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                } else {
                    response.put("mensaje", "No se pudo eliminar la categoría. Puede estar siendo utilizada por otros registros.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar eliminar la categoría");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la categoría debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/aprobarCurso")
    public ResponseEntity<Map<String, Object>> aprobarCurso(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer idSolicitudCurso = Integer.parseInt(obj.get("idSolicitudCurso").toString());
            
            if (idSolicitudCurso == null) {
                response.put("mensaje", "El ID de la solicitud de curso es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            String resultado = administradorService.aprobarCurso(idSolicitudCurso);
            
            if (resultado.contains("no existe")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.contains("no está en estado de revisión")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if (resultado.contains("aprobado exitosamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar aprobar el curso");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la solicitud de curso debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
