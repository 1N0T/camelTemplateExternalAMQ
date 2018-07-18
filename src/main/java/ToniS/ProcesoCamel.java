// mvn compile exec:java -Dexec.mainClass=ToniS.ProcesoCamel -Dexec.args="localhost 61616 miCola"

package ToniS;

import ToniS.*;

import org.apache.camel.*;
import org.apache.camel.main.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.activemq.camel.component.ActiveMQComponent;


public class ProcesoCamel {
 
    private org.apache.camel.main.Main instanciaCamel;
    public static void main(String[] args) throws Exception {
        String       servidor = args[0];	
        String       puerto   = args[1];	
        String       cola     = args[2];	
    	ProcesoCamel proceso  = new ProcesoCamel();
        proceso.inicia(servidor, puerto, cola);
    }

    public void inicia(String servidor, String puerto, String cola) throws Exception {
		
        // Creamos instancia de Main (permanece viva hasta se cancela la JVM)
        instanciaCamel = new org.apache.camel.main.Main();

        // Añadimos rutas
       	instanciaCamel.addRouteBuilder(new MiRouteBuilder(servidor, puerto, cola));

        // Añadimos listener de eventos
        instanciaCamel.addMainListener(new Events());
 
        // Se ejecuta hasta que termine la JVM
        System.out.println("=====================================================");
        System.out.println("Iniciando Camel. Usa ctrl + c para terminar la JVM.  ");
        System.out.println("=====================================================");
        instanciaCamel.run();
    }
 
    private static class MiRouteBuilder extends RouteBuilder {
        // Extendemos la clase RouteBuilder para recibir parámetros, por lo que
        // tenemos que definir un constructor que coincida con la firma
		private final String miServidor;
		private final String miPuerto;
		private final String miCola;

        // Este constructor no es necesario si no necesitamos pasarle parámetros
		private MiRouteBuilder(String servidor, String puerto, String cola) {
		    super();
		    this.miServidor = servidor;
		    this.miPuerto   = puerto;
		    this.miCola     = cola;
		}        

        @Override
        public void configure() throws Exception {

            System.out.println("He recibido los parámetros: " + this.miServidor + " " + this.miPuerto + " " + this.miCola);
            System.out.println("==================================================================================");

            ActiveMQComponent jms = new ActiveMQComponent(getContext());
            jms.setBrokerURL("tcp://" + this.miServidor + ":" + this.miPuerto);
            getContext().addComponent("jms", jms);

            from("timer://temporizador?period=100")
            .process(new Processor() {
                public void process(Exchange msg) throws Exception {
                    System.out.println("Procesando mensaje");
                }
            })
            .transform().simple("Este es mi mensaje")
            .marshal().json()
            .to("jms:queue:" + this.miCola);
        }
    }


	// No utilizamos los eventos pero lo dejamos a modo de plantilla por si en un futuro se necesitara.
    public static class Events extends MainListenerSupport {
 
        @Override
        public void afterStart(MainSupport main) {

            System.out.println("=====================");
            System.out.println("Camel se ha iniciado!");
            System.out.println("=====================");

        }
 
        @Override
        public void beforeStop(MainSupport main) {
            System.out.println("====================");
            System.out.println("Camel se va a parar!");
            System.out.println("====================");
        }
    }


}


