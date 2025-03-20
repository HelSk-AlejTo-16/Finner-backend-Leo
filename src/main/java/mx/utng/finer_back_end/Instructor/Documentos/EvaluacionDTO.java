package mx.utng.finer_back_end.Instructor.Documentos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class EvaluacionDTO {

    @Id
    private Integer idEvaluacion;

    private Integer idCurso;
    private String tituloEvaluacion;
    private String fechaCreacion;

    // Constructor sin parámetros
    public EvaluacionDTO() {}

    // Constructor con parámetros
    public EvaluacionDTO(Integer idEvaluacion, Integer idCurso, String tituloEvaluacion, String fechaCreacion) {
        this.idEvaluacion = idEvaluacion;
        this.idCurso = idCurso;
        this.tituloEvaluacion = tituloEvaluacion;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters and Setters
    public Integer getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Integer idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getTituloEvaluacion() {
        return tituloEvaluacion;
    }

    public void setTituloEvaluacion(String tituloEvaluacion) {
        this.tituloEvaluacion = tituloEvaluacion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
