package domainapp.modules.simple.dom.impl.rutina;

import domainapp.modules.simple.dom.impl.ejercicio.Ejercicio;
import domainapp.modules.simple.dom.impl.enums.Estado;
import domainapp.modules.simple.dom.impl.socio.Socio;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.repository.RepositoryService;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

@PersistenceCapable(identityType= IdentityType.DATASTORE, schema="gimnasio", table="rutinas")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="idRutina")
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
public class Rutina {

    @javax.jdo.annotations.Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private Socio socio;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Title(prepend = "Rutina: ")
    @Property()
    private String nombre;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property()
    private Estado estado;

    @Persistent(mappedBy = "rutina", dependentElement = "true")
    @Collection()
    @Getter @Setter
    private List<Ejercicio> ejercicio = new ArrayList<Ejercicio>();

    public Rutina() {
    }

    public Rutina(Socio socio, String nombre, Estado estado, List<Ejercicio> ejercicio) {
        this.socio = socio;
        this.nombre = nombre;
        this.estado = estado;
        this.ejercicio = ejercicio;
    }

    @Action()
    @ActionLayout(named = "Editar")
    public Rutina update(
            @ParameterLayout(named = "Nombre: ")
            final String nombre
    ){
        this.setNombre(nombre);
        return this;
    }

    public String default0Update() { return getNombre(); }

    @Action()
    @ActionLayout(named = "Cargar Ejercicio")
    public Rutina addEjercicio(
            @ParameterLayout(named="Nombre Ejercicio: ") final String nombreEjercicio,
            @ParameterLayout(named="Repeticion: ") final Integer repeticion,
            @ParameterLayout(named="Peso: ") final Integer peso,
            @ParameterLayout(named="Series: ") final Integer series

    ){
        final Ejercicio ejercicio = factoryService.instantiate(Ejercicio.class);
        ejercicio.setRutina(this);
        ejercicio.setNombreEjercicio(nombreEjercicio);
        ejercicio.setRepeticion(repeticion);
        ejercicio.setPeso(peso);
        ejercicio.setSeries(series);
        getEjercicio().add(ejercicio);
        repositoryService.persist(ejercicio);
        return this;
    }

    @Programmatic
    public void CambiarEstado(Estado estado){
        this.setEstado(estado);
    }

    @Action()
    @ActionLayout(named = "Activar")
    public Rutina Activo(){
        CambiarEstado(Estado.Activo);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(named = "Desactivar")
    public Rutina Inactivo(){
        CambiarEstado(Estado.Inactivo);
        return this;
    }

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    FactoryService factoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;
}
