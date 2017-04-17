package controllers;

import play.mvc.*;
import play.data.Form;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;
import java.io.File;
import java.nio.file.*;
import models.Member;
import models.Post;
import views.html.*;
import views.forms.EditProfile;
import utils.S3FileUpload;


/**
 */
public class Profile extends Controller {

    /**
     */
    public Result index(String username) {
    	if (username != null && !username.isEmpty()) {
    		Member member = Member.findByUsername(username);
                if(member != null) {
        	       return ok(profile.render(member));
                }
    	}
    	return badRequest(index.render());
    }

    public Result myProfile() {
    	if(Application.isLoggedIn()) {
    		Member member = Member.findByUsername(session().get("loggedIn"));
        	return ok(profile.render(member));
    	} else {
    		return badRequest(index.render());
    	}
    }

    public Result viewPosts(String username) {
        if (username != null && !username.isEmpty()) {
            Member member = Member.findByUsername(username);
                if(member != null) {
                   return ok(memberPosts.render(Post.findPageByAuthor(0, 12, member.username), member));
                }
        }
        return badRequest(index.render());
    }


    public Result viewPostPage(String username, int page) {
        if (username != null && !username.isEmpty()) {
            Member member = Member.findByUsername(username);
                if(member != null) {
                   return ok(memberPosts.render(Post.findPageByAuthor(page, 12, member.username), member));
                }
        }
        return badRequest(index.render());
    }

    public Result editProfile() {
        if(Application.isLoggedIn()) {
            Member member = Member.findByUsername(session().get("loggedIn"));

            EditProfile currentProfileInfo = new EditProfile(member.firstname, member.lastname,
            member.email, member.level, member.location, member.skills, member.bio);

            Form<EditProfile> profileForm = Form.form(EditProfile.class).fill(currentProfileInfo);

            return ok(editProfile.render(profileForm, member));
        } else {
            return badRequest(index.render());
        }
    }

    public Result submitChanges() {
        Member member = Member.findByUsername(session().get("loggedIn"));
        Form<EditProfile> profileForm = Form.form(EditProfile.class).bindFromRequest();
        String newProfilePicUrl = "";
        if (profileForm.hasErrors()) {
            flash("error", "Please complete above fields.");
            return ok(editProfile.render(profileForm, member));
        } else {
            // Profile image upload
            MultipartFormData<File> body = request().body().asMultipartFormData();
            FilePart<File> imageFilePart = body.getFile("image");
            if (imageFilePart != null) {
                String fileName = imageFilePart.getFilename();
                String contentType = imageFilePart.getContentType();
                File imageFile = imageFilePart.getFile();
                if (fileName != null && !fileName.equals("")) {
                    S3FileUpload.uploadFileToS3(imageFile, "profile-images", member.username, fileName);
                    newProfilePicUrl = S3FileUpload.getUrl("profile-images", member.username, fileName);
                }
            }
            member.updateInfo(profileForm.get(), newProfilePicUrl);
            return ok(profile.render(member));
        }
    }

    public Result resetProfilePic() {
        if(Application.isLoggedIn()) {
            Member member = Member.findByUsername(session().get("loggedIn"));
            member.resetProfilePic();
        }
        return redirect(routes.Profile.editProfile());
    }
}
