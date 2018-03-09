package walkingschoolbus.cmpt276.ca.proxy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import walkingschoolbus.cmpt276.ca.dataObjects.User;

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

    /**
     * MORE GOES HERE:
     * - Monitoring
     * - Groups
     */
}
