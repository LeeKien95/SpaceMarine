package game;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GameMenu extends Application {
	@FXML Button singleplayButton;
	@FXML Button multilayButton;
	@FXML Button exitButton;
	
	public static Stage primaryStage;
	public static BorderPane mainLayout;
	
	public void showMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/game/view/GameMenu.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void startSingleplay() throws IOException {
		Game myGame = new Game();
		myGame.run();
	}
	
	public void startMultiplay() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/game/view/GameMenu.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void exit() {
		System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Space Marine 1.0");
		showMenu();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
