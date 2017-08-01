package ch.blutch.battlephoto.controller.utils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.model.dao.impl.CategoryDaoImpl;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.service.impl.CategoryServiceImpl;

@Component("categoryConverter")
public class CategoryConverter implements Converter {
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		return categoryService.findCategoryByName(value);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		Category category = (Category) value;
		
		return category.getDescriptionFr();
	}

}
