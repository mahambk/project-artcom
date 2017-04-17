package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.persistence.*;
import play.data.validation.ValidationError;
import views.forms.EditTutorialForm;
import utils.S3FileUpload;


@Entity
public class Tutorial extends Model {

	@Id
	public int id;

	public String title;

	@ManyToOne(targetEntity = Member.class)
	public Member author;

	public String intro;
	public String body;
	public String tags;
	public String category;
	public String imageFile1;
	public String imageFile2;
	public String videoFile;

	public Date dateTimePosted;
	public Date dateTimeLastEdited;
	
	
	public static Find<Integer,Tutorial> find = new Find<Integer,Tutorial>(){};

	public Tutorial(String title, String intro, String body, String tags, String category) {
		this.title = title;
		this.intro = intro;
		this.body = body;
		this.tags = tags;
		this.category = category;
	}

	public Tutorial() {
		this.title = "";
		this.intro = "";
		this.body = "";
		this.tags = "";
	}

	public static Tutorial findById(int id) {
		return find.byId(id);
	}

	public static PagedList<Tutorial> findPageList(int page, int pageSize, String category) {
		if(category.equals("all")) {
			return find.where().orderBy("dateTimePosted desc").findPagedList(page, pageSize);
		} else {
		return find.where().ieq("category", category).orderBy("dateTimePosted desc").findPagedList(page, pageSize);
		}
	}

	public static PagedList<Tutorial> findPageList(int page, int pageSize) {
		return find.where().findPagedList(page, pageSize);
	}

	public static List<Tutorial> findByAuthor(String username) {
		return find.where().eq("author_username", username).orderBy("dateTimePosted desc").findList();
	}

	public static List<Tutorial> findRecentByAuthor(String username, int maxRows) {
		return find.where().eq("author_username", username).orderBy("dateTimePosted desc").setMaxRows(maxRows).findList();
	}

	public static boolean memberHasTutorials(Member member) {
		int postsNum = find.where().eq("author_username", member.username).findRowCount();
		if(postsNum > 0) {
			return true;
		}
		return false;
	}

	public static int memberTutorialCount(Member member) {
		return find.where().eq("author_username", member.username).findRowCount();

	}
	
	public String getImage1Url() {
		return S3FileUpload.getUrl("tutorial-images", String.valueOf(this.id), this.imageFile1);

	}

	public String getImage2Url() {
		return S3FileUpload.getUrl("tutorial-images", String.valueOf(this.id), this.imageFile2);

	}
	
	public String getVideoUrl() {
		return S3FileUpload.getUrl("tutorial-videos", String.valueOf(this.id), this.videoFile);

	}

	public String getDatePosted() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy");
		return dateFormat.format(this.dateTimePosted);
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
	public void deleteMediaFiles() {
		S3FileUpload.deleteFileFromS3("tutorial-images", String.valueOf(this.id), this.imageFile1);
		if (!imageFile2.equals("")) {
			S3FileUpload.deleteFileFromS3("tutorial-images", String.valueOf(this.id), this.imageFile2);
		}
		if (!videoFile.equals("")) {
			S3FileUpload.deleteFileFromS3("tutorial-videos", String.valueOf(this.id), this.videoFile);
		}
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
			errors.add(new ValidationError("title", "Please enter a title"));
		}

		if (body == null || body.length() == 0) {
			errors.add(new ValidationError("body", "Please insert tutorial body"));
		}

		return errors;
	}

	public void editTutorial(EditTutorialForm editedTutorial) {
		this.title = editedTutorial.title;
		this.intro = editedTutorial.intro;
		this.body = editedTutorial.body;
		this.category = editedTutorial.category;
		this.tags = editedTutorial.tags;
		save();
	}
}