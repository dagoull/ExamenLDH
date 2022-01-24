<!--
Copyright 2018-2021 Crown Copyright

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# Generador de Datos Sintéticos

Se trata de un generador de datos sintéticos relacionados con los alumnos y el personal PAS de la ULL.

Con este proyecto se pueden generar una gran cantidad de datos relacionadas con la temática que decida el usuario, dividiendo toda esta
información en tantos ficheros Avro o JSON que el usuario quiera. Al igual que tener la posibilidad de definir el numero de hilos paralelos
que se usarán para generer la información.

Para la realización de las distintas tareas del proyecto se han utilizado Maven, Doxygen, SonarCloud y Jenkins

## Instalación

Para empezar clonamos el repositorio de github
```bash
git clone https://github.com/gchq/synthetic-data-generator.git
```
Luego instalamos las dependencias con Maven
```bash
mvn clean install
```

## Build

Se ha compilado un fichero JAR para ejecutar el programa, por lo que para iniciar el generador utilizamos el siguiente comando
```bash
java -jar synthetic-data-generator.jar PATH NUM_GENERACION SALIDA FILES TYPE [THREADS]
```
Donde:
- PATH es el camino relativo para el almacenamiento de los resultados
- NUM_GENERACION es el número de datos a generar
- SALIDA es el tipo de formato de salida, donde debe de ir "-avro" o "-json"
- FILES es el número de ficheros a crear
- THREADS (opcional) es el numero de hilos a ejecutar

Por ejemplo, para generar 1,000,000 de alumnos en unos 15 ficheros en formato JSON, con 4 hilos y teniendo como salida de ficheros /data:
```bash
java -jar synthetic-data-generator.jar /data 1000000 -json 15 alumno 4
```
La ejecución se ha realizado con JDK 16, recomandamos su uso para la utilización de la aplicación

## Estructura

Un objeto `Alumno` contiene la siguiente información:
```
class Employee {
    UserId uid;
    String name;
    String dateOfBirth;
    PhoneNumber[] contactNumbers;
    EmergencyContact[] emergencyContacts;
    Address address;
    BankDetails bankDetails;
    String taxCode;
    Nationality nationality;
    Profesor[] profesor;
    String entradaULLDate;
    Grade grade;
    Campus campus;
    int matriculaAmount;
    int becaBonus;
    BirthLocation birthLocation;
    Sex sex;
}
```
Mientas que un objeto `Pas` contiene la siguiente información:
```
class Employee {
    UserId uid;
    String name;
    String dateOfBirth;
    PhoneNumber[] contactNumbers;
    EmergencyContact[] emergencyContacts;
    Address address;
    BankDetails bankDetails;
    String taxCode;
    Nationality nationality;
    Mate[] mate;
    String entradaULLDate;
    Grade grade;
    Campus campus;
    int expedienteAmount;
    int productividadBonus;
    BirthLocation birthLocation;
    Sex sex;
}
```
