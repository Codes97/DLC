package entityClasses;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "words", schema = "dlc", catalog = "")
public class WordsEntity {
    private int idWord;
    private String word;
    private Integer maxFrequency;
    private Integer maxDocuments;

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
        WordsEntity that = (WordsEntity) o;
        return idWord == that.idWord &&
                Objects.equals(word, that.word) &&
                Objects.equals(maxFrequency, that.maxFrequency) &&
                Objects.equals(maxDocuments, that.maxDocuments);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWord, word, maxFrequency, maxDocuments);
    }
}
