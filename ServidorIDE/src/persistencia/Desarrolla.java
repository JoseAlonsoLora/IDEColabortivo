/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author raymu
 */
@Entity
@Table(name = "desarrolla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Desarrolla.findAll", query = "SELECT d FROM Desarrolla d")
    , @NamedQuery(name = "Desarrolla.findByTipoProgramador", query = "SELECT d FROM Desarrolla d WHERE d.tipoProgramador = :tipoProgramador")
    , @NamedQuery(name = "Desarrolla.findByIdDesarrolla", query = "SELECT d FROM Desarrolla d WHERE d.idDesarrolla = :idDesarrolla")})
public class Desarrolla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "tipoProgramador")
    private String tipoProgramador;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDesarrolla")
    private Long idDesarrolla;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne
    private Programador idUsuario;
    @JoinColumn(name = "idProyecto", referencedColumnName = "idProyecto")
    @ManyToOne
    private Proyecto idProyecto;

    public Desarrolla() {
    }

    public Desarrolla(Long idDesarrolla) {
        this.idDesarrolla = idDesarrolla;
    }

    public String getTipoProgramador() {
        return tipoProgramador;
    }

    public void setTipoProgramador(String tipoProgramador) {
        this.tipoProgramador = tipoProgramador;
    }

    public Long getIdDesarrolla() {
        return idDesarrolla;
    }

    public void setIdDesarrolla(Long idDesarrolla) {
        this.idDesarrolla = idDesarrolla;
    }

    public Programador getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Programador idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Proyecto getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Proyecto idProyecto) {
        this.idProyecto = idProyecto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDesarrolla != null ? idDesarrolla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Desarrolla)) {
            return false;
        }
        Desarrolla other = (Desarrolla) object;
        if ((this.idDesarrolla == null && other.idDesarrolla != null) || (this.idDesarrolla != null && !this.idDesarrolla.equals(other.idDesarrolla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Desarrolla[ idDesarrolla=" + idDesarrolla + " ]";
    }
    
}
