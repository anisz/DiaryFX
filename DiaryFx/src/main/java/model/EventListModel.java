package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import utils.ApplicationException;
import utils.Queries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import converters.PersonConverter;
import converters.EventConverter;
import converters.CategoryConverter;

/**
 * Developed by anisz
 */
public class EventListModel {

	private ObservableList<EventFx> eventFxObservableList = FXCollections.observableArrayList();
	private ObservableList<PersonFx> personFxObservableList = FXCollections.observableArrayList();
	private ObservableList<CategoryFx> categoryFxObservableList = FXCollections.observableArrayList();

	private ObjectProperty<PersonFx> personFxObjectProperty = new SimpleObjectProperty<>(); 
	private ObjectProperty<CategoryFx> categoryFxObjectProperty = new SimpleObjectProperty<>();

	private List<EventFx> eventFxList = new ArrayList<>(); 

	public void init() throws ApplicationException {
		List<Event> events = Queries.queryForAll(Event.class);
		eventFxList.clear();
		events.forEach(event -> {
			this.eventFxList.add(EventConverter.convertToEventFx(event));
		});
		this.eventFxObservableList.setAll(eventFxList);

		initPeople();
		initCategory();
	}

	public void filterEventsList() {
		if (getPersonFxObjectProperty() != null && getCategoryFxObjectProperty() != null) {
			filterPredicate(predicatePerson().and(predicateCatgory()));
		} else if (getCategoryFxObjectProperty() != null) {
			filterPredicate(predicateCatgory());
		} else if (getPersonFxObjectProperty() != null) {
			filterPredicate(predicatePerson());
		} else {
			this.eventFxObservableList.setAll(this.eventFxList);
		}
	}

	public void deleteEvent(EventFx eventFx) throws ApplicationException {
		Queries.deleteById(Event.class, eventFx.getId());
		init();
	}
	
	private void initPeople() throws ApplicationException {
		List<Person> personList = Queries.queryForAll(Person.class);
		this.personFxObservableList.clear();
		personList.forEach(person -> {
			PersonFx personFx = PersonConverter.convertToPersonFx(person);
			this.personFxObservableList.add(personFx);
		});
	}

	private void initCategory() throws ApplicationException {
		List<Category> categories = Queries.queryForAll(Category.class);
		this.categoryFxObservableList.clear();
		categories.forEach(c -> {
			CategoryFx categoryFx = CategoryConverter.convertToCategoryFx(c);
			this.categoryFxObservableList.add(categoryFx);
		});
	}

	private Predicate<EventFx> predicateCatgory() {
		return eventFx -> eventFx.getCategoryFx().getId() == getCategoryFxObjectProperty().getId();
	}

	private Predicate<EventFx> predicatePerson() {
		return eventFx -> eventFx.getPersonFx().getId() == getPersonFxObjectProperty().getId();
	}

	private void filterPredicate(Predicate<EventFx> predicate) {
		List<EventFx> newList = eventFxList.stream().filter(predicate).collect(Collectors.toList());
		this.eventFxObservableList.setAll(newList);
	}

	public ObservableList<EventFx> getEventFxObservableList() {
		return eventFxObservableList;
	}

	public void setEventFxObservableList(ObservableList<EventFx> eventFxObservableList) {
		this.eventFxObservableList = eventFxObservableList;
	}

	public ObservableList<PersonFx> getPersonFxObservableList() {
		return personFxObservableList;
	}

	public void setPersonFxObservableList(ObservableList<PersonFx> personFxObservableList) {
		this.personFxObservableList = personFxObservableList;
	}

	public ObservableList<CategoryFx> getCategoryFxObservableList() {
		return categoryFxObservableList;
	}

	public void setCategoryFxObservableList(ObservableList<CategoryFx> categoryFxObservableList) {
		this.categoryFxObservableList = categoryFxObservableList;
	}

	public PersonFx getPersonFxObjectProperty() {
		return personFxObjectProperty.get();
	}

	public ObjectProperty<PersonFx> personFxObjectPropertyProperty() {
		return personFxObjectProperty;
	}

	public void setPersonFxObjectProperty(PersonFx personFxObjectProperty) {
		this.personFxObjectProperty.set(personFxObjectProperty);
	}

	public CategoryFx getCategoryFxObjectProperty() {
		return categoryFxObjectProperty.get();
	}

	public ObjectProperty<CategoryFx> categoryFxObjectPropertyProperty() {
		return categoryFxObjectProperty;
	}

	public void setCategoryFxObjectProperty(CategoryFx categoryFxObjectProperty) {
		this.categoryFxObjectProperty.set(categoryFxObjectProperty);
	}
}
