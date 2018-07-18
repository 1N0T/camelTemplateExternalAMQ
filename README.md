![logo](https://raw.github.com/1N0T/images/master/global/1N0T.png)

# camelTemplateExternalAMQ
Plantilla mínima camel con Main, recepción de parámetros y envío de mensajes a una cola
de un servidor ActiveMQ externo. Este tipo de contexto, permanece en ejecución hasta que se cancela la **JVM**

Los datos que definen el servidor ActiveMQ receptor, se tienen que pasar como parámetro.
 * Nombre o IP del servidor.
 * Puerto.
 * Nombre de la cola de mensajes.

Para ejecutar:

```bash
mvn compile exec:java -Dexec.mainClass=ToniS.ProcesoCamel -Dexec.args="localhost 61616 miCola"
```

Para empaquetar y ejecutar, podemos optar por alguna de las siguientes opciones:

```bash
mvn package && java -jar ./target/plantillaCamel.jar "localhost 61616 miCola"
mvn package && java -cp ./target/plantillaCamel.jar ToniS.ProcesoCamel "localhost 61616 miCola"
```
> **OJO !!!** no confundir el puerto de la consola de administración de **ActiveMQ** (normalmente 81616), con el puerto al que tienen que enviar los mensajes a la cola (61616 por defecto)
