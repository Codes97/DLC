package entityClasses;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PostlistPK implements Serializable {
    private int idDocument;
    private int idWord;

    public PostlistPK(int idDocument, int idWord) {
        this.idDocument = idDocument;
        this.idWord = idWord;
    }

    public PostlistPK() {
    }

    @Column(name = "idDocument")
    @Id
    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
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
        PostlistPK that = (PostlistPK) o;
        return idDocument == that.idDocument &&
                idWord == that.idWord;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDocument, idWord);
    }
}
