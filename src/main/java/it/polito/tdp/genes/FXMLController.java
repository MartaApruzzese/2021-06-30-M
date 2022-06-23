/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.genes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model ;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnContaArchi"
    private Button btnContaArchi; // Value injected by FXMLLoader

    @FXML // fx:id="btnRicerca"
    private Button btnRicerca; // Value injected by FXMLLoader

    @FXML // fx:id="txtSoglia"
    private TextField txtSoglia; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doContaArchi(ActionEvent event) {
    	txtResult.clear();
    	this.model.creaGrafo();
    	txtResult.setText("Grafo creato: "+this.model.getNVertici()+" vertici e "+this.model.getNumArchi()+" archi.\n");
    	txtResult.appendText("Peso minimo= "+this.model.getPesoMinimo());
    	txtResult.appendText("\nPeso massimo= "+this.model.getPesoMassimo());
    	
    	double soglia;
    	try {
    		soglia=Double.parseDouble(txtSoglia.getText());
    		if(soglia<this.model.getPesoMinimo() || soglia>this.model.getPesoMassimo()) {
    			txtResult.setText("Inserire un valore tra: "+this.model.getPesoMinimo()+" e "+this.model.getPesoMassimo());
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico.");
    		return;
    	}
    	
    	String res= this.model.getMaggioriMinori(soglia);
    	txtResult.appendText("\n"+res);
    }
    

    @FXML
    void doRicerca(ActionEvent event) {
    	txtResult.clear();
    	double soglia;
    	try {
    		soglia=Double.parseDouble(txtSoglia.getText());
    		if(soglia<this.model.getPesoMinimo() || soglia>this.model.getPesoMassimo()) {
    			txtResult.setText("Inserire un valore tra: "+this.model.getPesoMinimo()+" e "+this.model.getPesoMassimo());
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico.");
    		return;
    	}
    	
    	List<Integer> percorso=this.model.calcolaPercorso(soglia);
    	txtResult.setText("Il percorso trovato è lungo :"+ this.model.getLunghezza());
    	for(Integer i: percorso) {
    		txtResult.appendText("\n"+ i);
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnContaArchi != null : "fx:id=\"btnContaArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSoglia != null : "fx:id=\"txtSoglia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model ;
		
	}
}
