import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer
{
	public static final int PORT=8081;
	static ServerSocket serverSocket;
	static Socket socket;
	static LinkedList<Socket> list;
	static LinkedList<String> ulist;
	public static void main(String args[])
	{
		try
		{
			serverSocket=new ServerSocket(PORT);
			list=new LinkedList<Socket>();
			ulist=new LinkedList<String>();
			System.out.println("Server Started---");
			while(true)
			{
				try
				{
					socket=serverSocket.accept();
					ServerThread st=new ServerThread(socket,list,ulist);
					st.start();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(IOException e)
		{
			System.out.println("Outside Thread IOException");
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Closing Server.....");
			try
			{
				serverSocket.close();
			}
			catch(IOException se)
			{
				System.out.println("Problem Closing Server Socket.!");
				se.printStackTrace();
			}
			System.out.println("Server is Closed!");
		}
	}
}