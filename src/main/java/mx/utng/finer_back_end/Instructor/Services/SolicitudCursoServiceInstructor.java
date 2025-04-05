package mx.utng.finer_back_end.Instructor.Services;

import java.util.List;

import mx.utng.finer_back_end.Instructor.Documentos.CursoSolicitadoDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.SolicitudCursoRequest;

public interface SolicitudCursoServiceInstructor {
    List<CursoSolicitadoDTOInstructor> verCursosSolicitados(String estatus, Integer idInstructor);

    boolean editarSolicitudCurso(SolicitudCursoRequest request);
}
