package controllers;

import play.mvc.*;
import play.libs.Json;

import views.html.*;

import java.util.List;

import models.*;


public class TestingController extends Controller {

    public Result getMembers() {
    	List <Member> members = Member.find.all();
        return ok(Json.toJson(members));
    }

}