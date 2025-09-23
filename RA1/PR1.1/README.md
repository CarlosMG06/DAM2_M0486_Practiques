# Exemple de guardar dades a arxius #

En aquest projecte hi ha diversos exemples de com guardar dades en Java, cap a arxius de text, binaris, XML, CSV i objectes serialitzats

### Instruccions ###

Primer posar en funcionament el servidor

Després executar el client i comprovar com els càlculs obtenen resultat des del servidor

### Compilació i funcionament ###

Cal el 'Maven' per compilar el projecte
```bash
mvn clean
mvn compile
```

Per executar el projecte a Windows cal
```bash
.\run.ps1 com.project.Main
```

Per executar el projecte a Linux/macOS cal
```bash
./run.sh com.project.Main
```

Per fer anar classes específiques amb main:
```bash
.\run.ps1 com.project.EscripturaDadesPrimitives
./run.sh com.project.EscripturaDadesPrimitives
```

### Ordre recomanat d'estudi:

```
    GestioArxius.java

    EscripturaArxiuWriter.java
    LecturaArxiuScanner.java

    EscripturaArxiuList.java
    LecturaArxiuList.java

    EscripturaDadesPrimitives.java
    LecturaDadesPrimitives.java

    EscripturaObjectes.java
    LecturaObjectes.java

    EscripturaLlistes.java
    LecturaLlistes.java

    GestioCSV.java
    GestioXML.java
```