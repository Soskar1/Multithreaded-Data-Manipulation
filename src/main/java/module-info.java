module com.mdm.mdm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mdm.mdm to javafx.fxml;
    exports com.mdm.mdm;
}