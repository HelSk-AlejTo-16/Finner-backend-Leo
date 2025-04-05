package mx.utng.finer_back_end.Instructor.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mx.utng.finer_back_end.Instructor.Dao.SolicitudCursoDaoInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.CursoSolicitadoDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.SolicitudCursoRequest;
import mx.utng.finer_back_end.Instructor.Services.SolicitudCursoServiceInstructor;

@Service
public class SolicitudCursoServiceImplementInstructor implements SolicitudCursoServiceInstructor {

    @Autowired
    private SolicitudCursoDaoInstructor solicitudCursoDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<CursoSolicitadoDTOInstructor> verCursosSolicitados(String estatus, Integer idInstructor) {
        List<Object[]> resultados = solicitudCursoDao.verCursosSolicitados(estatus, idInstructor);
        List<CursoSolicitadoDTOInstructor> cursosSolicitados = new ArrayList<>();

        for (Object[] row : resultados) {
            CursoSolicitadoDTOInstructor dto = new CursoSolicitadoDTOInstructor(
                    (String) row[0], // titulo_curso_solicitado
                    (String) row[1], // descripcion
                    (String) row[2], // nombre_instructor
                    (String) row[3], // nombre_categoria
                    (String) row[4] // estatus
            );
            cursosSolicitados.add(dto);
        }
        return cursosSolicitados;
    }

    @Override
    public boolean editarSolicitudCurso(SolicitudCursoRequest request) {
        try {
            // Convertir los temas a JSON para pasarlos correctamente
            String temasJson = objectMapper.writeValueAsString(request.getTemas());

            // Llamar al método del DAO con los parámetros correctos
            return solicitudCursoDao.editarSolicitudCurso(
                    request.getIdSolicitudCurso(),
                    request.getIdUsuarioInstructor(),
                    request.getTituloCursoSolicitado(),
                    request.getDescripcion(),
                    request.getIdCategoria(),
                    request.getIdCurso(),
                    request.getImagen(),
                    temasJson); // Pasar los temas como String (JSON)
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
