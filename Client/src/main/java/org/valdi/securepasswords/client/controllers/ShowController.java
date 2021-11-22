package org.valdi.securepasswords.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.valdi.securepasswords.client.DesktopBootstrap;
import org.valdi.securepasswords.core.objects.Password;

import java.io.IOException;

public class ShowController {
    private final Password password;

    @FXML
    private TextField nameField;
    @FXML
    private ListView<ShowElement> elementsListView;

    public ShowController(Password password) {
        this.password = password;
    }

    @FXML
    private void initialize() throws IOException {
        //this.id = this.password.getId();
        this.elementsListView.setCellFactory(lv -> {
            ListCell<ShowElement> cell = new ListCell<>() {
                @Override
                protected void updateItem(ShowElement item, boolean empty) {
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
        this.nameField.setText(this.password.getName());
        for (Password.Element e : this.password) {
            ShowElement elem = this.newElement(e.isHide());
            elem.getNameLabel().setText(e.getName());
            elem.getValueField().setText(e.getValue());
            elementsListView.getItems().add(elem);
        }
    }

    private ShowElement newElement(boolean hide) throws IOException {
        FXMLLoader loader = new FXMLLoader(DesktopBootstrap.class.getResource("show-element.fxml"));
        loader.setControllerFactory((clazz) -> new ShowElement(hide));
        loader.load();
        return loader.getController();
    }

}
