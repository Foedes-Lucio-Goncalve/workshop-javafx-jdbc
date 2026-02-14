package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onmenuItemVendedorAction() {
		System.out.println("onmenuItemVendedor");
	}
	@FXML
	public void onmenuItemDepartamentoAction() {
		loadView("/gui/DepartmentList.fxml");
	}
	@FXML
	public void onmenuItemAboutAction() {
		loadView("/gui/about.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	public synchronized void loadView(String nomeAbsoluto) {
		
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
		VBox newVbox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVbox =  (VBox)((ScrollPane) mainScene.getRoot()).getContent();
		Node mainMenu =	mainVbox.getChildren().get(0);	
		mainVbox.getChildren().clear();
		mainVbox.getChildren().add(mainMenu);
		mainVbox.getChildren().addAll(newVbox.getChildren());
		
		
		}
		catch(IOException e ) {
		Alerts.showAlert("erro ", "erro carregando a pagina", e.getMessage(), AlertType.ERROR);	
		}
	}

}
