package model;

/**
 * Created by anisz
 */

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person implements BaseModel{
 
    public Person() {
    }

	@Id
    @GeneratedValue//(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SURNAME", nullable = false)
    private String surname;
    
    @OneToMany( mappedBy="person", cascade = CascadeType.MERGE) 
    private List<Event> events;
    
    public int getId() {
		return id; 
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return name + " " + surname + " ";
	}
}
