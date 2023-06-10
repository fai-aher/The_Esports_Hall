package co.edu.uniandes.dse.esports.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TorneoDetailDTO extends TorneoDTO{
    private List<EquipoDTO> equiposParticipantes = new ArrayList<>();
    private List<CompetenciaDTO> competencias = new ArrayList<>();
}
