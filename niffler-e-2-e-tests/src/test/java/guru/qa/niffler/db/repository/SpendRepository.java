package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.SpendEntity;

import java.util.UUID;

public interface SpendRepository {
    SpendEntity createInSpend(SpendEntity spend);

    void deleteInSpendByCategoryId(UUID id);
}
