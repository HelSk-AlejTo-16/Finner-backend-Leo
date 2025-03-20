package mx.utng.finer_back_end.Administrador.Implement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import mx.utng.finer_back_end.Administrador.Services.AdministradorService;
import java.util.Map;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private JavaMailSender javaMailSender;

    private final String API_KEY = "fb9a6eb9-05c4-4c7f-8b5b-9900053358cb";
    private final String API_URL = "https://api.apis.net.mx/v1/cedulaprofesional/";

    @Override
    @Transactional
    public String eliminarAlumnoCurso(String matricula, Integer idCurso) {
        try {
            String resultado = jdbcTemplate.queryForObject(
                "SELECT eliminar_alumno_curso(?, ?)", 
                String.class, 
                matricula, 
                idCurso
            );
            return resultado;
        } catch (Exception e) {
            return "Error al eliminar al alumno del curso: " + e.getMessage();
        }
    }
    
    @Override
    @Transactional
    public String rechazarCurso(Long idSolicitudCurso, String correoInstructor, String motivoRechazo, String tituloCurso) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM solicitudcurso WHERE id_solicitud_curso = ?", 
                Integer.class, 
                idSolicitudCurso
            );
            
            if (count != null && count > 0) {
                String estadoActual = jdbcTemplate.queryForObject(
                    "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                    String.class,
                    idSolicitudCurso
                );
                
                if ("rechazado".equals(estadoActual)) {
                    return "La solicitud ya ha sido rechazada anteriormente";
                }
                
                if ("aprobado".equals(estadoActual)) {
                    return "No se puede rechazar una solicitud que ya ha sido aprobada";
                }
                
                enviarCorreoRechazo(correoInstructor, motivoRechazo, tituloCurso);
                
                int filasAfectadas = jdbcTemplate.update(
                    "UPDATE solicitudcurso SET estatus = 'rechazado' WHERE id_solicitud_curso = ?", 
                    idSolicitudCurso
                );
                
                if (filasAfectadas > 0) {
                    return "Rechazado";
                } else {
                    return "Error al actualizar el registro";
                }
            } else {
                return "No se encontró la solicitud de curso con el ID proporcionado";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al rechazar el curso: " + e.getMessage();
        }
    }
    
    private void enviarCorreoRechazo(String correoInstructor, String motivoRechazo, String tituloCurso) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("finner.oficial.2025@gmail.com");
            mensaje.setTo(correoInstructor);
            mensaje.setSubject("Solicitud de curso rechazada - Finner");
            
            String cuerpoMensaje = "Estimado instructor,\n\n" +
                    "Le informamos que su solicitud para el curso \"" + tituloCurso + "\" ha sido rechazada.\n\n" +
                    "Motivo del rechazo: " + motivoRechazo + "\n\n" +
                    "Si tiene alguna duda o desea más información, por favor contacte al equipo administrativo.\n\n" +
                    "Atentamente,\n" +
                    "El equipo de Finner";
            
            mensaje.setText(cuerpoMensaje);
            javaMailSender.send(mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de rechazo: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public String crearCategoria(Integer idUsuarioInstructor, Integer idUsuarioAdmin, String nombreCategoria, String descripcion) {
        try {
            String resultado = jdbcTemplate.queryForObject(
                "SELECT solicitar_creacion_categoria(?, ?, ?, ?)", 
                String.class, 
                idUsuarioInstructor, 
                idUsuarioAdmin, 
                nombreCategoria, 
                descripcion
            );
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al crear la solicitud de categoría: " + e.getMessage();
        }
    }

    @Override
    @Transactional
    public String bloquearUsuario(String nombreUsuario) {
        try {
            String resultado = jdbcTemplate.queryForObject(
                "SELECT bloquear_usuario(?)", 
                String.class, 
                nombreUsuario
            );
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al bloquear al usuario: " + e.getMessage();
        } 
    }

    @Override
    @Transactional
    public String modificarCategoriaDescripcion(Integer idCategoria, String nuevaDescripcion) {
        try {
            String resultado = jdbcTemplate.queryForObject(
                "SELECT modificar_desc_categoria(?, ?)", 
                String.class, 
                idCategoria, 
                nuevaDescripcion
            );
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al modificar la descripción de la categoría: " + e.getMessage();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUsuario(String nombreUsuario) {
        try {
            Map<String, Object> usuario = jdbcTemplate.queryForMap(
                "SELECT u.*, r.rol " +
                "FROM Usuario u " +
                "JOIN Rol r ON u.id_rol = r.id_rol " +
                "WHERE u.nombre_usuario = ?",
                nombreUsuario
            );

            if (usuario.containsKey("numero_cedula") && usuario.get("numero_cedula") != null) {
                String numeroCedula = usuario.get("numero_cedula").toString();
                Map<String, Object> datosCedula = validarCedulaProfesional(numeroCedula);
                usuario.put("datos_cedula", datosCedula);
            }

            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al obtener los datos del usuario: " + e.getMessage());
            return error;
        }
    }

    private Map<String, Object> validarCedulaProfesional(String numeroCedula) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", API_KEY);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + numeroCedula,
                HttpMethod.GET,
                entity,
                Map.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al validar la cédula profesional: " + e.getMessage());
            return error;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarUsuarioNombre(String busqueda) {
        try {
            String termino = "%" + busqueda.toLowerCase() + "%";
            return jdbcTemplate.queryForList(
                "SELECT u.*, r.rol " +
                "FROM Usuario u " +
                "JOIN Rol r ON u.id_rol = r.id_rol " +
                "WHERE LOWER(u.nombre) LIKE ? " +
                "   OR LOWER(u.apellido_paterno) LIKE ? " +
                "   OR LOWER(u.apellido_materno) LIKE ?",
                termino, termino, termino
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Boolean eliminarCategoria(Integer idCategoria) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM categoria WHERE id_categoria = ?", 
                Integer.class, 
                idCategoria
            );
            
            if (count == null || count == 0) {
                System.err.println("La categoría con ID " + idCategoria + " no existe.");
                return false;
            }
            
            if (idCategoria == 0) {
                System.err.println("No se puede eliminar la categoría predeterminada (ID 0).");
                return false;
            }
            
            int cursosActualizados = jdbcTemplate.update(
                "UPDATE curso SET id_categoria = 0 WHERE id_categoria = ?", 
                idCategoria
            );
            
            int solicitudesActualizadas = jdbcTemplate.update(
                "UPDATE solicitudcurso SET id_categoria = 0 WHERE id_categoria = ?", 
                idCategoria
            );
            
            System.out.println("Cursos reasignados: " + cursosActualizados);
            System.out.println("Solicitudes reasignadas: " + solicitudesActualizadas);
            
            int rowsAffected = jdbcTemplate.update(
                "DELETE FROM categoria WHERE id_categoria = ?", 
                idCategoria
            );
            
            System.out.println("Filas afectadas al eliminar categoría: " + rowsAffected);
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar categoría: " + idCategoria);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public String aprobarCurso(Integer idSolicitudCurso) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM solicitudcurso WHERE id_solicitud_curso = ?", 
                Integer.class, 
                idSolicitudCurso
            );
            
            if (count == null || count == 0) {
                return "La solicitud de curso no existe.";
            }
            
            String estadoActual = jdbcTemplate.queryForObject(
                "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                String.class,
                idSolicitudCurso
            );
            
            if ("aprobado".equals(estadoActual)) {
                return "La solicitud ya ha sido aprobada anteriormente";
            }
            
            if ("rechazado".equals(estadoActual)) {
                return "No se puede aprobar una solicitud que ya ha sido rechazada";
            }
            
            int filasAfectadas = jdbcTemplate.update(
                "UPDATE solicitudcurso SET estatus = 'aprobado' WHERE id_solicitud_curso = ?", 
                idSolicitudCurso
            );
            
            if (filasAfectadas > 0) {
                return "El curso ha sido aprobado exitosamente.";
            } else {
                return "Error al actualizar el registro";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al aprobar el curso: " + e.getMessage();
        }
    }
}
