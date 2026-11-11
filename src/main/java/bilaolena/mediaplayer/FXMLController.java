package bilaolena.mediaplayer;

import java.io.File;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLController {
	//für die Bühne
	private Stage meineStage;
	//für den Player
	private MediaPlayer mediaplayer;

	//für die MediaView
	@FXML private MediaView mediaview;
	//für die ImageView mit dem Symbol
	@FXML private ImageView symbolLautsprecher, symbolPlay;
	// für den leichteren Zugriff
	@FXML private Button buttonPlay, buttonStop, buttonLautsprecher;
	//für das Listenfeld
	@FXML private ListView<String> liste;

	
	//die Methode setzt die Bühne auf den übergebenen Wert
	public void setMeineStage(Stage meineStage) {
		this.meineStage = meineStage;
	}

	// hier disable wir unsere SteuerungsButtons
	@FXML public void initialize() {
		steuerung(true);		
	}
	
	
	//die Methode zum Laden
	@FXML protected void ladenKlick(ActionEvent event) {
		//eine neue Instanz der Klasse FileChooser erzeugen
		FileChooser oeffnenDialog = new FileChooser();
		//den Titel für den Dialog setzen
		oeffnenDialog.setTitle("Datei öffnen");
		//die Filter setzen
		oeffnenDialog.getExtensionFilters().add(
			    new ExtensionFilter("Mediendateien", "*.mp3", "*.mp4"));

		//den Startordner auf den Benutzerordner setzen
		oeffnenDialog.setInitialDirectory(new File(System.getProperty("user.home")));

		//den Öffnen-dialog anzeigen und das Ergebnis beschaffen
		File datei = oeffnenDialog.showOpenDialog(meineStage);
		//wurde eine Datei ausgewählt
		if (datei != null)
			//dann über eine eigene Methode laden
			dateiLaden(datei);
	}
	

	// die Methode zum Neuladen
	@FXML protected void mauseKlick(MouseEvent event) {	
		// für den Eintrag in der Liste
	    String dateiname = liste.getSelectionModel().getSelectedItem();		    
		// wurde ein Eintrag ausgewählt?
	    if (dateiname != null) {
	        File datei = new File(dateiname);
	        dateiLaden(datei);
	    }				
	}


	// die Methode zur Wiedergabe / Pause
	@FXML protected void playKlick1(ActionEvent event) {
	    if (mediaplayer != null) {
	        // wir prüfen den Status
	        if (mediaplayer.getStatus() == Status.PLAYING) {
	            // Wiedergabe läuft, dann pausieren
	            mediaplayer.pause();
	            symbolSetzen("bilaolena/mediaplayer/icons/play.gif");
	        } 
	        else {
	            // Wiedergabe ist unterbrochen, dann fortsetzen
	            mediaplayer.play();
	            symbolSetzen("bilaolena/mediaplayer/icons/pause.gif");
	        }	                  
	    }
	}

	
	// die Methode zum Stoppen
	@FXML protected void stoppKlick(ActionEvent event) {
		//gibt es überhaupt einen Mediaplayer?
		if (mediaplayer != null) {
			//dann anhalten
			mediaplayer.stop();	
			symbolSetzen("bilaolena/mediaplayer/icons/play.gif");
		}
	}


	
	// die Methode zum Bild setzen
	private void symbolSetzen(String dateiname) {
		//das Bild erzeugen und anzeigen
		String pfad = "/" + dateiname; 
		Image bild = new Image(getClass().getResource(pfad).toExternalForm());
	    symbolPlay.setImage(bild);      
	}


	
	//die Methode für das Ein- und Ausschalten der Lautstärke
	@FXML protected void lautsprecherKlick(ActionEvent event) {
		//gibt es überhaupt einen Mediaplayer?
		String dateiname;
		if (mediaplayer != null) {
			//ist die Lautstärke 0?
			if (mediaplayer.getVolume() == 0) {
				//dann auf 100 setzen
				mediaplayer.setVolume(1.0);
				//und das "normale" Symbol setzen
				dateiname = "bilaolena/mediaplayer/icons/mute.gif";
			}
			else {
				//sonst auf 0 setzen
				mediaplayer.setVolume(0);
				//und das durchgestrichene Symbol setzen
				dateiname = "bilaolena/mediaplayer/icons/mute_off.gif";
			}
			//das Bild erzeugen und anzeigen
			String pfad = "/" + dateiname;
			Image bild = new Image(getClass().getResource(pfad).toExternalForm());
			symbolLautsprecher.setImage(bild);
		}
	}
	
	

	// die Methode zur Steuerung der Wiedergabe
	private void steuerung(boolean deaktivieren) {
		buttonPlay.setDisable(deaktivieren);
		buttonStop.setDisable(deaktivieren);
		buttonLautsprecher.setDisable(deaktivieren);
	}

	
	//die Methode zum Beenden
	@FXML protected void beendenKlick(ActionEvent event) {
		Platform.exit();
	}
	

	//die Methode lädt eine Datei
	// sie wird etwas umgebaut
	// der Eintrag wird nur dann neu in das Listenfeld eingetragen, wenn er noch nicht vorhanden ist
	// hier werden auch die Symbole für die Steuerung aktiviert, sobald eine Media erzeugt wird
	public void dateiLaden(File datei) {
		//läuft schon eine Wiedergabe?
		if (mediaplayer != null) {
			//dann anhalten
			mediaplayer.stop();
		}		
		//das Medium, den Mediaplayer und die MediaView erzeugen
		try {		
			Media medium = new Media(datei.toURI().toString());
			mediaplayer = new MediaPlayer(medium);
			mediaview.setMediaPlayer(mediaplayer);
			
			// hier rufen wir die Methode zur steuerung der Symbole und setzen auf false
			steuerung(false);
			//die Wiedergabe starten
			mediaplayer.play();
			
			// das Play-symbol auf "pause" setzen, da die Wiedergabe automatisch gestartet wird
			symbolSetzen("bilaolena/mediaplayer/icons/pause.gif");
			// das Lautsprecher-symbol auf "normal" setzen, falls es früher auf "mute" gesetzt wurde
			Image lautsprecherBild = new Image(
					getClass().getResource("/bilaolena/mediaplayer/icons/mute.gif").toExternalForm());
			symbolLautsprecher.setImage(lautsprecherBild);

			// den Pfad in das Listenfeld eintragen, nur wenn er noch nicht vorhanden ist
			if (!liste.getItems().contains(datei.toString())) {
				liste.getItems().add(datei.toString());				
			}
			//und die Titelleiste anpassen
			meineStage.setTitle("JavaFX Multimedia-Player " + datei.toString());	
		}
		catch(Exception ex) {
			//den Dialog erzeugen und anzeigen
			Alert meinDialog = new Alert(AlertType.INFORMATION, "Beim Laden hat es ein Problem gegeben.\n" + ex.getMessage());
			//den Text setzen
			meinDialog.setHeaderText("Bitte beachten");
			meinDialog.initOwner(meineStage);
			//den Dialog anzeigen
			meinDialog.showAndWait();
		}
	}
}	
