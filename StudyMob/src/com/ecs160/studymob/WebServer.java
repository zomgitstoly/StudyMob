package com.ecs160.studymob;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.json.JSONObject;

import android.util.Log;

public class WebServer {
	public static final String server = "http://isrobin.com/ecs160/action.php";
	//public static final String server = "http://localhost/studymob/action.php";
	String result;

	public WebServer() {

	}

	// blocking request
	public String sendHttpRequest(JSONObject json) {
		String result = "";
		String data = json.toString();
		Log.i(this.toString(), "Starting request.." + data);
		
		try {
			URL url = new URL(server);
			URLConnection connection = url.openConnection();

			// We need to make sure we specify that we want to provide input and
			// get output from this connection. We also want to disable caching,
			// so that we get the most up-to-date result. And, we need to
			// specify the correct content type for our data.
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			// Send the POST data
			DataOutputStream dataOut = new DataOutputStream(
					connection.getOutputStream());
			dataOut.writeBytes(data);
			dataOut.flush();
			dataOut.close();

			// get the response from the server and store it in result
			DataInputStream dataIn = new DataInputStream(
					connection.getInputStream());
			String inputLine;
			while ((inputLine = dataIn.readLine()) != null) {
				result += inputLine;
			}
			dataIn.close();

			Log.i(this.toString(), result);
			//result = new String(result.substring(0, result.indexOf('<')));
		} catch (IOException e) {
			/*
			 * In case of an error, we're going to return a null String. This
			 * can be changed to a specific error message format if the client
			 * wants to do some error handling. For our simple app, we're just
			 * going to use the null to communicate a general error in
			 * retrieving the data.
			 */
			e.printStackTrace();
			result = null;
		}

		Log.i(this.toString(), result);
		return result;
	}

	//non blocking request
	public String sendAsyncHttpRequest(String data) {
		ResultSetter setter = new ResultSetter() {
			public void setResult(String data) {
				result = data;
			}
		};
		RequestHandler thread = new RequestHandler(data);
		thread.setResultSetter(setter);
		return result;
	}

	public class RequestHandler extends Thread {
		String data;
		ResultSetter setter;

		public RequestHandler(String data) {this.data = data;}

		public void setResultSetter(ResultSetter setter) {
			this.setter = setter;
		}

		public void run() {
			setter.setResult(sendHttpRequest(data));
		}

		public String sendHttpRequest(String data) {
			String result = "";
			Log.i(this.toString(), "Starting request.." + data);
			try {
				URL url = new URL(server);
				URLConnection connection = url.openConnection();

				// We need to make sure we specify that we want to provide input and
				// get output from this connection. We also want to disable caching,
				// so that we get the most up-to-date result. And, we need to
				// specify the correct content type for our data.
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");

				// Send the POST data
				DataOutputStream dataOut = new DataOutputStream(
						connection.getOutputStream());
				dataOut.writeBytes(data);
				dataOut.flush();
				dataOut.close();

				// get the response from the server and store it in result
				DataInputStream dataIn = new DataInputStream(
						connection.getInputStream());
				String inputLine;
				while ((inputLine = dataIn.readLine()) != null) {
					result += inputLine;
				}
				dataIn.close();

				Log.i(this.toString(), result);
				result = new String(result.substring(0, result.indexOf('<')));
			} catch (IOException e) {
				/*
				 * In case of an error, we're going to return a null String. This
				 * can be changed to a specific error message format if the client
				 * wants to do some error handling. For our simple app, we're just
				 * going to use the null to communicate a general error in
				 * retrieving the data.
				 */
				e.printStackTrace();
				result = null;
			}

			Log.i(this.toString(), result);
			return result;
		}
	}

	public interface ResultSetter {
		public void setResult(String result);
	}

	public String getLocalIpAddress() {
		try {			
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					Log.i("WebServer.IPLookup", inetAddress.getHostAddress().toString());
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(this.toString(), ex.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
