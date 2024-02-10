package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.model.spend.SpendJson;


public abstract class SpendExtension {
    abstract protected SpendJson create(SpendJson spend);
}
