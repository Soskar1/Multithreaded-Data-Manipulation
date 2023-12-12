package com.mdm.mdm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FileProgressInfoListCell extends ListCell<FileProgressInfo> {
    @FXML private ProgressBar progressBar;
    @FXML private Label fileName;
    @FXML private AnchorPane anchorPane;
    @FXML private Label completedText;

    public FileProgressInfoListCell(ListView<FileProgressInfo> fileProgressInfoListView) { }

    @Override
    protected void updateItem(FileProgressInfo item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (anchorPane == null) {
                FXMLLoader loader = new FXMLLoader(FileProgressInfoListCell.class.getResource("file-progress.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                FileReader thread = item.getThread();
                thread.setUpdateProgressBar((progress) -> progressBar.setProgress(progress));
                thread.addOnWorkIsDoneEvent(this::showCompletedText);

                fileName.setText(item.getFileName());
            }

            setGraphic(anchorPane);
        }
    }

    public void showCompletedText() {
        completedText.setOpacity(1);
        progressBar.setOpacity(0);
    }
}
