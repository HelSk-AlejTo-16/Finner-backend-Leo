package mx.utng.finer_back_end.Administrador.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoDetalleDTO {
    private String titulo;
    private String descripcion;
    private String instructor;
    private String categoria;
    private Integer cantidadTemas;
    private Integer cantidadInscritos;
}