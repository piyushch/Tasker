package com.pc.tasker;

import javax.mail.internet.*;
import javax.mail.*;

import java.io.File;
import java.util.*;
import org.apache.commons.mail.*;

public class SendEmail
{
    public static void sendEmail(String to,final String mailSubject, final String mailBodyText,String [] attach,String [] attachdel) throws Exception {
        final String mailFrom = Util.getDBProperty("mail.from");
        
       // final String mailTo = to = Util.getProperty("mail.to");
        final String from = mailFrom;
        System.out.println("Mail from " + mailFrom);
        System.out.println("Mail to " + to);
        final String host = "localhost";
        final Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        final Session session = Session.getDefaultInstance(properties);
        try {
            final MimeMessage message = new MimeMessage(session);
            message.setFrom((Address)new InternetAddress(from));
            final String[] toarr = to.split(";");
            for (int i = 0; i < toarr.length; ++i) {
                message.addRecipient(Message.RecipientType.TO, (Address)new InternetAddress(toarr[i]));
            }
            message.setSubject(mailSubject);
            message.setContent((Object)mailBodyText, "text/html");
            
            ////////////////////////////
            
         // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mailBodyText, "text/html");
     
            // creates multi-part
            Multipart multipart = new MimeMultipart();
            
            multipart.addBodyPart(messageBodyPart);
            
            if (attach!=null){
            for (int i = 0; i < attach.length; i++) 
            	{
            		MimeBodyPart attachPart = new MimeBodyPart();           
            		attachPart.attachFile(attach[i].trim());
            		System.out.println("Attaching .. "+attach[i]);
            		multipart.addBodyPart(attachPart);
            	}
                
            }    
            message.setContent(multipart);
            
            ////////////////////////////
            
            
            Transport.send((Message)message);
            
            System.out.println("Sent message successfully....");
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }finally {
        	if (attachdel!=null){
                for (int i = 0; i < attachdel.length; i++) 
                	{
                		File file=new File(attachdel[i])   ;      
                		System.out.println("Deleting .. "+file.getAbsolutePath());
                		file.delete();
                	}
                    
                }    
		}
    }
}
