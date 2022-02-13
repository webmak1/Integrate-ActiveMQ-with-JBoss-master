package src;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Session Bean implementation class MySimplePerodicalTask
 */
@Stateless
public class MySimplePeriodicalTask {

    /**
     * Default constructor. 
     */
    public MySimplePeriodicalTask() {
    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    	System.out.println("+++ MySimplePeriodicalTask() called +++");
    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    }
    
    @Schedule(hour="*", minute="*", second="*/10")
    public void someServerTask(){
    	
    	try {
			InitialContext context = new InitialContext();
			
			System.out.println("+++ STEP 1 +++");
			
			QueueConnectionFactory connFactory = (QueueConnectionFactory)(context.lookup("java:/activemq/QueueConnectionFactory"));
		
			System.out.println("+++ STEP 2 +++");
			
			System.out.println(connFactory.getClass().toString());
			
			System.out.println("+++ STEP 3 +++");
			
			Connection conn = connFactory.createConnection();
			
			System.out.println("+++ STEP 4 +++");
			
			conn.start();
			
			System.out.println("+++ STEP 5 +++");
			 
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			System.out.println("+++ STEP 6 +++");
			
			Queue queue = session.createQueue(this.getClass().toString() + ".QUEUE");
			
			System.out.println("+++ STEP 7 +++");
			
			MessageProducer producer = session.createProducer(queue);
			
			System.out.println("+++ STEP 8 +++");
			
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			System.out.println("+++ STEP 9 +++");
			
			String content = (new Date()).toString();
			
			System.out.println("+++ STEP 10 +++");
			
			TextMessage msg = session.createTextMessage(content);
			
			System.out.println("+++ STEP 11 +++");
			
			producer.send(msg);
			
			System.out.println("+++ STEP 12 +++");
			
			producer.close();
			session.close();
			conn.close();
			

	    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
	    	System.out.println("+++ Task has been performed +++");
	    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
			
    	} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

}