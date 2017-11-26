package com.taimsoft.desktopui.util;

import com.taim.client.IClient;
import com.taim.dto.basedtos.BaseModelDTO;
import com.taimsoft.desktopui.uicomponents.FadingStatusMessage;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeleteEntityUtil<T extends BaseModelDTO> {
    private T entity;
    private IClient<T> entityClient;
    private Executor executor;
    private static final Logger logger = LoggerFactory.getLogger(DeleteEntityUtil.class);

    public DeleteEntityUtil(T entity, IClient<T> client){
        this.entity = entity;
        this.entityClient = client;
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void deleteEntity(TableView table, int index, String successfulMessage, Pane rootPane){
        Task<T> getEntityTask = new Task<T>() {
            @Override
            protected T call() throws Exception {
                return entityClient.getById(entity.getId());
            }
        };
        
        getEntityTask.setOnSucceeded(event -> {
            this.entity = getEntityTask.getValue();
            if(!this.entity.isDeleted()){
                this.entity.setDeleted(true);
                Task<T> updateEntityTask = new Task<T>() {
                    @Override
                    protected T call() throws Exception {
                        return entityClient.update(entity);
                    }
                };

                updateEntityTask.setOnSucceeded(event1 -> {
                    table.getItems().remove(index);
                    FadingStatusMessage.flash(successfulMessage, rootPane);
                });
                updateEntityTask.setOnFailed(event1 -> {
                    new AlertBuilder()
                            .alertType(Alert.AlertType.ERROR)
                            .alertContentText("Failed to delete object. Please refresh this page and try again")
                            .build()
                            .showAndWait();
                });

                executor.execute(updateEntityTask);
            }else{
                table.getItems().remove(index);
                FadingStatusMessage.flash(successfulMessage, rootPane);
            }
        });

        getEntityTask.setOnFailed(event -> {
            logger.error(getEntityTask.getException().getMessage());
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertContentText("Failed to retrieve the object to delete. Please refresh this page and try again later")
                    .build()
                    .showAndWait();
        });

        executor.execute(getEntityTask);
    }
}
