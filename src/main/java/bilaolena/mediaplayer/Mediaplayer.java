package bilaolena.mediaplayer;
import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Mediaplayer extends Application {

	@Override
	public void start(Stage meineStage) throws Exception {

		// eine Instanz von FXMLLoader erzeugen
		FXMLLoader meinLoader = new FXMLLoader(getClass().getResource("sb_mediaplayer.fxml"));
		
		// die Datei laden
		Parent rootParent = meinLoader.load();
		
		// den Controller beschaffen
		FXMLController meinController = meinLoader.getController();
		
		// die Bühne übergeben
		meinController.setMeineStage(meineStage);
		
		// die Scene erzeugen
		Scene meineScene = new Scene(rootParent, 800, 600);
		
		// den Titel setzen
		meineStage.setTitle("JavaFX Multimedia-Player");
		
		// die Scene setzen
		meineStage.setScene(meineScene);
		
		// im  Vollbild darstellen
		meineStage.setMaximized(true);
		
		// und anzeigen
		meineStage.show();
		
		File bilddatei = new File("bilaolena/mediaplayer/icons/icon.gif");
		Image bild = new Image(bilddatei.toURI().toString());
		meineStage.getIcons().add(bild);
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
