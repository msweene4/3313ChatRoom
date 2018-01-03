
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Vector;



//runnable thing is for implementing the server thread
public class Server implements Runnable 
{
	public static Vector<Thread> threads= new Vector<Thread>();
	private static boolean running = true;
	
	private static boolean responsive = true;
	//holding a list of our clients
	public static Vector<BufferedWriter> anon=new Vector<BufferedWriter>();
	public static ArrayList index = new ArrayList();
	
	//socket we will use for our server thread(the main will pass a port/socket thing)
	Socket csock;
	
	private static Socket sock;
	private static ServerSocket ssocket;
	
	//our server thread will need a socket allocated to it
	public Server(Socket _csock)
	{
		try
		{
			csock=_csock;
			//indicating that we are now connected
			System.out.println("Client is now connected ");
		}
		catch(Exception e)//if something goes wrong
		{
			//prints the throwable and the backtrace
			e.printStackTrace();
		}
	}
	
	//the run method implemented for the runnable thing
	public void run()
	{
		try
		{
			//https://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html
			//https://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html
			
			//the reader and writer, returning a inputstream and outputstream for the socket as bufferedwriters and bufferedreaders
	        BufferedReader reader = new BufferedReader(new InputStreamReader(csock.getInputStream()));
	        BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(csock.getOutputStream()));
	        
	        //adding a bufferedwriter representing a client to the list of clients so we can send responses to everyone.
	        anon.add(writer);
	        String msg = null;
            //infinite loop for receiving stuff
	        while(responsive)
            {
                //get the msg that a thread sends out
                if (responsive)
                {
                    msg = reader.readLine().trim();
                }
	        	
                if(msg.equals("updownleftright"))
                {
                    //server indicating that it got a message, testing getting from users
                    System.out.println("Received : "+msg);
                    running = false;
                    responsive = false;
                    ssocket.close();
                    break;
                }
                if (msg.equals("**DieInsect**"))
                {
                    //server indicating that it got a message, testing getting from users
                    System.out.println("Received : "+msg);
                    break;
                }
                //server indicating that it got a message, testing getting from users
                System.out.println("Received : "+msg);
           
                //loop for the amount of clients we have
                for (int i=0;i<anon.size();i++)//we now use our list of clients 
                {
                    try
                    {
                        //every client in the vector is a bufferedwriter, so we cycle through our vector, get each bufferedwritter, and we send the new message we got.
                        BufferedWriter bw = (BufferedWriter)anon.get(i);
                        bw.write(msg);
                        bw.write("\r\n");//ending
                        //flush() writes the content of the buffer to the destination and makes the buffer empty for further data to store but it does not closes the stream permanently
                        bw.flush();
                    }
                    catch(IOException e)
                    {
                        
                    	//throw e;
                        System.out.println("Here66");
//                    	e.printStackTrace();
                    }
                 }
            }
	        try 
	        {
	        reader.close();
	        }
	        catch(IOException e)
	        {
	        	System.out.println("failed to close reader");
	        	throw e;
	        }
	        try 
	        {
		    writer.close();
                }
                catch(IOException e)
                {
                    System.out.println("failed to close writer");
                    throw e;
                }
//	        try
//	        {
//	        	csock.close();
//	        }
//	        catch(IOException e)
//	        {
//	        	throw e;
//	        }
	        Thread.currentThread().interrupt();
		}
		catch(Exception e)
		{
			//prints the throwable and the backtrace
			e.printStackTrace();	
		}
	}
	
	//https://stackoverflow.com/questions/5915156/how-can-i-kill-a-thread-without-using-stop
	//https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#interrupt%28%29
//	public void killed()
//	{
//		try
//		{
//			System.out.println("Terminating");
//			Thread.sleep(20);
//		}
//		catch(InterruptedException ex) 
//		{
//			Thread.currentThread().interrupt();
//    	}
//	}
	//what runs when server.java is run, our main class
	 public static void main(String argv[]) throws Exception
	 {
		 //here we set our portnumb
		 int portnumb = 2012;
		 //indicator of the server.java being up
         System.out.println("Server up" );
         
         //establishing the socket with the portnumber we want
         ssocket = new ServerSocket(portnumb);         
         System.out.println("listening on port" +portnumb);
         
         while(running)
         {
        	 try
        	 {
        	 System.out.println("accepting into our server thread");
        	 
        	 //listening for a connection then accepting it and storing it as sock
        	 sock = ssocket.accept();
        	 Server server=new Server(sock);
        	 Thread serverThread=new Thread(server);
        	 
        	 threads.add(serverThread);
        	 //runnnnn
        	 
        	 serverThread.start();
        	 }
        	 catch(SocketException e)
        	 {
        		 System.out.println("offline");
        	 }
         }
         ssocket.close();
	 }

}
