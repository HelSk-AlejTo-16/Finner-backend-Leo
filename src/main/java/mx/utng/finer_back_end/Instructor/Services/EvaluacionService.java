package mx.utng.finer_back_end.Instructor.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.EvaluacionDao;
import mx.utng.finer_back_end.Instructor.Documentos.EvaluacionDTO;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionDao evaluacionDao;

    /**
     * Crea una nueva evaluación utilizando el repositorio.
     *
     * @param evaluacionDTO objeto con los datos de la evaluación.
     * @return ID de la evaluación creada.
     */
    public Integer crearEvaluacion(EvaluacionDTO evaluacionDTO) {
        // Usar el repositorio para crear la evaluación y obtener su ID
        return evaluacionDao.generarEvaluacion(evaluacionDTO.getIdCurso(), evaluacionDTO.getTituloEvaluacion());

        
    }
}
