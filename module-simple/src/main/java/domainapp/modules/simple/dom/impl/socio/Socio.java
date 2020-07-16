package domainapp.modules.simple.dom.impl.socio;

import domainapp.modules.simple.dom.impl.pagos.Pago;
import domainapp.modules.simple.dom.impl.persona.Persona;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.util.ArrayList;
import java.util.List;

@PersistenceCapable(identityType= IdentityType.DATASTORE, schema="gimnasio", table="socios")
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@javax.jdo.annotations.Unique(name="Socio_dni_UNQ", members = {"dni"})
@lombok.Getter @lombok.Setter


public class Socio extends Persona  {

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String historiaClinica;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private Integer nroEmergencia;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private Integer peso;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private Integer altura;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private Boolean asistencia;

    @Persistent(mappedBy = "socio", dependentElement = "true")
    @Collection()
    @Getter  @Setter
    private List<Pago> pago = new ArrayList<Pago>();

    public List<Pago> getPago() {
        return pago;
    }

    public void setPago(List<Pago> pago) {
        this.pago = pago;
    }

    public Socio() {
    }

    public Socio(String nombre, String apellido, Integer dni, Integer telefono, String direccion, LocalDate fechaNac, Integer estado, String historiaClinica, Integer nroEmergencia, Integer peso, Integer altura, Boolean asistencia) {
        super(nombre, apellido, dni, telefono, direccion, fechaNac, estado);
        this.historiaClinica = historiaClinica;
        this.nroEmergencia = nroEmergencia;
        this.peso = peso;
        this.altura = altura;
        this.asistencia = asistencia;
    }

    public Socio(String nombre, String apellido, Integer dni, Integer telefono, String direccion, LocalDate fechaNac, Integer estado, String historiaClinica, Integer nroEmergencia, Integer peso, Integer altura, Boolean asistencia, List<Pago> pago) {
        super(nombre, apellido, dni, telefono, direccion, fechaNac, estado);
        this.historiaClinica = historiaClinica;
        this.nroEmergencia = nroEmergencia;
        this.peso = peso;
        this.altura = altura;
        this.asistencia = asistencia;
        this.pago = pago;
    }



    @Action()
    public Socio cargarPago(
            @ParameterLayout(named="Dias Por Semana") final int diasPorSem,
            @ParameterLayout(named="Monto a Pagar") final int monto,
            @ParameterLayout(named="Fecha de Pago") final LocalDate fechaDePago,
            @ParameterLayout(named="Fecha de Proximo Pago") final LocalDate proximoPago
    ){

        final Pago pago = factoryService.instantiate(Pago.class);
        pago.setSocio(this);
        pago.setDiasPorSem(diasPorSem);
        pago.setMonto(monto);
        pago.setFechaDePago(fechaDePago);
        pago.setProximoPago(proximoPago);
        getPago().add(pago);
        repositoryService.persist(pago);

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
