package com.ignatieff.crawler;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import com.ignatieff.converter.GIFConverter;
import com.github.jreddit.submissions.Submission;
import com.github.jreddit.submissions.Submissions;
import com.github.jreddit.user.User;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;

public class Crawl {
	
	RestClient rc;
	User user;
	Submissions sub;

	public Crawl(String from, String to, String username, String password){
		
		System.out.println("\nASCIIConvert Bot loaded up!");
		System.out.println("Scraping from: /r/"+from);
		System.out.println("Uploading to:  /r/"+to);
		System.out.println("Username: " + username);
		System.out.println("Password: " + password+"\n");
		
		
		try {
			
			rc = new HttpRestClient();
			sub = new Submissions(rc);
			user = new User(rc, username, password);
			user.connect();
			
			 for (Submission submission : sub.getSubmissions(from,Submissions.Popularity.HOT, Submissions.Page.FRONTPAGE, user)) {
		          
				 try{
		          
		           String title = "[ASCII] "+submission.getTitle() + " [original by /u/"+submission.getAuthor()+"].";
				   URL url = new URL(submission.link);
				   
		           if(!url.getPath().contains(".gif"))continue;
		           
		           System.out.println("Downloading .gif from " +  submission.link);
		           
		           ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		           FileOutputStream fos = new FileOutputStream(url.getPath().hashCode()+".gif");
		           fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		           fos.close();
		           System.out.println("	File has been downloaded!");
		           System.out.println("Converting .gif...");
		           GIFConverter.convertGIF(url.getPath().hashCode()+".gif", "ascii_"+url.getPath().hashCode()+".gif", 2);
		           System.out.println("	Sucessfully converted a gif!");
		           System.out.println("Uploading .gif to imgur ...");
		           String imgurPath = doInBackground("ascii_"+url.getPath().hashCode()+".gif");
		           if(imgurPath==null){
		        	   System.out.println("	Unable to upload image to imgur...");
		        	   continue;
		           }
		           System.out.println("	Succesfully uploaded image to imgur! (url = " + imgurPath+").");
		           System.out.println("Uploading to reddit...");
		           if(user.submitLink(title, imgurPath, to)){
		        	   System.out.println("	Uploaded to reddit!\n");
		           }else{
		        	   System.out.println("	Unable to upload to reddit...");
		           }
		           
				 }catch(ArrayIndexOutOfBoundsException e){
					 System.out.println("	Something went wrong. Skipping...");
					 continue;
				 }
		     }
			
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected String doInBackground(String filename) {
        final String upload_to = "https://api.imgur.com/3/upload.json";
        final String API_key = "5e5553602c3229e";
        final String TAG = "Awais";

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(upload_to);
        httpPost.setHeader("Authorization", "Client-ID "+API_key);
        httpPost.setHeader("nsfw", "true");
        try {
            final MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("image", new FileBody(new File(filename)));
            entity.addPart("key", new StringBody(API_key));

            httpPost.setEntity(entity);

            final HttpResponse response = httpClient.execute(httpPost,
                    localContext);

            final String response_string = EntityUtils.toString(response
                    .getEntity());

            return getURL(response_string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static String getURL(String s){
		String l = s.substring(s.indexOf("link\":\"")+7);
		String x = l.substring(0,l.indexOf("\""));
		return x.replace("\\/", "/");
	}
}
