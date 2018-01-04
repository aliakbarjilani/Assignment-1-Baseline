package controllers;

import com.google.common.base.Optional;

import asg.cliche.Command;
import asg.cliche.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.Activity;
import models.User;
import parsers.AsciiTableParser;
import parsers.Parser;

public class PacemakerConsoleService {

  private PacemakerAPI paceApi = new PacemakerAPI();;
  private Parser console = new AsciiTableParser();
  private User loggedInUser = null;

  public PacemakerConsoleService() 
  {
  
  }

  // Starter Commands

  // ru (firstname, lastname, email, password)
  @Command(description = "Register: Create an account for a new user")
  public void register(@Param(name = "first name") String firstName,
      @Param(name = "last name") String lastName,
      @Param(name = "email") String email, @Param(name = "password") String password) 
  {
    console.renderUser(paceApi.createUser(firstName, lastName, email, password));
  }

  // gu()
  @Command(description = "List Users: List all users emails, first and last names")
  public void listUsers() 
  {
    console.renderUsers(paceApi.getUsers());
  }

  //login-user(email, password)
  @Command(description = "Login: Log in a registered user in to pacemaker")
  public void login(@Param(name = "email") String email,
      @Param(name = "password") String password) 
  {
    Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
    if (user.isPresent()) 
    {
      if (user.get().password.equals(password)) 
      {
        loggedInUser = user.get();
        console.println("Logged in " + loggedInUser.email);
        console.println("ok");
      } else 
      {
        console.println("Error on login");
      }
    }    
  }

  //logout
  @Command(description = "Logout: Logout current user")
  public void logout() 
  {
    console.println("Logging out " + loggedInUser.email);
    console.println("ok");
    loggedInUser = null;
  }

  //
  @Command(description = "Add activity: create and add an activity for the logged in user")
  public void addActivity(
      @Param(name = "type") String type,
      @Param(name = "location") String location,
      @Param(name = "distance") double distance) 
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      console.renderActivity(paceApi.createActivity(user.get().id, type, location, distance));
    }
  }

  //
  @Command(description = "List Activities: List all activities for logged in user")
  public void listActivities() 
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console
          .renderActivities(paceApi.getActivities(user.get().id));
    }
  }

  // Baseline Commands

  // al add-location(activity-id, longitude, latitude)
  @Command(description = "Add location: Append location to an activity")
  public void addLocation(@Param(name = "activity-id") String id,
      @Param(name = "longitude") double longitude,
      @Param(name = "latitude") double latitude) 
  {
    Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(id));
    if (activity.isPresent()) 
    {
      paceApi.addLocation(activity.get().id, latitude, longitude);
      console.println("ok");
    } 
    else 
    {
      console.println("not found");
    }
  }

  /*@Command(description = "ActivityReport: List all activities for logged in user, sorted alphabetically by type")
  public void activityReport() 
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      console.renderActivities(paceApi.listActivities(user.get().id, "type"));
    }
  }*/

  // ar activity-report()
  @Command(description = "Activity Report: List all activities for logged in user by distance. Sorted longest to shortest distance")
  public void activityReport()
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      List<Activity> reportActivities = new ArrayList<>();
      Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
      usersActivities.forEach(a -> {reportActivities.add(a);});
      reportActivities.sort((a1, a2) -> Double.compare(a2.distance, a1.distance));
      console.renderActivities(reportActivities);
    }
  } 
  
  @Command(description = "Activity Report: List all activities for logged in user by type.")
  public void activityReport(@Param(name = "byType: type") String type) 
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      List<Activity> reportActivities = new ArrayList<>();
      Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
      usersActivities.forEach(a -> {if (a.type.equals(type)) reportActivities.add(a);});
      reportActivities.sort((a1, a2) -> {if (a1.distance >= a2.distance) return -1; else return 1; });
      console.renderActivities(reportActivities);
    }
  }

  // lal list-activity-locations(activity-id)
  @Command(description = "List all locations for a specific activity")
  public void listActivityLocations(@Param(name = "activity-id") String id) 
  {
      console.renderLocations(paceApi.listActivityLocations(id));
  }

  // f follow(email)
  @Command(description = "Follow Friend: Follow a specific friend")
  public void followfriend(@Param(name = "email") String email) 
  {
    // If user is logged-in.
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      // Find the user having the email address 
      Optional<User> friend = Optional.fromNullable(paceApi.getUserByEmail(email));
      if (friend.isPresent()) 
      {
        // Save the user account in the FriendIndex listing against the email address same as emailIndex below.
        paceApi.Follow(email);
        console.println("Following " + email + " now");
        console.println("ok");
      } else
      {
        console.println("Invalid follow request, unrecognised email address..");
      }    
    } else
    {
      console.println("Invalid follow request, please login first..");
    }  
  }
  // lf list-friends

  // lf listfriends()

  @Command(description = "List Friends: List all of the friends of the logged in user")
  public void listFriends() 
  {
    // If user is logged-in.
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      console.renderUsers(paceApi.listFriends());
    }
  }

  // far friend-activity-report()

  // far friend-activity-report(email)  
  @Command(description = "Friend Activity Report - List all activities of specific friend, sorted alphabetically by type")
  public void friendActivityReport(@Param(name = "email") String email) 
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      // Find the friend having the email address 
      Optional<User> friend = Optional.fromNullable(paceApi.getFriendByEmail(email));
      if (friend.isPresent()) 
      {
        List<Activity> reportActivities = new ArrayList<>();
        Collection<Activity> friendActivities = paceApi.getActivities(friend.get().id);
        //friendActivities.forEach(a -> {if (a.type.equals(email)) reportActivities.add(a); });
        friendActivities.forEach(a -> {reportActivities.add(a);});
        reportActivities.sort((a1, a2) -> a1.type.compareTo(a2.type));
        console.renderfarActivities(reportActivities);
        //console.renderActivities(paceApi.getActivities(friend.get().id));
      } else
      {
        console.println("Invalid request, unrecognised email address..");
      }    
    } else
    {
      console.println("Invalid unfollow request, please login first..");
    }  
  }

  // uf unfollowfriend(email)
  // uf unfollowFriend(email)
  // Good Commands

  @Command(description = "Unfollow Friends: Stop following a friend")
  public void unfollowFriend(@Param(name = "email") String email) 
  {
    // If user is logged-in.
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      // Find the user having the email address 
      Optional<User> friend = Optional.fromNullable(paceApi.getUserByEmail(email));
      if (friend.isPresent()) 
      {
        // Save the user account in the FriendIndex listing against the email address same as emailIndex below.
        paceApi.unFollow(email);
        console.println(email + " unfollowed");
        console.println("ok");
      } else
      {
        console.println("Invalid follow request, unrecognised email address..");
      }    
    }
  }

  //mf message-friend(email, message)
  // mf messageFriend(email, message)
  @Command(description = "Message Friend: send a message to a friend")
  public void messageFriend(@Param(name = "email") String email,
      @Param(name = "message") String message) 
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      // Find the friend having the email address 
      Optional<User> friend = Optional.fromNullable(paceApi.getFriendByEmail(email));
      if (friend.isPresent()) 
      {
        console.println(paceApi.messageFriend(email, message));
      } else
      {
        console.println("Invalid request, unrecognised friend email..");
      }    
    } else
    {
      console.println("Invalid unfollow request, please login first..");
    }  
  }

  // lm listMessages()
  @Command(description = "List Messages: List all messages for the logged in user")
  public void listMessages() 
  {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) 
    {
      //console.renderMessages(paceApi.listMessages());
      
        List<String> userMessages = new ArrayList<>();
        Collection<String> messageCollection = paceApi.listMessages();
        messageCollection.forEach(a -> {userMessages.add(a);});
        console.renderMessages(userMessages);
      
      
      console.println("messages printed.");
    } else
    {
      console.println("Invalid unfollow request, please login first..");
    }      
  }

  @Command(description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
  public void distanceLeaderBoard() 
  {
  
  }

  // Excellent Commands

  @Command(description = "Distance Leader Board: distance leader board refined by type")
  public void distanceLeaderBoardByType(@Param(name = "byType: type") String type) 
  {
  
  }

  @Command(description = "Message All Friends: send a message to all friends")
  public void messageAllFriends(@Param(name = "message") String message) 
  {
  
  }

  @Command(description = "Location Leader Board: list sorted summary distances of all friends in named location")
  public void locationLeaderBoard(@Param(name = "location") String message) 
  {
  
  }

  // Outstanding Commands

  // Todo
}