/*
 * Author: Ruolan Shen
 */

package prj1;

import java.util.Arrays;

public class SDEScracker {
	public static void main(String args[]){
			String msg1 = "1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011";
			
			System.out.println("              Part 2 SDES Cracker");	
			System.out.println("------------------------------------------------");
		//First Part: encode the message CRYPTOGRAPHY	
		encode();
		System.out.println();
		System.out.println("------------------------------------------------");
		
		//Second Part: decode message and find the key
		crack(msg1);
		
		}
		
	
	public static void encode(){
		//change the input key 
		byte[] key = stringToByteArray("0111001101");
		String word = "CRYPTOGRAPHY";
		//convert string to byte
		byte[] text = CASCII.Convert(word);
			
		byte plaintext[] = new byte[8];
		
		 byte output[]= new byte[64];

	        int k=0;
	        for(int i=0;i<text.length;i+=8) {

	          //Separate input text into sets each contains 8 bits
	            for(int j=0,l=i;j<plaintext.length;j++,l++) {
	                plaintext[j] = text[l];
	                
	            }

	            // using SDES encrypt key and plaintext
	            byte cipherText[] = SDES.Encrypt(key, plaintext);
	          
	            // put 8 output together = 8x8 = 64 bits
	            for(int z=0;z<8;z++) {
	                output[k]=cipherText[z];
	                k++;
	            }

	        }
	        System.out.println();

		System.out.println("Part 1 : word to encrypt  " + word);
		
		System.out.println("key: "+ Arrays.toString(key));
		System.out.println("Cipher: \t"+ Arrays.toString(output));
		
	}
	
	
	public static byte[] crack(String encrypted){
		byte[] key = {};
		
		//last key need to use
		byte[] last = {1,1,1,1,1,1,1,1,1,1};
		
		int upperlimit = getDecimal(last);
		
		byte[] decoded = null;
		System.out.println("part 2: ");
		for(int i = 0; i < upperlimit; i++){
			
			key = getByte(i, 10);
			byte[] input = stringToByteArray(encrypted);
			 decoded = bruteForce(key, input);
			
					
				//check only if the given message is equal to the encrypt the bruteForce decode, print out the key
			if(check(input,decoded)) {
				System.out.println( "key decode  : "+Arrays.toString(key));
				System.out.println( "words: "+ CASCII.toString(decoded));
				
			}
				
			}
		
			return key;	
		
	}

	
	
	public static boolean check(byte[] input, byte[] text){
		byte[] key = {1, 0, 1, 1, 1, 1, 0, 1, 0, 0};
			
	byte plaintext[] = new byte[8];
	
	 byte output[]= new byte[952];

        int k=0;
        for(int i=0;i<text.length;i+=8) {

          
            for(int j=0,l=i;j<plaintext.length;j++,l++) {
                plaintext[j] = text[l];
                
            }

            // plain text separate into 8 different with each contains 8 bits
            byte cipherText[] = SDES.Encrypt(key, plaintext);
          
            // put output together 
            for(int z=0;z<8;z++) {
                output[k]=cipherText[z];
                k++;
            }

        }
        //check if the encode is same as the given message
        if(Arrays.toString(output).equals(Arrays.toString(input))) {
        	return true;
        }
        return false;
       
	}
	
	
	public static byte[] bruteForce(byte[] key, byte[] cipher){
		byte[] text = {};
				
		byte[][] arrays = groupByN(cipher, 8);
		
		
		for(byte[] group: arrays){
			
			byte[] p = SDES.Decrypt( key, group);
			 text = concatenateByteArrays(text, p);
			 	  
		}
		return text;
		}
	
	
	
	public static int byteArrayCount(byte[] arr){
		int count = 0;
		for(int i = 0; i < arr.length; i++){
			count++;
		}
		return count;
	}
	
	
	
	//seperate input byte into different set with 8 bits
	//group together as byte array 
	public static byte[][] groupByN(byte[] arr, int n){
		//total number of byte in msg1
		int totalCount = byteArrayCount(arr);
		//seperate into groups with 8 bits each
		int groupCount = (int)Math.ceil(totalCount/n);
		
		byte[][] groups = new byte[groupCount][n];
		
		for(int x = 0; x <  groupCount; x++){
			byte[] grouped = new byte[n];
			for(int y = 0; y < n; y++){
				int index = y + (x * n);
				grouped[y] = arr[index];
			}
			groups[x] = grouped;
			
		}
		
		return groups;
		
	}
	
	//convert binary to decimal
	public static int getDecimal(byte[] arr){
		int total = 0;
		int count = arr.length - 1;
		
		for(byte b: arr){
			if(b == 1)
				total += Math.pow(2, count);	
			count--;
		}
		
		return total;
  }
  
//return decimal into binary, store in the new byte as the input size 
	public static byte[] getByte(int decimal, int binary_size){
		byte[] binary = new byte[binary_size];
		
		while(binary_size > 0){
			binary[binary_size-1] = (byte) (decimal % 2);
			decimal = decimal >> 1;
			binary_size--;
		}
		
		return binary;
	}
	

//Concatenate two byte arrays
	public static byte[] concatenateByteArrays(byte[] arr1, byte[] arr2){
		byte[] concated = new byte[arr1.length + arr2.length];

		int count = 0;
		for(byte b : arr1){
			concated[count]=b;
			count++;
		}

		for(byte b : arr2){
			concated[count]=b;
			count++;
		}


		return concated;
	}
	
//String to byte array
	public static byte[] stringToByteArray(String s){
		byte[] array = new byte[s.length()];

		for (int i = 0; i < s.length(); i++){
		    char c = s.charAt(i);        
		    array[i] = (byte) Character.getNumericValue(c);
		}
		return array;
	}


}
