package controllers;

import play.mvc.*;

import views.html.*;

/**
 */
public class Browse extends Controller {

    /**
     */
    public Result index() {
        return ok(browse.render());
    }

    public Result discover() {
        return TODO;
    }


}
