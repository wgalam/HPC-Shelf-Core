package br.ufc.storm.io;

//Example extracted from http://qupera.blogspot.com.br/2013/02/howto-compress-and-uncompress-java-byte.html



import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.util.List;  
import java.util.Map;  
import java.util.zip.DataFormatException;  
import java.util.zip.Deflater;  
import java.util.zip.Inflater;  

public class Compressor {  
	public static void main(String[] args) {
		String s = "Teste de compressao";
		try {
			byte[] t = compress(s.getBytes());
			byte[] out = decompress(t);
			System.out.println(new String(out));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static byte[] compress(byte[] data) throws IOException {  
		long tempoInicial = System.currentTimeMillis();
		Deflater deflater = new Deflater();  
		deflater.setInput(data);  

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   

		deflater.finish();  
		byte[] buffer = new byte[524288000];   
		while (!deflater.finished()) {  
			int count = deflater.deflate(buffer); // returns the generated code... index  
			outputStream.write(buffer, 0, count);   
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  

		deflater.end();
		long tempoFinal = System.currentTimeMillis();
		System.out.println("Original: " + data.length / 1024 + " Kb");
		System.out.println("Compressed: " + output.length / 1024 + " Kb"+"in "+(tempoFinal-tempoInicial)+"ms");
		
		
		return output;  
	}  

	public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
		Inflater inflater = new Inflater();   
		inflater.setInput(data);  

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
		byte[] buffer = new byte[524288000];  
		while (!inflater.finished()) {  
			int count = inflater.inflate(buffer);  
			outputStream.write(buffer, 0, count);  
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  

		inflater.end();

		System.out.println("Original: " + data.length);
		System.out.println("Uncompressed: " + output.length);
		return output;  
	}  
} 
