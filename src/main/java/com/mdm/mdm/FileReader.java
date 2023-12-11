package com.mdm.mdm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader implements Runnable {
    private final Scanner scanner;
    private final Controller tableController;
    private final int waitTimeMs = 100;

    public FileReader(Controller tableController, String filePath) throws FileNotFoundException {
        File file = new File(filePath);

        if (!file.isFile()) {
            throw new FileNotFoundException("File " + filePath + " does not exist");
        }

        scanner = new Scanner(file);
        this.tableController = tableController;
    }

    @Override
    public void run() {
        scanner.nextLine(); //skip naming row

        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            Person person = convertToPerson(data);
            tableController.addPerson(person);

            try {
                Thread.sleep(waitTimeMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        scanner.close();
    }

    private Person convertToPerson(String data) {
        String[] separatedData = data.split(",");

        int id = Integer.parseInt(separatedData[0]);
        String firstName = separatedData[1];
        String lastName = separatedData[2];
        String email = separatedData[3];
        String gender = separatedData[4];
        String country = separatedData[5];
        String domainName = separatedData[6];
        String birthDate = separatedData[7];

        return new Person(id, firstName, lastName, email, gender, country, domainName, birthDate);
    }
}
