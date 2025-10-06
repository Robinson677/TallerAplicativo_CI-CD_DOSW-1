# TallerAplicativo_CI-CD_DOSW-1

**Autor:** 
- Robinson Steven Nunez Portela


**Nombre de la rama Principal:**

- `main`

**Rama en la que se trabajara:**

- `develop`



# CASO DE ESTUDIO – MÁSTER CHEF CELEBRITY


Se nos pide crear un sitio web para un programa de comida, en cual quieren que los televidentes puedan consultar 
y aprender las recetas a lo largo de las temporadas ya realizadas. 

Los clientes también deben poder interactuar en el sitio de manera que puedan publicar sus recetas. 


---

## Pruebas de ejecución

**Requisitos:**

Compilamos el proyecto con el comando 

```bash
mvn compile
```

![alt text](docs/imagenes/compila.png)

💻 *Compilaciòn de SonarQube:*

1. Arrancamos el SonarQube

```bash
 docker run -d --name sonarqube \
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true \
  -p 9003:9000 sonarqube:latest
```

2. Vamos al localhost y generamos el token

http://localhost:9003

*token:*  sqa_783afb48be895f28e94d66b3d982f927b36c3bb6

3. Verificamos que compile

```bash
 mvn clean verify
```
4. luego se manda el analisis a SonarQube

```bash
$env:SONAR_TOKEN="squ_49d8276d8344f3e99f6e8687fc2b6191abbfe6a2" 
 mvn -U "-Dsonar.host.url=http://localhost:9003" "-Dsonar.login=$env:SONAR_TOKEN" clean verify sonar:sonar
```

5. y asi compilo y esta listo para las pruebas

![alt text](docs/imagenes/sonar.png)
