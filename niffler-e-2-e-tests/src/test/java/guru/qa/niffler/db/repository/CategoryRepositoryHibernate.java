package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Map;

import static guru.qa.niffler.db.Database.*;

public class CategoryRepositoryHibernate extends JpaService implements CategoryRepository{

    public CategoryRepositoryHibernate() {
        super(
                Map.of(
                        SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager()
                )
        );
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        persist(SPEND, category);
        return category;
    }

}
