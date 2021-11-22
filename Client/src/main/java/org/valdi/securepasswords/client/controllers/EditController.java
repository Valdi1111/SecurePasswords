package org.valdi.securepasswords.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.valdi.securepasswords.client.DesktopBootstrap;
import org.valdi.securepasswords.core.objects.Password;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditController {
    private Password password;
    private int id;

    @FXML
    private TextField nameField;
    @FXML
    private TextField linkField;
    @FXML
    private ListView<EditElement> elementsListView;

    public EditController() {

    }

    public EditController(Password password) {
        this.password = password;
    }

    @FXML
    private void initialize() throws IOException {
        this.elementsListView.setCellFactory(lv -> {
            ListCell<EditElement> cell = new ListCell<>() {
                @Override
                protected void updateItem(EditElement item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null) {
                        setGraphic(item.getPane());
                    }
                    else {
                        setGraphic(null);
                    }
                }
            };
            cell.setEditable(false);
            return cell;
        });
        if (this.password != null) {
            this.id = this.password.getId();
            this.nameField.setText(this.password.getName());
            this.linkField.setText(this.password.getLink());
            for (Password.Element e : this.password) {
                EditElement elem = this.newElement();
                elem.getNameField().setText(e.getName());
                elem.getValueField().setText(e.getValue());
                elem.getHideCheck().setSelected(e.isHide());
                elementsListView.getItems().add(elem);
            }
            this.password = null;
        } else {
            this.onAdd();
            this.onAdd();
            this.onAdd();
        }
    }

    private EditElement newElement() throws IOException {
        FXMLLoader loader = new FXMLLoader(DesktopBootstrap.class.getResource("edit-element.fxml"));
        loader.load();
        return loader.getController();
    }

    @FXML
    private void onAdd() throws IOException {
        EditElement e = this.newElement();
        this.elementsListView.getItems().add(e);
    }

    @FXML
    private void onDelete() {
        EditElement element = this.elementsListView.getSelectionModel().getSelectedItem();
        if (element == null) {
            return;
        }

        this.elementsListView.getItems().remove(element);
    }

    @FXML
    private void onSave() {
        if(this.nameField.getText().isBlank()) {
            // TODO send warning name null
            return;
        }
        List<String> strCheck = new ArrayList<>();
        for(EditElement le : this.elementsListView.getItems()) {
            String str = le.getNameField().getText();
            if(str.isBlank()) {
                // TODO send warning element name null
                return;
            }
            if(strCheck.contains(str)) {
                // TODO send warning element name duplicate
                return;
            }
            strCheck.add(str);
        }

        this.password = new Password();
        this.password.setId(this.id);
        this.password.setName(this.nameField.getText().strip());
        this.password.setLink(this.linkField.getText().strip());
        for (EditElement e : this.elementsListView.getItems()) {
            Password.Element elem = new Password.Element();
            elem.setName(e.getNameField().getText().strip());
            elem.setValue(e.getValueField().getText().strip());
            elem.setHide(e.getHideCheck().isSelected());
            this.password.addElement(elem);
        }

        this.onBack();
    }

    @FXML
    private void onBack() {
        Stage primaryStage = (Stage) this.elementsListView.getScene().getWindow();
        primaryStage.close();
    }

    public Password getPassword() {
        return this.password;
    }

}
