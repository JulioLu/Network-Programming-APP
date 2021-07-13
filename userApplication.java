import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Networks2APP  {

	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		//menu();
		
		
		
		DatagramSocket s = new DatagramSocket();
		String packetInfo = "E7580";

		byte[] txbuffer = packetInfo.getBytes();
		int serverPort = 38002;
		byte[] hostIP = { (byte)xxx,(byte)xxx,(byte)xxx,(byte)xxx };
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length, hostAddress,serverPort);
		int clientPort = 48002;
		DatagramSocket r = new DatagramSocket(clientPort);
		r.setSoTimeout(8000);
		byte[] rxbuffer = new byte[132];
		DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		if(packetInfo.indexOf("E")>=0) 
			echo(rxbuffer,q, r,p,s);
		else if(packetInfo.indexOf("M")>=0) 
			image( rxbuffer,q,r,p,s,packetInfo);
		else if(packetInfo.indexOf("A")>=0 && packetInfo.indexOf("Q")<=0 ) 
			AudioDPCM(rxbuffer,q,r,p,s,packetInfo);
		else if(packetInfo.indexOf("A")>=0 && packetInfo.indexOf("Q")>=0 ) 
			AudioAQDPCM(rxbuffer,q,r, p, s,packetInfo);
		else if(packetInfo.indexOf("V")>=0)
			//for(;;)
				//vehicleUDP(rxbuffer,q, r,p,s, packetInfo);
		
		//echoSpamming(rxbuffer,q, r,p,s, packetInfo);
		//vehicleSpamming(rxbuffer,q, r,p,s, packetInfo, serverPort);
		//IthakiCopter(2);
		//vehicle( hostAddress);
		//ithakicopter(hostAddress);
		//vehicle(hostAddress);
		s.close();
		r.close();
	}
	
	
	
	static void vehicleSpamming(byte [] rxbuffer,DatagramPacket q,DatagramSocket r,DatagramPacket p,DatagramSocket s,String packetInfo,int serverPort) throws IOException {
		byte[] hostIP = { (byte),(byte),(byte),(byte) };
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		ArrayList<Float> Engine_run_time =new ArrayList<Float>();
		ArrayList<Float> Intake_air_temperature =new ArrayList<Float>();
		ArrayList<Float> Throttle_position =new ArrayList<Float>();
		ArrayList<Float> Engine_RPM =new ArrayList<Float>();
		ArrayList<Float> Vehicle_speed =new ArrayList<Float>();
		ArrayList<Float> Coolant_temperature =new ArrayList<Float>();
		
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis()<startTime+(4*1000*60)) {
			p =new DatagramPacket((packetInfo+"01 1F").getBytes(),(packetInfo+"01 1F").getBytes().length, hostAddress,serverPort);
			Engine_run_time.add(vehicleUDP(rxbuffer,q, r,p,s, packetInfo+"01 1F"));
			
			p =new DatagramPacket((packetInfo+"01 0F").getBytes(),(packetInfo+"01 0F").getBytes().length, hostAddress,serverPort);
			Intake_air_temperature.add(vehicleUDP(rxbuffer,q, r,p,s, packetInfo+"01 0F"));
			
			p =new DatagramPacket((packetInfo+"01 11").getBytes(),(packetInfo+"01 11").getBytes().length, hostAddress,serverPort);
			Throttle_position.add(vehicleUDP(rxbuffer,q, r,p,s, packetInfo+"01 11"));
			
			p =new DatagramPacket((packetInfo+"01 0C").getBytes(),(packetInfo+"01 0C").getBytes().length, hostAddress,serverPort);
			Engine_RPM.add(vehicleUDP(rxbuffer,q, r,p,s, packetInfo+"01 0C"));
			
			p =new DatagramPacket((packetInfo+"01 0D").getBytes(),(packetInfo+"01 0D").getBytes().length, hostAddress,serverPort);
			Vehicle_speed.add(vehicleUDP(rxbuffer,q, r,p,s, packetInfo+"01 0D"));
			
			p =new DatagramPacket((packetInfo+"01 05").getBytes(),(packetInfo+"01 05").getBytes().length, hostAddress,serverPort);
			Coolant_temperature.add(vehicleUDP(rxbuffer,q, r,p,s, packetInfo+"01 05"));
		}
		
		saveDataToFile(Engine_run_time, packetInfo+"01 1F");
		saveDataToFile(Intake_air_temperature, packetInfo+"01 0F");
		saveDataToFile(Throttle_position, packetInfo+"01 11");
		saveDataToFile(Engine_RPM, packetInfo+"01 0C");
		saveDataToFile(Vehicle_speed, packetInfo+"01 0D");
		saveDataToFile(Coolant_temperature, packetInfo+"01 05");
	}
	

	static void echoSpamming(byte [] rxbuffer,DatagramPacket q,DatagramSocket r,DatagramPacket p,DatagramSocket s, String packetInfo) throws IOException {
		ArrayList<Float> serverResponse =new ArrayList<Float>();
		ArrayList<Float> serverbps8000=new ArrayList<Float>();
		long startTime1 = System.currentTimeMillis();
		long t0=System.currentTimeMillis();
		while(System.currentTimeMillis()<startTime1+(4*1000*60)) {
			serverResponse.add((float) echo(rxbuffer, q, r, p, s));
			long t1=System.currentTimeMillis()-t0;
			serverbps8000.add((float) t1);	
			//long t1=System.currentTimeMillis()+(long)echo(rxbuffer, q, r, p, s) -t0;
		}
	 
	  	ArrayList<Float> bps = new ArrayList<Float>();
		int counter = 0;
		for(int i=8000; i<serverbps8000.get(serverbps8000.size()-1); i+=1000){
			counter = 0;
			for(int j=0; serverbps8000.get(j)<i; j++)
				if((i-serverbps8000.get(j))<8000)
					counter++;
			bps.add((float) (counter*256/8.0));
		}
		
				
		saveDataToFile(bps, "bps "+packetInfo);
		R1(serverResponse,packetInfo);
	}
	
	/*
	 	ArrayList<Float> serverResponse =new ArrayList<Float>();
		ArrayList<Float> serverTimep8000=new ArrayList<Float>();
		long startTime1 = System.currentTimeMillis();
		long t0=System.currentTimeMillis();
		while(System.currentTimeMillis()<startTime1+(0.5*1000*60)) {
			serverResponse.add((float) echo(rxbuffer, q, r, p, s));
			long t1=System.currentTimeMillis()-t0;
			serverTimep8000.add((float) t1);	
			//long t1=System.currentTimeMillis()+(long)echo(rxbuffer, q, r, p, s) -t0;
		}
	 
	  	ArrayList<Float> bps = new ArrayList<Float>();
		int counter = 0;
		for(int i=8000; i<serverTimep8000.get(serverTimep8000.size()-1); i+=1000){
			counter = 0;
			for(int j=0; serverTimep8000.get(j)<i; j++)
				if((i-serverTimep8000.get(j))<8000)
					counter++;
			bps.add((float) (counter*256/8.0));
		}
		
				
		saveDataToFile(bps, "bps");
		RTT(serverResponse,packetInfo);*/
	
	
	public static float echo(byte [] rxbuffer,DatagramPacket q,DatagramSocket r,DatagramPacket p,DatagramSocket s) throws IOException {
		//for (;;) {
		long time = 0;
		s.send(p);
			try {
				long startTime =  System.currentTimeMillis();
				r.receive(q);
				time =System.currentTimeMillis()-startTime;
				String message = new String(rxbuffer,0,q.getLength());
				System.out.println(message);
				System.out.println(time);
				}catch (Exception x) {
					System.out.println(x);
				}
			return ((float)time);
		//}
	}
	
	static void IthakiCopterTCP(InetAddress hostAddress) throws IOException {
		Socket socket= new Socket(hostAddress,38078);
		PrintWriter p1 =new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
		for(int i=0; i<100; i++) {
			p1.println("AUTO FLIGHTLEVEL=200 LMOTOR=120 RMOTOR=120 PILOT \r\n");
			System.out.println(in.readLine());
		}
		socket.close();
	}
	static void IthakiCopter(int minutes) throws SocketException {
		DatagramSocket r = new DatagramSocket(48078);
		r.setSoTimeout(4000);
		
		byte[] rxbuffer = new byte[113];
		DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis()<startTime+(minutes*1000*60)) {
			try {
				r.receive(q);
				String message = new String(rxbuffer,0,q.getLength());
				System.out.println(message);
				
			} catch (IOException e) { e.printStackTrace();}
		}
		r.close();
	}
	static void vehicleTCP(InetAddress hostAddress) throws IOException {
		
		for(int i =0; i<2; i++) {
			Socket socket = new Socket(hostAddress,29078);
			PrintWriter p1 =new PrintWriter(socket.getOutputStream(),true);
			BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
				p1.println("01 1F\r");
				
				System.out.println(in.readLine());
				for(int j=0; j<5; j++) {
					socket = new Socket(hostAddress,29078);
					p1 =new PrintWriter(socket.getOutputStream(),true);
					in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
					p1.println("01 1F\r");
					System.out.println(in.readLine());
				}
		}
		//socket.close();
		
	}
	
	public static float vehicleUDP(byte [] rxbuffer,DatagramPacket q,DatagramSocket r,DatagramPacket p,DatagramSocket s,String packetInfo) throws IOException {
		//for (;;) {
			s.send(p);
			if(packetInfo.indexOf("1F")>=0)
				try {
					r.receive(q);
					String message = new String(rxbuffer,0,q.getLength());
					System.out.println(message);
					BigInteger XX = new BigInteger(message.substring(6,8), 16);
					BigInteger YY = new BigInteger(message.substring(9,11), 16);
					int result = 256*XX.intValue()+YY.intValue();
					System.out.println("Engine run time: " + result + " sec");
					return result;
					
				} catch (IOException e) {e.printStackTrace();}
			else if(packetInfo.indexOf("0F")>=0) 
				try {
					r.receive(q);
					String message = new String(rxbuffer,0,q.getLength());
					System.out.println(message);
					BigInteger XX = new BigInteger(message.substring(6,8), 16);
					int result = XX.intValue()-40;
					System.out.println("Intake air temperature: " + result + " \B0C");
					return result;
					
				} catch (IOException e) {e.printStackTrace();}
			else if(packetInfo.indexOf("11")>=0) 
				try {
					r.receive(q);
					String message = new String(rxbuffer,0,q.getLength());
					System.out.println(message);
					BigInteger XX = new BigInteger(message.substring(6,8), 16);
					float result = (float) (100*XX.intValue()/255.0);
					System.out.println("Throttle position: " + result + " %");
					return result;
					
				} catch (IOException e) {e.printStackTrace();}
			else if(packetInfo.indexOf("0C")>=0) 
				try {
					r.receive(q);
					String message = new String(rxbuffer,0,q.getLength());
					System.out.println(message);
					BigInteger XX = new BigInteger(message.substring(6,8), 16);
					BigInteger YY = new BigInteger(message.substring(9,11), 16);
					float result = (float) ((XX.intValue()*256+ YY.intValue())/4.0);//((XX*256)+YY)/4
					System.out.println("Engine RPM: " + result + "RPM");
					return result;
				} catch (IOException e) {e.printStackTrace();}
			else if(packetInfo.indexOf("0D")>=0) 
				try {
					r.receive(q);
					String message = new String(rxbuffer,0,q.getLength());
					System.out.println(message);
					BigInteger XX = new BigInteger(message.substring(6,8), 16);
					System.out.println("Vehicle speed: " + XX.intValue() + " Km/h");
					return XX.intValue();
				} catch (IOException e) {e.printStackTrace();}
			else if(packetInfo.indexOf("05")>=0) 
				try {
					r.receive(q);
					String message = new String(rxbuffer,0,q.getLength());
					System.out.println(message);
					BigInteger XX = new BigInteger(message.substring(6,8), 16);
					int result = XX.intValue()-40;
					System.out.println("Coolant temperature: " + result + " \B0C");
					return result;
				} catch (IOException e) {e.printStackTrace();}
		//}
			return 0;
	}
	
	static void image(byte [] rxbuffer,DatagramPacket q,DatagramSocket r,DatagramPacket p,DatagramSocket s,String packetInfo) throws IOException, UnsupportedAudioFileException{
		ArrayList<Byte> imagebytes= new ArrayList<>();
		System.out.println("Download image...");
		s.send(p);
		for (;;) {
			try {
				r.receive(q);
				byte[] response = new byte[q.getLength()];
				System.arraycopy(rxbuffer, 0, response, 0, q.getLength());
					
				for(int i=0; i<response.length; i++) {
					imagebytes.add(response[i]);	
				}
				}catch (Exception x) {
					System.out.println(x);
					break;
				}
			//String next="NEXT";
			//byte[] n= next.getBytes();
			// byte[] hostIP = { (byte)155,(byte)207,(byte)18,(byte)208 };
			// InetAddress hostAddress = InetAddress.getByAddress(hostIP);
			//p =new DatagramPacket(n,n.length, hostAddress,38018);
			//s.send(p);
			
		}
		try {
			saveImage(imagebytes,packetInfo);
		}catch(Exception x) {
			System.out.println(x);
		}
	}
	
	static void saveImage(ArrayList<Byte> imageByte,String request) throws IOException {
		System.out.println("Saving image...");
		byte[] img = new byte[imageByte.size()];
	    for(int j=0; j < imageByte.size(); j++)
	    	img[j] = imageByte.get(j);
	    InputStream stream = new ByteArrayInputStream(img);
	    BufferedImage image= ImageIO.read(stream);
		ImageIO.write(image,"jpeg", new File("C:\\Users\\Julio\\Desktop\\Sessions\\Session2\\"+request+".jpeg"));
		System.out.println("Image saved successfully...");
	}
	
	
	static void AudioDPCM(byte [] rxbuffer,DatagramPacket q,DatagramSocket r,DatagramPacket p,DatagramSocket s, String request) throws IOException {
		ArrayList<Byte> audioBytes =new ArrayList<Byte>();
		ArrayList<Integer> differences =new ArrayList<Integer>();
		int Sample2=0;
		int Sample1;
		System.out.println("Download audio...");
		s.send(p);
		for(;;) {
			try {
				r.receive(q);
				byte[] response = new byte[q.getLength()];
				System.arraycopy(rxbuffer, 0, response, 0, q.getLength());
				
				int byte15=15;
	            int byte240=240;
	            for(int i=0; i<response.length; i++) {
		            int a = response[i];
		            int Nibble1 = (byte15 & a);
		            int Nibble2 = ((byte240 & a)>>4);
		            int Difference1 = (Nibble1-8)*2;
		            int Difference2 = (Nibble2-8)*2;
		            Sample1 = Sample2 + Difference1;
		            Sample2 = Sample1 + Difference2;
					audioBytes.add((byte)Sample1);
					audioBytes.add((byte)Sample2);
					differences.add(Difference1);
					differences.add(Difference2);
					
	            }
			}
            catch (IOException e) {e.printStackTrace(); break;}   
		}
		
		try {
			audioPlay(audioBytes, 8);
		} catch (LineUnavailableException e) {}//e.printStackTrace();}
		try {
			saveaudio(audioBytes,request,8);
		} catch (UnsupportedAudioFileException | IOException e1) {}//e1.printStackTrace();}
		saveDataToFileByte(audioBytes,request+"samples");
		saveDataToFileINT(differences,request+"differences");
	}
	
	public static void AudioAQDPCM(byte [] rxbuffer,DatagramPacket q,DatagramSocket r,DatagramPacket p,DatagramSocket s, String request) throws IOException {
		ArrayList<Byte> audioBytes =new ArrayList<Byte>();
		ArrayList<Integer> differences =new ArrayList<Integer>();
		ArrayList<Integer> means =new ArrayList<Integer>();
		ArrayList<Integer> steps =new ArrayList<Integer>();
		System.out.println("Download audio...");
		s.send(p);
		for(;;) {
			try {
				r.receive(q);
				byte[] response = new byte[q.getLength()];
				System.arraycopy(rxbuffer, 0, response, 0, q.getLength());
				
				int mean = (response[1] << 8) | (response[0] & 0xFF);   
			    int step = (response[3] << 8) | (response[2] & 0xFF);
					
		    	for(int j=4; j<response.length; j++){
		    		int Nibble1 = (byte) (response[j] >> 4) & 0xF;
					int Nibble2 = (byte) (response[j] & 0xF);
		    		int Difference1 = (Nibble1-8);
		    		int Difference2 = (Nibble2-8);
		    		int Sample1 =Difference1*step +mean;
			        int Sample2 = Difference2*step+mean;
					audioBytes.add((byte)(Sample1 & 0xFF));
					audioBytes.add((byte)((Sample1 >> 8) & 0xFF));
					audioBytes.add((byte)(Sample2 & 0xFF));
					audioBytes.add((byte)((Sample2 >> 8) & 0xFF));
					means.add(mean);
					steps.add(step);
					differences.add(Difference1);
					differences.add(Difference2);
		         }
			}catch(Exception e){
				e.printStackTrace();
				break;
			}
		}
		
		
		
		try {
			audioPlay(audioBytes, 16);
		} catch (LineUnavailableException e) {e.printStackTrace();}
		
		
		try {
			saveaudio(audioBytes,request,16);
		} catch (UnsupportedAudioFileException | IOException e) {e.printStackTrace();}
		
		saveDataToFileINT(means,request+"means");
		saveDataToFileINT(steps,request+"steps");
		saveDataToFileByte(audioBytes,request+"samples");
		saveDataToFileINT(differences,request+"differences");
	}
	
	static void audioPlay(ArrayList<Byte> audioBytes, int qu) throws LineUnavailableException {
		System.out.println("Playing audio...");
		AudioFormat linearPCM = new AudioFormat(8000,qu,1,true,false);
		SourceDataLine lineOut = AudioSystem.getSourceDataLine(linearPCM);
		lineOut.open(linearPCM,audioBytes.size());
		lineOut.start();
		byte[] audioBufferOut = new byte[audioBytes.size()];
		for(int i=0; i<audioBytes.size(); i++)
			audioBufferOut[i]=audioBytes.get(i);
		lineOut.write(audioBufferOut,0,audioBytes.size());
		lineOut.stop();
		lineOut.close();
	}
	public static void saveaudio(ArrayList<Byte> audioBytes, String request,int qu) throws UnsupportedAudioFileException, IOException
	{	System.out.println("Saving audio...");
		byte[] audio = new byte[audioBytes.size()];
	    for(int j=0; j < audioBytes.size(); j++)
	    	audio[j] = audioBytes.get(j);
		InputStream b_in = new ByteArrayInputStream(audio);
		AudioFormat format = new AudioFormat(8000, qu, 1, true, false);
        AudioInputStream stream = new AudioInputStream(b_in, format, audio.length);
        File file = new File("C:\\Users\\Julio\\Desktop\\Sessions\\Session2\\"+request+".wav");
        AudioSystem.write(stream, Type.WAVE, file);
        System.out.println("Audio saved successfully...");
	    
	}
	
	static void  R1(ArrayList<Float> RTT, String packetInfo) {
		float alpha =(float) (7/8.0), beta =  (float) (3/4.0); //PARAMETERS
		int gamma = 4;
		ArrayList<Float> SRTT_List = new ArrayList<Float>();
		ArrayList<Float> RTT_Sigma_List = new ArrayList<Float>();
		ArrayList<Float> RTO_List = new ArrayList<Float>();
		
		SRTT_List.add((1-alpha)*RTT.get(0));
		RTT_Sigma_List.add((1-beta) *Math.abs(SRTT_List.get(0)-RTT.get(0)));
		RTO_List.add(SRTT_List.get(0)+RTT_Sigma_List.get(0)*gamma);
		
		
		for(int i=1; i<RTT.size(); i++) {
			SRTT_List.add((1 - alpha) * RTT.get(i) + alpha*SRTT_List.get(i-1));
			RTT_Sigma_List.add(beta*RTT.get(i-1) + (1-beta)*Math.abs(SRTT_List.get(i)-RTT.get(i)));
			RTO_List.add(SRTT_List.get(i) + gamma*RTT_Sigma_List.get(i));
		}
		
		saveDataToFile(SRTT_List, "SRTT "+packetInfo);
		saveDataToFile(RTT_Sigma_List, "SIGMA(VAR) "+packetInfo);
		saveDataToFile(RTO_List, "RTO "+packetInfo);
		saveDataToFile(RTT, "responseTime "+packetInfo);
}

	static void saveDataToFile(ArrayList<Float> data, String request) {
		try {
	          System.out.println("Attempting To Save Array Contents To File...");
	          BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Julio\\Desktop\\Sessions\\"+request+".txt", false));
	          for(int i = 0; i < data.size(); i++) {
	            String x = Float.toString(data.get(i)); // Note you have to cast it as strings if not already
	            writer.write(x);
	            writer.newLine();  // More Platform-independent that using write("\n");
	          }
	          writer.flush();
	          writer.close();
	          System.out.println("Saved Array To File Successfully...");
	        } 
	        catch (IOException e) {
	          System.out.println("Couldnt Save Array To File... ");
	          e.printStackTrace();
	        }
	}
	
	static void saveDataToFileINT(ArrayList<Integer> data, String request) {
		try {
	          System.out.println("Attempting To Save Array Contents To File...");
	          BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Julio\\Desktop\\Sessions\\Session2\\"+request+".txt", false));
	          for(int i = 0; i < data.size(); i++) {
	            String x = Float.toString(data.get(i)); // Note you have to cast it as strings if not already
	            writer.write(x);
	            writer.newLine();  // More Platform-independent that using write("\n");
	          }
	          writer.flush();
	          writer.close();
	          System.out.println("Saved Array To File Successfully...");
	        } 
	        catch (IOException e) {
	          System.out.println("Couldnt Save Array To File... ");
	          e.printStackTrace();
	        }
	}
	
	static void saveDataToFileByte(ArrayList<Byte> data, String request) {
		try {
	          System.out.println("Attempting To Save Array Contents To File...");
	          BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Julio\\Desktop\\Sessions\\Session2\\"+request+".txt", false));
	          for(int i = 0; i < data.size(); i++) {
	            String x = Float.toString(data.get(i)); // Note you have to cast it as strings if not already
	            writer.write(x);
	            writer.newLine();  // More Platform-independent that using write("\n");
	          }
	          writer.flush();
	          writer.close();
	          System.out.println("Saved Array To File Successfully...");
	        } 
	        catch (IOException e) {
	          System.out.println("Couldnt Save Array To File... ");
	          e.printStackTrace();
	        }
	}
	
