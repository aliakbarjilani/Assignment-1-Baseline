package controllers;

import io.javalin.Javalin;

public class RestMain {
  
  public static void main(String[] args) throws Exception 
  {
    Javalin app = Javalin.start(7000);
    PacemakerRestService service = new PacemakerRestService();
    configRoutes(app, service);
  }

  static void configRoutes(Javalin app, PacemakerRestService service) 
  {
    app.get("/users", ctx -> {service.listUsers(ctx);});      // gu
   
    app.post("/users", ctx -> {service.createUser(ctx);});    // ru
    
    // lu (login-User)
    
    // l  (log-out)
    
    app.get("/users/:id", ctx -> {service.listUser(ctx);});   
    
    app.get("/users/:id/activities", ctx -> {service.getActivities(ctx);});   //

    app.post("/users/:id/activities", ctx -> {service.createActivity(ctx);}); // aa add-activity(type, location, distance)
    
    app.get("/users/:id/activities/:activityId", ctx -> {service.getActivity(ctx);});
    
    // Baseline /////////////////////////////////////////////////////////////////////////////////////////
    
    app.post("/users/activities/:activityId/", ctx -> {service.addLocation(ctx);});   // al add-location(activity-id, longitude, latitude)

    app.get("/users/activities/:activityId/locations", ctx -> {service.getActivityLocations(ctx);});  // lal list-activity-locations(activity-id)
    
    app.get("",  ctx -> {});  // ar activity-report()
    
    app.get("",  ctx -> {});  // f follow(email)
    
    app.get("",  ctx -> {});  // lf listfriends()    
    
    app.get("",  ctx -> {});  // far friend-activity-report()    
    
    app.delete("/users", ctx -> {service.deleteUsers(ctx);});

    app.delete("/users/:id", ctx -> {service.deleteUser(ctx);});

    app.delete("/users/:id/activities", ctx -> {service.deleteActivities(ctx);});
 
  }
  
}
