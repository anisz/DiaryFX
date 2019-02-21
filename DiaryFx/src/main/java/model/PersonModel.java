package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Queries;

import java.sql.SQLException;
import java.util.List;

import converters.PersonConverter;

/**
 * Developed by anisz
 */

public class PersonModel {

    private ObjectProperty<PersonFx> personFxObjectProperty = new SimpleObjectProperty<>(new PersonFx());
    private ObjectProperty<PersonFx> personFxObjectPropertyEdit = new SimpleObjectProperty<>(new PersonFx());

    private ObservableList<PersonFx> personFxObservableList = FXCollections.observableArrayList();


    public void init() throws Exception {
        List<Person> personList = Queries.queryForAll(Person.class); 
        this.personFxObservableList.clear();
        personList.forEach(person -> {
            PersonFx personFx = PersonConverter.convertToPersonFx(person);
            this.personFxObservableList.add(personFx);
        });

    }

    public void savePersonEditInDataBase() throws Exception {
        saveOrUpdate(this.getPersonFxObjectPropertyEdit());

    }
    public void savePersonInDataBase() throws Exception {
    	saveOrUpdate(this.getPersonFxObjectProperty());
    }

    public void deletePersonInDataBase() throws Exception, SQLException {
    	Queries.deleteById(Person.class, this.getPersonFxObjectPropertyEdit().getId());
        this.init();	
    }

    private void saveOrUpdate(PersonFx personFxObjectPropertyEdit) throws Exception {
        Person person = PersonConverter.converToPerson(personFxObjectPropertyEdit);
        Queries.create(person);
        this.init();
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

    public ObservableList<PersonFx> getPersonFxObservableList() {
        return personFxObservableList;
    }

    public void setPersonFxObservableList(ObservableList<PersonFx> personFxObservableList) {
        this.personFxObservableList = personFxObservableList;
    }

    public PersonFx getPersonFxObjectPropertyEdit() {
        return personFxObjectPropertyEdit.get();
    }

    public ObjectProperty<PersonFx> personFxObjectPropertyEditProperty() {
        return personFxObjectPropertyEdit;
    }

    public void setPersonFxObjectPropertyEdit(PersonFx personFxObjectPropertyEdit) {
        this.personFxObjectPropertyEdit.set(personFxObjectPropertyEdit);
    }
}
