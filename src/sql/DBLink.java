package sql;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;



	public class DBLink extends SendEmail {
		//z is the total number of entries.
		static int z = 0,x = 0,y = 0,p = 0;
		//arrays to store the database entries.
		static int id1[] = new int [100];
		static String from1[] = new String[100];
		static String pass1[] = new String[100];
		static String to1[] = new String[100];
		static String subject1[] = new String[100];
		static String body1[] = new String[200];
		
		static int id2[] = new int [100];
		static String from2[] = new String[100];
		static String pass2[] = new String[100];
		static String to2[] = new String[100];
		static String subject2[] = new String[100];
		static String body2[] = new String[200];
		
		
	     public static void main(String args[]){
		   try
		    {
		    	//Code to connect to database where "mysql://127.0.0.1/email" is path to database
		    	//and root is user name and password is password to database.
		        Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1/email","root","password");
		        Statement stment = conn.createStatement();
		        
		        
		        String sql = "Select * from emailqueue";
		        ResultSet rs = stment.executeQuery(sql);
		        
		        //Getting the results of query one by one and calculating the number of entries z.
		        while (rs.next()) {
		            z++;
		        }
		        //partitioning the database in two parts and storing in the arrays.
		        //Partition 1 : from 1 to z/2.
		        String sql1 = "Select * from emailqueue where id between 1 and " + (z/2);
		        ResultSet rs1 = stment.executeQuery(sql1);
		        int i = 0;
		        //Getting the results of query one by one
		        while (rs1.next()) {
		            //Retrieve by column name
		            id1[i] = rs1.getInt("id");
		            from1[i] = rs1.getString("from_email_address");
		            pass1[i] = rs1.getString("password");
		            to1[i] = rs1.getString("to_email_address");
		            subject1[i] = rs1.getString("subject");
		            body1[i] = rs1.getString("body");
		            i++;
		    }
		        int j = 0;
		        //Partition 2 : from z/2 to z.
		        String sql2 = "Select * from emailqueue where id >" + z/2;
		        ResultSet rs2 = stment.executeQuery(sql2);
		        while (rs2.next()) {
		            //Retrieve by column name
		            id2[j] = rs2.getInt("id");
		            from2[j] = rs2.getString("from_email_address");
		            pass2[j] = rs2.getString("password");
		            to2[j] = rs2.getString("to_email_address");
		            subject2[j] = rs2.getString("subject");
		            body2[j] = rs2.getString("body");
		            j++;
		            if(id2[j] >= z)
		            	break;
		    }
		        
		        
		    }
		    catch(Exception err)
		    {
		        System.out.println(err);
		    }
		      //Creation of thread which will send the emails from id 1 to z/2.
		   	  Thread t1 = new Thread(){
		      public void run(){
		    	  try{
		    		  if(x<=z/2){
		    		  sendonetohalf(x);
		    		  x++;
		    		  Thread.sleep(5);
		    		  }
		    	  }catch(Exception e){
		    		  System.err.println(e);
		    	  }
		      }
		    	  
		      };
		    //Creation of thread which will send the emails from id z/2 to z.
		    //p is the remaining elements after z/2.
		        p = z-(z/2);
		      Thread t2 = new Thread(){
		      public void run(){
			    	 
			    	  try{
			    		  if(y<p){
			    		  sendremaining(y);
			    		  y++;
			    		  Thread.sleep(10);
			    		  }
			    	  }catch(Exception e){
			    		  System.err.println(e);
			    	  }
			      }
	   };
	   	t1.start();
	   	t2.start();
	   }
	   //function for sending the emails with id 1 to z/2. 
	   public static void sendonetohalf(int id) throws AddressException, MessagingException
	    {
		 //generateandsendemail() is contained in SendEmail.java file.
		    int i = 0;
		    //array of threads of size z/2.
		    Thread[] t = new Thread[z/2];
		    while(i != z/2)
		    {
		    	      t[i] = new Thread(){
				      public void run(){
				    	  try{
				    		  SendEmail.generateAndSendEmail(id1[id],from1[id],pass1[id],to1[id],subject1[id],body1[id]);
				    		   Thread.sleep(1000);
				    	  }catch(Exception e){
				    		  System.err.println(e);
				    	  }
				      }
				    	  
				      };
				      i++;
		    }
	    }
	   
	   //function for sending the emails with id z/2 to z.
		public static void sendremaining(int id) throws AddressException, MessagingException
		{
			//generateandsendemail() is contained in SendEmail.java file.
			int j = 0;
			//array of threads of size z/2.
			Thread[] t = new Thread[z/2];
		    while(j != p)
		    {
		    	      t[j] = new Thread(){
				      public void run(){
				    	  try{
				    		  
				    			  SendEmail.generateAndSendEmail(id2[id],from2[id],pass2[id],to2[id],subject2[id],body2[id]);
				    		      
				    			  Thread.sleep(1000);
				    	  }catch(Exception e){
				    		  System.err.println(e);
				    	  }
				      }
				    	  
				      };
				      j++;
		    }
			
		}
	   
	}