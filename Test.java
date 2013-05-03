import java.util.Arrays;
import java.util.Scanner;


public class Test {
	public static void main(String[] args) {
//		/long start_stamp, ReplicaInfo accepted_rInfo, int myID
		long stamp = System.currentTimeMillis();
		
		ReplicaInfo a = new ReplicaInfo(stamp++, null, 0);
		a.setAcceptStamp(1234567);
		System.out.println("A: " + a);
		
		ReplicaInfo b = new ReplicaInfo(stamp++, a, 1);
		b.setAcceptStamp(1234568);
		System.out.println("B: " + b);
		
		ReplicaInfo c = new ReplicaInfo(stamp++, b, 2);
		c.setAcceptStamp(1234569);
		System.out.println("C: " + c);
		
//		ReplicaInfo d  = new ReplicaInfo(c.toString());
//		System.out.println("D: " + d);
//		System.out.println("Accepted_SID: " + d.getAcceptedSID());
//		System.out.println("Accepted_stamp: " + d.getStartedStamp());
//		System.out.println("Accepted_info: " + d.getAcceptedInfo());
//		System.out.println("Accept_stamp: " + d.getAcceptStamp());
//		System.out.println("My ID: " + d.getMyID());
		
//		Write w = new Write(stamp, stamp+1, c, false, "edit", "Lover Supreme", "Love Supreme.mp3");
//		System.out.println(w);
		
//		Write w2 = new Write(w.toString());
//		System.out.println(w2);
//		System.out.println("AcceptStamp: " + w2.getAcceptStamp());
//		System.out.println("AcceptID: " + w2.getServerId());
//		System.out.println("Cmd: " + w2.getUpdateCmd());
//		System.out.println("Song: " + w2.getSong());
//		System.out.println("URL: " + w2.getURL());
//		System.out.println("CSN: " + w2.getCSN());
//		System.out.println("ReplicaInfo: " + w2.getReplicaInfo());
		
		String s = "abc;;;124";
		System.out.println(Arrays.toString(s.split(";;;")));
		
		s = "ABC;;;";
		System.out.println(Arrays.toString(s.split(";;;")));
	}
}
