package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.swevices.DepartmentService;
import model.swevices.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private DepartmentService departmentservice;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBrithDate;
	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBrithDate;
	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btnSave;;

	@FXML
	private Button btnCancelar;

	private ObservableList<Department> obsList;

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
		} catch (ValidationException e) {
			setErrorMessage(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("erro na hora de salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListerner() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}

	}

	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException exception = new ValidationException("erro de validacao");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addErro("name", "o campo nao pode ser vazio");
		}
		obj.setName(txtName.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addErro("email", "o campo nao pode ser vazio");
		}
		obj.setEmail(txtEmail.getText());
		
		if (dpBrithDate.getValue() == null) {
			exception.addErro("birthDate", "o campo nao pode ser vazio");
		}
		else
		{
		Instant instant = Instant.from(dpBrithDate.getValue().atStartOfDay(ZoneId.systemDefault()));
        obj.setBirthDate(Date.from(instant));
		}
		 
        
        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addErro("baseSalary", "o campo nao pode ser vazio");
		}
        obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
        
		obj.setDepartment(comboBoxDepartment.getValue());
		
		
		if (exception.getErros().size() > 0) {
			throw exception;
		}

		return obj;
	}

	public void onBtnCancelar(ActionEvent evento) {
		Utils.CurrentState(evento).close();
	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentservice = departmentService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBrithDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("entidade não foi estanciada");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBrithDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if (entity.getDepartment() == null)
		{
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
		
		
	}

	public void loadAssociatedObjects() {
		if (departmentservice == null) {
			throw new IllegalStateException("departamento nao foi instanciado");
		}
		List<Department> list = departmentservice.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessage(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
//		if (fields.contains("name")) {
//			labelErrorName.setText(erros.get("name"));
//		}
//		else
//		{
//			labelErrorName.setText(erros.get(""));
//		}
		
		// ternario
//		labelErrorName.setText(( fields.contains ("name")? erros.get("name"): ""));
//		labelErrorEmail.setText((fields.contains ("email")? erros.get("email"): ""));
//		labelErrorBrithDate.setText((fields.contains("birthDate")? erros.get("birthDate"): ""));
//		labelErrorBaseSalary.setText((fields.contains("baseSalary")? erros.get("baseSalary"): ""));
		
//		if (fields.contains("email")) {
//			labelErrorEmail.setText(erros.get("email"));
//		}
//		else {
//			labelErrorEmail.setText(erros.get(""));
//		}
//		
//		if (fields.contains("birthDate")) {
//			labelErrorBrithDate.setText(erros.get("birthDate"));
//		}
//		else {
//			labelErrorBrithDate.setText(erros.get(""));
//		}
//		if (fields.contains("baseSalary")) {
//			labelErrorBaseSalary.setText(erros.get("baseSalary"));
//		}
//		else {
//			labelErrorBaseSalary.setText(erros.get(""));
//		}
		
	  	// ternario
		labelErrorName.setText(( fields.contains ("name")? erros.get("name"): ""));
		labelErrorEmail.setText((fields.contains ("email")? erros.get("email"): ""));
		labelErrorBrithDate.setText((fields.contains("birthDate")? erros.get("birthDate"): ""));
		labelErrorBaseSalary.setText((fields.contains("baseSalary")? erros.get("baseSalary"): ""));


	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
