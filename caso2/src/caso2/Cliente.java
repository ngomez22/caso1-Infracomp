package caso2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.PEMUtil;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.jca.GetInstance;
import sun.security.provider.X509Factory;

public class Cliente {
	
	public static final int PUERTO = 4443;
	public static final String HOLA = "HOLA";
	public static final String ALGORITMOS = "ALGORITMOS";
	public static final String ALGS = "DES";
	public static final String ALGA = "RSA";
	public static final String ALGD = "HMACMD5";						
	
	public static final String OK = "OK";
	public static final String ERROR = "ERROR";

	public static void main(String[] args) throws Exception{
		//Se inicializan el Socket y los canales de comunicación.
		Socket cliente = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			cliente = new Socket("localhost", PUERTO);
			pw = new PrintWriter(cliente.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			System.out.println("Inicialización exitosa.");
		} catch(Exception e) {
			System.out.println("No se pudo establecer conexión con el servidor.");
			return;
		}
		//Se inicia la comuniación
		try {		
			//Se confirma la conexión.
			pw.println(HOLA);
			String respuesta = br.readLine();
			if(!respuesta.equals(OK)){
				System.out.println("No se recibió respuesta del servidor.");
				cliente.close();
				return;
			}
			System.out.println("Se obtuvo respuesta del servidor.");
			
			//Se seleccionan los aloritmos a utilizar y se envían al servidor.
			pw.println(ALGORITMOS + ":" + ALGS + ":" + ALGA + ":" + ALGD);
			
			//Se espera verificación de que el servidor es compatible con los algoritmos
			respuesta = br.readLine();
			if(!respuesta.equals(OK)){
				System.out.println("Algoritmos incompatibles con el servidor.");
				cliente.close();
				return;
			}
			System.out.println("Se enviaron los algoritmos al servidor y fueron reconocidos.");
			
			//Se genera el par de llaves del cliente
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGA);
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			kpg.initialize(1024, sr);
			KeyPair llavesCliente = kpg.generateKeyPair();
			
			//Se genera el certificado digital del cliente
			X509Certificate cert = generarCertificado(llavesCliente);
			
			//Se envía el certificado al servidor en formato pem
			BASE64Encoder encoder = new BASE64Encoder();
			pw.println(X509Factory.BEGIN_CERT);
			pw.print(encoder.encodeBuffer(cert.getEncoded()));
			pw.println(X509Factory.END_CERT);
			System.out.println("Se envio el certificado al servidor.");
			
			//Se espera la respuesta del servidor con el certificado
			respuesta = br.readLine();
			if(!respuesta.equals(X509Factory.BEGIN_CERT)){
				System.out.println("El servidor no reconocio el certificado digital.");
				cliente.close();
				return;
			}
			String certServidorCodificado = respuesta + "\n";
			String line;
			do {
				line = br.readLine();
				certServidorCodificado += line + "\n";
			} while (!line.equals(X509Factory.END_CERT));
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream is = new ByteArrayInputStream(certServidorCodificado.getBytes("UTF-8"));
			X509Certificate certServidor = (X509Certificate) cf.generateCertificate(is);
			System.out.println("Se recibio el certificado del servidor.");
			
			//Se extrae la llave publica del certificado digital del servidor
			PublicKey llavePublicaServidor = certServidor.getPublicKey();
			
			pw.println(OK);
			pw.println(OK);
			respuesta = br.readLine();
			
			//Se recibe el mensaje con la llaves simetrica encriptada
			String llaveSimetricaEncryptada = br.readLine();
			System.out.println("Se recibio la llave simetrica enctriptada del servidor");
			
			//Se decripta la llave simetrica
			String llaveSimetrica = decriptar(llavesCliente.getPrivate(), llaveSimetricaEncryptada);
			
			pw.println(encriptar(llavePublicaServidor, llaveSimetrica));
			System.out.println("Se envio la llave simetrica enctriptada al servidor");
			
			respuesta = br.readLine();
			if(!respuesta.equals(OK)){
				System.out.println("El servidor encontro un problema con la llave simetrica encriptada.");
				cliente.close();
				return;
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		cliente.close();
	}
	
	private static X509Certificate generarCertificado(KeyPair keyPair) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		X509Certificate cert = null;
		try {
			Date startDate = sdf.parse("01/08/2016");
			Date expiryDate = sdf.parse("01/08/2017");
			BigInteger serialNumber = new BigInteger("83861006689788998");
			X509V1CertificateGenerator cg = new X509V1CertificateGenerator();
			X500Principal p = new X500Principal("CN=Christian Potdevin");
			cg.setSerialNumber(serialNumber);
			cg.setIssuerDN(p);
			cg.setNotBefore(startDate);
			cg.setNotAfter(expiryDate);
			cg.setSubjectDN(p);
			cg.setPublicKey(keyPair.getPublic());
			cg.setSignatureAlgorithm("MD2withRSA");
			cert = cg.generate(keyPair.getPrivate());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return cert;
	}
	
	private static String encriptar(Key llave, String mensaje) {
		String mensajeEncriptadoEnHex = null;
		try {
			char[] hexArray = "0123456789ABCDEF".toCharArray();
			byte[] mensajeEncriptado = null;
			Cipher cifrador = Cipher.getInstance("RSA");
			cifrador.init(Cipher.ENCRYPT_MODE, llave);
			mensajeEncriptado = cifrador.doFinal(mensaje.getBytes());
		    mensajeEncriptadoEnHex = bytesAHex(mensajeEncriptado);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mensajeEncriptadoEnHex;
	}
	
	private static String decriptar(Key llave, String mensajeEncriptado) {
		String mensajeDecriptado = null;
	    byte[] bytesMensajeEncriptado = hexABytes(mensajeEncriptado);
		try {
			Cipher decriptador = Cipher.getInstance("RSA");
			decriptador.init(Cipher.DECRYPT_MODE, llave);
			byte[] bytes = decriptador.doFinal(bytesMensajeEncriptado);
			mensajeDecriptado = new String(bytes, "UTF-8");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mensajeDecriptado;
	}
	
	private static byte[] hexABytes(String hex) {
		int len = hex.length();
	    byte[] bytes = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i+1), 16));
	    }
	    return bytes;
	}
	
	private static String bytesAHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v/16];
	        hexChars[j * 2 + 1] = hexArray[v%16];
	    }
	    String hex = new String(hexChars);
	    return hex;
	}
}
