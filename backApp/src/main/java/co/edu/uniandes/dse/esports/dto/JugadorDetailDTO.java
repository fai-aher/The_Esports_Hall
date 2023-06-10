package co.edu.uniandes.dse.esports.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorDetailDTO extends JugadorDTO{
    
    private List<TorneoDTO> torneosParticipados = new ArrayList<>();
    private List<CompetenciaDTO> competencia;
    private List<CompetenciaDTO> competenciasParticipadas = new ArrayList<>();
    private List<LogrosDTO> logrosObtenidos = new ArrayList<>();
}
