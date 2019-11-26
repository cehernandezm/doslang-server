<h1 align="center">
  <br>
  <a></a>
  <br>
 DOSLANG
  <br>
</h1>

<h4 align="center">Doslang(Server) es una aplicacion desarrollada en java. Permite traducir un lenguaje de alto nivel(Pascal) en uno de bajo nivel(Cuadruplos).</h4>


  [![GitHub issues](https://img.shields.io/github/issues/Naereen/StrapDown.js.svg)](https://github.com/wolfghost9898/doslang-server/issues) [![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://lbesson.mit-license.org/)  

<p align="center">
  <a href="#instalar">Instalar</a> •
  <a href="#librerias">Librerias</a>•
  <a href="#como-funciona">Como Funciona</a> •
  <a href="#license">Licencia</a>•
  <a href="#soporte">Soporte</a>
</p>

![server](https://user-images.githubusercontent.com/20384738/69673822-d3878400-1060-11ea-8df0-1075f29fe538.png)


## Instalar

Para clonar esta aplicacion necesitas tener instalado [Git](https://git-scm.com) and [Netbeans v11.1](https://netbeans.org/)

```bash
# Clonar el repositorio
$ git clone https://github.com/wolfghost9898/doslang-server

# Entrar a la carpeta
$ cd doslang-server

```

Nota: Para ejecutar el proyecto es necesario importarlo con netbeans. 



## Como Funciona
>La comunicacion de servidor-cliente se realiza a traves de sockets con la parte del [cliente](https://github.com/wolfghost9898/doslang-client)
>El cliente envia en formato JSON un conjunto de archivos a analizar.
>La aplicacion lo analiza lexicamente y sintacticamente, si hay un error en esta parte de la ejecucion se procede a regresar un JSON con los errores.
>Se procede a generar el codigo de bajo nivel(cuadruplos) y a su vez se analiza semanticamente. Si existe algun error se reporta a traves de un JSON y si el analisis se completo exitosamente se retorna un texto plano.
>El cliente puede solicitar un reporte de las variables analizadas, la aplicacion retornara un JSON con los resultados

## Librerias
* [Engine.io](https://github.com/socketio/engine.io-client-java)
* [json](https://jar-download.com/artifacts/org.json)
* [okhttp](https://square.github.io/okhttp/)
* [okio](https://github.com/square/okio) 
  [socket.io-java-client](https://github.com/Gottox/socket.io-java-client)
* [JFlex](https://jflex.de/) Analizador Lexico
*   [Cup](http://www2.cs.tum.edu/projects/cup/install.php) Analizador Sintactico




## License
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**

## Soporte


- Twitter at <a href="https://twitter.com/cehernandezz" target="_blank">`@cehernandezz`</a>

---

