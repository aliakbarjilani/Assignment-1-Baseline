package controllers;

import io.javalin.Context;
import models.Activity;
import models.Location;
import models.User;
import static models.Fixtures.users;

public class PacemakerRestService {

  PacemakerAPI pacemaker = new PacemakerAPI();
  User loggedInUser = null;

  PacemakerRestService() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
  }

  public void listUsers(Context ctx) {
    ctx.json(pacemaker.getUsers());
  }
  
  public void createUser(Context ctx) {
    User user = ctx.bodyAsClass(User.class);
    User newUser = pacemaker
        .createUser(user.firstname, user.lastname, user.email, user.password);
    ctx.json(newUser);
  }
  
  public void listUser(Context ctx) {
    String id = ctx.param("id");
    ctx.json(pacemaker.getUser(id));
  }
  
  public void getActivities(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(user.activities.values());
    } else {
      ctx.status(404);
    }
  }

  public void createActivity(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if (user != null) {
      Activity activity = ctx.bodyAsClass(Activity.class);
      Activity newActivity = pacemaker
          .createActivity(id, activity.type, activity.location, activity.distance);
      ctx.json(newActivity);
    } else {
      ctx.status(404);
    }
  }
  
  public void getActivity(Context ctx) {
    String aid = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(aid);
    if (activity != null) {
      ctx.json(activity);
    } else {
      ctx.status(404);
    }
  }
  
  public void addLocation(Context ctx) {
    String aid = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(aid);
    if (activity != null) {
      Location location = ctx.bodyAsClass(Location.class);
      activity.route.add(location);
      ctx.json(activity.route);
    } else {
      ctx.status(404);
    }
  }
  
  public void getActivityLocations(Context ctx) {
    String aid = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(aid);
    if (activity != null) {
      ctx.json(activity.route);
    } else {
      ctx.status(404);
    }
  }
    
  public void deleteUser(Context ctx) {
    String id = ctx.param("id");
    ctx.json(pacemaker.deleteUser(id));
  }

  public void deleteUsers(Context ctx) {
    pacemaker.deleteUsers();
    ctx.json(204);
  }
  
  public void deleteActivities(Context ctx) {
    String id = ctx.param("id");
    pacemaker.deleteActivities(id);
    ctx.json(204);
  }
  
  public void listActivityLocations(Context ctx) {
    String id = ctx.param("activity-id");
    pacemaker.listActivityLocations(id);
    ctx.json(204);
  }

}