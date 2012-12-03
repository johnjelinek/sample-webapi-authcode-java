package com.tradestation.samplejfxauthcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Auth Code Example
 * 
 */
public class App extends Application {
	public static void main(String[] args) { launch(args); }

	@Override public void start(final Stage primaryStage) {
		// create the scene
		primaryStage.setTitle("Auth Code Example");
		primaryStage.setScene(new Scene(new Browser(), 750, 500));
		primaryStage.show();
	}
}

class Browser extends Region {
	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();
	final String WebAPIBaseUrl = "https://api.tradestation.com/v2";
	final String APIKey = "your key goes here";
	final String APISecret = "your secret goes here";
	final String RedirectUri = "your redirect uri here";
	
	public Browser() {		
		// load the web page
		String url = WebAPIBaseUrl + "/authorize?client_id=" + APIKey + "&response_type=code&redirect_uri=" + RedirectUri;
		webEngine.load(url);
		
		// add the web view to the scene
		getChildren().add(browser);
		
		// Add OnChanged Event Listener
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State t1) {
				if (t1 == Worker.State.SUCCEEDED) {
					try {
						List<NameValuePair> queryStringParams = URLEncodedUtils.parse(new URI(webEngine.getLocation()), "UTF-8");
						for (Iterator<NameValuePair> i = queryStringParams.iterator(); i.hasNext();) {
							NameValuePair param = i.next();
							if (param.getName().equals("code")) {
								try {
									AccessToken token = GetTokenFromAuthCode(param.getValue());
									System.out.println(token.getAccess_token());
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (URISyntaxException e) {
						// failed to parse querystring
					}
				}
			}
		});
	}
	
	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		this.layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	private AccessToken GetTokenFromAuthCode(String authCode) throws IOException {
		String authServiceURI = WebAPIBaseUrl + "/security/authorize";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(authServiceURI);
		StringBuilder builder = new StringBuilder();
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
		parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
		parameters.add(new BasicNameValuePair("code", authCode));
		parameters.add(new BasicNameValuePair("client_id", APIKey));
		parameters.add(new BasicNameValuePair("redirect_uri", RedirectUri));
		parameters.add(new BasicNameValuePair("client_secret", APISecret));

		post.setEntity(new UrlEncodedFormEntity(parameters));
		HttpResponse response = client.execute(post);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String line = "";
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(builder.toString(), AccessToken.class);
	}
}