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
            //return badRequest(index.render());
        //}
        
    }

    public Result viewPostCommentArea(int postId) {
        //if (postId != null && !postId.isEmpty()) {
            Post post = Post.findById(postId);
            return ok(viewPost.render(post));
        //} else {
            //return badRequest(index.render());
        //}
    }

    public Result viewPostFeedbackArea(int postId) {
        //if (postId != null && !postId.isEmpty()) {
            Post post = Post.findById(postId);
            return ok(viewPost.render(post));
        //} else {
            //return badRequest(index.render());
        //}
        
    }

    public Result newPost() {
        Form<Post> form = Form.form(Post.class).fill(new Post());
       return ok(newPost.render("", form));
    }   

    public Result uploadPost() {

        Form<Post> form = Form.form(Post.class).bindFromRequest();

        if (form.hasErrors()) {
            String errorMsg = form.errorsAsJson().toString();
            flash("error", "Please complete above fields.");
            return ok(newPost.render(errorMsg, form));
        } else {
            Post post = form.get();
            MultipartFormData<File> body = request().body().asMultipartFormData();
            FilePart<File> image = body.getFile("image");
            if (image != null) {
                String fileName = image.getFilename();
                String contentType = image.getContentType();
                File imageFile = image.getFile();
                if (fileName != null && !fileName.equals("")) {
                    java.nio.file.Path currentRelativePath = Paths.get("");
                    String relativePath = currentRelativePath.toAbsolutePath().toString();
                    String filePath = relativePath + "/public/images/post-images/";
                    imageFile.renameTo(new File(filePath, fileName));
                    post.imageFile = "images/post-images/" + fileName;
                    Member member = Member.findByUsername(session().get("loggedIn"));
                    post.author = member;
                    post.save();
                    return ok(viewPost.render(post));
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

        return badRequest(viewPost.render(post));
     }

    public Result submitChanges(int postId) {
        Form<EditPostForm> form = Form.form(EditPostForm.class).bindFromRequest();
        Post post = Post.findById(postId);

        if (form.hasErrors()) {
            flash("error", "Please complete above fields.");
            return ok(editPost.render(form, postId));
        } else {
            post.editPost(form.get());
            return ok(viewPost.render(post));
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
                return ok(viewPost.render(post));
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
                return ok(viewPost.render(post));
            } else {
                flash("success", "Form parsed with no errors.");
                Feedback fdbk = fdbkForm.get();
                Member member = Member.findByUsername(session().get("loggedIn"));
                fdbk.author = member;
                fdbk.post = post;

                MultipartFormData<File> body = request().body().asMultipartFormData();
                FilePart<File> image = body.getFile("image");
                if (image != null) {
                    String fileName = image.getFilename();
                    String contentType = image.getContentType();
                    File imageFile = image.getFile();
                    if (fileName != null && !fileName.equals("")) {
                        fdbk.imageAttached = true;
                        java.nio.file.Path currentRelativePath = Paths.get("");
                        String relativePath = currentRelativePath.toAbsolutePath().toString();
                        String filePath = relativePath + "/public/images/feedback-images/";
                        imageFile.renameTo(new File(filePath, fileName));
                        fdbk.imageFile = "images/feedback-images/" + fileName;
                    } else {
                        fdbk.imageAttached = false;
                    }
                }
                fdbk.save();
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
