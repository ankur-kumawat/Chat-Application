import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatApp
{
	public String user,msg,sender,txt,key,prvt="$3#^#3$";
	public Socket socket;
	public PrintWriter out;
	public BufferedReader in;
	public JFrame w;
	public JTextArea ta;
	public JTextField tf;
	public JButton snd;
	public DefaultListModel listmodel;
	public JList list;
	public HashMap<String,PrivateChat> pvt;
	public PrivateChat pc;
	public ChatApp(String u)throws Exception
	{
		user=u;
		socket=new Socket(InetAddress.getByName("Ankur-PC"),8081);
		try
		{
			System.out.println("Socket created"+socket);
			out=new PrintWriter(socket.getOutputStream(),true);
			out.println("$1#^#1$"+user);
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			w=new JFrame();
			
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(w);
			
			w.setTitle("Global Chat :- "+user);
			ta=new JTextArea();
			ta.setEditable(false);
			w.add(new JScrollPane(ta));
			tf=new JTextField(20);
			snd=new JButton("Send");
			pvt=new HashMap<String,PrivateChat>();
			JPanel jp=new JPanel(new FlowLayout());
			jp.add(tf);
			jp.add(snd);
			listmodel=new DefaultListModel();
			list=new JList(listmodel);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listmodel.addElement(user);
			w.add(jp,BorderLayout.SOUTH);
			w.add(list,BorderLayout.EAST);
			w.setBounds(new Rectangle(200,200,400,500));
			w.setVisible(true);
			snd.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					txt=tf.getText();
					if(!txt.equals(""))
					{
						out.println(txt+"$2#^#2$"+user+"$2#^#2$");
						ta.append("\n"+user+" : "+txt);
						tf.setText("");
					}
				}
			});
			new Thread(new Runnable()
			{
				public void run()
				{
					while(true)
					{
						try
						{
							msg=in.readLine();
							if(msg==null)
								return;
							if(msg.indexOf("$4#^#4$~~~$4#^#4$~~~$4#^#4$")!=-1)
							{
								String brk=msg.substring("$4#^#4$~~~$4#^#4$~~~$4#^#4$".length(),msg.length());
								if(brk.equals(user))
								{
									if(!pvt.isEmpty())
									{
										System.out.println("Hey good bye");
										Set set=pvt.keySet();
										Iterator iter=set.iterator();
										while(iter.hasNext())
										{
											String key=(String)iter.next();
											PrivateChat pc=pvt.get(key);
											pc.setMSG("$5#^#5$");
										}
									}
								
									w.setVisible(false);
									break;
								}
								if(!pvt.isEmpty())
								{
									listmodel.removeElement(brk);
									pc=pvt.get(brk);
									pc.setMSG("$4#^#4$~~~$4#^#4$~~~$4#^#4$");
									pvt.remove(brk);
									ta.append(brk+" Logged Off.\n");
								}
							}
							
							if(msg.indexOf("$2#^#2$")!=-1)
							{
								sender=msg.substring(msg.indexOf("$2#^#2$")+("$2#^#2$".length()),msg.lastIndexOf("$2#^#2$"));
								if(!sender.equals(user))
								{
									msg=msg.substring(0,msg.indexOf("$2#^#2$"));
									ta.append("\n"+sender+" : "+msg);
								}
								System.out.println(msg);
								//ta.append(sender+" : "+msg);
							}
							if(msg.indexOf(prvt)!=-1)
							{
								sender=msg.substring(msg.lastIndexOf(prvt)+prvt.length(),msg.length());
									if(pvt.containsKey(sender))
									{
										pc=pvt.get(sender);
										pc.showWindow();
									}
									else
									{
										pc=new PrivateChat(sender,user,socket);
										pvt.put(sender,pc);
									}
									pc.setMSG(msg);
							}
							if(msg.indexOf("$1#^#1$")!=-1&&msg.indexOf(user)==-1)
							{
								listmodel.addElement(msg.substring("$1#^#1$".length(),msg.length()));
								ta.append(msg.substring("$1#^#1$".length(),msg.length())+" Logged in.\n");
							}
						}
						catch(IOException ioe)
						{
							ioe.printStackTrace();
						}
					}
				}
			}).start();
			list.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent me)
				{
					if(me.getClickCount()==2)
					{
						key=(String)listmodel.getElementAt(list.getSelectedIndex());
						if(!key.equals(user))
						{
							if(!pvt.containsKey(key))
							{
								pc=new PrivateChat(key,user,socket);
								pvt.put(key,pc);
							}
							else
							{
								pc=pvt.get(key);
								pc.showWindow();
							}
						}
					}
				}
			});
			w.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			w.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent we)
				{
					System.out.println("Window Event");
					Window w=we.getWindow();
					w.dispose();
					out.println("$4#^#4$~~~$4#^#4$~~~$4#^#4$"+user);
					System.exit(0);
				}
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		try
		{
			new ChatApp(args[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}