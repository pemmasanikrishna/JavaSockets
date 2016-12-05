import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ChatServer {

	static List<Socket> clientSockets;
	static List<String> loginNames;

	public ChatServer() throws IOException {
		ServerSocket server = new ServerSocket(5217);
		clientSockets = new ArrayList<>();
		loginNames = new ArrayList<>();

		while (true) {
			Socket client = server.accept();
			new AcceptClient(client);
		}
	}

	class AcceptClient extends Thread {
		Socket clientSocket;
		DataInputStream din;
		DataOutputStream dout;

		public AcceptClient(Socket client) throws IOException {

			clientSocket = client;
			din = new DataInputStream(clientSocket.getInputStream());
			dout = new DataOutputStream(clientSocket.getOutputStream());

			String loginName = din.readUTF();
			loginNames.add(loginName);
			clientSockets.add(clientSocket);

			start();

		}

		public void run() {

			while (true) {

				try {

					String msgFromClient = din.readUTF();
					StringTokenizer st = new StringTokenizer(msgFromClient);

					String loginName = st.nextToken();
					String msgType = st.nextToken();
					String msg = "";

					int rem = -1;

					while (st.hasMoreTokens())
						msg += " " + st.nextToken();

					for (int i = 0; i < clientSockets.size(); i++) {
						Socket socket = clientSockets.get(i);

						DataOutputStream cOut = new DataOutputStream(socket.getOutputStream());

						if (msgType.equals("LOGIN"))
							cOut.writeUTF(loginName + " has logged in.");
						else if (msgType.equals("DATA"))
							cOut.writeUTF(loginName + ": " + msg);
						else {
							if (loginName.equals(loginNames.get(i)))
								rem = i;
							cOut.writeUTF(loginName + " has logged out.");
						}
					}

					loginNames.remove(rem);
					clientSockets.remove(rem);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new ChatServer();
	}
}
