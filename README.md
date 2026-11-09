# Programa 6A: busqueda de x y Jenkins

Se busca x >= 0 tal que la integral t de Student entre 0 y x sea p.
Requiere Java 17 o posterior y Maven. p es el area desde cero, no la
probabilidad acumulada desde menos infinito: 0 <= p < 0.5, dof entero de 1 a 340.

## Reuso de 5A

`Simpson`, `DistribucionT`, `Gamma` y `OutFile` se copiaron sin cambios de 5A.
Se conservaron sus pruebas. La entrada anterior queda en `Main5A` y sus
pruebas en `Main5ATest`; `Main` ahora recibe p y dof. `Busqueda` es nueva.
La carpeta original 5A no fue modificada.

## Ejecucion

Desde esta carpeta:

```powershell
mvn clean verify
java -cp target/classes Main 0.20 6
java -cp target/classes Main 0.45 15
java -cp target/classes Main 0.495 4
```

Cada ejecucion muestra p, dof, x, integral y error absoluto y sobrescribe
`resultado.txt` usando OutFile. Los argumentos invalidos terminan con codigo 1.
JUnit se publica en `target/surefire-reports`; la cobertura queda en
`target/site/jacoco/index.html`.

## Requisitos y algoritmo

Fuente: `6a.pdf`, paginas 4 y 5 del archivo, tabla 1 y Search algorithm.

| p | dof | x esperado en el PDF |
|---|---|---|
| 0.20 | 6 | 0.55338 |
| 0.45 | 15 | 1.75305 |
| 0.495 | 4 | 4.60409 |

Se inicia x=1 y d=0.5. Se integra con Simpson reutilizado; si falta area,
se suma d; si sobra, se resta d. Al cambiar el signo del error se divide d
entre dos antes del siguiente ajuste. Se acepta abs(p-integral) < 0.00001.
p=0 devuelve x=0. Hay un limite de 100000 iteraciones y se informa error
si se agota o la integracion no converge.

La tolerancia se aplica al area, por lo que x puede diferir de los cinco
decimales impresos en la tabla, especialmente en las colas. Las pruebas
comprueban el error de area y, por separado, la proximidad a la tabla.
Se mantiene tambien la tolerancia interna de Simpson de 5A (0.00001).
La integracion numerica y la busqueda tienen limites finitos: valores
extremos cercanos a 0.5 pueden terminar con un error de convergencia.

## Jenkins

Se aplica la seccion Jenkins de `1a.pptx`, diapositiva 48, siguiendo
https://www.jenkins.io/doc/tutorials/build-a-java-app-with-maven/.
El pipeline contiene Build, Test y Deliver, publica JUnit, archiva JaCoCo,
el JAR y las salidas de los tres casos. Deliver ejecuta el programa; no
requiere desplegar un servidor web.

1. Iniciar Docker Desktop y esperar a que su motor este listo.
2. En PowerShell, desde la carpeta MedicionDeSoftware:

   ```powershell
   cd jenkins/quickstart-tutorials
   docker compose --profile maven up -d
   docker compose ps
   ```

3. Abrir http://localhost:8080. En el entorno del tutorial se usa admin/admin.
4. Crear un item Pipeline llamado `Programa6A`.
5. Seleccionar **Pipeline script from SCM**, SCM **Git**, URL del repositorio
   que contenga este proyecto y la rama correspondiente (`*/main` o `*/master`).
6. Si se publica solamente el contenido de 6A en la raiz del repositorio,
   indicar **Script Path: Jenkinsfile**. El pipeline espera pom.xml en esa raiz.
   La URL del repositorio 5A existente no contiene automaticamente estos archivos:
   primero debe incorporarse 6A al repositorio elegido.
7. Guardar y seleccionar **Build Now**. El agente Linux debe tener `mvn` y
   Java >=17 en PATH; el perfil Maven del tutorial proporciona ese agente.
8. Comprobar Build, Test y Deliver en verde, resultados de pruebas y artifacts.

Para un repositorio que contenga las carpetas 5A y 6A, usar Script Path
`6A/Jenkinsfile` y envolver los pasos de cada etapa en `dir('6A')`, incluyendo
los pasos post que publican reportes. La configuracion incluida esta preparada
para publicar 6A como proyecto independiente, igual que 5A.

No se agrega Gitlab ni un workflow nuevo de GitHub Actions: el complemento
solicitado para esta actividad es Jenkins. El PDF incluye formatos PSP2.1;
los tiempos personales, estimaciones historicas y registros de defectos deben
completarse con datos reales, no inferirse de una ejecucion de Maven.

## Evidencias para entregar

- Codigo y Jenkinsfile versionados en el repositorio elegido.
- Captura de la ejecucion Jenkins con las tres etapas.
- Resultados JUnit y reporte JaCoCo generado.
- Salidas de los tres casos y comparacion con la tabla del PDF.
- Si el docente exige el paquete PSP2.1 completo, completar los formatos
  enumerados en la pagina 14 del PDF con los registros personales.

La ejecucion local de Maven no sustituye una ejecucion real del job Jenkins.
