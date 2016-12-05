import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable{

	Socket socket;
	JTextArea jta;
	
	JButton send,logout;
	JTextField jtf;
	
	Thread thread;
	
	DataInputStream din;
	DataOutputStream dout;
	
	String loginName;
	
	public ChatClient(String login) throws UnknownHostException, IOException {
		
		super(login);
		loginName = login;
		
		jta = new JTextArea(18,50);
		jtf = new JTextField(50);
		
		send = new JButton("Send");
		logout = new JButton("Logout");
		
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					dout.writeUTF(loginName + " "+ "DATA "+ jtf.getText().toString());
					jtf.setText("");
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		
		logout.addActionListener(e ->
		{
			try {
				dout.writeUTF(loginName + " " + "LOGOUT");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		);
		
		socket = new Socket("localhost", 5217);
		
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		
		dout.writeUTF(loginName);
		dout.writeUTF(loginName + " " + "LOGIN");
		
		thread = new Thread(this);
		thread.start();
		setup();
	}
	
	private void setup() {
		
		setSize(600, 400);
		
		JPanel panel = new JPanel();
		
		panel.add(new JScrollPane(jta));
		panel.add(jtf);
		panel.add(send);
		panel.add(logout);
		
		add(panel);
		
		setVisible(true);
		
	}

	@Override
	public void run() {
		
		while(true)
		{
			try {
				
				jta.append("\n"+ din.readUTF());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
	}

}
