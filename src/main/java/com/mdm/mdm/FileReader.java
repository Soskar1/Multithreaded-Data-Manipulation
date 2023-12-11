package com.mdm.mdm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileReader implements Runnable {
    private final Scanner scanner;
    private final Controller tableController;
    private final int waitTimeMs = 100;
    private final FileProgressInfo info;
    private final int lineCount;

    public FileReader(Controller tableController, String filePath, FileProgressInfo info) throws IOException {
        File file = new File(filePath);

        if (!file.isFile()) {
            throw new FileNotFoundException("File " + filePath + " does not exist");
        }

        scanner = new Scanner(file);
        this.tableController = tableController;
        this.info = info;

        try (Stream<String> fileStream = Files.lines(Paths.get(filePath))) {
            lineCount = (int) fileStream.count();
        }
    }

    @Override
    public void run() {
        int line = 0;
        scanner.nextLine(); //skip naming row

        while (scanner.hasNextLine()) {
            ++line;
            String data = scanner.nextLine();
            Person person = convertToPerson(data);
            tableController.addPerson(person);

            info.setProgress((float) line / lineCount);

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
