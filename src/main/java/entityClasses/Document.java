package entityClasses;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "documents", schema = "dlc", catalog = "")
public class Document implements Serializable {
    private int idDocument;
    private String docName;
    private String url;

    @Id
    @Column(name = "idDocument")
    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }

    @Basic
    @Column(name = "docName")
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return idDocument == document.idDocument &&
                Objects.equals(docName, document.docName) &&
                Objects.equals(url, document.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDocument, docName, url);
    }
}