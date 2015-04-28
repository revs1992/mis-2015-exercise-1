package mmbuw.com.brokenproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import mmbuw.com.brokenproject.R;

public class AnotherBrokenActivity extends Activity {

    Context context = this;
    EditText url;
    TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_broken);

        url = (EditText)findViewById(R.id.url);
        textViewResult = (TextView)findViewById(R.id.result);


        Intent intent = getIntent();
        String message = intent.getStringExtra(BrokenActivity.EXTRA_MESSAGE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.another_broken, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchHTML(View view) throws IOException {

        Task task = new Task();
        task.execute(url.getText().toString());
    }

    private class Task extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String responseAsString = "";
            try {
                HttpGet httpGet = new HttpGet(params[0]);
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 500;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 500;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);
                HttpResponse response = client.execute(httpGet);

                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    response.getEntity().writeTo(outStream);
                    responseAsString = outStream.toString();
                    System.out.println("Response string: " + responseAsString);
                } else {
                    response.getEntity().getContent().close();
                    throw new IOException(status.getReasonPhrase());
                }
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "Can not resolve your url", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
            } catch (IllegalStateException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid url", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
            } catch (IllegalArgumentException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid url", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
            }
            return responseAsString;
        }

        protected void onPostExecute(String result) {
            textViewResult.setText(result);
        }
    }
}
