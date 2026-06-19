package zoo;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import zoo.animal.Eagle;
import zoo.animal.Gorilla;
import zoo.animal.Lion;
import zoo.animal.Parrot;
import zoo.animal.Salmon;
import zoo.animal.Snake;
import zoo.animal.Tiger;
import zoo.animal.Trout;
import zoo.enclosure.Aquarium;
import zoo.enclosure.CatHouse;
import zoo.enclosure.MammalHouse;
import zoo.enclosure.Terrarium;

public class Main {

    public static void main(String[] args) {
        configureLogging(Level.FINE);

        Zoo zoo = new Zoo();

        Aquarium aquarium = new Aquarium("Amazonas Aquarium");
        aquarium.add(new Trout("Toni"));
        aquarium.add(new Salmon("Sammy"));

        Terrarium terrarium = new Terrarium("Reptilienhaus");
        terrarium.add(new Snake("Sissi"));

        MammalHouse mammalHouse = new MammalHouse("Saeugetierhaus");
        mammalHouse.add(new Gorilla("Goro"));

        CatHouse catHouse = new CatHouse("Katzenhaus");
        catHouse.add(new Lion("Leo"));
        catHouse.add(new Tiger("Tara"));

        zoo.addEnclosure(aquarium);
        zoo.addEnclosure(terrarium);
        zoo.addEnclosure(mammalHouse);
        zoo.addEnclosure(catHouse);

        System.out.println(zoo.summary());

        System.out.println("Alle Tiere:");
        zoo.getAllAnimals().forEach(animal -> System.out.println("- " + animal.name()));

        System.out.println("Alle Saeugetiere:");
        zoo.getAllMammals().forEach(animal -> System.out.println("- " + animal.name()));

        System.out.println("Tiere mit Namen laenger als 4 Zeichen:");
        zoo.getAnimalsByPredicate(animal -> animal.name().length() > 4)
                .forEach(animal -> System.out.println("- " + animal.name()));

        System.out.println("Anzahl Tiere nach Typ:");
        zoo.countAnimalsByType()
                .forEach((type, count) -> System.out.println(type.getSimpleName() + ": " + count));

        System.out.println("Ueberfuellte Gehege bei maximal 1 Tier:");
        zoo.getOvercrowdedEnclosures(1)
                .forEach(enclosure -> System.out.println("- " + enclosure.getName()));

        zoo.findEnclosureByName("Nicht vorhanden");

        // Beispiel fuer Bird, damit auch diese Tierklasse verwendet wird.
        System.out.println("Beispiel-Vogel: " + new Eagle("Erik").name());
        System.out.println("Noch ein Vogel: " + new Parrot("Paula").name());
    }

    private static void configureLogging(Level level) {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);

        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
        }

        Logger.getLogger(Zoo.class.getName()).setLevel(level);
    }
}