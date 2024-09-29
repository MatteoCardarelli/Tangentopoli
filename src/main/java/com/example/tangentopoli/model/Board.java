package com.example.tangentopoli.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private ArrayList<Box> boxes = new ArrayList<>();
    private int size;

    public Board(int playerN) {
        ConcreteBoxFactory boxFactory = new ConcreteBoxFactory();
        switch (playerN) {
            case 2, 3:
                boxes.add(boxFactory.createStart());

                boxes.add(boxFactory.createProperty("Piazza Duomo", 180, 16, 90, "Orange"));
                boxes.add(boxFactory.createTax("Tangente", 200));
                boxes.add(boxFactory.createProperty("Via della Spiga", 200, 18, 100, "Orange"));
                boxes.add(boxFactory.createCardBox("Imprevisto"));

                boxes.add(boxFactory.createPrison());

                boxes.add(boxFactory.createProperty("Piazza Fontana", 200, 22, 100, "Gray"));
                boxes.add(boxFactory.createProperty("Piazza San Babila", 220, 24, 110, "Gray"));
                boxes.add(boxFactory.createSociety("Mediaset", 100, 40));
                boxes.add(boxFactory.createProperty("Piazza della Scala", 240, 28, 120, "Gray"));

                boxes.add(boxFactory.createParking());

                boxes.add(boxFactory.createProperty("Viale Mazzini", 300, 32, 150, "Blue"));
                boxes.add(boxFactory.createProperty("Viale Monza", 320, 34, 160, "Blue"));
                boxes.add(boxFactory.createStation( "Milano Centrale", 100, 50, 40));
                boxes.add(boxFactory.createCardBox("Probabilità"));

                boxes.add(boxFactory.createGoToPrison());

                boxes.add(boxFactory.createProperty("Corso Buenos Aires", 400, 40, 200, "Green"));
                boxes.add(boxFactory.createStation( "Venezia Santa Lucia", 100, 50, 40));
                boxes.add(boxFactory.createSociety("Montedison", 100, 40));
                boxes.add(boxFactory.createProperty("Corso Garibaldi", 420, 45, 210, "Green"));

                break;
            case 4,5:
                boxes.add(boxFactory.createStart());
                break;
            case 6:
                boxes.add(boxFactory.createStart());
                break;
            default:
                break;
        }

        size = boxes.size();
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
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
