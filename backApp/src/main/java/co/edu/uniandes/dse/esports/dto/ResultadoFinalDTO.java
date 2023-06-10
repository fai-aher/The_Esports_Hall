package co.edu.uniandes.dse.esports.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoFinalDTO {

    private long id;
    private Integer posicionFinal;
    private Boolean parteDeEmpate;
    private EquipoDTO equipoInvolucrado;
    private CompetenciaDTO competenciaRelacionada;
    
}
