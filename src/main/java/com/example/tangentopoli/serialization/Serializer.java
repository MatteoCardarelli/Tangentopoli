package com.example.tangentopoli.serialization;

import com.example.tangentopoli.service.GameService;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Serializer implements Serializable {
    public static void serialize(GameService game) {
        try {
            // Directory in cui salvare i file
            String directoryPath = "saves";
            File directory = new File(directoryPath);

            // Creazione della directory se non esiste
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Nome base per i file di salvataggio
            String baseFileName = "game";
            String fileExtension = ".ser";

            // Pattern per identificare file già salvati
            Pattern pattern = Pattern.compile(baseFileName + "(\\d+)" + fileExtension);

            // Trova il numero progressivo massimo
            int maxNumber = 0;
            for (File file : directory.listFiles()) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    int number = Integer.parseInt(matcher.group(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                }
            }

            // Incrementa il numero per il nuovo file
            int nextNumber = maxNumber + 1;
            String newFileName = directoryPath + "/" + baseFileName + nextNumber + fileExtension;

            // Serializzazione dell'oggetto
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(newFileName))) {
                oos.writeObject(game);
                System.out.println("Partita salvata con successo in: " + newFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializes a game object from the specified file name.
     *
     * @param gameName The name of the serialized game file.
     * @return The deserialized game object.
     * @throws IOException If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public static Object deserialize(String gameName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/" + gameName))) {
            return ois.readObject(); // Cast to specific type if needed (e.g., GameService)
        }
    }
}
