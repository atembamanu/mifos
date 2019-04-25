package org.mifos.mobilebanking.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.SSLCertificateSocketFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;
import org.mifos.mobilebanking.MifosSelfServiceApp;
import org.mifos.mobilebanking.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class CreateClientActivity extends AppCompatActivity {

    String url="https://techsavanna.net:8181/mifos/index.php";
    public static final String MyPREFERENCES = "basicinfo" ;

    AppCompatButton btnClient;


    EditText etdatebirth;
    SharedPreferences sharedpreferences;

    EditText etphonenumber;

    EditText etlastname;

    EditText etmiddlename;

    EditText etfirstname;
    public static final String firstnameacc = "firstnameaccKey";
    public static final String lastnameacc = "lastnameaccKey";
    public static final String middlenameacc = "middlenameaccKey";
    public static final String phonenumberacc="phonenumberaccKey";
    public static final String accountIdacc="accountIdaccKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client);

        etdatebirth= findViewById(R.id.et_date_birth);
        etphonenumber=findViewById(R.id.et_phone_number);
        etlastname=findViewById(R.id.et_last_name);
        etmiddlename=findViewById(R.id.et_middle_name);
        etfirstname=findViewById(R.id.et_first_name);

        btnClient=findViewById(R.id.btn_client);
        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String firstname = etfirstname.getText().toString();
                String middlename = etmiddlename.getText().toString();
                String idno = etdatebirth.getText().toString();
                 String lastname = etlastname.getText().toString();
                 String phonenumber = etphonenumber.getText().toString();

//createClient(firstname,middlename,lastname,phonenumber,idno);

                sendPost(firstname,middlename,lastname,phonenumber,idno);

            }
        });
    }
    private void createClient(String firstname,String middlename,String lastname,String phonenumber,String datebirth){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("firstname", firstname);
        params.put("mobileNo", phonenumber);
        params.put("lastname", lastname);
        params.put("externalId", datebirth);
       // params.put("active","true");
        //params.put("phonenumber", phonenumber);
       /// params.put("activationDate", datebirth);
      //  params.put("username", "admin");
       // params.put("password", "password");
      //  params.put("tenantIdentifier", "default");
        JSONObject nn=new JSONObject(params);

        System.out.println("Jsonn"+nn);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url,  new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(final JSONObject response) {
                        // display response
                        Log.d("ooo Response", response.toString());
//


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // Log.e(TAG, "On ErrorResponse: " + error.getMessage());
                        VolleyLog.e("Error: ", error.getMessage());
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            // Print Error!

                            System.out.println("Error:"+jsonError);
                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

// add it to the RequestQueue
        MifosSelfServiceApp.getInstance().addToRequestQueue(getRequest);

    }

    public void sendPost(final String firstname, final String middlename, final String lastname, final String phonenumber, final String datebirth) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL urls = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) urls.openConnection();

                    if (conn instanceof HttpsURLConnection) {
                        HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                        httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                        httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
                    }
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("firstname", firstname);
                    jsonParam.put("mobileNo", phonenumber);
                    jsonParam.put("lastname", lastname);
                    jsonParam.put("externalId", datebirth);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Output from Server .... \n");
                   // System.out.println("jsonnn" + br.readLine());
                    String accountId=br.readLine();

                    if(accountId !=null ){
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        System.out.println("Account No"+accountId);
                        editor.putString(firstnameacc, firstname);
                        editor.putString(lastnameacc, lastname);
                       // editor.putString(middlenameacc, lastname);

                        editor.putString(phonenumberacc,phonenumber);
                        editor.putString(accountIdacc, accountId);
                        editor.commit();


                        Intent intent=new Intent(CreateClientActivity.this,RegistrationActivity.class);
                        startActivity(intent);

                    }
                    Log.i("MSG new", conn.getInputStream().toString());


                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
