/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author raymu
 */
@Entity
@Table(name = "proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proyecto.findAll", query = "SELECT p FROM Proyecto p")
    , @NamedQuery(name = "Proyecto.findByIdProyecto", query = "SELECT p FROM Proyecto p WHERE p.idProyecto = :idProyecto")
    , @NamedQuery(name = "Proyecto.findByNombre", query = "SELECT p FROM Proyecto p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Proyecto.findByFechaCreacion", query = "SELECT p FROM Proyecto p WHERE p.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Proyecto.findByLenguaje", query = "SELECT p FROM Proyecto p WHERE p.lenguaje = :lenguaje")})
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProyecto")
    private Long idProyecto;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fechaCreacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "lenguaje")
    private String lenguaje;
    @OneToMany(mappedBy = "idProyecto")
    private Collection<Desarrolla> desarrollaCollection;
    @JoinColumn(name = "idArchivo", referencedColumnName = "idArchivo")
    @ManyToOne
    private Archivo idArchivo;

    public Proyecto() {
    }

    public Proyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    @XmlTransient
    public Collection<Desarrolla> getDesarrollaCollection() {
        return desarrollaCollection;
    }

    public void setDesarrollaCollection(Collection<Desarrolla> desarrollaCollection) {
        this.desarrollaCollection = desarrollaCollection;
    }

    public Archivo getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Archivo idArchivo) {
        this.idArchivo = idArchivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProyecto != null ? idProyecto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proyecto)) {
            return false;
        }
        Proyecto other = (Proyecto) object;
        if ((this.idProyecto == null && other.idProyecto != null) || (this.idProyecto != null && !this.idProyecto.equals(other.idProyecto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Proyecto[ idProyecto=" + idProyecto + " ]";
    }
    
}
