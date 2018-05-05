/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "words")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Words.findAll", query = "SELECT w FROM Words w")
    , @NamedQuery(name = "Words.findByIdWord", query = "SELECT w FROM Words w WHERE w.idWord = :idWord")
    , @NamedQuery(name = "Words.findByWord", query = "SELECT w FROM Words w WHERE w.word = :word")
    , @NamedQuery(name = "Words.findByMaxFrequency", query = "SELECT w FROM Words w WHERE w.maxFrequency = :maxFrequency")
    , @NamedQuery(name = "Words.findByMaxBooks", query = "SELECT w FROM Words w WHERE w.maxBooks = :maxBooks")})
public class Words implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idWord")
    private Integer idWord;
    @Column(name = "word")
    private String word;
    @Column(name = "maxFrequency")
    private Integer maxFrequency;
    @Column(name = "maxBooks")
    private Integer maxBooks;

    public Words() {
    }

    public Words(Integer idWord) {
        this.idWord = idWord;
    }

    public Integer getIdWord() {
        return idWord;
    }

    public void setIdWord(Integer idWord) {
        this.idWord = idWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(Integer maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public Integer getMaxBooks() {
        return maxBooks;
    }

    public void setMaxBooks(Integer maxBooks) {
        this.maxBooks = maxBooks;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idWord != null ? idWord.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Words)) {
            return false;
        }
        Words other = (Words) object;
        if ((this.idWord == null && other.idWord != null) || (this.idWord != null && !this.idWord.equals(other.idWord))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.Words[ idWord=" + idWord + " ]";
    }
    
}
