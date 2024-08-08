package com.example.tangentopoli.model;

import java.util.ArrayList;

public class Board {
    private ArrayList<Box> boxes = new ArrayList<>();

    public Board(int playerN) {
        ConcreteBoxFactory boxFactory = new ConcreteBoxFactory();
        switch (playerN) {
            case 2, 3:
                boxes.add(boxFactory.createStart());

                boxes.add(boxFactory.createProperty("Piazza Duomo", 180, 40, 90, "Arancione"));
                boxes.add(boxFactory.createTax("Tangente", 90));
                boxes.add(boxFactory.createProperty("Via della Spiga", 200, 50, 100, "Arancione"));
                boxes.add(boxFactory.createTax("Imprevisto", 90));//imprevisto

                boxes.add(boxFactory.createPrison());

                boxes.add(boxFactory.createProperty("Piazza Fontana", 200, 50, 100, "Grigio"));
                boxes.add(boxFactory.createProperty("Piazza San Babila", 220, 60, 110, "Grigio"));
                boxes.add(boxFactory.createSociety("Mediaset", 100, 40, 30));
                boxes.add(boxFactory.createProperty("Piazza della Scala", 240, 70, 120, "Grigio"));

                boxes.add(boxFactory.createTax("Parcheggio", 90));//Parcheggio

                boxes.add(boxFactory.createProperty("Viale Mazzini", 300, 80, 150, "Blu"));
                boxes.add(boxFactory.createProperty("Viale Monza", 320, 90, 160, "Blu"));
                boxes.add(boxFactory.createStation( "Milano Centrale", 100, 50, 40));
                boxes.add(boxFactory.createProperty( "Via Aldo Moro", 120, 30, 40, "Blu"));

                boxes.add(boxFactory.createTax("Vai in prigione", 90));//Vai in prigione

                boxes.add(boxFactory.createProperty("Corso Buenos Aires", 400, 100, 200, "Verde"));
                boxes.add(boxFactory.createStation( "Venezia Santa Lucia", 100, 50, 40));
                boxes.add(boxFactory.createSociety("Montedison", 100, 40, 30));
                boxes.add(boxFactory.createProperty("Corso Garibaldi", 420, 110, 210, "Verde"));

                break;
            case 4,5:

                break;
            case 6:

                break;
            default:
                break;
        }
    }

    //Returns a Box from a given index (position)
    public Box getBoxFromIndex(int boxIndex) {
        return boxes.get(boxIndex % boxes.size());
    }

    //Returns the index of a specific Box
    public int getBoxIndex(Box box) {
        return boxes.indexOf(box);
    }
}
