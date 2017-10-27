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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "archivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Archivo.findAll", query = "SELECT a FROM Archivo a")
    , @NamedQuery(name = "Archivo.findByIdArchivo", query = "SELECT a FROM Archivo a WHERE a.idArchivo = :idArchivo")
    , @NamedQuery(name = "Archivo.findByNombre", query = "SELECT a FROM Archivo a WHERE a.nombre = :nombre")})
public class Archivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idArchivo")
    private Long idArchivo;
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "contenido")
    private String contenido;
    @OneToMany(mappedBy = "idArchivoPadre")
    private Collection<Archivo> archivoCollection;
    @JoinColumn(name = "idArchivoPadre", referencedColumnName = "idArchivo")
    @ManyToOne
    private Archivo idArchivoPadre;
    @OneToMany(mappedBy = "idArchivo")
    private Collection<Proyecto> proyectoCollection;

    public Archivo() {
    }

    public Archivo(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Long getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @XmlTransient
    public Collection<Archivo> getArchivoCollection() {
        return archivoCollection;
    }

    public void setArchivoCollection(Collection<Archivo> archivoCollection) {
        this.archivoCollection = archivoCollection;
    }

    public Archivo getIdArchivoPadre() {
        return idArchivoPadre;
    }

    public void setIdArchivoPadre(Archivo idArchivoPadre) {
        this.idArchivoPadre = idArchivoPadre;
    }

    @XmlTransient
    public Collection<Proyecto> getProyectoCollection() {
        return proyectoCollection;
    }

    public void setProyectoCollection(Collection<Proyecto> proyectoCollection) {
        this.proyectoCollection = proyectoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArchivo != null ? idArchivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Archivo)) {
            return false;
        }
        Archivo other = (Archivo) object;
        if ((this.idArchivo == null && other.idArchivo != null) || (this.idArchivo != null && !this.idArchivo.equals(other.idArchivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Archivo[ idArchivo=" + idArchivo + " ]";
    }
    
}
