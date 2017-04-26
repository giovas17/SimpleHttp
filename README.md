# SimpleHttp

SimpleHttp es una libreria que te ayudara a realizar llamadas Http Restful de manera muy sencilla para Android.

SimpleHttp is a library that will help you to make Http Restful in easy way for Android.

## Descarga / Download
Incluye la siguiente dependecia en tu archivo `build.gradle`. **Por favor utiliza la última version disponible.**

Include the following dependency in your `build.gradle` file. **Please use the latest version available.**

#### Gradle
```gradle
repositories{
    jcenter();
}

dependencies{
    compile 'com.softwaremobility:simplehttp:1.0.4'
}
```

#### Maven
```xml
<dependency>
  <groupId>com.softwaremobility</groupId>
  <artifactId>simplehttp</artifactId>
  <version>1.0.4</version>
  <type>pom</type>
</dependency>
```

#### Ivy
```Ivy
<dependency org='com.softwaremobility' name='simplehttp' rev='1.0.4'>
  <artifact name='simplehttp' ext='pom' ></artifact>
</dependency>
```

### Permisos / Permissions
Agrega los permisos al archivo `AndroidManifest.xml`

Add permissions in `AndroidManifest.xml` file

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

## Uso / Usage
### Definiendo Ambiente / Defining Environment
Para definir tu url base para las llamadas se ocupan dos metódos principales `testPath(String url_base)` y `productionPath(String url_base)` , al menos uno de ellos debe definirse antes de poder hacer una llamada.  

To define you base url for the calls you can use two methods `testPath(String url_base)` and `productionPath(String url_base)` , at least one of them must be called before trying to make http calls.

- Ejemplo / Example:
```java
    NetworkConnection.testPath("http://54.190.16.14:3001/api");
    NetworkConnection.productionPath("http://example/api");
```
>**Nota:** Por default se toma la url de test, pueden usar ambas y en base a el `Build Variant` que se este usando (debug - test, release - production). No es necesario llamar a estos métodos cada vez que se quiera hacer una llamada Http, pero al menos una vez debe ser llamado alguno de los dos.

>**Note:** By default the test url is taken, you can use both methods at the same time and according to `Build Variant` that is used (debug - test, release - production). Is not necessary to call this methods every time that you need to make Http calls, but at least one of them must be call it.

### Llamadas Http / Http calls
Una llamada simple necesita del endpoint, el tipo de llamada, parametros y encabezados.

In order to make a simple http call an endpoint is needed as type of call, parameters and headers.

```java
Uri endpoint = Uri.parse("airlines");

NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
    @Override
    public void onSuccessfullyResponse(String response) {
        // code when call is successfully
    }
       
    @Override
    public void onErrorResponse(String error, String message, int code) {
        // code if error happened. 
    }
}).doRequest(Connection.REQUEST.GET,endpoint,null,null,null);
```

Esta llamada se hace ocupando la url puesta con `testPath(String path)` ó `productionPath(String path)` añadiendose el endpoint.

This call uses the base url set by `testPath(String path)` or `productionPath(String path)` and adding the endpoint.

- Suponiendo que la llamada se hizo ocupando test la url final es: `http://54.190.16.14:3001/api/airlines`.
- Assuming that test is used the final url is: `http://54.190.16.14:3001/api/airlines`.

### Agregando parámetros / Adding paramenters
Los parámetros y encabezados se agregan utilizando un objeto `Map<String, String>`

The parameters and headers are added using an object `Map<String, String>`
#### GET

```java
        Uri endpoint = Uri.parse("airlines");
        Map<String, String> params = new HashMap<>();
        params.put("name","aeromexico");
        params.put("country","Mexico");
        params.put("searchTerm", "The airline that begins with aero");

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {
                // code when call is successfully
            }

            @Override
            public void onErrorResponse(String error, String message, int code) {
                // code if error happened.
            }
        }).doRequest(Connection.REQUEST.GET,endpoint,params,null,null);
```
* Url generada / Url generated: `http://54.190.16.14:3001/api/airlines?name=aeromexico&searchTerm=The%20airline%20that%20begins%20with%20aero&country=Mexico`

#### POST / PUT / PATCH

```java
        Uri endpoint = Uri.parse("airlines");
        Map<String, String> params = new HashMap<>();
        params.put("name","aeromexico");
        params.put("country","Mexico");
        params.put("searchTerm", "The airline that begins with aero");

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {
                // code when call is successfully
            }

            @Override
            public void onErrorResponse(String error, String message, int code) {
                // code if error happened.
            }
        }).doRequest(Connection.REQUEST.POST,endpoint,params,null,null);
```
* Url generada / Url generated: `http://54.190.16.14:3001/api/airlines`

Los parametros son codificados en la url por eso no se muestran en la url final

The parameters are encoded in the url for that reason are not showed in the final url


### Metódos Http soportados / Supported Http methods

SimpleHttp usa un enum de la clase "Connection" para poder definir que metodo Http se desea ocupar

SimpleHttp uses an enum of the class "Connection" in order to define what Http method you want to use.

* GET    - Connection.REQUEST.GET
* POST   - Connection.REQUEST.POST
* PUT    - Connection.REQUEST.PUT
* PATCH  - Connection.REQUEST.PATCH
* DELETE - Connection.REQUEST.DELETE

#### Developed By
Giovani González - <darkgeat@gmail.com>

## License

Copyright (C) 2017 - 2020, Giovani González and Software Mobility, Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

*   Redistributions of source code must retain the above copyright notice, this     
    list of conditions and the following disclaimer.

*   Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

*   Neither the name of Software Mobility nor the names of its
    contributors may be used to endorse or promote products derived from
    this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.