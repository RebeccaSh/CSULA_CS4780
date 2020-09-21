package rsa;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyGen {
	private BigInteger p;  			// p 512-bit
	private BigInteger q; 				// q 512-bit
	private static BigInteger n; 			// n  1024 bits
	private BigInteger totient;    			// totient(n)
	private static BigInteger relativePrime; 		// also known as e, gcd(relativePrime, totient) = 1
	private static BigInteger d;					// d = e^(-1) mod totient
	
	public static void main(String[] args){
		KeyGen keyGen = new KeyGen();
		
		System.out.println("P: " + keyGen.getPrimeP()+"\n");
		
		System.out.println("Q: " + keyGen.getPrimeQ()+"\n");
		
		System.out.println("E: " + keyGen.getRelativePrime()+"\n");
		
		System.out.println("===========================================================================================================================");
		
		System.out.println("N: " + keyGen.getKeyProduct()+"\n");
		
		System.out.println("D: " + keyGen.getD()+"\n");

		
		keyGen.publicKeyToFile();
		keyGen.privateKeyToFile();
		
		
		System.out.println("=========================================================================================================================");
		//values e and n 
		List<BigInteger> publicKey = getPublic();
		//values d and n 
		List<BigInteger> privateKey = getPrivate();
		System.out.println("public key:e and n " + publicKey +"\n");
		System.out.println("private key:d and n " + privateKey+"\n");
		
	}
	
	public KeyGen(){
		p = BigInteger.probablePrime(1024/2, new Random());
		q = BigInteger.probablePrime(1024/2, new Random());
		
		//p cannot equal to q
		while(p.equals(q)){
			q = BigInteger.probablePrime(1024/2, new Random());
		}
		
		// n = p x q
		n = p.multiply(q);
		
		// totient(n) = (p-1) x (q-1)
		totient = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		// e, gcd(relativePrime, totient) = 1
		relativePrime  = BigInteger.probablePrime(1024/2, new Random());
		while(true){
			
			if(!relativePrime.equals(p) && !relativePrime.equals(q) && (relativePrime.gcd(totient)).equals(BigInteger.ONE)){
				break;
			}
			relativePrime  = BigInteger.probablePrime(1024/2, new Random());
		}
		
		// d = e^(-1) mod totient
		d = relativePrime.modInverse(totient);
	}
	

	public static List<BigInteger> getPublic(){
		// public key = < relativePrime, totient >
		List<BigInteger> publicKey = new ArrayList<BigInteger>();
		
		publicKey.add(relativePrime);
		publicKey.add(n);
		
		return publicKey;
	}
	
	public static List<BigInteger> getPrivate(){
		// private key = < d, totient >
		List<BigInteger> privateKey = new ArrayList<BigInteger>();
		
		privateKey.add(d);
		privateKey.add(n);
		
		return privateKey;
	}
	//write public key into rsa file
	public void publicKeyToFile(){
		try(OutputStream pubkey = new FileOutputStream("pubkey.rsa");
			ObjectOutputStream final_publickey = new ObjectOutputStream(pubkey);){
			final_publickey.writeObject(this.getRelativePrime());
			final_publickey.writeObject(this.getKeyProduct());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//write priavte key into rsa file
	public void privateKeyToFile(){
		try(OutputStream privatekey = new FileOutputStream("privkey.rsa");
			ObjectOutputStream final_privatekey = new ObjectOutputStream(privatekey);){
			final_privatekey.writeObject(this.getD());
			final_privatekey.writeObject(this.getKeyProduct());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public BigInteger getPrimeP() {
		return p;
	}

	public BigInteger getPrimeQ() {
		return q;
	}

	public BigInteger getKeyProduct() {
		return n;
	}

	public BigInteger getTotient() {
		return totient;
	}

	public BigInteger getRelativePrime() {
		return relativePrime;
	}

	public BigInteger getD() {
		return d;
	}
	
	
}