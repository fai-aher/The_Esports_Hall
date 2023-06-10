package co.edu.uniandes.dse.esports.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EquipoDetailDTO extends EquipoDTO{

    private List<TorneoDTO> torneosParticipados = new ArrayList<>();
    private List<CompetenciaDTO> competenciasParticipadas = new ArrayList<>();
    private List<CompetenciaDTO> competenciasGanadas = new ArrayList<>();
    private List<ResultadoFinalDTO> resultadosEnCompetencias = new ArrayList<>();
    private List<JugadorDTO> integrantes = new ArrayList<>();


}
