/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.txtK.clear();
    	txtResult.appendText("Creazione grafo...\n");
    	
    	try {
    		int numPorzioni = Integer.parseInt(this.txtPorzioni.getText());
    		if(numPorzioni < 1) {
    			this.txtResult.setText("Inserire un numero positivo di porzioni");
    			return;
    		}
    		
    		this.model.creaGrafo(numPorzioni);
    		
    		this.txtResult.appendText("#vertici: "+this.model.getNVertici() + "\n");
    		this.txtResult.appendText("#archi: "+this.model.getNArchi() + "\n");
    		
    		this.btnCalorie.setDisable(false);
    		this.btnSimula.setDisable(false);
    		
    		this.boxFood.getItems().clear();
    		this.boxFood.getItems().addAll(this.model.getVertici());
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero intero di porzioni");
    	}
    	
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...\n");
    	this.txtK.clear();
    	
    	Food f = this.boxFood.getValue();
    	
    	if(f == null) {
    		this.txtResult.setText("selezionare un cibo");
    		return;
    	}
    	
    	List<Result> risultato = this.model.calcolaAdiacentiMax(f);
    	
    	if(risultato.size()==0) {
    		
    		this.txtResult.setText("vertice isolato");
    		return;
    	}
    	
    	for(int i = 0; i<5 && i < risultato.size(); i++) {
    		this.txtResult.appendText(risultato.get(i).toString());
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...");
    	
    	Food f = this.boxFood.getValue();
    	
    	if(f == null) {
    		this.txtResult.setText("selezionare un cibo");
    		return;
    	}
    	
    	try {
    		int numStazioni = Integer.parseInt(this.txtK.getText());
    		if(numStazioni < 1 || numStazioni > 10) {
    			this.txtResult.setText("Inserire un numero tra 1 e 10 di stazioni");
    			return;
    		}
    		
    		String messaggio = this.model.simula(f, numStazioni);
    		this.txtResult.setText(messaggio);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero intero di porzioni");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
