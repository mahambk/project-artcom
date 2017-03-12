package controllers;

import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;
import java.io.File;
import java.nio.file.*;
import views.html.*;
import models.Post;
import models.Member;
import play.data.Form;

/**
 */
public class Posts extends Controller {

    /*
     **/
    public Result viewPost(int postId) {
        //if (postId != null && !postId.isEmpty()) {
            Post post = Post.findById(postId);
            return ok(viewPost.render(post));
        //} else {
            //return badRequest(home.render());
        //}
        
    }


    public Result uploadPost() {

        Form<Post> form = Form.form(Post.class).bindFromRequest();

        if (form.hasErrors()) {
            flash("error", "Please complete above fields.");
            return badRequest(newPost.render(""));
        } else {
            Post post = form.get();
            MultipartFormData<File> body = request().body().asMultipartFormData();
            FilePart<File> image = body.getFile("image");
            if (image != null) {
                String fileName = image.getFilename();
                String contentType = image.getContentType();
                File imageFile = image.getFile();
                java.nio.file.Path currentRelativePath = Paths.get("");
                String path = currentRelativePath.toAbsolutePath().toString();
                String filePath = path + "/public/images/post-images/";
                imageFile.renameTo(new File(filePath, fileName));
                post.imageFile = fileName;
                //Member member = Member.findByUsername(session().get("loggedIn"));
                Member member = Member.findByUsername("snowwhite");
                post.author = member;
                post.save();
                return ok(viewPost.render(post));
                //return ok(newPost.render(post.toString()));
            } else {
                flash("error", "Missing file");
                return badRequest(home.render());
            }
        }
     }

    public Result newPost() {
	   return ok(newPost.render(""));
    }



}
