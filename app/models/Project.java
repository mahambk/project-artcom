package models;

import com.avaje.ebean.Model;

import java.util.*;
import javax.persistence.*;

@Entity
public class Project extends Model {

	@Id
	public int id;

	public String projectTitle;

	@ManyToMany
	@JoinTable(
		name="project_creators",
		joinColumns=@JoinColumn(name="project_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="creator_username", referencedColumnName="username"))
	public List<Member> authors;

	public String description;

	public Date dateCreated;

	
	public static Find<Integer,Project> find = new Find<Integer,Project>(){};

	public Project(String title) {
		this.projectTitle = title;
	}

	public Project() {

	}

	public static Project findById(int id) {
		return find.byId(id);
	}

	public static List<Project> findAll() {
		return find.where().findList();
	}

	@Override
	public void save() {
		dateCreated();
		super.save();
	}

	@PrePersist
	public void dateCreated() {
		this.dateCreated = new Date();
	}

}