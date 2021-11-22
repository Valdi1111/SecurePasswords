package org.valdi.securepasswords.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class EditElement {
    @FXML
    private FlowPane pane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField valueField;
    @FXML
    private CheckBox hideCheck;

    public FlowPane getPane() {
        return pane;
    }

    public TextField getNameField() {
        return this.nameField;
    }

    public TextField getValueField() {
        return this.valueField;
    }

    public CheckBox getHideCheck() {
        return this.hideCheck;
    }
}
