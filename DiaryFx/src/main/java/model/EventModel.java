package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.ApplicationException;
import utils.PersistenceManager;
import utils.Queries;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import converters.PersonConverter;
import converters.PictureConverter;
import converters.EventConverter;
import converters.CategoryConverter;

/**
 * Developed by anisz
 */
public class EventModel {

	private ObjectProperty<EventFx> eventFxObjectProperty = new SimpleObjectProperty<>(new EventFx());
	private ObservableList<CategoryFx> categoryFxObservableList = FXCollections.observableArrayList();
	private ObservableList<PersonFx> personFxObservableList = FXCollections.observableArrayList();

	public void init() throws ApplicationException {
		initPersonList();
		initCategoryList();
	}

	private void initCategoryList() throws ApplicationException {
		List<Category> categoryList = Queries.queryForAll(Category.class);
		categoryFxObservableList.clear();
		categoryList.forEach(c -> {
			CategoryFx categoryFx = CategoryConverter.convertToCategoryFx(c);
			categoryFxObservableList.add(categoryFx);
		});
	}

	private void initPersonList() throws ApplicationException {
		List<Person> personList = Queries.queryForAll(Person.class);
		this.personFxObservableList.clear();
		personList.forEach(person -> {
			PersonFx personFx = PersonConverter.convertToPersonFx(person);
			this.personFxObservableList.add(personFx);
		});
	}

	public void saveEventInDataBase() throws ApplicationException {

		Event editedEvent = Queries.findById(Event.class, this.getEventFxObjectProperty().getId());

		if (editedEvent == null) {
			Event event = EventConverter.convertToEvent(this.getEventFxObjectProperty());
			// z categoryFx i PersonFX pobieram id
			Category category = Queries.findById(Category.class,
					this.getEventFxObjectProperty().getCategoryFx().getId());
			Person person = Queries.findById(Person.class, this.getEventFxObjectProperty().getPersonFx().getId());
			event.setCategory(category);
			event.setPerson(person);
			Queries.create(event);
		} else {
			editedEvent.setTitle(this.getEventFxObjectProperty().getTitle());
			editedEvent.setDescription(this.getEventFxObjectProperty().getDescription());
			editedEvent.setRating(this.getEventFxObjectProperty().getRating());
			editedEvent.setReleaseDate(this.getEventFxObjectProperty().getReleaseDate());
			editedEvent.setAddedDate(this.getEventFxObjectProperty().getAddedDate());
			editedEvent.setImage(PictureConverter.convertToByteArray(this.getEventFxObjectProperty().getImageFX()));
			Category category = Queries.findById(Category.class, this.getEventFxObjectProperty().getCategoryFx().getId());
			Person person = Queries.findById(Person.class, this.getEventFxObjectProperty().getPersonFx().getId());
			editedEvent.setCategory(category);
			editedEvent.setPerson(person);
			Queries.update(editedEvent);
		}
	}

	public EventFx getEventFxObjectProperty() {
		return eventFxObjectProperty.get(); 
	}

	public ObjectProperty<EventFx> eventFxObjectPropertyProperty() {
		return eventFxObjectProperty;
	}

	public void setEventFxObjectProperty(EventFx eventFxObjectProperty) {
		this.eventFxObjectProperty.set(eventFxObjectProperty); 
	}

	public ObservableList<CategoryFx> getCategoryFxObservableList() {
		return categoryFxObservableList;
	}

	public void setCategoryFxObservableList(ObservableList<CategoryFx> categoryFxObservableList) {
		this.categoryFxObservableList = categoryFxObservableList;
	}

	public ObservableList<PersonFx> getPersonFxObservableList() {
		return personFxObservableList;
	}

	public void setPersonFxObservableList(ObservableList<PersonFx> personFxObservableList) {
		this.personFxObservableList = personFxObservableList;
	}

}