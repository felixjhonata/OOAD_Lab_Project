package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.Main;

public class Main extends Application {
	private static Stage stage;

	public static void redirect(Scene newScene) {
		stage.setScene(newScene);
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Main.stage = stage;
		stage.setTitle("Calouse");
		new LoginView();
	}
}

/*
"vmArgs": "--module-path \"D:\\openjfx-23.0.1_windows-x64_bin-sdk\\javafx-23.0.1\\lib\" --add-modules javafx.controls,javafx.fxml"


 */