import java.io.*;
import java.net.*;
import javax.servlet.http.*;

public class subcover extends HttpServlet
{
	private static String fbAppID="1383863391860833",fbAppSecret=“***”;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	{
		try
		{
			if(req.getRequestURI().equals("/subcover/direct"))
			{
				String uri=req.getParameter("url");
				try
				{	
					resp.setContentType("image/"+uri.substring(uri.lastIndexOf(".")+1));
					URL url = new URL(uri);
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        InputStream in=connection.getInputStream();
			        OutputStream out=resp.getOutputStream();
			        byte buff[]=new byte[200];
		            int o;
		            while((o=in.read(buff))!=-1)
		            		out.write(buff,0, o);
		            out.close();
				}catch(Exception e)
				{
					try{
						resp.getWriter().println(e.getMessage());
					}catch(Exception e2){}
				}
			}
			else if(req.getRequestURI().equals("/subcover/oauth/facebook/"))
			{
				/*
				 * https://www.facebook.com/dialog/oauth?client_id=1383863391860833&redirect_uri=http://yogash1290.appspot.com/subcover/oauth/facebook/&scope=user_photos,publish_stream
				 */
				String code=req.getParameter("code");
				String access_token=facebook.getAccessToken(fbAppID, fbAppSecret, "http://yogash1290.appspot.com/subcover/oauth/facebook/", code);
				//resp.getWriter().println("Access token="+access_token);
				resp.getWriter().println(
						readFromURL("http://yogash1290.appspot.com/subcover/index.html")
							.replace("access_token=\"\"",
										"access_token=\""+access_token+"\"")
						);
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	public static String readFromURL(String rURL)
	{
		String res="";
		try
		{	
			URL url = new URL(rURL);
	
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        InputStream in=connection.getInputStream();
	        byte buff[]=new byte[in.available()];
            int ch;
            while((ch=in.read(buff))!=-1)
            		res+=new String(buff,0,ch);
		}catch(Exception e)
		{
			return e.getMessage();
		}
		return res;		
	}
}
