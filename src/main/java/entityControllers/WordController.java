package entityControllers;

import entityClasses.Word;
import entityDao.WordDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class WordController {
    @Inject
    WordDao dao;

    public WordController() {
    }

    public Word add(int idWord, String word, Integer maxFrequency, Integer maxDocuments) {
        Word w = new Word(idWord, word, maxFrequency, maxDocuments);
        dao.add(w);
        return w;
    }
}
