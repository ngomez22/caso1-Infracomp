package caso2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
			
			respuesta = br.readLine();
			if(!respuesta.equals(OK)){
				System.out.println("Algoritmos incompatibles con el servidor.");
				cliente.close();
				return;
			}
			System.out.println("Algoritmos reconocidos por el servidor.");
			
			//
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		cliente.close();
	}
}
