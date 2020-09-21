package rsa;

import java.io.File;
import java.util.Scanner;

public class Main { 
	
	public static void main(String[] args) {
		String filepath = "";
		Scanner scan = new Scanner(System.in);


		String input;
		do {
			input = "0";
			while (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 4) {
				System.out.println("Main Menu-----------------------------------\n"
						+ "1. Generate Keys.\n" 
						+ "2. Sign a message.\n"
						+ "3. Verify a message.\n" 
						+ "4. Exit.\n"
						+ "Please enter the task number [1-4]:\n");

				if (scan.hasNext())
					input = scan.next();
				try {
					Integer.parseInt(input);
				} catch (Exception e) {
					System.out.println("Not a valid selection! Try again.\n");
					input = "0";
				}
			}

			if (Integer.parseInt(input) == 1) {
				KeyGen kg = new KeyGen();
				//Start printing out the keygen values...
				System.out.println("Prime P is " + kg.getPrimeP());
				System.out.println("Prime Q is " + kg.getPrimeQ());
				System.out.println("n = (p x q) is " + kg.getKeyProduct());
				System.out.println("Totient is " + kg.getTotient());
				System.out.println("e is " + kg.getRelativePrime());
				System.out.println("d is " + kg.getD());
				
				kg.privateKeyToFile();
				kg.publicKeyToFile();
				System.out.println();
			}
			if (Integer.parseInt(input) == 2) {
				System.out.println("Please enter the .txt you wish to encrypt: ");
				filepath = scan.next();
				
				DigitalSignature ds = new DigitalSignature();
				try{
					System.out.println("Now attempting to sign... " + filepath);
					ds.sender(new File(filepath));
					System.out.println("Message has been signed. Now returning signed file.");
					System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if (Integer.parseInt(input) == 3) {
				System.out.println("Please enter the .txt.signed you wish to decrypt: ");
				filepath = scan.next();
				
				DigitalSignature ds = new DigitalSignature();
				try{
					System.out.println("Now attempting to read... " + filepath);
					ds.receiver(new File(filepath));
					System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}

		}while(Integer.parseInt(input) != 4);
	}
}