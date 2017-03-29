package models;

import com.avaje.ebean.Model;

import java.util.*;
import javax.persistence.*;
//import views.forms

@Entity
public class Feedback extends Model {

	@Id
	public int id;

	public String fdbkBody;

	@ManyToOne(targetEntity = Member.class)
	public Member author;

	@ManyToOne(targetEntity = Post.class)
	public Post post;

	public Date dateTimeSent;

	
	public static Find<Integer,Feedback> find = new Find<Integer,Feedback>(){};

	public Feedback(String fdbkBody) {
		this.fdbkBody = fdbkBody;
	}

	public Feedback() {

	}

	public static Feedback findById(int id) {
		return find.byId(id);
	}

	public static List<Feedback> findAll() {
		return find.where().findList();
	}

	public String toString() {
		return String.format("[Feedback ID: '%d', Body: '%s', Author: '%s', Post ID: '%s', Date & Time: '%s']",
		this.id, this.fdbkBody, this.author.username, this.post.id, this.dateTimeSent);
	}

	@Override
	public void save() {
		dateSent();
		super.save();
	}

	@PrePersist
	public void dateSent() {
		this.dateTimeSent = new Date();
	}

}