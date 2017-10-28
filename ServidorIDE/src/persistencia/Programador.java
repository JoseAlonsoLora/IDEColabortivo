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
    , @NamedQuery(name = "Programador.findByNombreUsuario", query = "SELECT p FROM Programador p WHERE p.nombreUsuario = :nombreUsuario")
    , @NamedQuery(name = "Programador.findByContrasena", query = "SELECT p FROM Programador p WHERE p.contrasena = :contrasena")
    , @NamedQuery(name = "Programador.findByCorreoElectronico", query = "SELECT p FROM Programador p WHERE p.correoElectronico = :correoElectronico")})
public class Programador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombreUsuario")
    private String nombreUsuario;
    @Column(name = "contrasena")
    private String contrasena;
    @Column(name = "correoElectronico")
    private String correoElectronico;
    @OneToMany(mappedBy = "nombreUsuario")
    private Collection<Desarrolla> desarrollaCollection;

    public Programador() {
    }

    public Programador(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreUsuario != null ? nombreUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programador)) {
            return false;
        }
        Programador other = (Programador) object;
        if ((this.nombreUsuario == null && other.nombreUsuario != null) || (this.nombreUsuario != null && !this.nombreUsuario.equals(other.nombreUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Programador[ nombreUsuario=" + nombreUsuario + " ]";
    }
    
}
