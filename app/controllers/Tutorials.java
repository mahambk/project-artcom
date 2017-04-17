package controllers;

import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;
import java.io.File;
import java.nio.file.*;
import views.html.*;
import models.*;
import play.data.Form;
import views.forms.EditTutorialForm;
import utils.S3FileUpload;

/**
 */
public class Tutorials extends Controller {

    /*
     **/
    public Result browseTutorials(int page, String category){
        return ok(browseTutorials.render(Tutorial.findPageList(page, 12, category), category));
    }

    public Result viewTutorial(int tutorialId) {
        Tutorial tutorial = Tutorial.findById(tutorialId);
        if (tutorial != null) {
            Member author = Member.findByUsername(tutorial.author.username);
            return ok(viewTutorial.render(tutorial, author));
        }
        return badRequest(index.render());
    }

    public Result newTutorial() {
        if(Application.isLoggedIn()){
            Form<Tutorial> form = Form.form(Tutorial.class).fill(new Tutorial());
            return ok(newTutorial.render("", form));
        }
        return redirect(routes.Application.login());
    }

    public Result viewMemberTutorials(String username) {
        if (username != null && !username.isEmpty()) {
            Member member = Member.findByUsername(username);
                if(member != null) {
                   return ok(memberTutorials.render(member));
                }
        }
        return badRequest(index.render());
    }   

    public Result uploadTutorial() {

        Form<Tutorial> form = Form.form(Tutorial.class).bindFromRequest();
        Member member = Member.findByUsername(session().get("loggedIn"));

        if (form.hasErrors()) {
            return ok(newTutorial.render("Please complete above fields.", form));
        } else {
            Tutorial tutorial = form.get();

            // Check if a video file was uploaded
            boolean uploadVideo = false;
            String videoFileName = "";
            MultipartFormData<File> body = request().body().asMultipartFormData();
            FilePart<File> videoFilePart = body.getFile("video");
            if (videoFilePart != null) {
                videoFileName = videoFilePart.getFilename();
                File videoFile = videoFilePart.getFile();
                //String contentType = video.getContentType();
                if (videoFileName != null && !videoFileName.equals("")) {
                    tutorial.videoFile = videoFileName;
                    uploadVideo = true;
                } else {
                    tutorial.videoFile = "";
                }
            }

            // Check if the second image was uploaded
            boolean uploadImage2 = false;
            String image2FileName = "";
            FilePart<File> image2FilePart = body.getFile("image2");
            if (image2FilePart != null) {
                image2FileName = image2FilePart.getFilename();
                File image2File = image2FilePart.getFile();
                //String contentType = video.getContentType();
                if (image2FileName != null && !image2FileName.equals("")) {
                    tutorial.imageFile2 = image2FileName;
                    uploadImage2 = true;
                } else {
                    tutorial.imageFile2 = "";
                }
            }
            
            // Get the first image and upload all files to AWS S3 bucket
            FilePart<File> image1FilePart = body.getFile("image1");
            if (image1FilePart != null) {
                String image1FileName = image1FilePart.getFilename();
                File image1File = image1FilePart.getFile();
                if (image1FileName != null && !image1FileName.equals("")) {
                    tutorial.author = member;
                    tutorial.imageFile1 = image1FileName;
                    tutorial.save();
                    if(uploadVideo) {
                        File videoFile = videoFilePart.getFile();
                        S3FileUpload.uploadFileToS3(videoFile, "tutorial-videos", String.valueOf(tutorial.id), videoFileName);
                    }
                    if(uploadImage2) {
                        File image2File = image2FilePart.getFile();
                        S3FileUpload.uploadFileToS3(image2File, "tutorial-images", String.valueOf(tutorial.id), image2FileName);
                    }
                    S3FileUpload.uploadFileToS3(image1File, "tutorial-images", String.valueOf(tutorial.id), image1FileName);
                    return ok(viewTutorial.render(tutorial, member));
                }
            }  
        }
        return ok(newTutorial.render("Please choose an image file to upload", form));
    }

    public Result editTutorial(int tutorialId) {
        Tutorial tutorial = Tutorial.findById(tutorialId);

        if(tutorial.author.username.equals(session().get("loggedIn"))) {
            EditTutorialForm currentTutorialForm = new EditTutorialForm(tutorial.id, tutorial.title,
                tutorial.intro, tutorial.body, tutorial.tags, tutorial.category);
            Form<EditTutorialForm> form = Form.form(EditTutorialForm.class).fill(currentTutorialForm);

            return ok(editTutorial.render(form, tutorialId));
        }

        return redirect(routes.Tutorials.viewTutorial(tutorial.id));
    }

    public Result submitChanges(int tutorialId) {
        Form<EditTutorialForm> form = Form.form(EditTutorialForm.class).bindFromRequest();
        Tutorial tutorial = Tutorial.findById(tutorialId);

        if (form.hasErrors()) {
            flash("error", "Please complete above fields.");
            return ok(editTutorial.render(form, tutorialId));
        } else {
            tutorial.editTutorial(form.get());
            return redirect(routes.Tutorials.viewTutorial(tutorial.id));
        }
    }

    public Result deleteTutorial(int tutorialId) {
        Tutorial tutorial = Tutorial.findById(tutorialId);
        String author = tutorial.author.username;

        if(author.equals(session().get("loggedIn"))) {
            tutorial.delete();
        }
        return redirect(routes.Profile.index(author));
    }

}
