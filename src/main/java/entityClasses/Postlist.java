package entityClasses;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(PostlistPK.class)
public class Postlist implements Serializable {
    private int idDocument;
    private int idWord;
    private Integer frequency;

    public Postlist() {
    }

    public Postlist(int idDocument, int idWord, Integer frequency) {
        this.idDocument = idDocument;
        this.idWord = idWord;
        this.frequency = frequency;
    }

    @Id
    @Column(name = "idDocument")
    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }

    @Id
    @Column(name = "idWord")
    public int getIdWord() {
        return idWord;
    }

    public void setIdWord(int idWord) {
        this.idWord = idWord;
    }

    @Basic
    @Column(name = "frequency")
    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Postlist postlist = (Postlist) o;
        return idDocument == postlist.idDocument &&
                idWord == postlist.idWord &&
                Objects.equals(frequency, postlist.frequency);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDocument, idWord, frequency);
    }
}
