package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.SpendEntity;


import static guru.qa.niffler.db.Database.*;

public class SpendRepositoryHibernate extends JpaService implements SpendRepository {

    public SpendRepositoryHibernate() {
        super(SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager());
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        persist(SPEND, spend.getCategory());
        persist(SPEND, spend);
        return spend;
    }
}
