package controllers;

import play.mvc.*;
import models.Post;
import views.html.*;

/**
 */
public class Browse extends Controller {

    /**
     */
    public Result index() {
        return ok(browse.render("drawing"));
    }

    public Result list(int page, String category){
        return ok(browsePage.render(Post.findPageList(page, 8, category), category));
    }

    public Result byCategory(String category) {
        return ok(browse.render(category));
    }

    /*public Result searchPosts() {

    }

    public Result listSearchResults() {

    }*/

    public Result discover() {
        return TODO;
    }


}
