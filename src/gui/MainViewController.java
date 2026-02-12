package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onmenuItemDepartamento");
	}
	@FXML
	public void onmenuItemAboutAction() {
		System.out.println("menuItemAbout");
	}
	
	
	
	
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}

}
