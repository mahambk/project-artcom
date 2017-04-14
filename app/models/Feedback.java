package models;

import com.avaje.ebean.Model;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;
import utils.S3FileUpload;

@Entity
public class Feedback extends Model {

	@Id
	public int id;

	public String fdbkBody;

	public String imageFile;

	@ManyToOne(targetEntity = Member.class)
	public Member author;

	@ManyToOne(targetEntity = Post.class)
	public Post post;

	public Date dateTimeSent;

	public boolean imageAttached;

	
	public static Find<Integer,Feedback> find = new Find<Integer,Feedback>(){};

	public Feedback(String fdbkBody) {
		this.fdbkBody = fdbkBody;
	}

	public Feedback() {
	}

	public static Feedback findById(int id) {
		return find.byId(id);
	}

	public static List<Feedback> findFor(int postId) {
		return find.where().eq("post_id", postId).orderBy("dateTimeSent desc").findList();
	}

	public static List<Feedback> findAll() {
		return find.where().findList();
	}

	public String getDateTimeSent() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy 'at' h:mm a");
		return dateFormat.format(this.dateTimeSent);
	}

	public String getImageUrl() {
		return S3FileUpload.getUrl("feedback-images", String.valueOf(this.id), this.imageFile);

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

	@PreRemove
	public void deleteImageFile() {
		S3FileUpload.deleteFileFromS3("feedback-images", String.valueOf(this.id), this.imageFile);
	}

}