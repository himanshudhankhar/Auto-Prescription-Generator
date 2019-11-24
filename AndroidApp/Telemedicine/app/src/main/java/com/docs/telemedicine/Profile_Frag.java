package com.docs.telemedicine;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Frag extends Fragment {
    private TextInputEditText symptoms;
    private EditText disease;
    private CheckBox emergency;
    private Button sendButton;

    public Profile_Frag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_, container, false);
        symptoms = (TextInputEditText)view.findViewById(R.id.symptoms_input);
        disease = (EditText)view.findViewById(R.id.disease_input);
        emergency = (CheckBox)view.findViewById(R.id.chkIos);
        sendButton = (Button)view.findViewById(R.id.schedule_button);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(),"Check whether all fields are filled or not",Toast.LENGTH_LONG).show();
               if(symptoms.getText().toString().trim().matches("") && disease.getText().toString().trim().matches("")){
                   Toast.makeText(getContext(),"Symptoms and Disease field both cannot be empty!!",Toast.LENGTH_LONG).show();
               return;
               }else{
                   //make a request to the backened
                   JSONObject postData = new JSONObject();
                   try {
                   postData.put("symptoms",symptoms.getText().toString().trim());
                   postData.put("disease", disease.getText().toString().trim());
                   postData.put("emergency",emergency.isChecked());
                   postData.put("deviceID",  android.provider.Settings.Secure.getString(getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID) );
                   postData.put("mobileNumber","9079161380");
                       new SendDeviceDetails().execute("http://5fc0c10b.ngrok.io/schedule_meeting", postData.toString());

                 symptoms.setText("");
                 disease.setText("");
                 emergency.setChecked(false);
                 Toast.makeText(getContext(),"Made Request for Appointment",Toast.LENGTH_LONG).show();
                   } catch (JSONException e) {
                       Toast.makeText(getContext(), "JSON Exception", Toast.LENGTH_SHORT).show();
                       e.printStackTrace();
                       return;
                   }
               }

                }
            });





        return  view;
    }



}

class SendDeviceDetails extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String data = "";

        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("PostData=" + params[1]);
            wr.flush();
            wr.close();

            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
    }
}