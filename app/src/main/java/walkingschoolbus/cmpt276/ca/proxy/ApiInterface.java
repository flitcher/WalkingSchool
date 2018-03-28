package walkingschoolbus.cmpt276.ca.proxy;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;

/**
 * Created by Kawai on 3/5/2018.
 */

public interface ApiInterface {
    @GET("getApiKey")
    Call<String> getApiKey(@Query("groupName") String groupName,
                           @Query("sfuUserId") String sfuId);

    @POST("/users/signup")
    Call<User> createNewUser(@Body User user);

    @POST("/login")
    Call<Void> login(@Body User userWithEmailAndPassword);

    @GET("/users")
    Call<List<User>> getUsers();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") Long userId);

    @GET("/users/byEmail")
    Call<User> getUserByEmail(@Query("email") String email);

    @POST("/users/{id}")
    Call<User> editUser(@Path("id") Long userId,@Body User user);



    @GET("/users/{id}/monitorsUsers")
    Call<List<User>> getMonitorUser(@Path("id") Long userId);

    @GET ("/users/{id}/monitoredByUsers")
    Call<List<User>> getMonitoredByUser(@Path("id") Long userId);

    @POST ("/users/{id}/monitorsUsers")
    Call<List<User>> addMonitorUsers(@Path("id") Long userId, @Body Map<String,Long> body);

    @POST ("/users/{id}/monitoredByUsers")
    Call<List<User>> addMonitoredByUsers(@Path("id") Long userId,@Body Map<String,Long> body);


    @DELETE("/users/{idA}/monitorsUsers/{idB}")
    Call<Void> deleteMonitorUser(@Path("idA") Long userIdA, @Path("idB") Long userIdB);

    @DELETE("/users/{idA}/monitoredByUsers/{idB}")
    Call<Void> deleteMonitoredByUser(@Path("idA") Long userIdA, @Path("idB") Long userIdB);

    /**
     * MORE GOES HERE:
     * - Monitoring
     * - Groups
     */
    @GET("/groups")
    Call<List<WalkingGroups>> getGroups();

    @POST("/groups")
    Call<WalkingGroups> createNewGroup(@Body WalkingGroups walkingGroups);

    @GET("/groups/{id}")
    Call<WalkingGroups> getOneGroup(@Path("id") Long groupID);

    @POST("/groups/{id}")
    Call<WalkingGroups> updateExistingGroup(@Path("id") Long groupID, @Body WalkingGroups walkingGroups);

    @DELETE("/groups/{id}")
    Call<Void> deleteGroup(@Path("id") Long groupID);

    @GET("/groups/{id}/memberUsers")
    Call<List<User>> getGroupMembers(@Path("id") Long groupID);

    @POST("/groups/{id}/memberUsers")
    Call<List<User>> addNewGroupMember(@Path("id") Long groupID, @Body Map<String, Long> userID);

    @DELETE("/groups/{idA}/memberUsers/{idB}")
    Call<Void> deleteGroupMember(@Path("idA") Long groupID, @Path("idB") Long UserID);


    //IN App message
    //Return all messages:
    @GET ("/messages")
    Call<List<Message>> getAllMessage();

    //Only return messages with is-emergency flag set:
    @GET ("/messages?is-emergency=true")
    Call<List<Message>> getAllEmergencyMessage();

    //Only return messages sent to group 42:
    @GET ("/messages?")
    Call<List<Message>> getMessageToGroup(@Query("togroup") Long group);

    //Only return messages sent to group 42 and are an emergency:
    @GET ("/messages?is-emergency=true")
    Call<List<Message>> getEmergencyMessageToGroup(@Query("togroup") Long groupID);

    //Only return messages for user 85:
    @GET ("/messages?")
    Call<List<Message>> getMessageForUser(@Query("foruser") Long UserID);

    //Only return messages for user 85 which are unread:
    @GET ("/messages?status=unread")
    Call<List<Message>> getUnreadMessage(@Query ("foruser")Long UserID);

    //Only return messages for user 85 which are read:
    @GET ("/messages?status=read")
    Call<List<Message>> getReadMessage(@Query("foruser") Long UserID);

    //Only return messages for user 85 which are unread and emergency:
    @GET ("/messages?status=unread&is-emergency=true")
    Call<List<Message>> getUnreadEmergencyMessage(@Query("foruser") Long UserID);

    //New message to group:
    @POST ("/messages/togroup/{groupId}")
    Call<Message> sendMessagesToGroup(@Path("groupId") Long groupId,@Body Message body );

    //New message to the ‘parents’ of a user:
    @POST ("/messages/toparentsof/{userId}")
    Call<Message> sendMessagesToParentOfUser(@Path("userId") Long UserID,@Body Message body );

    //Get one message:
    @GET ("/messages/{id}")
    Call<Message> getOneMessageById(@Path("id") Long MessageID);

    //Mark message as read/unread by user:
    @POST ("/messages/{messageId}/readby/{userId}")
    Call<User> markMessageByUser(@Path("userId") Long UserID,@Path("messageId") Long MessageID,@Body Boolean readState);



}
