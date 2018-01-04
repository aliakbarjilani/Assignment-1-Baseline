package parsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bethecoder.ascii_table.ASCIITable;
import com.bethecoder.ascii_table.impl.CollectionASCIITableAware;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

import models.Activity;
import models.Location;
import models.User;

public class Parser {

  public void println(String s) {
    System.out.println(s);
  }

  public void renderUser(User user) {
    System.out.println(user.toString());
  }

  public void renderUsers(Collection<User> users) {
    System.out.println(users.toString());
  }

  public void renderActivity(Activity activities) {
    System.out.println(activities.toString());
  }

  public void renderActivities(Collection<Activity> activities) {
    System.out.println(activities.toString());
  }
  
  public void renderfarActivities(List<Activity> activities) {
    System.out.println(activities.toString());
  }

  public void renderLocations(List<Location> locations) {
    System.out.println(locations.toString());
  }
  
  public void renderFriends(Collection<User> friends)
  {
    System.out.println(friends.toString());
  }
  
  public void renderMessages(List<String> messages) 
  {
    System.out.println(messages.toString());
  }
  
}