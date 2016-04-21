/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.preloader;

import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author daniel
 */
public class PreloaderFX extends Preloader {

        Stage stage;
        //boolean noLoadingProgress = true;

//        public static final String APPLICATION_ICON
//            = PreloaderFX.class.getResource("")
        public static final String SPLASH_IMAGE
            = "http://fxexperience.com/wp-content/uploads/2010/06/logo.png";

        private Pane splashLayout;
        private ProgressBar loadProgress;
        private Label progressText;
        private static final int SPLASH_WIDTH = 676;
        private static final int SPLASH_HEIGHT = 227;

        @Override
        public void init() {
            ImageView splash = new ImageView(new Image(
                SPLASH_IMAGE
            ));
            loadProgress = new ProgressBar();
            loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
            progressText = new Label("Cargando sistema . . .");
            splashLayout = new VBox();
            splashLayout.getChildren().addAll(splash, loadProgress, progressText);
            progressText.setAlignment(Pos.CENTER);
            splashLayout.setStyle(
                "-fx-padding: 5; "
                + "-fx-background-color: white; "
                + "-fx-border-width:5; "
            );
            splashLayout.setEffect(new DropShadow());
        }

        @Override
        public void start(Stage stage) throws Exception {
            System.out.println("PreloaderFx::start();");

            //this.stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Title");
            stage.getIcons().add(new Image("/com/tpv/resources/people.png"));
            stage.initStyle(StageStyle.UNDECORATED);
            final Rectangle2D bounds = Screen.getPrimary().getBounds();
            stage.setScene(new Scene(splashLayout));
            stage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
            stage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
            stage.show();

            this.stage = stage;
        }

        @Override
        public void handleProgressNotification(ProgressNotification pn) {
            System.out.println("PreloaderFx::handleProgressNotification(); progress = " + pn.getProgress());
            //application loading progress is rescaled to be first 50%
            //Even if there is nothing to load 0% and 100% events can be
            // delivered
            if (pn.getProgress() != 1.0 /*|| !noLoadingProgress*/) {
                loadProgress.setProgress(pn.getProgress() / 2);
                /*if (pn.getProgress() > 0) {
                noLoadingProgress = false;
                }*/
            }
        }

        @Override
        public void handleStateChangeNotification(StateChangeNotification evt) {
            //ignore, hide after application signals it is ready
            System.out.println("PreloaderFx::handleStateChangeNotification(); state = " + evt.getType());
        }

        @Override
        public void handleApplicationNotification(PreloaderNotification pn) {
            if (pn instanceof ProgressNotification) {
                //expect application to send us progress notifications 
                //with progress ranging from 0 to 1.0
                double v = ((ProgressNotification) pn).getProgress();
                System.out.println("PreloaderFx::handleApplicationNotification(); progress = " + v);
                //if (!noLoadingProgress) {
                //if we were receiving loading progress notifications 
                //then progress is already at 50%. 
                //Rescale application progress to start from 50%               
                v = 0.5 + v / 2;
                //}
                loadProgress.setProgress(v);
            } else if (pn instanceof StateChangeNotification) {
                System.out.println("PreloaderFx::handleApplicationNotification(); state = " + ((StateChangeNotification) pn).getType());
                //hide after get any state update from application
                stage.hide();
            }
        }
    }
