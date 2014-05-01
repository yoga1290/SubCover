import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * 
 * @author yoga1290
 * @see https://developers.facebook.com/docs/authentication/server-side/
 * @see https://developers.facebook.com/docs/authentication/permissions/
 * @see https://developers.facebook.com/docs/opengraph/using-app-tokens/
 */
public class facebook 
{
		@Deprecated
		public static String getAccessToken(String CLIENT_ID,String CLIENT_SECRET,String code)
		{
			String res="";
			try
			{
//				https://graph.facebook.com/oauth/access_token?client_id=AppID&redirect_uri=http://yoga1290.appspot.com/oauth/facebook/callback/&client_secret=SECRET&code=
				URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id="+CLIENT_ID
								+"&redirect_uri=http://yoga1290.appspot.com/oauth/facebook/callback/"
								+"&client_secret="+CLIENT_SECRET
								+"&code="+code);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            InputStream in=connection.getInputStream();
	            byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
	            in.close();
	            
	            //Extract the access token
	            return res.substring(res.indexOf("access_token=")+13,res.indexOf("&expires="));
			}catch(Exception e){
				res=res+" <br>Error: "+e.getMessage();
			}
			return res;
		}
		public static String getAccessToken(String CLIENT_ID,String CLIENT_SECRET,String redirect_uri,String code)
		{
			String res="";
			try
			{
				URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id="+CLIENT_ID
								+"&redirect_uri="+redirect_uri
								+"&client_secret="+CLIENT_SECRET
								+"&code="+code);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            InputStream in=connection.getInputStream();
	            byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
	            in.close();
	            
	            //Extract the access token
	            return res.substring(res.indexOf("access_token=")+13,res.indexOf("&expires="));
			}catch(Exception e){
				res=res+" <br>Error: "+e.getMessage();
			}
			return res;
		}
		public static String getAppAccessToken(String CLIENT_ID,String CLIENT_SECRET)
		{
			String res="";
			try
			{
				URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id="+CLIENT_ID
								+"&client_secret="+CLIENT_SECRET
								+"&grant_type=client_credentials");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            InputStream in=connection.getInputStream();
	            byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
	            in.close();
	            
	            //Extract the access token
	            return res.substring(13);
			}catch(Exception e){
				res=res+" <br>Error: "+e.getMessage();
			}
			return res;
		}
		public static String getUser(String access_token)
		{
			String res="";
			try
			{
				URL url = new URL("https://graph.facebook.com/me?access_token="+access_token);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        InputStream in=connection.getInputStream();
		        byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
		        in.close();
			}catch(Exception e){
				res=e.getMessage();
			}
			return res;
		}
		public static String extractJSON(String feild,String txt)
		{
			try{
				String tmp=txt.substring(txt.indexOf(feild)+feild.length()+3,txt.length());
				return txt.substring(txt.indexOf(feild)+feild.length()+3,tmp.indexOf("\"")+txt.indexOf(feild)+feild.length()+3);
			}catch(Exception e){
				return e.getMessage();
			}
		}
		
		/**
		 * 
		 * @param access_token access token with a friends_about_me permission 
		 * @return
		 */
		public static String[] getFriendsID(String access_token)
		{
			String txt="";
			String fin[];
			try
			{
				URL url = new URL("https://graph.facebook.com/me/friends?access_token="+access_token);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        InputStream in=connection.getInputStream();
		        byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		txt+=new String(buff,0,ch);
		        in.close();
		        String tmp[]=txt.split("},");
		        fin=new String[tmp.length];
		        for(int i=0;i<tmp.length;i++)
		        		fin[i]=extractJSON("id", tmp[i]);
			}catch(Exception e){
				fin=new String[]{e.getMessage()};
			}
			return fin;
		}
		
		
		/**
		 * 
		 * @param access_token access token with a publish_stream permission
		 * @param userID
		 * @param message
		 * @return
		 */
		public static String post(String access_token,String userID,String message)
		{
			String res="";
			try
			{	
				URL url = new URL("https://graph.facebook.com/"+userID+"/feed?access_token="+access_token);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoOutput(true);
		        connection.setRequestMethod("POST");
		        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		        connection.setRequestProperty("Accept-Charset", "UTF-8");
		        
		        OutputStream ops=connection.getOutputStream();
		        ops.write("message=".getBytes());
		        ops.write(message.getBytes("UTF-8"));
		        ops.close();
		        InputStream in=connection.getInputStream();
		        byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
			}catch(Exception e)
			{
				res=e.getMessage();
			}
			return res;
		}
		public static String postLink(String access_token,String userID,String link,String message,String picture,String name,String caption,String description )
		{
			String res="";
			try
			{	
				URL url = new URL("https://graph.facebook.com/"+userID+"/links?access_token="+access_token);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoOutput(true);
		        connection.setRequestMethod("POST");
		
		        
		        connection.setRequestProperty("message", URLEncoder.encode(message, "UTF-8"));
		        connection.setRequestProperty("link", link);
		        connection.setRequestProperty("picture", picture);
		        connection.setRequestProperty("name", URLEncoder.encode(name, "UTF-8"));
		        connection.setRequestProperty("caption", URLEncoder.encode(caption, "UTF-8"));
		        connection.setRequestProperty("description", URLEncoder.encode(description, "UTF-8"));
		        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		        writer.write("link="+link+"\n");
		        writer.close();
		        InputStream in=connection.getInputStream();
		        byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
			}catch(Exception e)
			{
				res=e.getMessage();
			}
			return res;
		}
		
		
		public static String postNotification(String app_access_token,String user_id,String template,String href)
		{
			String res="facebook.postNotification";
			try
			{	
				URL url = new URL("https://graph.facebook.com/"+user_id+"/notifications?access_token="+app_access_token+"&template="+  URLEncoder.encode(template, "UTF-8")+"&href="+href);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoOutput(true);
		        connection.setRequestProperty("Accept-Charset", "UTF-8");
		        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		        connection.setRequestMethod("POST");
		        
		        InputStream in=connection.getInputStream();
		        byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
			}catch(Exception e)
			{
				res=res+"<br>"+e.getMessage();
			}
			return res;
		}
		public static String Action(String acess_token,String namespace,String ActionType,String objectType,String objectURL)
		{
			String res="facebook.Action";
			try
			{	
				URL url = new URL("https://graph.facebook.com/me/"+namespace+":"+ActionType+"?"+objectType+"="+objectURL+"&access_token="+acess_token);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoOutput(true);
		        connection.setRequestMethod("POST");
		        
		        InputStream in=connection.getInputStream();
		        byte buff[]=new byte[in.available()];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
			}catch(Exception e)
			{
				res=res+"<br>"+e.getMessage();
			}
			return res;
		}
		public static String refreshAccessToken(String APP_ID,String APP_SECRET,String old_access_token)
		{
			String res="";
			try
			{	
				URL url = new URL("https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id="+APP_ID+"&client_secret="+APP_SECRET+"&fb_exchange_token="+old_access_token);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoOutput(true);
		        connection.setRequestMethod("POST");
		        
		        InputStream in=connection.getInputStream();
		        byte buff[]=new byte[500];
	            int ch;
	            while((ch=in.read(buff))!=-1)
	            		res+=new String(buff,0,ch);
				return res.substring(res.indexOf("access_token=")+13,res.indexOf("&"));
			}catch(Exception e)
			{
				return res+"<br>"+e.getMessage();
			}
		}
	}
