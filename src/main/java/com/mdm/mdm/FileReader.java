package com.mdm.mdm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileReader implements Runnable {
    private final Scanner scanner;
    private final Controller tableController;
    private final int waitTimeMs = 5;
    private final int lineCount;
    private Consumer<Double> updateProgressBar;
    private final List<Action> onWorkIsDone = new ArrayList<>();
    private boolean isFinished = false;

    public FileReader(Controller tableController, String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.isFile()) {
            throw new FileNotFoundException("File " + filePath + " does not exist");
        }

        scanner = new Scanner(file);
        this.tableController = tableController;

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

            if (updateProgressBar != null) {
                Double percentage = (double)line / lineCount;
                updateProgressBar.accept(percentage);
            }

            try {
                Thread.sleep(waitTimeMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        scanner.close();

        isFinished = true;
        if (!onWorkIsDone.isEmpty()) {
            for (Action event : onWorkIsDone) {
                event.execute();
            }
        }
    }

    public void setUpdateProgressBar(Consumer<Double> event) {
        updateProgressBar = event;
    }

    public void addOnWorkIsDoneEvent(Action event) {
        onWorkIsDone.add(event);
    }

    private Person convertToPerson(String data) {
        String[] separatedData = data.split(",");

        int id = Integer.parseInt(separatedData[0]);
        String firstName = separatedData[1];
        String lastName = separatedData[2];
        String email = separatedData[3];
        String gender = separatedData[4];
        String country;
        String domainName;
        String birthDate;

        if (separatedData[5].charAt(0) != '\"') {
            country = separatedData[5];
            domainName = separatedData[6];
            birthDate = separatedData[7];
        } else {
            country = separatedData[5] + separatedData[6];
            domainName = separatedData[7];
            birthDate = separatedData[8];
        }

        return new Person(id, firstName, lastName, email, gender, country, domainName, birthDate);
    }

    public boolean isFinished() {
        return isFinished;
    }
}
