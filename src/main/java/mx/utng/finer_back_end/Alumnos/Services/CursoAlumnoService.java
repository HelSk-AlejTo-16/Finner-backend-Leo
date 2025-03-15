package mx.utng.finer_back_end.Alumnos.Services;

import java.util.List;
import mx.utng.finer_back_end.Alumnos.Documentos.CursoDetalleAlumnoDTO;

public interface CursoAlumnoService {
    /*
     * Metodo getCurso(idCurso)
     * idCurso Int 
     * Return: Obj Curso
     */
    List<CursoDetalleAlumnoDTO> getCurso(Integer idCurso);

    Boolean inscribirseCurso(Integer idUsuario, Integer idCurso);
}
