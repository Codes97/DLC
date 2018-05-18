package entityControllers;

import entityDao.DocumentDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DocumentController {
    @Inject
    DocumentDao dao;

    public DocumentController() {
    }
}
