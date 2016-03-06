package subcover.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

















import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import subcover.repository.Bus;
import subcover.repository.BusRepository;


@Controller
@RequestMapping(value="/bus")
public class BusCtrl
{
	@Autowired
	private BusRepository busRepo;
	
	@RequestMapping(value="/", produces="application/json",method=RequestMethod.POST)
	public @ResponseBody Bus insert(@RequestBody Bus bus,HttpServletResponse response)
	{
		return busRepo.save(bus);
	}
	
	@RequestMapping(value="/", produces="application/json",method=RequestMethod.GET)
	public @ResponseBody Bus get(@RequestParam String id,HttpServletResponse response)
	{
		return busRepo.findOne(id);
	}
		
}
