package co.edu.unbosque.model.persistencie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileHandler {

    private static final String FOLDER_NAME = "data";

    public static void checkFolder() {
        File folder = new File(FOLDER_NAME);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdir();
        }
    }

    public static void writeSerializer(String fileName, Object content) {
        try {
            File file = new File(FOLDER_NAME + "/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(content);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error al escribir archivo: " + fileName);
        }
    }

    public static Object readSerialized(String fileName) {
        try {
            File file = new File(FOLDER_NAME + "/" + fileName);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object content = ois.readObject();
            ois.close();
            fis.close();
            return content;
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + fileName);
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada al deserializar: " + fileName);
        }
        return null;
    }
}
