import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login {
	
	public static void main(String[] args) {
		
		JFrame login = new JFrame("Login");
		JPanel panel = new JPanel();
		JTextField loginName = new JTextField(20);
		JButton enter = new JButton("Login");
		
		panel.add(loginName);
		panel.add(enter);
		login.setSize(300, 100);
		login.add(panel);
		login.setVisible(true);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		enter.addActionListener(e -> 
		{
			try {
				new ChatClient(loginName.getText());
				login.setVisible(false);
				login.dispose();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

}
