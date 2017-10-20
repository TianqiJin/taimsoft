package com.taimsoft.desktopui.uicomponents;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FadingStatusMessage {

    public static void flash(String message, Pane pane) {
        Font font = Font.font("Verdana", FontWeight.NORMAL, 20);
        Color boxColor = Color.GREY;
        Color textColor = Color.WHITE;
        double duration = 3000;
        double arcH = 5;
        double arcW = 5;

        final Rectangle rectangle = new Rectangle();
        final Text text = new Text(message);


        double x = 0;
        double y = 0;
        text.setLayoutX(x);
        text.setLayoutY(y);
        text.setFont(font);
        text.setFill(textColor);

        pane.getChildren().add(rectangle);
        pane.getChildren().add(text);

        Bounds bounds = text.getBoundsInParent();

        double sWidth = pane.getWidth();
        double sHeight = pane.getHeight();

        x = sWidth / 2 - (bounds.getWidth() / 2);
        y = sHeight / 2 - (bounds.getHeight() / 2);
        text.setLayoutX(x);
        text.setLayoutY(y);
        bounds = text.getBoundsInParent();
        double baseLineOffset = text.getBaselineOffset();

        rectangle.setFill(boxColor);
        rectangle.setLayoutX(x - arcW);
        rectangle.setLayoutY(y - baseLineOffset - arcH);
        rectangle.setArcHeight(arcH);
        rectangle.setArcWidth(arcW);
        rectangle.setWidth(bounds.getWidth() + arcW * 2);
        rectangle.setHeight(bounds.getHeight() + arcH * 2);

        FadeTransition ft = new FadeTransition(Duration.millis(duration), rectangle);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        ft.setOnFinished(event -> {
            pane.getChildren().remove(rectangle);
            pane.getChildren().remove(text);
        });
        FadeTransition ft2 = new FadeTransition(Duration.millis(duration + (duration * .1)), text);
        ft2.setFromValue(1.0);
        ft2.setToValue(0.0);
        ft2.play();
    }
}
