package caso2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;

import caso3.Generator;

public class Cliente {

	public static final int PUERTO = 4444;
	public static final String HOLA = "HOLA";
	public static final String ALGORITMOS = "ALGORITMOS";
	public static final String ALGS = "AES";
	public static final String ALGA = "RSA";
	public static final String ALGD = "HMACMD5";
	public static final String OK = "OK";
	public static final String ERROR = "ERROR";
	public static final String CERTI_CLIENTE = "CERTFICADOCLIENTE";
	public static final String CERTI_SERVIDOR = "CERTIFICADOSERVIDOR";
	public static final String CIFRADOKC = "CIFRADOKC+";
	public static final String CIFRADOKS = "CIFRADOKS+";
	public static final String CIFRADOLS1 = "CIFRADOLS1";
	public static final String CIFRADOLS2 = "CIFRADOLS2";

	private BufferedWriter data;
	private long tiempoAutenticacion;
	private long tiempoConsulta;

	public Cliente(BufferedWriter data) {
		this.data = data;
	}
	public void run() throws Exception{

		//Se inicializan el Socket y los canales de comunicación.
		Socket cliente = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		try {
			//186.80.244.70
			cliente = new Socket("192.168.0.34", PUERTO);
			pw = new PrintWriter(cliente.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			System.out.println("Inicialización exitosa.");
		} catch(Exception e) {
			System.out.println("No se pudo establecer conexión con el servidor.");
			Generator.registrarFalla();
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

			pw.println(CERTI_CLIENTE);
			System.out.println("Se envio el certificado al servidor.");

			//Se espera la respuesta del servidor con el certificado
			respuesta = br.readLine();
			if(!respuesta.equals(CERTI_SERVIDOR)){
				System.out.println("El servidor no reconocio el certificado digital.");
				cliente.close();
				return;
			}
			System.out.println("Se recibio el certificado del servidor.");
			pw.println(OK);

			//Se mide el tiempo inicial para la autenticación de los participantes
			long tInicial = System.currentTimeMillis();

			respuesta = br.readLine();
			if(!respuesta.equals(CIFRADOKC)){
				System.out.println(ERROR);
			}
			System.out.println("Se recibió el mensaje cifrado");

			pw.println(CIFRADOKS);

			respuesta = br.readLine();
			if(!respuesta.equals(OK)){
				System.out.println(ERROR);
			}

			//Se registra el tiempo que se tarda en autenticar las llaves.
			tiempoAutenticacion = System.currentTimeMillis()-tInicial;


			System.out.println("El servidor recibió mensaje cifrado");

			tInicial = System.currentTimeMillis(); 

			pw.println(CIFRADOLS1);

			respuesta = br.readLine();
			if(!respuesta.equals(CIFRADOLS2)){
				Generator.registrarFalla();
				System.out.println(ERROR);
			}
			System.out.println("Fin.");

			//Se registra el tiempo de la consulta.
			tiempoConsulta = System.currentTimeMillis() - tInicial;

			//Se imprimen los datos al archivo.
			float t1 = tiempoAutenticacion/1000;
			float t2 = tiempoConsulta/1000;
			data.write(tiempoAutenticacion + ", " + tiempoConsulta+"\n");
			data.flush();
			Generator.contar();
		} catch (IOException e) {
			System.out.println("FALLAAAAAAAAA");
			Generator.registrarFalla();
		}

		cliente.close();
	}

}
