package co.edu.uniandes.dse.esports.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CompetenciaDetailDTO extends CompetenciaDTO{

    private List<EquipoDTO> equiposParticipantes = new ArrayList<>();
    private List<ResultadoFinalDTO> resultadosFinales = new ArrayList<>();
    
}
