package models;

import com.avaje.ebean.Model;

import java.util.*;
import javax.persistence.*;
//import views.forms

@Entity
public class Post extends Model {

	@Id
	public int id;

	public String postType;
	public String title;
	public String subtitle;

	@ManyToOne(targetEntity = Member.class)
	public Member author;

	public String description;
	public String tags;	// probably should parse into an array
	public String category; // probably should be an instance of a 'category' type
	public String imageFile;

	public Date datePosted;

	public Date dateLastEdited;
	public boolean feedbackEnabled;
	
	
	public static Find<Integer,Post> find = new Find<Integer,Post>(){};

	public Post(String title, String subtitle, String description,
		String tags, String postType, String category, boolean feedbackEnabled) {
		this.title = title;
		this.subtitle = subtitle;
		this.description = description;
		this.tags = tags;
		this.postType = postType;
		this.category = category;
		this.feedbackEnabled = feedbackEnabled;
	}

	public Post() {

	}

	public static Post findById(int id) {
		return find.byId(id);
	}

	public static List<Post> findByCategory(String category) {
		return find.where().ieq("category", category).findList();
	}

	public static List<Post> findAll() {
		return find.where().findList();
	}

	public String toString() {
		return String.format("[Post ID: '%d', Title: '%s', Subtitle: '%s', Description: '%s', Tags: '%s',"
			+ "Post type: '%s', Category: '%s', Feedback enabled: '%s']", this.id, this.title, this.subtitle,
			this.description, this.tags, this.postType, this.category, this.feedbackEnabled);
	}

	@Override
	public void save() {
		datePosted();
		super.save();
	}

	@Override
	public void update() {
		dateLastEdited();
		super.update();
	}

	@PrePersist
	public void datePosted() {
		this.datePosted = this.dateLastEdited = new Date();
	}

	@PreUpdate
	public void dateLastEdited() {
		this.dateLastEdited = new Date();
	}

}