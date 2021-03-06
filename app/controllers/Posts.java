package controllers;

import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;
import java.io.File;
import java.nio.file.*;
import views.html.*;
import models.*;
import play.data.Form;
import views.forms.EditPostForm;
import utils.S3FileUpload;

/**
 */
public class Posts extends Controller {

    /*
     **/
    public Result viewPost(int postId) {
        Post post = Post.findById(postId);
        if (post != null) {
            Member author = Member.findByUsername(post.author.username);
            return ok(viewPost.render(post, author));
        }
        return badRequest(index.render());
    }

    public Result viewPostCommentArea(int postId) {
        Post post = Post.findById(postId);
        if (post != null) {
            Member author = Member.findByUsername(post.author.username);
            return ok(viewPost.render(post, author));
        }
        return badRequest(index.render());
    }

    public Result viewPostFeedbackArea(int postId) {
        Post post = Post.findById(postId);
        if (post != null) {
            Member author = Member.findByUsername(post.author.username);
            return ok(viewPost.render(post, author));
        }
        return badRequest(index.render());
        
    }

    public Result newPost() {
        if(Application.isLoggedIn()){
            Form<Post> form = Form.form(Post.class).fill(new Post());
            return ok(newPost.render("", form));
        }
        return redirect(routes.Application.login());
    }   

    public Result uploadPost() {

        Form<Post> form = Form.form(Post.class).bindFromRequest();
        Member member = Member.findByUsername(session().get("loggedIn"));

        if (form.hasErrors()) {
            return ok(newPost.render("Please complete above fields.", form));
        } else {
            Post post = form.get();
            
            // Upload image file to AWS S3 bucket
            MultipartFormData<File> body = request().body().asMultipartFormData();
            FilePart<File> imageFilePart = body.getFile("image");
            if (imageFilePart != null) {
                String fileName = imageFilePart.getFilename();
                File imageFile = imageFilePart.getFile();
                
                //String contentType = image.getContentType();
                if (fileName != null && !fileName.equals("")) {
                    post.author = member;
                    post.imageFile = fileName;
                    post.save();
                    S3FileUpload.uploadFileToS3(imageFile, "post-images", String.valueOf(post.id), fileName);
                    return ok(viewPost.render(post, member));
                }
            }  
        }
            return ok(newPost.render("Please choose an image file to upload", form));
     }

     public Result editPost(int postId) {
        Post post = Post.findById(postId);

        if(post.author.username.equals(session().get("loggedIn"))) {
            EditPostForm currentPostForm = new EditPostForm(post.id, post.title, post.subtitle, post.description, post.tags,
                post.category, post.feedbackEnabled);
            Form<EditPostForm> form = Form.form(EditPostForm.class).fill(currentPostForm);

            return ok(editPost.render(form, postId));
        }

        return redirect(routes.Posts.viewPost(post.id));
     }

    public Result submitChanges(int postId) {
        Form<EditPostForm> form = Form.form(EditPostForm.class).bindFromRequest();
        Post post = Post.findById(postId);

        if (form.hasErrors()) {
            flash("error", "Please complete above fields.");
            return ok(editPost.render(form, postId));
        } else {
            post.editPost(form.get());
            return redirect(routes.Posts.viewPost(post.id));
        }
    }

    public Result deletePost(int postId) {
        Post post = Post.findById(postId);
        String author = post.author.username;

        if(author.equals(session().get("loggedIn"))) {
            post.delete();
        }
        return redirect(routes.Profile.index(author));
    }

     public Result sendComment(int postId) {
        if(Application.isLoggedIn()){
            Form<Comment> cmntForm = Form.form(Comment.class).bindFromRequest();
            Post post = Post.findById(postId);
            if (cmntForm.hasErrors()) {
                flash("error", "Illegal characters.");
                return redirect(routes.Posts.viewPost(post.id));
            } else {
                flash("success", "Form parsed with no errors.");
                Comment cmnt = cmntForm.get();
                Member member = Member.findByUsername(session().get("loggedIn"));
                cmnt.author = member;
                cmnt.post = post;
                cmnt.save();
                return redirect(routes.Posts.viewPostCommentArea(postId));
            }
        }
        return redirect(routes.Application.login());
     }

     public static boolean canDeleteComment(int cmntId, int postId) {
        if(Application.isLoggedIn()){
            Comment comment = Comment.findById(cmntId);
            Post post = Post.findById(postId);
            String cmntAuthor = comment.author.username;
            String postAuthor = post.author.username;
            String currentUser = session().get("loggedIn");
            if(currentUser.equals(cmntAuthor) || currentUser.equals(postAuthor)) {
                return true;
            }
        }
        return false;
     }

     public Result deleteComment(int cmntId, int postId) {
        Comment comment = Comment.findById(cmntId);
        if(Posts.canDeleteComment(cmntId, postId)) {
             comment.delete();
         }
         return redirect(routes.Posts.viewPostCommentArea(comment.post.id));
     }

    public Result sendFeedback(int postId) {
        if(Application.isLoggedIn()){
            Form<Feedback> fdbkForm = Form.form(Feedback.class).bindFromRequest();
            Post post = Post.findById(postId);
            if (fdbkForm.hasErrors() || post.feedbackEnabled == false) {
                flash("error", "Illegal characters.");
                return redirect(routes.Posts.viewPost(post.id));
            } else {
                flash("success", "Form parsed with no errors.");
                Feedback fdbk = fdbkForm.get();
                Member member = Member.findByUsername(session().get("loggedIn"));
                fdbk.author = member;
                fdbk.post = post;
                MultipartFormData<File> body = request().body().asMultipartFormData();
                FilePart<File> imageFilePart = body.getFile("image");
                if (imageFilePart != null) {
                    String fileName = imageFilePart.getFilename();
                    String contentType = imageFilePart.getContentType();
                    File imageFile = imageFilePart.getFile();
                    fdbk.imageAttached = false;

                    if (fileName != null && !fileName.equals("")) {
                        fdbk.imageAttached = true;
                        fdbk.imageFile = fileName;
                        fdbk.save();
                        S3FileUpload.uploadFileToS3(imageFile, "feedback-images", String.valueOf(fdbk.id), fileName);
                    } else {
                        fdbk.save();
                    }
                }
                
                return redirect(routes.Posts.viewPostFeedbackArea(postId));
            }
        }
        return redirect(routes.Application.login());
     }

    public static boolean canDeleteFeedback(int fdbkId, int postId) {
       if(Application.isLoggedIn()){
           Feedback feedback = Feedback.findById(fdbkId);
           Post post = Post.findById(postId);
           String fdbkAuthor = feedback.author.username;
           String postAuthor = post.author.username;
           String currentUser = session().get("loggedIn");
           if(currentUser.equals(fdbkAuthor) || currentUser.equals(postAuthor)) {
               return true;
           }
       }
       return false;
    }

    public Result deleteFeedback(int fdbkId, int postId) {
       Feedback feedback = Feedback.findById(fdbkId);
       if(Posts.canDeleteFeedback(fdbkId, postId)) {
            feedback.delete();
        }
        return redirect(routes.Posts.viewPostFeedbackArea(feedback.post.id));
    }

    

}
