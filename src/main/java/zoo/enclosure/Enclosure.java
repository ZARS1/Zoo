package zoo.enclosure;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import zoo.animal.Animal;

public class Enclosure<T extends Animal> {

    private final String name;
    private final Set<T> inhabitants = new LinkedHashSet<>();

    public Enclosure(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean add(T animal) {
        return inhabitants.add(animal);
    }

    public boolean remove(T animal) {
        return inhabitants.remove(animal);
    }

    public List<T> getInhabitants() {
        return new ArrayList<>(inhabitants);
    }

    public int size() {
        return inhabitants.size();
    }
}