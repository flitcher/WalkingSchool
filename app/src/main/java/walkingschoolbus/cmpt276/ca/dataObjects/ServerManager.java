package walkingschoolbus.cmpt276.ca.dataObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.appUI.AddActivity;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;


/**
 * Created by Jonathan Chen on 2018/3/11.
 */

public class ServerManager {
    private static ApiInterface proxy;
    private static User userManager = User.getInstance();
    private static final String APIKEY ="E14DEF58-61CB-4425-B6FB-BDBD807E44CF ";
    private static boolean Login = false;
    private static Context currentContext;

    //setLogin
    public static void setDoLogin(boolean login){
        Login = login;
    }

    public static void connectToServerWithoutToken(Context context){
        currentContext = context;
        proxy = ProxyBuilder.getProxy(APIKEY,null);
    }
    public static void connectToServerWithToken(Context context){
        currentContext = context;
        proxy = ProxyBuilder.getProxy(APIKEY,userManager.getToken());
    }
    //register
    public static void createNewUser(User user)
    {
        Call<User> caller = proxy.createNewUser(user);
        ProxyBuilder.callProxy(currentContext,caller,returnedUser->responseRegister(returnedUser));
    }
    private static void responseRegister(User user){
        String TAG = "Server";
        Log.w(TAG, "Server replied with user: " + user.toString());
    }
    //refresh token
    public static void refreshToken(){
        ProxyBuilder.setOnTokenReceiveCallback(token->onReceiveToken(token));
    }

    private static  void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        String TAG = "Proxy";
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        userManager.setToken(token);
        proxy = ProxyBuilder.getProxy(APIKEY,userManager.getToken());
    }
    //login
   public static void getUserByEmail(){

        Call<User> caller = proxy.getUserByEmail(userManager.getEmail());
        ProxyBuilder.callProxy(currentContext,caller,returedUser->responseAutoLogin(returedUser));

    }

    private static void responseAutoLogin(User user){
        userManager.setUser(user);
        Call<List<User>> callerForResetParent = proxy.getMonitoredByUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext,callerForResetParent,returnedList->resetParentList(returnedList));
        Call<List<User>> callerForResetChild = proxy.getMonitorUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext,callerForResetChild,returnedList->resetChildList(returnedList));

    }

    public static boolean doLogin(){
        return Login;
    }

    public static void Login(){
        Call<Void> caller = proxy.login(userManager.getUser());
        ProxyBuilder.callProxy(currentContext,caller,returnedNothing->reponseLogin(returnedNothing));
    }
    private static void reponseLogin(Void Nothing){
        String TAG = "Proxy";
        Log.w(TAG, "Server replied to login request (no content was expected).");
        ServerManager.getUserByEmail();
    }
    //for add child
    public static void addMonitorUser (String email){
        Call<User> callerForEmail = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(currentContext, callerForEmail,returnedUser -> addChild(returnedUser));
    }
    private static void addChild(User user) {
            long userId = user.getId();

            Map<String,Long> body = new HashMap<String, Long>();
            body.put("id", userId);

            Call<List<User>> callerForAdd = proxy.addMonitorUsers(userManager.getId(), body);
            ProxyBuilder.callProxy(currentContext, callerForAdd, returnedList -> resetChildList(returnedList));
    }
    private static void resetChildList(List<User> list) {
        userManager.setMonitorsUsers(list);
    }



    //for add parent
    public static void addMonitedByUser(String email){
        Call<User> callerForEmail = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(currentContext, callerForEmail, returnedUser -> addParent(returnedUser));
    }
    private static void addParent(User user) {
        long userId = user.getId();

        Map<String,Long> body = new HashMap<String, Long>();
        body.put("id", userId);

        Call<List<User>> callerForAdd = proxy.addMonitoredByUsers(userManager.getId(), body);
        ProxyBuilder.callProxy(currentContext, callerForAdd, returnedList -> resetParentList(returnedList));
    }
    private static void resetParentList(List<User> list) {
        userManager.setMonitoredByUsers(list);
    }


    //for delete child

    public static void deleteMoniterUser(Long userId){
        Call<Void> callerForDelete = proxy.deleteMonitorUser(userManager.getId(),userId);
        ProxyBuilder.callProxy(currentContext, callerForDelete,returnedNothing -> deleteChild(returnedNothing));
    }
    private static void deleteChild(Void Nothing){
        Call<List<User>> callerForReset =proxy.getMonitorUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext, callerForReset,returnedList->resetChildList(returnedList));
    }

    // for delete parent
    public static void deleteMonitoredByUser(Long userId){
        Log.i("test ",""+userManager.getId());
        Call<Void> callerForDelete = proxy.deleteMonitoredByUser(userManager.getId(),userId);
        ProxyBuilder.callProxy(currentContext,callerForDelete,returnedNothing ->deleteParent(returnedNothing));

    }
    private static void  deleteParent(Void Nothing){
        Call<List<User>> callerForReset =proxy.getMonitoredByUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext,callerForReset,returnedList->resetParentList(returnedList));
    }
}
