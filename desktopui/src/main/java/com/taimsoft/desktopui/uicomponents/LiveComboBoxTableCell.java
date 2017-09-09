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
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        comboBox.prefWidthProperty().bind(this.widthProperty());
        comboBox.valueProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> obs, T oldValue, T newValue) {
                // attempt to update property:
                ObservableValue<T> property = getTableColumn().getCellObservableValue(getIndex());
                if (property instanceof WritableValue) {
                    ((WritableValue<T>) property).setValue(newValue);
                }
            }
        });


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
}
