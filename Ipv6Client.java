import java.io.*;
import java.net.*;

/*
 * simulates Ipv6 packet format
 */
public class Ipv6Client {

	public static void main(String[] args) throws Exception{
		try(Socket socket = new Socket("codebank.xyz",  38004)){
			for(int i = 0; i < 12; i++){
				byte[] data = new byte[(int)Math.pow(2, i+1)];
				byte[] packet = createPacket(data, socket);
				sendPacket(packet, socket, data.length);
			}
		}
	}
	
	/*
	 * creates the packet contents
	 */
	private static byte[] createPacket(byte[] data, Socket socket){
		byte[] packet = new byte[40 + data.length];
		InetAddress ad = socket.getInetAddress();
		byte[] dstAddress = ad.getAddress();
		
		packet[0] = 96;
		packet[1] = 0;
		packet[2] = 0;
		packet[3] = 0;
		packet[4] = (byte) ((data.length & 0xFF00) >> 8);
		packet[5] = (byte) (data.length & 0x00FF);
		packet[6] = 17;
		packet[7] = 20;
		//extending ipV4 source address to ipV6
		for(int i = 8; i < 18; i++){
			packet[i] = 0;
		}
		packet[18] = (byte)0xFF;
		packet[19] = (byte)0xFF;
		packet[20] = 127;
		packet[21] = 0;
		packet[22] = 0;
		packet[23] = 1;
		
		//extending ipV4 destination address to ipV6
		for(int i = 24; i < 34; i++){
			packet[i] = 0;
		}
		packet[34] = (byte) 0xFF;
		packet[35] = (byte) 0xFF;
		for(int i = 0; i < 4; i++){
			packet[36 + i] = dstAddress[i];
		}
		
		return packet;
	}
	
	/*
	 * sends the packets
	 */
	private static void sendPacket(byte[] packet, Socket socket, int size) throws Exception{
		OutputStream out = socket.getOutputStream();
		InputStream in = socket.getInputStream();
		out.write(packet);
		byte[] rec = new byte[4];
		in.read(rec);
		String magic = "";
		for(byte e: rec){
			magic += Integer.toHexString(Byte.toUnsignedInt(e));
		}
		System.out.println("Data size: " + size);
		System.out.println(magic.toUpperCase());
		
	}
	
	
}
