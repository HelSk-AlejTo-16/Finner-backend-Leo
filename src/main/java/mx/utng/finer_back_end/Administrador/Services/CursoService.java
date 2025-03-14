package mx.utng.finer_back_end.Administrador.Services;

import java.util.List;
import mx.utng.finer_back_end.Administrador.Documentos.CursoDetalleDTO;

public interface CursoService {
    /*
     * Metodo getCurso(idCurso)
     * idCurso Int 
     * Return: Obj Curso
     */
    List<CursoDetalleDTO> getCurso(Integer idCurso);
}
