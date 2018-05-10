package entityClasses;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PostlistEntityPK implements Serializable {
    private int idDocuments;
    private int idWord;

    @Column(name = "idDocuments")
    @Id
    public int getIdDocuments() {
        return idDocuments;
    }

    public void setIdDocuments(int idDocuments) {
        this.idDocuments = idDocuments;
    }

    @Column(name = "idWord")
    @Id
    public int getIdWord() {
        return idWord;
    }

    public void setIdWord(int idWord) {
        this.idWord = idWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostlistEntityPK that = (PostlistEntityPK) o;
        return idDocuments == that.idDocuments &&
                idWord == that.idWord;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDocuments, idWord);
    }
}
