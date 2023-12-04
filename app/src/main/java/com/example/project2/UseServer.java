package com.example.project2;



import android.content.Context;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseServer {
    private static UseServer instance;
    RequestQueue queue;
    String session;
    public UseServer(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static synchronized UseServer getInstance(Context context) {
        if (instance == null) {
            instance = new UseServer(context.getApplicationContext());
        }
        return instance;
    }

    void updateJobPosting(HandleResponse callback, int id, String jobTitle, String description, String salary, boolean active) {
        String url = "http://162.243.172.218:5000/updatePosting";
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

    void updateProfile(HandleResponse callback, int id, String address, String aboutMe, String name, String phone,String email, String workHistory, String education) {
        String url = "http://162.243.172.218:5000/updateProfile";
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


    void updateEmployer(HandleResponse callback, int employer_id, String company_name, String location) {
        String url = "http://162.243.172.218:5000/updateEmployer";
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

    void insertEmployer(HandleResponse callback, int employer_id, String company_name, String location) {
        String url = "http://162.243.172.218:5000/insertEmployerInfo";
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

    void insertApplication(HandleResponse callback, int jobPostingID, int applicant_id, String message) {
        String url = "http://162.243.172.218:5000/insertApp";
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

    // status should be (approved/pending/denied)
    void updateApplication(HandleResponse callback, int jobPostingID, int applicant_id, String message, String status) {
        String url = "http://162.243.172.218:5000/updateApplication";
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

    void changePassword(HandleResponse callback, String username, String password) {
        String url = "http://162.243.172.218:5000/changePassword";
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
    void logout(HandleResponse callback){
        String url = "http://162.243.172.218:5000/logout";
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
    // to get the profile of whoever is signed in, pass in empty quotes for username, if you want to get a specific user, pass in their username
    void myAccount(HandleResponse callback, String username){
        String url = "http://162.243.172.218:5000/myAccount";
        if(!username.equals(""))
            url = "http://162.243.172.218:5000/myAccount?username=" + username;
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
    // active = all    : gets all jobs (active and inactive)
    // active = ""     : all active jobs
    // active = false  : all inactive jobs
    void allJobs(HandleResponse callback, String active){
        String url = "http://162.243.172.218:5000/allJobs";
        if(!active.equals(""))
            url = "http://162.243.172.218:5000/allJobs?active=" + active;
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

    void oneJob(HandleResponse callback, int id){
        String url = "http://162.243.172.218:5000/oneJob?id=" + id;
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


    // change jobs "active" status
    void activeJob(HandleResponse callback, int id, boolean active){
        String url = "http://162.243.172.218:5000/activeJob?id=" + id + "&active=" + active;
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

    void newRating(HandleResponse callback, int reviewer_id, int employer_id, int rating){
        String url = "http://162.243.172.218:5000/insertRating?employer_id=" + employer_id + "&reviewer_id=" + reviewer_id + "&rating=" + reviewer_id;
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

    void companyReviews(HandleResponse callback, int employer_id) {
        String url = "http://162.243.172.218:5000/companyReviews?employer_id=" + employer_id;
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

    void getUserApplications(HandleResponse callback, int applicant_id) {
        String url = "http://162.243.172.218:5000/getUserApp?id=" + applicant_id;
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
        String url = "http://162.243.172.218:5000/getUserApp?id=" + job_posting_id;
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

    void getJobsForCategory(HandleResponse callback, String type) {
        String url = "http://162.243.172.218:5000/jobCategory?type=" + type.replace(" ", "%20");
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

    void jobByEmployer(HandleResponse callback, int employer_id) {
        String url = "http://162.243.172.218:5000/jobByEmployer?employer_id=" + employer_id;
        StringRequest jobByEmployer = new StringRequest(Request.Method.GET, url,
                response -> callback.response(response),
                error -> callback.response(error.getMessage())
        );
        queue.add(jobByEmployer);
    }
    void verifyLogin(HandleResponse callback) {
        String url = "http://162.243.172.218:5000/verifyLogin" ;
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


    void createAccount(HandleResponse callback, String role, String username, String password){
        String url = "http://162.243.172.218:5000/createUser";
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

    void login(HandleResponse callback, String username, String password){
        String url = "http://162.243.172.218:5000/login";
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
                                Log.d("test", "getBody: " + username);
                                Log.d("test", "getBody: " + password);
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

    void createPosting(HandleResponse callback, int employer_id, String job_title, String description, String salary, String type){
        String url = "http://162.243.172.218:5000/createPosting";
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
                session = cookies.split(";")[0];
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(createAccountRequest);
    }




}
