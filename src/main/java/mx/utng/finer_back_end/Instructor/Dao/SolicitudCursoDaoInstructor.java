package mx.utng.finer_back_end.Instructor.Dao;

import java.lang.reflect.Array;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CursoDocumento;
import mx.utng.finer_back_end.Instructor.Documentos.TemaDTO;

@Repository
public interface SolicitudCursoDaoInstructor extends JpaRepository<CursoDocumento, Integer> {
    @Query(value = "SELECT * FROM verCursosSolicitados(:estatus, :idInstructor)", nativeQuery = true)
    List<Object[]> verCursosSolicitados(@Param("estatus") String estatus, @Param("idInstructor") Integer idInstructor);

    @Query(value = "SELECT editar_solicitud_curso(:idSolicitudCurso, :idUsuarioInstructor, :tituloCursoSolicitado, "
            + ":descripcion, :idCategoria, :idCurso, :imagen, CAST(:temasJson AS jsonb))", nativeQuery = true)
    boolean editarSolicitudCurso(
            @Param("idSolicitudCurso") Integer idSolicitudCurso,
            @Param("idUsuarioInstructor") Integer idUsuarioInstructor,
            @Param("tituloCursoSolicitado") String tituloCursoSolicitado,
            @Param("descripcion") String descripcion,
            @Param("idCategoria") Integer idCategoria,
            @Param("idCurso") Integer idCurso,
            @Param("imagen") String imagen,
            @Param("temasJson") String temasJson);

}
