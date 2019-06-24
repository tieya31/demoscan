package com.example.camerademo.activity;

import com.example.camerademo.R;
import com.example.camerademo.decode.DecodeThread;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CaptureResultActivity extends BaseActivity {

	private ImageView mResultImage;
	private TextView mResultText;

	//post request
	RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
	
	@ViewInject(R.id.tv_codeType)
	private TextView tv_codeType; //条码类型
	@ViewInject(R.id.tv_dataType)
	private TextView tv_dataType; //数据类型
	@ViewInject(R.id.tv_time)
	private TextView tv_time; //扫描的时间
	@ViewInject(R.id.tv_metadata) 
	private TextView tv_metadata; //元数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		ViewUtils.inject(this);

		Bundle extras = getIntent().getExtras();

		mResultImage = (ImageView) findViewById(R.id.result_image);
		mResultText = (TextView) findViewById(R.id.result_text);

		if (null != extras) {

			String result = extras.getString("result");
			mResultText.setText(result);
			
			String codeType = extras.getString("codeType");
			tv_codeType.setText(getString(R.string.text_code_type) + codeType);
			
			String dataType = extras.getString("dataType");
			tv_dataType.setText(getString(R.string.text_data_type) + dataType);
			
			String metadata = extras.getString("metadata");
			tv_metadata.setText(getString(R.string.text_metadata) + metadata);

			Bitmap barcode = null;
			byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
			if (compressedBitmap != null) {
				barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
				barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
			}
			mResultImage.setImageBitmap(barcode);
			
			tv_time.setText(getString(R.string.text_time) + getDateFormat("yyyy-MM-dd HH:mm", System.currentTimeMillis()));
		}
	}
	
	@OnClick({R.id.tv_back})
	private void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back: //返回
			finish();
			break;

		default:
			break;
		}
	}

	//post request

	// check network connection

	public boolean checkNetworkConnection() {
		ConnectivityManager connMgr = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		boolean isConnected = false;
		if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
			// show "Connected" & type of network "WIFI or MOBILE"
		//	tvIsConnected.setText("Connected "+networkInfo.getTypeName());
			// change background color to red
		//	tvIsConnected.setBackgroundColor(0xFF7CCC26);


		} else {
			// show "Not Connected"
		//	tvIsConnected.setText("Not Connected");
			// change background color to green
		//	tvIsConnected.setBackgroundColor(0xFFFF0000);
		}

		return isConnected;
	}

	private String httpPost(String myUrl) throws IOException, JSONException {
		String result = "";

		URL url = new URL(myUrl);

		// 1. create HttpURLConnection
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

		// 2. build JSON object
		JSONObject jsonObject = buidJsonObject();

		// 3. add JSON content to POST request body
		setPostRequestContent(conn, jsonObject);

		// 4. make POST request to the given URL
		conn.connect();

		// 5. return response message
		return conn.getResponseMessage()+"";

	}

	private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			// params comes from the execute() call: params[0] is the url.
			try {
				try {
					return httpPost(urls[0]);
				} catch (JSONException e) {
					e.printStackTrace();
					return "Error!";
				}
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}
		// onPostExecute displays the results of the AsyncTask.
	/*	@Override
		protected void onPostExecute(String result) {
			tvResult.setText(result);
		}*/
	}

	public void send(View view) {
		Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
		// perform HTTP POST request
		if(checkNetworkConnection())
			new HTTPAsyncTask().execute("localhost//8085/product/productInfo");
		else
			Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();

	}

	private JSONObject buidJsonObject() throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("id", tv_codeType.getText().toString());
		jsonObject.accumulate("name",  tv_dataType.getText().toString());
		jsonObject.accumulate("quantity",  tv_metadata.getText().toString());
		//jsonObject.accumulate("color",  tv_metadata.getText().toString());

		return jsonObject;
	}



	private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

		OutputStream os = conn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		writer.write(jsonObject.toString());
		Log.i(MainActivity.class.toString(), jsonObject.toString());
		writer.flush();
		writer.close();
		os.close();
	}





}
