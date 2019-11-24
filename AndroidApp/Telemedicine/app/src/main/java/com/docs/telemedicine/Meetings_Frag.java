package com.docs.telemedicine;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Meetings_Frag extends Fragment {


    public Meetings_Frag() {
        // Required empty public constructor
    }

    private TextInputEditText otp_field ;
    private TextInputEditText key_field;
    private Button otp_button;
    private Button key_button;
    OkHttpClient client;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         client = new OkHttpClient();
        View view =  inflater.inflate(R.layout.fragment_meetings_, container, false);
    otp_field = (TextInputEditText)view.findViewById(R.id.otp_enter);
    key_field = (TextInputEditText)view.findViewById(R.id.meeting_key_enter);
    otp_button  = (Button)view.findViewById(R.id.otp_button);
    key_button = (Button)view.findViewById(R.id.meeting_key_button);

    otp_button.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        //make a request to verify the otp and start a new activity
String otp = otp_field.getText().toString().trim();
if(otp.matches("")){
    Toast.makeText(getContext(),"Enter OTP first",Toast.LENGTH_LONG).show();
    return;
}
            Request request = new Request.Builder()
                    .url("http://5fc0c10b.ngrok.io/verify_otp/"+otp_field.getText().toString().trim())
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_LONG).show();
                        otp_field.setText("");
                        }
                    });
                    e.printStackTrace();
                }
                @Override public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                        final   String resp = responseBody.string();

                        if (resp.matches("wrong_otp")) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "otp is wrong", Toast.LENGTH_LONG).show();

                                    otp_field.setText("");
                                }});
                            return;
                        } else if (resp.matches("network failure")) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "Connection problem", Toast.LENGTH_SHORT).show();
                                    otp_field.setText("");
                                }});
                            return;
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    otp_field.setText("");
                                    //make a new activity here and run the video calling functionality
                                    //resp is the seesion key , token key
                                    Intent intent = new Intent(getContext(), VideoCallingActivity.class);

                                    JSONObject  jsonObject = null;
                                    try {
                                        jsonObject  = new JSONObject(resp);

                                    if(jsonObject==null){
                                        try{
                                            jsonObject=new JSONObject("{SESSION_ID:2344,TOKEN:5402,API_KEY:2342}");
                                        }catch(JSONException ee){
                                            ee.getStackTrace();
                                        }
                                    }
                                        intent.putExtra("SESSION_ID",jsonObject.getString("SESSION_ID"));

                                        intent.putExtra("API_KEY",jsonObject.getString("API_KEY"));
                                    intent.putExtra("TOKEN",jsonObject.getString("TOKEN"));


                                    startActivity(intent);
                                    }catch(JSONException ee){
                                        ee.getStackTrace();
                                    }
                                }});
                            return;
                        }

                    }
                }
            });
        }
    });
    key_button.setOnClickListener(new View.OnClickListener(){
  @Override
        public void onClick(View v) {
      //make a request to verify the meeting_key and get the otp
      String key_entered = key_field.getText().toString().trim();
      if (key_entered.matches("")) {
          Toast.makeText(getContext(), "Enter Meeting Key", Toast.LENGTH_SHORT).show();
          return;
      }
      Request request = new Request.Builder()
              .url("http://5fc0c10b.ngrok.io/verify_meeting_key/"+key_field.getText().toString().trim())
              .build();

      client.newCall(request).enqueue(new Callback() {
          @Override public void onFailure(Call call, IOException e) {

              getActivity().runOnUiThread(new Runnable() {
                  public void run() {
                      Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_LONG).show();
                  }
              });
              e.printStackTrace();
          }

          @Override public void onResponse(Call call, Response response) throws IOException {
              try (ResponseBody responseBody = response.body()) {
                  if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


               final   String resp = responseBody.string();

                  if (resp.matches("wrong_key")) {
                getActivity().runOnUiThread(new Runnable() {
                          public void run() {
                              Toast.makeText(getContext(), "Key is wrong", Toast.LENGTH_LONG).show();

                              key_field.setText("");
                          }});
                      return;
                  } else if (resp.matches("network failure")) {
                      getActivity().runOnUiThread(new Runnable() {
                          public void run() {
                              Toast.makeText(getContext(), "Connection problem", Toast.LENGTH_SHORT).show();
                              key_field.setText("");
                          }});
                      return;
                  } else {
                      getActivity().runOnUiThread(new Runnable() {
                          public void run() {
                              Toast.makeText(getContext(), "Key matched you will get the otp" , Toast.LENGTH_LONG).show();
                              key_field.setText("");
                          }});
                      return;
                  }

              }
          }
      });

  }

    });

        return view;
    }





}
