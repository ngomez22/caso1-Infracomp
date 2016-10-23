package caso2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;

import sun.misc.BASE64Encoder;
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
			System.out.println("Algoritmos reconocidos por el servidor.");
			
			//Se genera el par de llaves del cliente
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGA);
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			kpg.initialize(1024, sr);
			KeyPair keyPair = kpg.generateKeyPair();
			
			//Se genera el certificado digital del cliente
			X509Certificate cert = generarCertificado(keyPair);
			
			//Se envía el certificado al servidor en formato .pem
			BASE64Encoder encoder = new BASE64Encoder();
			pw.println(X509Factory.BEGIN_CERT);
			pw.print(encoder.encodeBuffer(cert.getEncoded()));
			pw.println(X509Factory.END_CERT);
			
			//Se espera la respuesta del servidor al certificado
			respuesta = br.readLine();
			if(!respuesta.equals(X509Factory.BEGIN_CERT)){
				System.out.println("Servidor no reconoció el certificado digital.");
				cliente.close();
				return;
			}
			String certificado = br.readLine();
			respuesta = br.readLine();
			if(!respuesta.equals(X509Factory.END_CERT)){
				System.out.println("Error recibiendo certificado digital del servidor.");
				cliente.close();
				return;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		cliente.close();
	}
	
	public static X509Certificate generarCertificado(KeyPair keyPair) {
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
}
