package model.swevices;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;



public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();

	public List<Department> findAll(){
		//List<Department> list = new ArrayList<>();
		//list.add(new Department(1,"Livros"));
		//list.add(new Department(2,"Computadores"));
		//list.add(new Department(3,"Eletronocos"));
		// return list;
		return dao.findAll();
		
	}
	
	public void saveOrUpdadte (Department obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}
}
