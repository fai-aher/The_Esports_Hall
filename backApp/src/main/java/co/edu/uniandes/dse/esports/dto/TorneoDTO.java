package co.edu.uniandes.dse.esports.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class TorneoDTO {
    private long id;
    private Date fechaFinalizacion;
    private String paisRealizacion;
    private String nombreTorneo;
    private String imagenRepresentativa;
    private String enlacePaginaWeb;
    private String organizador;
    private String videojuego;
}
