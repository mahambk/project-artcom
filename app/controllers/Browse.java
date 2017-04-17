package controllers;

import play.mvc.*;
import models.Post;
import views.html.*;
import play.data.Form;
import views.forms.SearchForm;


/**
 */
public class Browse extends Controller {

    /**
     */
    public Result browseByCategory(int page, String category){
        return ok(browsePage.render(Post.findPageList(page, 24, category), category));
    }

    public Result searchPosts() {
        Form<SearchForm> form = Form.form(SearchForm.class).bindFromRequest();
        String query = form.get().query;
        return ok(postSearchResults.render(Post.findSearchPage(0, 24, query), query));
    }

    public Result getSearchResultPage(int page, String query){
        return ok(postSearchResults.render(Post.findSearchPage(page, 24, query), query));
    }

    /*public Result listSearchResults() {

    }*/

    public Result discover() {
        return TODO;
    }

}
