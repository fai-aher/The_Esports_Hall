package co.edu.uniandes.dse.esports.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa un libro en la persistencia
 *
 * @author 
 */

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
public class LogrosEntity extends BaseEntity {

        private String descripcion;
        
        @PodamExclude
        @ManyToOne
        public JugadorEntity jugador;

}
