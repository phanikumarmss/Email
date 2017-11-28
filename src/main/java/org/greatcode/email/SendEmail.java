package org.greatcode.email;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
/**
 * Hello world!
 *
 */
public class SendEmail 
{
	private static String subject="";
	private static String username="";
	private static String password="";
    public static void main( String[] args )
    {
    	try
    	{
	    	byte[] encoded = Files.readAllBytes(Paths.get(args[1]));
	    	//Copy template content to String
			String template=new String(encoded, StandardCharsets.UTF_8);
			System.out.println(template);
			File file = new File(args[0]);
	    	FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			String rename_values=properties.getProperty("RENAME_VARIABLE");
			username=properties.getProperty("USER_NAME");
			password=properties.getProperty("PASSWORD");
			subject=properties.getProperty("SUBJECT");
			int i=rename_values.split(",").length;
			String values[]=rename_values.split(",");
			String recipients[]=properties.getProperty("RECIPIENTS").split(",");
			String cc[]=properties.getProperty("RECIPIENTS_CC").split(",");
			System.out.println(i);
			String data[]=properties.getProperty(values[0]).split(",");
			int j=0;
			j=properties.getProperty(values[0]).split(",").length;
			System.out.println(j);
			String common_properties[]=properties.getProperty("COMMON_PROPERTIES").split(",");
			for(int r=0;r<recipients.length;r++)
			{
				for(int k=0;k<i;k++)
				{
					for(int l=0;l<j;l++)
					{
						template=template.replaceAll("\\$"+values[k],data[l]);
						System.out.println("Entered"+"$"+values[k]+""+data[l]);
					}
				}
				for(int temp=0;temp<common_properties.length;temp++)
					template=template.replaceAll("\\$"+common_properties[temp],properties.getProperty(common_properties[temp]));
				System.out.println(template);
		    	sendMail(recipients[r],template,cc);
			}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    public static String sendMail(String To,String mail,String[] bcc)
    {
    	try 
		{
    		System.out.println(username+"hello");
    		System.out.println(password);
    		System.out.println(subject);
    		//final String from = "phanikumardadi.kumar@gmail.com";
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            //pro.put("mail.smtp.ssl.trust", "smtp.gmail.com");
			Authenticator authenticator = new Authenticator() 
			{
                @Override
                protected PasswordAuthentication getPasswordAuthentication() 
				{
                    return new PasswordAuthentication(username, password);
                }
            };
            Session session = Session.getDefaultInstance(properties, authenticator);
            Message message = new MimeMessage(session);
            System.out.println("username"+username);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(To));
            for(int i=0;i<bcc.length;i++)
            {
            	message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(bcc[i]));
            }
            message.setSubject(subject);
            //m.setText(message);
            message.setContent(mail, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("sucessfully send");
            return "success";
        }
		catch (Exception ex) 
		{
            ex.printStackTrace();
        }
        return "error";    
    }
}