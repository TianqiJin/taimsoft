package com.taim.desktopui.controllers.details;

import javafx.stage.Stage;

public interface IDetailController<T> {
        void initDetailData(T obj);
        void setStage(Stage stage);
}
