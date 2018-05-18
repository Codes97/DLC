package entityControllers;

import entityDao.PostlistDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PostlistController {
    @Inject
    PostlistDao dao;

    public PostlistController() {
    }
}
