package mx.utng.finer_back_end.Alumnos.Implement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import mx.utng.finer_back_end.Alumnos.Dao.CursoAlumnoDao;
import mx.utng.finer_back_end.Alumnos.Documentos.CertificadoDetalleDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.CursoDetalleAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.PuntuacionAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Services.CursoAlumnoService;

@Service
public class CursoAlumnoImplement implements CursoAlumnoService {

    @Autowired
    private CursoAlumnoDao cursoDao;

    @Override
    @Transactional
    public List<CursoDetalleAlumnoDTO> getCurso(Integer idCurso) {
        List<Object[]> resultados = cursoDao.verCursoDetalles(idCurso);
        List<CursoDetalleAlumnoDTO> detalles = new ArrayList<>();

        for (Object[] row : resultados) {
            CursoDetalleAlumnoDTO cursoDetalle = new CursoDetalleAlumnoDTO(
                    (String) row[0],
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (Integer) row[4],
                    (Integer) row[5]);
            detalles.add(cursoDetalle);
        }
        return detalles;
    }

    @Override
    @Transactional
    public Boolean inscribirseCurso(Integer idUsuario, Integer idCurso) {
        Boolean reinscripcionCurso = cursoDao.validarReinscripcionAlumno(idCurso, idUsuario);
        if (reinscripcionCurso == true) {
            return cursoDao.inscribirseCursoAlumno(idCurso, idUsuario);
        } else {
            return reinscripcionCurso;
        }
    }

    @Override
    @Transactional
    public List<PuntuacionAlumnoDTO> verPuntuacion(Integer idInscripcion) {
        List<Object[]> puntuacionAlumno = cursoDao.verPuntuacion(idInscripcion);
        List<PuntuacionAlumnoDTO> puntuacionAlumnoDTO = new ArrayList<>();

        for (Object[] row : puntuacionAlumno) {
            PuntuacionAlumnoDTO puntacionA = new PuntuacionAlumnoDTO(
                    (Integer) row[0],
                    (Integer) row[1],
                    (BigDecimal) row[2]);
            puntuacionAlumnoDTO.add(puntacionA);
        }
        return puntuacionAlumnoDTO;
    }

    public String bajaCursoAlumno(Integer idInscricpion) {
        return cursoDao.bajaCursoAlumno(idInscricpion);
    }

    @Override
    public CertificadoDetalleDTO obtenerDetallesCertificado(Integer idInscripcion) {
        List<Object[]> resultados = cursoDao.obtenerDetallesCertificado(idInscripcion);
        
        if (resultados != null && !resultados.isEmpty()) {
            Object[] fila = resultados.get(0);
            
            if (fila != null && fila.length >= 8) {
                try {
                    CertificadoDetalleDTO certificadoDetalles = new CertificadoDetalleDTO(
                        (Integer) fila[0], 
                        (String) fila[1], 
                        (String) fila[2],  
                        (String) fila[3], 
                        (String) fila[4],  
                        (String) fila[5], 
                        ((java.sql.Date) fila[6]).toLocalDate(), 
                        LocalDate.now() 
                    );
                    
                    return certificadoDetalles;
                } catch (ClassCastException e) {
                    System.err.println("Error de conversión de tipo: " + e.getMessage());
                    return null;
                }
            } else {
                System.out.println("La fila no tiene la cantidad de columnas esperada.");
                return null; 
            }
        } else {
            System.out.println("No se encontraron resultados para la inscripción " + idInscripcion);
            return null;
        }
    }
    

}
