# Pràctica PR1.3 - Punt de partida #

Aquest projecte correspon a la pràctica PR1.3 relacionada amb treball amb documents XML

### Instruccions ###

Primer posar en funcionament el servidor

Després executar el client i comprovar com els càlculs obtenen resultat des del servidor

### Compilació i funcionament ###

Cal el 'Maven' per compilar el projecte
```bash
mvn clean
mvn compile
mvn clean compile test package
```

Per executar el projecte a Windows cal
```bash
.\run.ps1 com.project.pr13.PR13Main
```

Per executar el projecte a Linux/macOS cal
```bash
./run.sh com.project.pr13.PR13Main
```

Per fer anar classes específiques amb main:
```bash
.\run.ps1 com.project.pr13.PR130Main
./run.sh com.project.pr13.PR130Main
```

Per executar sense usar script propi, directament amb maven:
```bash
mvn exec:java "-Dexec.mainClass=com.project.pr13.PR13Main"
```

Per executar, un cop generat l'artefacte .jar
```bash
java -cp ./target/ams2-m0486-pr11-repo-ref-1.0.1.jar com.project.pr13.PR13Main
```

### Execució de tests ###
Per executar, un cop generat l'artefacte .jar
```bash
# Executar TOTS els tests
mvn test
# Executar un test individual especificant package o només nom del test
mvn test "-Dtest=com.project.pr13.PR130MainTest"
mvn test -Dtest=PR130MainTest
# Executar múltiples tests específics (separats per comes)
mvn test -Dtest="PR130MainTest,PR131MainTest,PR132MainTest"
# Tots els tests que comencin amb "Lectura"
mvn test -Dtest="Lectura*"
# Tots els tests que continguin "Arxiu"
mvn test -Dtest="*Arxiu*"
# Tests específics de List i Scanner
mvn test -Dtest="*List*,*Scanner*"
```

### Visual Studio Code: resseteig de l'entorn de programació Java ###

Si Visual Studio code no es comporta com esperem i hem provat a solucionar-ho sense èxit podem provar aquestes dues solucions:

* Recarregar la Finestra: Obre la Paleta de Comandes (**Ctrl+Maj+P**), escriu Developer: "**Reload Window**" i prem Enter.

* Netejar l'Espai de Treball: Si recarregar no funciona, obre de nou la Paleta de Comandes (**Ctrl+Maj+P**), escriu "**Java: Clean Java Language Server Workspace**" i prem Enter. Aquesta és una solució molt eficaç per a molts problemes relacionats amb Java a VS Code. Se't demanarà que recarreguis i tornis a escanejar el projecte.
