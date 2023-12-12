package com.mdm.mdm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class Controller implements Initializable {
    @FXML private TableView<Person> personTableView;
    @FXML private TableColumn<Person, Integer> personID;
    @FXML private TableColumn<Person, String> personFirstName;
    @FXML private TableColumn<Person, String> personLastName;
    @FXML private TableColumn<Person, String> personEmail;
    @FXML private TableColumn<Person, String> personGender;
    @FXML private TableColumn<Person, String> personCountry;
    @FXML private TableColumn<Person, String> personDomainName;
    @FXML private TableColumn<Person, String> personBirthDate;

    @FXML private ListView<FileProgressInfo> fileProgressList;

    @FXML private DatePicker fromDate;
    @FXML private DatePicker toDate;
    @FXML private ChoiceBox<TableColumns> columnsChoiceBox;
    @FXML private CheckBox descendingSortCheckBox;
    @FXML private Spinner<Integer> fromID;
    @FXML private Spinner<Integer> toID;

    private List<Person> personList;
    private final List<FileReader> activeThreads = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        columnsChoiceBox.getItems().setAll(TableColumns.values());

        fileProgressList.setCellFactory(FileProgressInfoListCell::new);

        loadDataFromFile("assets/MOCK_DATA1.csv");
        loadDataFromFile("assets/MOCK_DATA2.csv");
        loadDataFromFile("assets/MOCK_DATA3.csv");
    }

    private void initializeTableView() {
        personID.setCellValueFactory(new PropertyValueFactory<>("id"));
        personFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        personLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        personEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        personGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        personCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        personDomainName.setCellValueFactory(new PropertyValueFactory<>("domainName"));
        personBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
    }

    public synchronized void addPerson(Person person) {
        personTableView.getItems().add(person);
    }

    private void loadDataFromFile(String filePath) {
        FileReader fileReader;
        Path path = Paths.get(filePath);

        try {
            fileReader = new FileReader(this, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileProgressInfo fileProgressInfo = new FileProgressInfo(path.getFileName().toString(), fileReader);
        fileProgressList.getItems().add(fileProgressInfo);

        fileReader.addOnWorkIsDoneEvent(this::initializeFilters);

        Thread thread = new Thread(fileReader);
        thread.start();

        activeThreads.add(fileReader);
    }

    private synchronized void initializeFilters() {
        if (!activeThreads.isEmpty()) {
            activeThreads.removeIf(FileReader::isFinished);

            if (!activeThreads.isEmpty()) {
                return;
            }
        }

        personList = personTableView.getItems();
        var minDate = personList.stream()
                        .map(Person::getBirthDate)
                        .min(Comparator.naturalOrder())
                        .get();

        var maxDate = personList.stream()
                        .map(Person::getBirthDate)
                        .max(Comparator.naturalOrder())
                        .get();

        fromDate.setValue(minDate);
        toDate.setValue(maxDate);

        Person lastPerson = personList.get(personList.size() - 1);
        SpinnerValueFactory<Integer> fromIdFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, lastPerson.getId());
        SpinnerValueFactory<Integer> toIdFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, lastPerson.getId());
        fromIdFactory.setValue(1);
        toIdFactory.setValue(lastPerson.getId());
        fromIdFactory.valueProperty().addListener(obs -> filter());
        toIdFactory.valueProperty().addListener(obs -> filter());
        fromID.setValueFactory(fromIdFactory);
        toID.setValueFactory(toIdFactory);

        columnsChoiceBox.valueProperty().addListener(obs -> filter());
        fromDate.valueProperty().addListener(obs -> filter());
        toDate.valueProperty().addListener(obs -> filter());
        descendingSortCheckBox.selectedProperty().addListener(obs -> filter());

        enableFilters();
    }

    private void enableFilters() {
        fromDate.setDisable(false);
        toDate.setDisable(false);
        columnsChoiceBox.setDisable(false);
        descendingSortCheckBox.setDisable(false);
        fromID.setDisable(false);
        toID.setDisable(false);
    }

    public void filter() {
        List<Person> filteredList = personList;

        filteredList = filterByDate(filteredList);
        filteredList = filterByID(filteredList);
        filteredList = sortColumn(filteredList);

        personTableView.getItems().setAll(filteredList);
    }

    private List<Person> filterByDate(List<Person> personList) {
        return personList.stream()
                .filter(p -> p.getBirthDate().isAfter(fromDate.getValue()) || p.getBirthDate().equals(fromDate.getValue()))
                .filter(p -> p.getBirthDate().isBefore(toDate.getValue()) || p.getBirthDate().equals(toDate.getValue()))
                .toList();
    }

    private List<Person> filterByID(List<Person> personList) {
        return personList.stream()
                .filter(p -> p.getId() >= fromID.getValue())
                .filter(p -> p.getId() <= toID.getValue())
                .toList();
    }

    private List<Person> sortColumn(List<Person> personList) {
        if (columnsChoiceBox.getValue() == null) {
            return personList;
        }

        return switch (columnsChoiceBox.getValue()) {
            case ID -> descendingSortCheckBox.isSelected() ? personList.stream().sorted(Comparator.comparing(Person::getId).reversed()).toList() :
                personList.stream().sorted(Comparator.comparing(Person::getId)).toList();

            case FIRST_NAME -> descendingSortCheckBox.isSelected() ? personList.stream().sorted(Comparator.comparing(Person::getFirstName).reversed()).toList() :
                personList.stream().sorted(Comparator.comparing(Person::getFirstName)).toList();

            case LAST_NAME -> descendingSortCheckBox.isSelected() ? personList.stream().sorted(Comparator.comparing(Person::getLastName).reversed()).toList() :
                personList.stream().sorted(Comparator.comparing(Person::getLastName)).toList();

            case EMAIL -> descendingSortCheckBox.isSelected() ? personList.stream().sorted(Comparator.comparing(Person::getEmail).reversed()).toList() :
                personList.stream().sorted(Comparator.comparing(Person::getEmail)).toList();

            case GENDER -> descendingSortCheckBox.isSelected() ? personList.stream().sorted(Comparator.comparing(Person::getGender).reversed()).toList() :
                personList.stream().sorted(Comparator.comparing(Person::getGender)).toList();

            case COUNTRY -> descendingSortCheckBox.isSelected() ? personList.stream().sorted(Comparator.comparing(Person::getCountry).reversed()).toList() :
                personList.stream().sorted(Comparator.comparing(Person::getCountry)).toList();

            case DOMAIN_NAME -> descendingSortCheckBox.isSelected() ? personList.stream().sorted(Comparator.comparing(Person::getDomainName).reversed()).toList() :
                personList.stream().sorted(Comparator.comparing(Person::getDomainName)).toList();
        };
    }
}