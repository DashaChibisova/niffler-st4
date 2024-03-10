package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CategoryEntity;

public interface CategoryRepository {
    CategoryEntity createCategory(CategoryEntity category);

}
