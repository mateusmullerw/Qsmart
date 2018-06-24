#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <SoftwareSerial.h>


const char password[] = "12345678";
const char ssid[] = "Qsmart" ;


String header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
String html_1 = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'/><meta charset='utf-8'><style>body {font-size:140%;} #main {display: table; margin: auto;  padding: 0 10px 0 10px; } h2,{text-align:center; } .button { padding:10px 10px 10px 10px; width:100%;  background-color: #4CAF50; font-size: 120%;}</style><title>Serial</title></head><body><div id='main'>";
String html_msg = "";
String html_4 = "</div></body></html>";

WiFiServer server(80);
SoftwareSerial ESPserial(13, 15);
/*
//String separator
String getValue(String data, char separator, int index)
{
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;

    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}*/

void sendToArduino(String ciclo){
  ESPserial.write(ciclo.c_str());
  html_msg = ciclo + "<br><br>";
  Serial.println(ciclo);
  }

void setup() {
  ESPserial.begin(115200);
  Serial.begin(115200);
    Serial.println();
    Serial.print("Configuring access point...");
    WiFi.softAP(ssid, password);

    IPAddress myIP = WiFi.softAPIP();
    Serial.print("AP IP address: ");
    Serial.println(myIP);
    server.begin();
    Serial.println("HTTP server started");
}

void loop() {
  String request;
  String comando;
  String str1;
    WiFiClient client = server.available();
    if (!client)  {  return;  }
 
    request = client.readStringUntil('\r');
    Serial.println(request);
    sendToArduino(request);
    /*comando = getValue(request,'&', 1);
    Serial.println(comando);
     if (comando == "novo") {
      Serial.println("Case1");
      
     }else if(comando == "deleta"){
      Serial.println("Case2");
      
    }else if(comando == "start"){
      Serial.println("Case3");
      str1 = getValue(request,'&', 2);
      Serial.println(str1);
      sendToArduino(str1);
      
    }else {}*/
 
    client.flush();
    client.print( header );
    client.print( html_1 );    
    client.print( html_msg );
    client.print( html_4);

 
    delay(5);
}

