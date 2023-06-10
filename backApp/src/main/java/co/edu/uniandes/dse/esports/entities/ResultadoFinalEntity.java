package co.edu.uniandes.dse.esports.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;


/**
 * Clase que representa un resultado final como entidad.
 *
 * @author fai-aher
 */

 @Getter
 @Setter
 @Entity
 @EqualsAndHashCode(callSuper = true)

public class ResultadoFinalEntity extends BaseEntity {

    private Integer posicionFinal;
    private Boolean parteDeEmpate;

    @PodamExclude
    @ManyToOne
    private CompetenciaEntity competenciaRelacionada;

    @PodamExclude
    @ManyToOne
    private EquipoEntity equipoInvolucrado;






    
}
