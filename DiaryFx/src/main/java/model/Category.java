package model;

/**
 * Created by anisz
 */

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

@Entity
public class Category implements BaseModel {

	public Category() {
	}

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "NAME", nullable = false, unique = true) 
	private String name;

	@OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE) 
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

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return name;

	}
}
