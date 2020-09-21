/*
 * Author: Nicholas Torres
 */

package prj1;
import java.util.HashMap;
public class SDES {
	
	public static byte[] cipherComponentOne = new byte[4];	//Left half of plaintext/ciphertext in Encryption/decryption
	public static byte[] cipherComponentTwo = new byte[4];	//Left half of plaintext/ciphertext in Encryption/decryption
	public static byte[][] roundKeys = new byte[2][8];		//Array to hold both 8 bit round keys for SDES
	public static void main(String[] args) {
			
		System.out.println("              Part 1 SDES Table");
		System.out.println("------------------------------------------------");
		System.out.println("  Raw Key   |   Plain Text   |  Cipher Text   ");
		System.out.println("------------------------------------------------");
		encryptDemo();
		decreyptDemo();

	}
		public static void encryptDemo(){
		byte[][] key = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		key[2] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		key[3] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };

		plaintext[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };
		plaintext[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };

		for(int i = 0; i < 4; i++){
			ciphertext[i] = SDES.Encrypt(key[i], plaintext[i]);
			printArray(key[i]);
			System.out.print("       ");
			printArray(plaintext[i]);
			System.out.print("       ");
			printArray(ciphertext[i]);
			System.out.println();
			System.out.println("------------------------------------------------");
		}
	}

	public static void decreyptDemo(){
		byte[][] key = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key[0] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key[1] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key[2] = new byte[]{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };
		key[3] = new byte[]{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };

		ciphertext[0] = new byte[]{ 0, 0, 0, 1, 1, 1, 0, 0 };
		ciphertext[1] = new byte[]{ 1, 1, 0, 0, 0, 0, 1, 0 };
		ciphertext[2] = new byte[]{ 1, 0, 0, 1, 1, 1, 0, 1 };
		ciphertext[3] = new byte[]{ 1, 0, 0, 1, 0, 0, 0, 0 };

		for(int i = 0; i < 4; i++){
			plaintext[i] = SDES.Decrypt(key[i], ciphertext[i]);
			printArray(key[i]);
			System.out.print("       ");
			printArray(plaintext[i]);
			System.out.print("       ");
			printArray(ciphertext[i]);
			System.out.println();
			System.out.println("------------------------------------------------");
			
		}
	}

	public static void printArray(byte[] array){
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i]);
	}
	
	/**
	 * shiftLeft performs a circular shift of the given array "arr".
	 * "arr" is shifted "shiftBy" times.
	 * @param arr The array to be shifted.
	 * @param shiftBy How many times the array shall be shifted.
	 */
	public static void shiftLeft(byte[] arr, int shiftBy) {
		int length = arr.length;
		if(shiftBy == 1) {
			byte[] tempArr = new byte[length];
			byte firstBit = arr[0];
			for(int i = 0; i < length - 1; i++) {
				tempArr[i] = arr[i + 1];
			}
			tempArr[length - 1] = firstBit;
			System.arraycopy(tempArr, 0, arr, 0, length);
		} else {
			byte[] tempArr = new byte[length];
			byte firstBit = arr[0];
			byte secondBit = arr[1];
			for(int i = 0; i < length - 2; i++) {
				tempArr[i] = arr[i + 2];
			}
			tempArr[length - 2] = firstBit;
			tempArr[length - 1] = secondBit;
			System.arraycopy(tempArr, 0, arr, 0, length);
		}
	}
	/**
	 * The initial permutation of the raw key. (Straight p-box)
	 * @param rawKey The user input raw key to be permuted.
	 */
	public static void initialPermutation(byte[] rawKey) {
		HashMap<Integer, Integer> initialKeyPermutationTable = new HashMap<Integer, Integer>();
		initialKeyPermutationTable.put(0, 2);
		initialKeyPermutationTable.put(1, 4);
		initialKeyPermutationTable.put(2, 1);
		initialKeyPermutationTable.put(3, 6);
		initialKeyPermutationTable.put(4, 3);
		initialKeyPermutationTable.put(5, 9);
		initialKeyPermutationTable.put(6, 0);
		initialKeyPermutationTable.put(7, 8);
		initialKeyPermutationTable.put(8, 7);
		initialKeyPermutationTable.put(9, 5);
		byte[] tempArr = new byte[10];
		for(int i = 0; i < 10; i++) {
			tempArr[i] = rawKey[initialKeyPermutationTable.get(i)];
		}
		System.arraycopy(tempArr, 0, rawKey, 0, 10);
	}
	/**
	 * The compression p-box that transforms a left-shifted key into an 8 bit SDES round key.
	 * @param key The left-shifted key to be transformed into an 8 bit SDES round key.
	 * @return The transformed 8 bit SDES round key.
	 */
	public static byte[] compressKey(byte[] key) {
		HashMap<Integer, Integer> keyCompressionPermutationTable = new HashMap<Integer, Integer>();	//Compression p-box for compressing 56 bit input into 48 bit feistel round key
	 	
		keyCompressionPermutationTable.put(0, 5);
		keyCompressionPermutationTable.put(1, 2);
		keyCompressionPermutationTable.put(2, 6);
		keyCompressionPermutationTable.put(3, 3);
		keyCompressionPermutationTable.put(4, 7);
		keyCompressionPermutationTable.put(5, 4);
		keyCompressionPermutationTable.put(6, 9);
		keyCompressionPermutationTable.put(7, 8);

		byte[] compressedKey = new byte[8];
		for(int i = 0; i < 8; i++) {
			compressedKey[i] = key[keyCompressionPermutationTable.get(i)];
		}
		return compressedKey;
	}
	/**
	 * Generates both SDES round keys.
	 * @param rawKey The raw key entered by the user to generate both 8 bit SDES round keys.
	 */
	public static void generateKeys(byte[] rawKey) {
		initialPermutation(rawKey);
		byte[] firstHalf = new byte[5];
		byte[] secondHalf = new byte[5];
		for(int i = 0; i < 5; i++) {
			firstHalf[i] = rawKey[i];
			secondHalf[i] = rawKey[i + 5];
		}
		shiftLeft(firstHalf, 1);
		shiftLeft(secondHalf, 1);
		for(int i = 0; i < 5; i++) {
			rawKey[i] = firstHalf[i];
			rawKey[i + 5] = secondHalf[i];
		}
   		System.arraycopy(compressKey(rawKey), 0, roundKeys[0], 0, 8);
		shiftLeft(firstHalf, 2);
		shiftLeft(secondHalf, 2);
		for(int i = 0; i < 5; i++) {
			rawKey[i] = firstHalf[i];
			rawKey[i + 5] = secondHalf[i];
		}
		System.arraycopy(compressKey(rawKey), 0, roundKeys[1], 0, 8);
	}
	/**
	 * The initial plaintext/ciphertext p-box before SDES rounds.
	 * @param text The plaintext/ciphertext entered by the user.
	 */
	public static void initialTextPermutation(byte[] text) {
		HashMap<Integer, Integer> initialTextPermutationTable = new HashMap<Integer, Integer>();	//Initial p-box for text/ciphertext transformation
		
		initialTextPermutationTable.put(0, 1);
		initialTextPermutationTable.put(1, 5);
		initialTextPermutationTable.put(2, 2);
		initialTextPermutationTable.put(3, 0);
		initialTextPermutationTable.put(4, 3);
		initialTextPermutationTable.put(5, 7);
		initialTextPermutationTable.put(6, 4);
		initialTextPermutationTable.put(7, 6);

		byte[] tempArr = new byte[8];
		for(int i = 0; i < 8; i++) {
			tempArr[i] = text[initialTextPermutationTable.get(i)];
		}
		System.arraycopy(tempArr, 0, text, 0, 8);
		for(int i = 0; i < 4; i++) {
			cipherComponentOne[i] = text[i];
			cipherComponentTwo[i] = text[i + 4];			
		}
	}
	/**
	 * The final plaintext/ciphertext p-box after SDES rounds.
	 * @param text The plaintext/ciphertext generated by SDES rounds.
	 */
	public static void finalTextPermutation(byte[] text) {
		HashMap<Integer, Integer> finalTextPermutationTable = new HashMap<Integer, Integer>();	//Final p-box for text/ciphertext transformation 
		finalTextPermutationTable.put(0, 3);
		finalTextPermutationTable.put(1, 0);
		finalTextPermutationTable.put(2, 2);
		finalTextPermutationTable.put(3, 4);
		finalTextPermutationTable.put(4, 6);
		finalTextPermutationTable.put(5, 1);
		finalTextPermutationTable.put(6, 7);
		finalTextPermutationTable.put(7, 5);
		byte[] tempArr = new byte[8];
		System.arraycopy(text, 0, tempArr, 0, 8);
		for(int i = 0; i < 8; i++) {
			text[i] = tempArr[finalTextPermutationTable.get(i)];
		}
	}
	/**
	 * Expands 4 bit DES function input to 8 bit array.
	 * @return Expanded 8 bit array.
	 */
	public static byte[] expansionPermutation() {
		 HashMap<Integer, Integer> expansionTable = new HashMap<Integer, Integer>();	//Expanison p-box for DES function
	expansionTable.put(0, 3);
		expansionTable.put(1, 0);
		expansionTable.put(2, 1);
		expansionTable.put(3, 2);
		expansionTable.put(4, 1);
		expansionTable.put(5, 2);
		expansionTable.put(6, 3);
		expansionTable.put(7, 0);
		byte[] expandedArr = new byte[8];
		for(int i = 0; i < 8; i++) {
			expandedArr[i] = cipherComponentTwo[expansionTable.get(i)];
		}
		return expandedArr;
	}
	/**
	 * SDES function substep for compressing SDES XOR output into 4 bit array.
	 * @param input 8 bit SDES function XOR output.
	 * @return 4 bit array array to be transformed by mixer permutation table. (straight SDES function p-box)
	 */
	public static byte[] sBoxCompression(byte[] input) {
		byte[][] sBox1;	//s-box #1 in SDES
		 byte[][] sBox2;

		byte[][] binLookup = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
			byte[][] box1 = {{1, 0, 3, 2},
						{3, 2, 1, 0},
						{0, 2, 1, 3},
						{3, 1, 3, 2}};
		sBox1 = box1;
		byte[][] box2 = {{0, 1, 2, 3},
						{2, 0, 1, 3},
						{3, 0, 1, 0},
						{2, 1, 0, 3}};
		sBox2 = box2;

		byte[] output = new byte[4];
		byte[][] sBoxInputs = new byte[2][4];
		byte[][] sBoxOutputs = new byte[2][2];
		byte inputOutputCounter = 0;
		for(int i = 0; i < 2; i++) {
			sBoxInputs[i][0] = input[inputOutputCounter++];
			sBoxInputs[i][1] = input[inputOutputCounter++];
			sBoxInputs[i][2] = input[inputOutputCounter++];
			sBoxInputs[i][3] = input[inputOutputCounter++];
		}
		inputOutputCounter = 0;
		for(int i = 0; i < 2; i++) {
			switch(i) {
				case 0:
					System.arraycopy(binLookup[sBox1[(2 * sBoxInputs[0][0]) + sBoxInputs[0][3]][(2 * sBoxInputs[0][1]) + sBoxInputs[0][2]]], 0, sBoxOutputs[0], 0, 2);
					break;
				case 1:
					System.arraycopy(binLookup[sBox2[(2 * sBoxInputs[1][0]) + sBoxInputs[1][3]][(2 * sBoxInputs[1][1]) + sBoxInputs[1][2]]], 0, sBoxOutputs[1], 0, 2);
					break;
				default:
					break;
			}
		}
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				output[inputOutputCounter++] = sBoxOutputs[i][j];
			}
		}
		return output;
	}
	/**
	 * Final step in SDES function which transforms s-box output. Uses mixer permutation table p-box to transform array.
	 * @param compressedArr The array compressed by SDES function s-boxes.
	 */
	public static void DESFunctionPermute(byte[] compressedArr) {
		HashMap<Integer, Integer> mixerPermutationTable = new HashMap<Integer, Integer>();	//Final p-box in DES function (straight p-box)
		mixerPermutationTable.put(0, 1);
		mixerPermutationTable.put(1, 3);
		mixerPermutationTable.put(2, 2);
		mixerPermutationTable.put(3, 0);
	
		byte[] tempArr = new byte[4];
		System.arraycopy(compressedArr, 0, tempArr , 0, 4);
		for(int i = 0; i < 4; i++) {
			compressedArr[i] = tempArr[mixerPermutationTable.get(i)];
		}
	}
	/**
	 * The SDES function.
	 * @param round SDES encryption round number.
	 * @return The SDES function output that should be XOR'ed with the left side of the plaintext/ciphertext.
	 */
	public static byte[] DESFunctionEncrypt(int round) {
		byte[] tempArr = expansionPermutation();
		byte[] key = roundKeys[round - 1];
		for(int i = 0; i < 8; i++) {
			tempArr[i] = (byte)(tempArr[i] ^ key[i]);
		}
		byte[] compressedArr = sBoxCompression(tempArr);
		DESFunctionPermute(compressedArr);
		return compressedArr;
	}
	/**
	 * The SDES function.
	 * @param round SDES encryption round number.
	 * @return The SDES function output that should be XOR'ed with the left side of the plaintext/ciphertext.
	 */
	public static byte[] DESFunctionDecrypt(int round) {
		byte[] tempArr = expansionPermutation();
		byte[] key = roundKeys[2 - round];
		for(int i = 0; i < 8; i++) {
			tempArr[i] = (byte)(tempArr[i] ^ key[i]);
		}
		byte[] compressedArr = sBoxCompression(tempArr);
		DESFunctionPermute(compressedArr);
		return compressedArr;
	}
	public static void swapper() {
		byte[] temp = cipherComponentOne;
		cipherComponentOne = cipherComponentTwo;
		cipherComponentTwo = temp;
	}
	
	/**
	 * SDES encryption algorithm.
	 * @param plaintext The user entered plaintext to be encrypted.
	 * @param rawKey The user entered key to be used for encryption.
	 * @return The encrypted ciphertext.
	 */
	public static byte[] Encrypt(byte[]rawKeyParameter , byte[]plaintextParameter ) {
		byte[] rawKey = new byte[10];
		byte[] plaintext = new byte[8];
		System.arraycopy(plaintextParameter, 0, plaintext, 0, 8);
		System.arraycopy(rawKeyParameter, 0, rawKey, 0, 10);
		generateKeys(rawKey);
		initialTextPermutation(plaintext);
		byte[] desFunctionArr = DESFunctionEncrypt(1);
		for(int i = 0; i < 4; i++) {
			cipherComponentOne[i] = (byte)(cipherComponentOne[i] ^ desFunctionArr[i]);
		}
		swapper();
		desFunctionArr = DESFunctionEncrypt(2);
		for(int i = 0; i < 4; i++) {
			cipherComponentOne[i] = (byte)(cipherComponentOne[i] ^ desFunctionArr[i]);
		}
		byte[] ciphertext = new byte[8];
		for(int i = 0; i < 4; i++) {
			ciphertext[i] = cipherComponentOne[i];
			ciphertext[i + 4] = cipherComponentTwo[i];
		}
		finalTextPermutation(ciphertext);
		return ciphertext;
	}
	/**
	 * SDES decryption algorithm.
	 * @param ciphertext The user entered ciphertext to be decrypted.
	 * @param rawKey The user entered key to be used for decryption.
	 * @return The decrypted plaintext.
	 */
	public static byte[] Decrypt(byte[] rawKeyParameter, byte[] ciphertextParameter) {
		byte[] ciphertext = new byte[8];
		byte[] rawKey = new byte[10];
		System.arraycopy(ciphertextParameter, 0, ciphertext, 0, 8);
		System.arraycopy(rawKeyParameter, 0, rawKey, 0, 10);
		generateKeys(rawKey);
		initialTextPermutation(ciphertext);
		byte[] desFunctionArr = DESFunctionDecrypt(1);
		for(int i = 0; i < 4; i++) {
			cipherComponentOne[i] = (byte)(cipherComponentOne[i] ^ desFunctionArr[i]);
		}
		swapper();
		desFunctionArr = DESFunctionDecrypt(2);
		for(int i = 0; i < 4; i++) {
			cipherComponentOne[i] = (byte)(cipherComponentOne[i] ^ desFunctionArr[i]);
		}
		byte[] plaintext = new byte[8];
		for(int i = 0; i < 4; i++) {
			plaintext[i] = cipherComponentOne[i];
			plaintext[i + 4] = cipherComponentTwo[i];
		}
		finalTextPermutation(plaintext);
		return plaintext;
	}
}