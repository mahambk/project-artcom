package controllers;

import play.mvc.*;
import views.html.*;
import models.Member;
import play.data.Form;
import views.forms.SearchForm;


/**
 */
public class Community extends Controller {

    /**
     */
    public Result index() {
        return ok(community.render(Member.findAllPagedList(0, 4), ""));
    }

    public Result listMembers(int page) {
        return ok(community.render(Member.findAllPagedList(page, 4), ""));
    }

    public Result searchMembers() {
        Form<SearchForm> form = Form.form(SearchForm.class).bindFromRequest();
        String query = form.get().query;
        return ok(community.render(Member.findSearchPage(0, 10, query), query));
    }

    public Result getSearchResultPage(int page, String query){
        return ok(community.render(Member.findSearchPage(page, 10, query), query));
    }

}
