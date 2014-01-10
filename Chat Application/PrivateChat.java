import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;


/* Name of Sender and Receiver both are to be sent in order for application to work  */
public class PrivateChat implements ActionListener
{
	public String receiver,sender;
	public Socket socket;
	private String msg;
	private JFrame f;
	private JButton snd;
	private JTextArea ta;
	private JTextField tf;
	private PrintWriter out;
	PrivateChat(String r,String s,Socket sk)
	{
		receiver=r;
		sender=s;
		socket=sk;
		f=new JFrame();
		f.setTitle(receiver);
		ta=new JTextArea();
		ta.setEditable(false);
		f.add(new JScrollPane(ta));
		JPanel p=new JPanel();
		tf=new JTextField(20);
		snd=new JButton("Send");
		snd.addActionListener(this);
		p.add(tf);
		p.add(snd);
		f.add(p,BorderLayout.SOUTH);
		f.setBounds(300,300,350,300);
		f.setVisible(true);
		try
		{
			out=new PrintWriter(socket.getOutputStream(),true);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	public void actionPerformed(ActionEvent ae)
	{
		msg=tf.getText();
		ta.append(sender+" : "+msg+'\n');
		out.println("$3#^#3$"+receiver+"$3#^#3$"+msg+"$3#^#3$"+sender);
		tf.setText("");
	}
	public void setMSG(String msg)
	{
		if(msg.equals("$4#^#4$~~~$4#^#4$~~~$4#^#4$"))
		{
			ta.append(receiver+" has been logged off..");
			snd.setEnabled(false);
		}
		if(msg.equals("$5#^#5$"))
		{
			f.setVisible(false);
			f.dispose();
		}
		if(msg.indexOf("$3#^#3$")!=-1)
		{
			String txt=msg.replaceAll(sender,"");
			String x="$3#^#3$";
			txt=txt.substring((x+x).length(),txt.lastIndexOf(x));
			ta.append(receiver+" : "+txt+'\n');
		}
	}
	public void showWindow()
	{
		f.setVisible(true);
	}
}