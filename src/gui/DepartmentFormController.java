package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.swevices.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;

	private DepartmentService service;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btnSave;;

	@FXML
	private Button btnCancelar;

	public void subscribeDataChangedListerner(DataChangeListener listner) {
		dataChangeListener.add(listner);
	}

	@FXML
	public void onBtnSave(ActionEvent evento) {
		if (entity == null) {
			throw new IllegalStateException("o departamento nao foi instanciado");
		}
		if (service == null) {
			throw new IllegalStateException("o servico nao foi instanciado");
		}

		try {

			entity = getFormData();
			service.saveOrUpdadte(entity);
			notifyDataChangeListerner();
			Utils.CurrentState(evento).close();
		} 
		catch (ValidationException e) {
			setErrorMessage(e.getErros());
		}
		 catch (DbException e) {
			Alerts.showAlert("erro na hora de salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListerner() {
		for(DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
		
	}

	private Department getFormData() {
		Department obj = new Department();
		ValidationException exception = new ValidationException("erro de validacao");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals(""))
		{
			exception.addErro("name", "o campo nao pode ser vazio");
		}
		
		obj.setName(txtName.getText());
		
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	public void onBtnCancelar(ActionEvent evento) {
		Utils.CurrentState(evento).close();
	}

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("entidade não foi estanciada");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtId.setText(entity.getName());
	}
	private void setErrorMessage(Map<String,String> erros) {
		Set<String> fields = erros.keySet();
		if(fields.contains("name")) {
			labelErrorName.setText(erros.get("name"));
		}
			
	}

}
