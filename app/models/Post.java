package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.persistence.*;
//import views.forms
import play.data.validation.ValidationError;
import views.forms.EditPostForm;
import utils.S3FileUpload;


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
	public String tags;
	public String category;
	public String imageFile;

	public String imageUrl;

	public Date dateTimePosted;

	public Date dateTimeLastEdited;
	public boolean feedbackEnabled;
	
	
	public static Find<Integer,Post> find = new Find<Integer,Post>(){};

	public Post(String title, String subtitle, String description,
		String tags, String category, boolean feedbackEnabled) {
		this.title = title;
		this.subtitle = subtitle;
		this.description = description;
		this.tags = tags;
		//this.postType = postType;
		this.category = category;
		this.feedbackEnabled = feedbackEnabled;
	}

	public Post() {
		this.postType = "none";
		this.title = "";
		this.subtitle = "";
		this.description = "";
		this.tags = "";
		this.feedbackEnabled = false;
	}

	public static Post findById(int id) {
		return find.byId(id);
	}

	public static List<Post> findByCategory(String category) {
		return find.where().ieq("category", category).findList();
	}

	public static PagedList<Post> findPageList(int page, int pageSize, String category) {
		return find.where().ieq("category", category).findPagedList(page, pageSize);
	}

	public static PagedList<Post> findPageByAuthor(int page, int pageSize, String username) {
		return find.where().eq("author_username", username).orderBy("dateTimePosted desc").findPagedList(page, pageSize);
	}

	public static Post findRecentByAuthor(String username, int no) {
		List<Post> list = find.where().eq("author_username", username).orderBy("dateTimePosted desc").findList();
		Post post;
		if (no < list.size()) {
			post = list.get(no);
		} else {
			return null;
		}
		return post;
	}

	public static List<Post> findAll() {
		return find.where().findList();
	}

	public static List<Post> findRecent(int maxRows) {
		return find.where().orderBy("dateTimePosted desc").setMaxRows(maxRows).findList();
	}

	public static PagedList<Post> findSearchPage(int page, int pageSize, String query) {
		return find.where()
		  .or()
		    .and()
		      .ilike("title", "%" + query + "%")
		      .endJunction()
		    .and()
		      .ilike("category", "%" + query + "%")
		      .endJunction()
		    .and()
		      .ilike("subtitle", "%" + query + "%")
		      .endJunction()
		    .and()
		      .ilike("tags", "%" + query + "%")
		      .endJunction()
		    .and()
		      .ilike("author_username", "%" + query + "%")
		      .endJunction()
		    .findPagedList(page, pageSize);
	}

	public static boolean memberHasPosts(Member member) {
		int postsNum = find.where().eq("author_username", member.username).findRowCount();
		if(postsNum > 0) {
			return true;
		}
		return false;
	}

	public static int memberPostCount(Member member) {
		return find.where().eq("author_username", member.username).findRowCount();

	}
	
	public String getImageUrl() {
		return S3FileUpload.getUrl("post-images", String.valueOf(this.id), this.imageFile);

	}

	public String getDatePosted() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy");
		return dateFormat.format(this.dateTimePosted);
	}

	public String toString() {
		return String.format("[Post ID: '%d', Title: '%s', Subtitle: '%s', Description: '%s', Tags: '%s',"
			+ "Post type: '%s', Category: '%s', Feedback enabled: '%s']", this.id, this.title, this.subtitle,
			this.description, this.tags, this.postType, this.category, this.feedbackEnabled);
	}

	@Override
	public void save() {
		dateTimePosted();
		super.save();
	}

	@Override
	public void update() {
		dateTimeLastEdited();
		super.update();
	}

	@PreRemove
	public void deleteImageFile() {
		S3FileUpload.deleteFileFromS3("post-images", String.valueOf(this.id), this.imageFile);
	}

	@PrePersist
	public void dateTimePosted() {
		this.dateTimePosted = this.dateTimeLastEdited = new Date();
	}

	@PreUpdate
	public void dateTimeLastEdited() {
		this.dateTimeLastEdited = new Date();
	}

	/**
	* Form validation.
	* Checks title is not empty
	*/
	public List<ValidationError> validate() {

		List<ValidationError> errors = new ArrayList<>();

		if (title == null || title.length() == 0) {
			errors.add(new ValidationError("title", "Please enter a post title"));
		}

		/*if (imageFile == null) {
			errors.add(new ValidationError("imageFile", "Please select an image file"));
		}*/

		return errors;
	}

	public void editPost(EditPostForm editedPost) {
		this.title = editedPost.title;
		this.subtitle = editedPost.subtitle;
		this.description = editedPost.description;
		this.tags = editedPost.tags;
		this.category = editedPost.category;
		this.feedbackEnabled = editedPost.feedbackEnabled;
		save();
	}
}