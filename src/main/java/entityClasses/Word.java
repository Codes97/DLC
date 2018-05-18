package entityClasses;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "words", schema = "dlc", catalog = "")
@NamedQueries(
        @NamedQuery(name="insertWord",
                query="INSERT INTO words(idWord, word, maxFrequency, maxDocuments) VALUES(?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "maxFrequency=" +
                        "(SELECT MAX(p.frequency) " +
                        "FROM postlist p JOIN words w ON(p.idWord=w.idWord) " +
                        "WHERE w.word = ?), " +
                        "maxDocuments=(SELECT COUNT(*) FROM postlist WHERE idDocument=?)")
)

public class Word implements Serializable {
    private int idWord;
    private String word;
    private Integer maxFrequency;
    private Integer maxDocuments;

    public Word(int idWord, String word, Integer maxFrequency, Integer maxDocuments) {
        this.idWord = idWord;
        this.word = word;
        this.maxFrequency = maxFrequency;
        this.maxDocuments = maxDocuments;
    }

    public Word() {
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
    @Column(name = "word")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Basic
    @Column(name = "maxFrequency")
    public Integer getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(Integer maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    @Basic
    @Column(name = "maxDocuments")
    public Integer getMaxDocuments() {
        return maxDocuments;
    }

    public void setMaxDocuments(Integer maxDocuments) {
        this.maxDocuments = maxDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return idWord == word1.idWord &&
                Objects.equals(word, word1.word) &&
                Objects.equals(maxFrequency, word1.maxFrequency) &&
                Objects.equals(maxDocuments, word1.maxDocuments);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWord, word, maxFrequency, maxDocuments);
    }
}