/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author John
 */
@Entity
@Table(name = "documents")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documents.findAll", query = "SELECT d FROM Documents d")
    , @NamedQuery(name = "Documents.findByIdDocuments", query = "SELECT d FROM Documents d WHERE d.idDocuments = :idDocuments")
    , @NamedQuery(name = "Documents.findByTitle", query = "SELECT d FROM Documents d WHERE d.title = :title")
    , @NamedQuery(name = "Documents.findByUrl", query = "SELECT d FROM Documents d WHERE d.url = :url")})
public class Documents implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idDocuments")
    private Integer idDocuments;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;

    public Documents() {
    }

    public Documents(Integer idDocuments) {
        this.idDocuments = idDocuments;
    }

    public Integer getIdDocuments() {
        return idDocuments;
    }

    public void setIdDocuments(Integer idDocuments) {
        this.idDocuments = idDocuments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocuments != null ? idDocuments.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documents)) {
            return false;
        }
        Documents other = (Documents) object;
        if ((this.idDocuments == null && other.idDocuments != null) || (this.idDocuments != null && !this.idDocuments.equals(other.idDocuments))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Documents[ idDocuments=" + idDocuments + " ]";
    }
    
}
