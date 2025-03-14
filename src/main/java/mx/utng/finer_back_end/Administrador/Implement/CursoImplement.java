package mx.utng.finer_back_end.Administrador.Implement;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import mx.utng.finer_back_end.Administrador.Dao.CursoDao;
import mx.utng.finer_back_end.Administrador.Documentos.CursoDetalleDTO;
import mx.utng.finer_back_end.Administrador.Services.CursoService;

@Service
public class CursoImplement implements CursoService {

    @Autowired
    private CursoDao cursoDao;

    @Override
    @Transactional
    public List<CursoDetalleDTO> getCurso(Integer idCurso) {
        List<Object[]> resultados = cursoDao.verCursoDetalles(idCurso);
        List<CursoDetalleDTO> detalles = new ArrayList<>();

        for (Object[] row : resultados) {
            CursoDetalleDTO cursoDetalle = new CursoDetalleDTO(
                (String) row[0],  
                (String) row[1],  
                (String) row[2], 
                (String) row[3],  
                (Integer) row[4], 
                (Integer) row[5] 
            );
            detalles.add(cursoDetalle);
        }
        return detalles;
    }
}
