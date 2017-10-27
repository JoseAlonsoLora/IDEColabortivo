/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author raymu
 */
@Entity
@Table(name = "programador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programador.findAll", query = "SELECT p FROM Programador p")
    , @NamedQuery(name = "Programador.findByIdUsuario", query = "SELECT p FROM Programador p WHERE p.idUsuario = :idUsuario")
    , @NamedQuery(name = "Programador.findByCorreoElectronico", query = "SELECT p FROM Programador p WHERE p.correoElectronico = :correoElectronico")})
public class Programador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private Long idUsuario;
    @Column(name = "correoElectronico")
    private String correoElectronico;
    @OneToMany(mappedBy = "idUsuario")
    private Collection<Desarrolla> desarrollaCollection;
    @OneToMany(mappedBy = "idUsuario")
    private Collection<Cuenta> cuentaCollection;

    public Programador() {
    }

    public Programador(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @XmlTransient
    public Collection<Desarrolla> getDesarrollaCollection() {
        return desarrollaCollection;
    }

    public void setDesarrollaCollection(Collection<Desarrolla> desarrollaCollection) {
        this.desarrollaCollection = desarrollaCollection;
    }

    @XmlTransient
    public Collection<Cuenta> getCuentaCollection() {
        return cuentaCollection;
    }

    public void setCuentaCollection(Collection<Cuenta> cuentaCollection) {
        this.cuentaCollection = cuentaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programador)) {
            return false;
        }
        Programador other = (Programador) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Programador[ idUsuario=" + idUsuario + " ]";
    }
    
}
