/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="voliMinimo"
    private TextField voliMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="numeroOreTxtInput"
    private TextField numeroOreTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnOttimizza"
    private Button btnOttimizza; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	try {
    		Integer voliMin = Integer.parseInt(voliMinimo.getText());
    		model.creaGrafo(voliMin);
    		txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi\n", model.nVertici(), model.nArchi()));
    		
    		cmbBoxAeroportoPartenza.getItems().addAll(model.getAeroporti(voliMin));
    		btnAnalizza.setDisable(false);
            btnAeroportiConnessi.setDisable(false);
            btnOttimizza.setDisable(true);
            numeroOreTxtInput.setDisable(true);
            voliMinimo.setDisable(false);
    		
    	}catch(NumberFormatException e) {
    		txtResult.appendText("Deve inserire un numero intero reale\n");
    		return;
    	}
    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	Airport aeroporto = cmbBoxAeroportoPartenza.getValue();
    	if(cmbBoxAeroportoPartenza==null) {
    		txtResult.appendText("E' necessario scegliere un aeroporto dal menu\n");
    	}
    	List<Vicino> vicini = model.getAdiacenti(aeroporto);
    	for(Vicino v: vicini) {
    		txtResult.appendText(v.toString()+ "\n");
    	}
    	
    	btnAnalizza.setDisable(false);
        btnAeroportiConnessi.setDisable(false);
        btnOttimizza.setDisable(false);
        numeroOreTxtInput.setDisable(false);
        voliMinimo.setDisable(false);
		
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert voliMinimo != null : "fx:id=\"voliMinimo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroOreTxtInput != null : "fx:id=\"numeroOreTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnOttimizza != null : "fx:id=\"btnOttimizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        
        btnAnalizza.setDisable(false);
        btnAeroportiConnessi.setDisable(true);
        btnOttimizza.setDisable(true);
        numeroOreTxtInput.setDisable(true);
        voliMinimo.setDisable(false);
        //GESTIONE ERRORE: se l'utente cambia il numero min di voli
        voliMinimo.textProperty().addListener((Observable, oldvalue, newvalue)-> {
        	btnAnalizza.setDisable(false);
            btnAeroportiConnessi.setDisable(true);
            btnOttimizza.setDisable(true);
            numeroOreTxtInput.setDisable(true);
            voliMinimo.setDisable(false);
            cmbBoxAeroportoPartenza.getItems().clear();
        });
        //GESTIONE ERRORE: se l'utente cambia l'aeroporto selezionato
        cmbBoxAeroportoPartenza.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue)->{
        	btnAnalizza.setDisable(false);
            btnAeroportiConnessi.setDisable(false);
            btnOttimizza.setDisable(true);
            numeroOreTxtInput.setDisable(true);
            voliMinimo.setDisable(false);
        	
        });
    }
    
    public void setModel(Model model) {
  		this.model = model;
  		
  	}
}
