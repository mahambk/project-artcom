package controllers;

import play.mvc.*;

import views.html.*;

/**
 */
public class Community extends Controller {

    /**
     */
    public Result index() {
        return ok(community.render());
    }

}
