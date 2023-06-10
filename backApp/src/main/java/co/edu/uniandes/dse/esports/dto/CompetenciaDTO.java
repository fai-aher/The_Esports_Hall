package co.edu.uniandes.dse.esports.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CompetenciaDTO {
	private Long id;
	private String nombre;
	private String duracion;
	private EquipoDTO equipoGanador;
	private TorneoDTO torneo;
    private JugadorDTO jugador;
    private JugadorDTO mvp;
    
}


