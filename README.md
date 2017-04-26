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
```maven
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

```permissions
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
Uri uri = Uri.parse("airlines");

NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
    @Override
    public void onSuccessfullyResponse(String response) {
        // code when call is successfully
    }
       
    @Override
    public void onErrorResponse(String error, String message, int code) {
        // code if error happened. 
    }
}).doRequest(Connection.REQUEST.GET,uri,null,null,null);
```