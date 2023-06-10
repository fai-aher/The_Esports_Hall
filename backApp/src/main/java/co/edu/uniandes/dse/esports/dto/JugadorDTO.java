package co.edu.uniandes.dse.esports.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorDTO {

    private long id;
    private String nombre;
    private String nickname;
    private String nacionalidad;
    private String fotografia;
    private Date fechaNacimiento;
    private EquipoDTO equipo;
}
