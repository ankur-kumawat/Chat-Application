import java.io.*;
import java.util.*;
import java.net.*;

public class ServerThread extends Thread
{
	LinkedList<Socket> list;
	LinkedList<String> ulist;
	Socket socket;
	ServerThread(Socket s,LinkedList<Socket> l,LinkedList<String> ul)
	{
		socket=s;
		list=l;
		ulist=ul;
	}
	public void run()
	{
		try
		{
			System.out.println("Connection Accepted : "+socket);
			list.addLast(socket);
			
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter o=new PrintWriter(socket.getOutputStream(),true);
			String str=null;
			while(true)
			{
				str=in.readLine();
				System.out.println("Closer"+str);
				if(str.indexOf("$4#^#4$~~~$4#^#4$~~~$4#^#4$")!=-1)
				{
					for(int i=0;i<list.size();i++)
					{
						Socket soc=list.get(i);
						PrintWriter out=new PrintWriter(soc.getOutputStream(),true);
						out.println(str);
					}
					list.remove(socket);
					String u=str.substring("$4#^#4$~~~$4#^#4$~~~$4#^#4$".length(),str.length());
					ulist.remove(u);
					break;
				}
				if(str.indexOf("$1#^#1$")!=-1)
				{
					String a=str.substring("$1#^#1$".length(),str.length());
					ulist.addLast(a);
					String s;
					for(int i=0;i<ulist.size();i++)
					{
						s=ulist.get(i);
						if(!s.equals(a))
							o.println("$1#^#1$"+s);
					}
					for(int i=0;i<list.size();i++)
					{
						Socket soc=list.get(i);
						if(soc!=socket)
						{
							PrintWriter out=new PrintWriter(soc.getOutputStream(),true);
							System.out.println(soc);
							out.println(str);
						}
						
					}
				}
				if(str.indexOf("$3#^#3$")!=-1)
				{
					String x="$3#^#3$";
					String recevr=str.substring(x.length(),str.indexOf(x,1));
					int a=ulist.indexOf(recevr);
					System.out.println(recevr);
					Socket s=list.get(a);
					PrintWriter out=new PrintWriter(s.getOutputStream(),true);
					out.println(str);
				}
				if(str.indexOf("$2#^#2$")!=-1)
				{
					for(int i=0;i<list.size();i++)
					{
						Socket s=list.get(i);
						PrintWriter out=new PrintWriter(s.getOutputStream(),true);
						out.println(str);
					}
				}
				/*else
					for(int i=0;i<list.size();i++)
					{
						Socket s=list.get(i);
						if(s!=socket)
						{
							PrintWriter out=new PrintWriter(s.getOutputStream(),true);
							System.out.println(s);
							out.println(str);
						}
						
					}
				*/
				
			}
		}
		catch(IOException e)
		{
			System.out.println("Inside Thread IOException");
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Closing Client Connection");
			try
			{
				socket.close();
			}
			catch(IOException se)
			{
				System.out.println("Problem Closing Socket.!");
				se.printStackTrace();
			}
		}
	}
}