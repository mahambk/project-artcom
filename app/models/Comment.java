package models;

import com.avaje.ebean.Model;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;
//import views.forms

@Entity
public class Comment extends Model {

	@Id
	public int id;

	public String cmntBody;

	@ManyToOne(targetEntity = Member.class)
	public Member author;

	@ManyToOne(targetEntity = Post.class)
	public Post post;

	public Date dateTimeSent;

	
	public static Find<Integer,Comment> find = new Find<Integer,Comment>(){};

	public Comment(String cmntBody) {
		this.cmntBody = cmntBody;
	}

	public Comment() {

	}

	public static Comment findById(int id) {
		return find.byId(id);
	}

	public static List<Comment> findFor(int postId) {
		return find.where().eq("post_id", postId).orderBy("dateTimeSent desc").findList();
	}

	public static List<Comment> findAll() {
		return find.where().findList();
	}

	public String getDateTimeSent() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy 'at' h:mm a");
		return dateFormat.format(this.dateTimeSent);
	}

	public String toString() {
		return String.format("[Comment ID: '%d', Body: '%s', Author: '%s', Post ID: '%s', Date & Time: '%s']",
		this.id, this.cmntBody, this.author.username, this.post.id, this.dateTimeSent);
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