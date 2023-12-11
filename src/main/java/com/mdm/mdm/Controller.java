package com.mdm.mdm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        startThreads();
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

    private void startThreads() {
        FileReader fileReader1;
        FileReader fileReader2;
        FileReader fileReader3;

        try {
            fileReader1 = new FileReader(this, "assets/MOCK_DATA1.csv");
            fileReader2 = new FileReader(this, "assets/MOCK_DATA2.csv");
            fileReader3 = new FileReader(this, "assets/MOCK_DATA3.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Thread thread1 = new Thread(fileReader1);
        Thread thread2 = new Thread(fileReader2);
        Thread thread3 = new Thread(fileReader3);

        thread1.start();
        thread2.start();
        thread3.start();
    }

    public synchronized void addPerson(Person person) {
        personTableView.getItems().add(person);
    }
}