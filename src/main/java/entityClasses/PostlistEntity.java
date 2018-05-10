package entityClasses;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "postlist", schema = "dlc", catalog = "")
@IdClass(PostlistEntityPK.class)
public class PostlistEntity {
    private int idDocuments;
    private int idWord;
    private Integer frequency;

    @Id
    @Column(name = "idDocuments")
    public int getIdDocuments() {
        return idDocuments;
    }

    public void setIdDocuments(int idDocuments) {
        this.idDocuments = idDocuments;
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
        PostlistEntity that = (PostlistEntity) o;
        return idDocuments == that.idDocuments &&
                idWord == that.idWord &&
                Objects.equals(frequency, that.frequency);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDocuments, idWord, frequency);
    }
}
