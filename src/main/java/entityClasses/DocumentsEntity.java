package entityClasses;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "documents", schema = "dlc", catalog = "")
public class DocumentsEntity {
    private int idDocument;
    private String nombre;
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
    @Column(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        DocumentsEntity that = (DocumentsEntity) o;
        return idDocument == that.idDocument &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDocument, nombre, url);
    }
}
