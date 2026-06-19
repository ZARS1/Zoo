package zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import zoo.animal.Animal;
import zoo.animal.Bird;
import zoo.animal.Fish;
import zoo.animal.Mammal;
import zoo.animal.Reptile;
import zoo.enclosure.Enclosure;

public class Zoo {

    private static final Logger LOGGER = Logger.getLogger(Zoo.class.getName());

    private final List<Enclosure<? extends Animal>> enclosures = new ArrayList<>();

    public boolean addEnclosure(Enclosure<? extends Animal> enclosure) {
        LOGGER.log(Level.INFO, "Adding enclosure: {0}", enclosure.getName());

        boolean added = enclosures.add(enclosure);

        LOGGER.log(Level.FINE, "Zoo now has {0} enclosures.", enclosures.size());
        return added;
    }

    public List<Enclosure<? extends Animal>> getEnclosures() {
        LOGGER.info("Getting all enclosures.");

        return List.copyOf(enclosures);
    }

    public Enclosure<? extends Animal> findEnclosureByName(String name) {
        LOGGER.log(Level.INFO, "Searching enclosure by name: {0}", name);

        Enclosure<? extends Animal> result =
                enclosures.stream()
                        .filter(enclosure -> enclosure.getName().equalsIgnoreCase(name))
                        .findFirst()
                        .orElse(null);

        if (result == null) {
            LOGGER.log(Level.WARNING, "No enclosure found with name: {0}", name);
        }

        return result;
    }

    public List<Animal> getAllAnimals() {
        LOGGER.info("Getting all animals.");

        List<Animal> animals =
                enclosures.stream()
                        .flatMap(enclosure -> enclosure.getInhabitants().stream())
                        .map(animal -> (Animal) animal)
                        .toList();

        LOGGER.log(Level.FINE, "Found {0} animals.", animals.size());
        return animals;
    }

    public List<Mammal> getAllMammals() {
        LOGGER.info("Getting all mammals.");

        List<Mammal> mammals =
                getAllAnimals().stream()
                        .filter(Mammal.class::isInstance)
                        .map(Mammal.class::cast)
                        .toList();

        LOGGER.log(Level.FINE, "Found {0} mammals.", mammals.size());
        return mammals;
    }

    public List<Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
        LOGGER.info("Getting animals by predicate.");

        List<Animal> result = getAllAnimals().stream().filter(predicate).toList();

        LOGGER.log(Level.FINE, "Predicate matched {0} animals.", result.size());
        return result;
    }

    public Map<Class<? extends Animal>, Long> countAnimalsByType() {
        LOGGER.info("Counting animals by type.");

        return getAllAnimals().stream()
                .collect(Collectors.groupingBy(Animal::getClass, Collectors.counting()));
    }

    public List<Enclosure<? extends Animal>> getOvercrowdedEnclosures(int maxAnimals) {
        LOGGER.log(Level.INFO, "Searching overcrowded enclosures. Max animals: {0}", maxAnimals);

        if (maxAnimals < 0) {
            LOGGER.severe("Maximum number of animals must not be negative.");
            throw new IllegalArgumentException("maxAnimals must not be negative");
        }

        List<Enclosure<? extends Animal>> result =
                enclosures.stream()
                        .filter(enclosure -> enclosure.getInhabitants().size() > maxAnimals)
                        .toList();

        LOGGER.log(Level.FINE, "Found {0} overcrowded enclosures.", result.size());
        return result;
    }

    public String summary() {
        LOGGER.info("Creating zoo summary.");

        Map<String, Long> groups =
                getAllAnimals().stream()
                        .collect(Collectors.groupingBy(this::animalGroup, Collectors.counting()));

        long animalCount = groups.values().stream().mapToLong(Long::longValue).sum();

        String animalSummary =
                groups.entrySet().stream()
                        .map(entry -> entry.getValue() + " " + entry.getKey())
                        .collect(Collectors.joining(", "));

        String summary =
                "Zoo mit "
                        + enclosures.size()
                        + " Gehegen und "
                        + animalCount
                        + " Tieren: "
                        + animalSummary;

        LOGGER.log(Level.FINE, summary);
        return summary;
    }

    private String animalGroup(Animal animal) {
        if (animal instanceof Mammal) {
            return "Mammals";
        }
        if (animal instanceof Bird) {
            return "Birds";
        }
        if (animal instanceof Fish) {
            return "Fish";
        }
        if (animal instanceof Reptile) {
            return "Reptiles";
        }

        LOGGER.severe("Unknown animal group found.");
        return "Unknown";
    }
}