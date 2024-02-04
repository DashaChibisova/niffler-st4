package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.db.Database.*;

public class SpendRepositoryHibernate extends JpaService implements SpendRepository {

    public SpendRepositoryHibernate() {
        super(SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager());
    }

    @Override
    public SpendEntity createInSpend(SpendEntity spend) {
        persist(SPEND, spend.getCategory());
        persist(SPEND, spend);
        return spend;
    }

    @Override
    public void deleteInSpendByCategoryId(UUID id) {
        CategoryEntity toBeDeletedCategory = Optional.of(entityManager(SPEND).find(CategoryEntity.class, id)).get();
        SpendEntity toBeDeletedSpend = Optional.of(entityManager(SPEND)
                .createQuery("SELECT s FROM SpendEntity s WHERE s.category.id = :id", SpendEntity.class)
                .setParameter("id", toBeDeletedCategory.getId()).getSingleResult()).get();
        remove(SPEND, toBeDeletedSpend);
        remove(SPEND, toBeDeletedCategory);
    }

}
