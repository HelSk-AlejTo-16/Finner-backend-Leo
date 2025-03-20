package mx.utng.finer_back_end.Instructor.Implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import mx.utng.finer_back_end.Instructor.Dao.CursoCategoriaDao;
import mx.utng.finer_back_end.Instructor.Documentos.CorreccionCursoDTO;
import mx.utng.finer_back_end.Instructor.Documentos.CursoDetalleCategoriaDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoCategoriaService;

@Service
public class CursoCategoriaImplement implements CursoCategoriaService{
     @Autowired
    private CursoCategoriaDao cursoCategoriaDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CursoDetalleCategoriaDTO> filtrarCursoCategoria(String busqueda) {
        List<Object[]> resultados = cursoCategoriaDao.filtrarCursosCategoria(busqueda);
        List<CursoDetalleCategoriaDTO> detalles = new ArrayList<>();

        for (Object[] row : resultados) {
            CursoDetalleCategoriaDTO dto = new CursoDetalleCategoriaDTO(
                (String) row[0], // titulo_curso
                (String) row[1], // descripcion
                (String) row[2], // nombre_instructor
                (String) row[3], // apellido_paterno
                (String) row[4], // apellido_materno
                (String) row[5]  // nombre_categoria
            );
            detalles.add(dto);
        }
        return detalles;
    }

     
    //VerCategoriaSolicitada
    @Override
    public Map<String, Object> verCategoriaSolicitada(Integer idInstructor, Integer idEstatus) {
        try {
            String sql = "SELECT verificar_categoria_solicitada(?, ?)";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, idInstructor, idEstatus);
            return result;
        } catch (Exception e) {
            return null;
        }      
    }

     @Override
    public Map<String, Object> corregirCursoRechazado(Integer id, Integer idEstatus, CorreccionCursoDTO correccion) {
        try {
            String sql = "SELECT corregir_curso_rechazado(?, ?, ?, ?, ?)";
            Map<String, Object> result = jdbcTemplate.queryForMap(
                sql, 
                id,
                idEstatus,
                correccion.getTituloCurso(),
                correccion.getDescripcion(),
                correccion.getObservaciones()
            );
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
