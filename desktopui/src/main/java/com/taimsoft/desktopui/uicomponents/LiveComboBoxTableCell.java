package com.taimsoft.desktopui.uicomponents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * Created by Tjin on 9/6/2017.
 */
public class LiveComboBoxTableCell<S,T> extends TableCell<S, T> {

    private final ComboBox<T> comboBox ;

    public LiveComboBoxTableCell(ObservableList<T> items) {
        this.comboBox = new ComboBox<>(items);
        this.comboBox.setPromptText("SELECT ACTION");
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        comboBox.prefWidthProperty().bind(this.widthProperty());
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            comboBox.setValue(item);
            setGraphic(comboBox);
        }
    }

    public ComboBox<T> getComboBox() {
        return comboBox;
    }
}
