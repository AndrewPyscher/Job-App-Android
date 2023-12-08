package com.example.project2;



import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseServer {
    private static UseServer instance;
    RequestQueue queue;
    String session;
    SharedPreferences sp;

    // constructor thats used for the first instance of this class
    public UseServer(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    // constructor that takes in session key
    public UseServer(Context context, String session){
        queue = Volley.newRequestQueue(context);
        this.session = session;
    }

    public static synchronized UseServer getInstance(Context context) {
        if (instance == null) {
            instance = new UseServer(context.getApplicationContext());
        }
        return instance;
    }

    // all methods below here will use server to do CRUD operations (not really any deletions)

    // route that updates a job thats been posted
    void updateJobPosting(HandleResponse callback, int id, String jobTitle, String description, String salary, boolean active) {
        String url = "https://dominitechnicus.com/updatePosting";
        StringRequest updateJob = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("id", id);
                    jsonParams.put("job_title", jobTitle);
                    jsonParams.put("description", description);
                    jsonParams.put("salary", salary);
                    jsonParams.put("active", active);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(updateJob);
    }

    // route that updates a users profile
    void updateProfile(HandleResponse callback, int id, String address, String aboutMe, String name, String phone,String email, String workHistory, String education) {
        String url = "https://dominitechnicus.com/updateProfile";
        StringRequest updateProfile = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("id", id);
                    jsonParams.put("address", address);
                    jsonParams.put("about_me", aboutMe);
                    jsonParams.put("name", name);
                    jsonParams.put("phone", phone);
                    jsonParams.put("email", email);
                    jsonParams.put("workHistory", workHistory);
                    jsonParams.put("education", education);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(updateProfile);
    }


    // route that updates an employers info
    void updateEmployer(HandleResponse callback, int employer_id, String company_name, String location) {
        String url = "https://dominitechnicus.com/updateEmployer";
        StringRequest updateEmployer = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("employer_user_id", employer_id);
                    jsonParams.put("company_name", company_name);
                    jsonParams.put("location", location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(updateEmployer);
    }

    // route thats adds an employers info, used on account creation
    void insertEmployer(HandleResponse callback, int employer_id, String company_name, String location) {
        String url = "https://dominitechnicus.com/insertEmployerInfo";
        StringRequest insertEmployer = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("employer_user_id", employer_id);
                    jsonParams.put("company_name", company_name);
                    jsonParams.put("location", location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(insertEmployer);
    }

    // route to add an application from an applicant to an employer
    void insertApplication(HandleResponse callback, int jobPostingID, int applicant_id, String message) {
        String url = "https://dominitechnicus.com/insertApp";
        StringRequest insertApplication = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("jp_id", jobPostingID);
                    jsonParams.put("applicant_id", applicant_id);
                    jsonParams.put("message", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(insertApplication);
    }

    // route that updates status of application
    // status should be (approved/pending/denied)
    void updateApplication(HandleResponse callback, int jobPostingID, int applicant_id, String message, String status) {
        String url = "https://dominitechnicus.com/updateApplication";
        StringRequest updateApplication = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("jp_id", jobPostingID);
                    jsonParams.put("applicant_id", applicant_id);
                    jsonParams.put("message", message);
                    jsonParams.put("status", status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(updateApplication);
    }

    // route that changes a password
    void changePassword(HandleResponse callback, String username, String password) {
        String url = "https://dominitechnicus.com/changePassword";
        StringRequest changePassword = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username", username);
                    jsonParams.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(changePassword);
    }
    // route that logs a user out
    void logout(HandleResponse callback){
        String url = "https://dominitechnicus.com/logout";
        StringRequest changePassword = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(changePassword);
    }
    // route that gets a users account info (applicant)
    // to get the profile of whoever is signed in, pass in empty quotes for username, if you want to get a specific user, pass in their username
    void myAccount(HandleResponse callback, String username){
        String url = "https://dominitechnicus.com/myAccount";
        if(!username.equals(""))
            url = "https://dominitechnicus.com/myAccount?username=" + username;
        StringRequest myAccount = new StringRequest(Request.Method.GET, url,

                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(myAccount);
    }
    // route that gets all jobs posted
    // active = all    : gets all jobs (active and inactive)
    // active = ""     : all active jobs
    // active = false  : all inactive jobs
    public void allJobs(HandleResponse callback, String active){
        String url = "https://dominitechnicus.com/allJobs";
        if(!active.equals(""))
            url = "https://dominitechnicus.com/allJobs?active=" + active;
        StringRequest myAccount = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(myAccount);
    }

    // route that gets back one job
    void oneJob(HandleResponse callback, int id){
        String url = "https://dominitechnicus.com/oneJob?id=" + id;
        StringRequest oneJob = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(oneJob);
    }

    // route that changes a jobs status
    // change jobs "active" status
    void activeJob(HandleResponse callback, int id, boolean active){
        String url = "https://dominitechnicus.com/activeJob?id=" + id + "&active=" + active;
        StringRequest oneJob = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(oneJob);
    }

    // route that gets a users role (applicant or employer)
    void getRole(HandleResponse callback, int id){
        String url = "https://dominitechnicus.com/getRole?id=" + id;
        StringRequest getRole = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(getRole);
    }

    // route that gets a companies name
    void getCompanyName(HandleResponse callback, int id){
        String url = "https://dominitechnicus.com/getCompanyName?id=" + id;
        StringRequest getCompanyName = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(getCompanyName);
    }

    // route that creates a new review for a company
    void newRating(HandleResponse callback, int reviewer_id, int employer_id, int rating){
        String url = "https://dominitechnicus.com/insertRating?employer_id=" + employer_id + "&reviewer_id=" + reviewer_id + "&rating=" + reviewer_id;
        StringRequest newRating = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(newRating);
    }

    // route that gets all reviews for a company
    void companyReviews(HandleResponse callback, int employer_id) {
        String url = "https://dominitechnicus.com/companyReviews?employer_id=" + employer_id;
        StringRequest companyReviews = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(companyReviews);
    }

    // route that gets all applications that an applicant has applied to
    void getUserApplications(HandleResponse callback, int applicant_id) {
        String url = "https://dominitechnicus.com/getUserApp?id=" + applicant_id;
        StringRequest companyReviews = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(companyReviews);
    }

    // get all the applications for a specific job posting
    void getEmployerApplications(HandleResponse callback, int job_posting_id) {
        String url = "https://dominitechnicus.com/getUserApp?id=" + job_posting_id;
        StringRequest companyReviews = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(companyReviews);
    }

    // route that gets all jobs by a specific category
    void getJobsForCategory(HandleResponse callback, String type) {
        String url = "https://dominitechnicus.com/jobCategory?type=" + type.replace(" ", "%20");
        StringRequest companyReviews = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(companyReviews);
    }

    // route that gets all the jobs posted by an employer
    void jobByEmployer(HandleResponse callback, int employer_id) {
        String url = "https://dominitechnicus.com/jobByEmployer?employer_id=" + employer_id;
        StringRequest jobByEmployer = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        );
        queue.add(jobByEmployer);
    }
    // route that verifies if a user is logged in
    void verifyLogin(HandleResponse callback) {
        String url = "https://dominitechnicus.com/verifyLogin" ;
        StringRequest verifyLogin = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(verifyLogin);
    }


    // route to create a new account
    void createAccount(HandleResponse callback, String role, String username, String password){
        String url = "https://dominitechnicus.com/createUser";
        StringRequest createAccountRequest = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("role", role);
                    jsonParams.put("username", username);
                    jsonParams.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(createAccountRequest);
    }

    // route to login
    // saves the session in the response
    void login(HandleResponse callback, String username, String password){
        String url = "https://dominitechnicus.com/login";
        StringRequest createAccountRequest = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response + "<><>"+session),
                error -> callback.response(String.valueOf(error.networkResponse))
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username", username.trim());
                    jsonParams.put("password", password.trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                String cookies = headers.get("Set-Cookie");
                session = cookies.split(";")[0];
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(createAccountRequest);
    }

    // route to create a new job posting
    void createPosting(HandleResponse callback, int employer_id, String job_title, String description, String salary, String type){
        String url = "https://dominitechnicus.com/createJob";
        StringRequest createAccountRequest = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("employer_id", employer_id);
                    jsonParams.put("job_title", job_title);
                    jsonParams.put("description", description);
                    jsonParams.put("salary", salary);
                    jsonParams.put("type",type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                String cookies = headers.get("Set-Cookie");
                Log.d("test", response.toString());
                session = cookies.split(";")[0];
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(createAccountRequest);
    }
    // route to insert user info, used when creating an account
    void insertUserInfo(HandleResponse callback, int id, String address, String about_me, String name, String phone, String workHistory, String education, String email){
        String url = "https://dominitechnicus.com/insertUserInfo";
        StringRequest createAccountRequest = new StringRequest(Request.Method.POST, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("id", id);
                    jsonParams.put("address", address);
                    jsonParams.put("about_me", about_me);
                    jsonParams.put("name", name);
                    jsonParams.put("phone", phone);
                    jsonParams.put("email", email);
                    jsonParams.put("workHistory", workHistory);
                    jsonParams.put("education", education);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Connection", "keep-alive");
                headers.put("Cookie", session);
                return headers;
            }
        };
        queue.add(createAccountRequest);
    }




}
