package org.valdi.securepasswords.client.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import org.kordamp.ikonli.javafx.FontIcon;

public class ShowElement {
    public static final String HIDE = "far-eye-slash";
    public static final String SHOW = "far-eye";
    public static final String COPY = "fas-copy";

    private BooleanProperty hidden;
    private boolean hide;

    @FXML
    private FlowPane pane;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField valueField;
    @FXML
    private PasswordField hiddenField;
    @FXML
    private Button copyButton;
    @FXML
    private Button hideButton;

    public ShowElement(boolean hide) {
        this.hidden = new SimpleBooleanProperty(hide);
        this.hide = hide;
    }

    @FXML
    private void initialize() {
        this.copyButton.setGraphic(new FontIcon(COPY));
        this.hideButton.setGraphic(new FontIcon(SHOW));

        if(!hide) {
            this.hideButton.setVisible(false);
        }

        this.valueField.managedProperty().bind(this.hidden.not());
        this.valueField.visibleProperty().bind(this.hidden.not());

        this.hiddenField.managedProperty().bind(this.hidden);
        this.hiddenField.visibleProperty().bind(this.hidden);

        this.valueField.textProperty().bindBidirectional(this.hiddenField.textProperty());
    }

    @FXML
    private void onCopy() {
        ClipboardContent content = new ClipboardContent();
        content.putString(this.valueField.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    private void onHide() {
        if(this.hidden.get()) {
            this.hidden.set(false);
            this.hideButton.setGraphic(new FontIcon(HIDE));
            return;
        }

        this.hidden.set(true);
        this.hideButton.setGraphic(new FontIcon(SHOW));
    }

    public FlowPane getPane() {
        return pane;
    }

    public Label getNameLabel() {
        return this.nameLabel;
    }

    public TextField getValueField() {
        return this.valueField;
    }
}
