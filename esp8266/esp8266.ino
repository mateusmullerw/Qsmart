#include <SoftwareSerial.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
// Definição dos pinos do motor de passo
const int stepPin = 5;
const int dirPin = 4                            ; 
//
const char password[] = "12345678";
const char ssid[] = "Qsmart" ;

//String html_1 = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'/><meta charset='utf-8'><style>body {font-size:140%;} #main {display: table; margin: auto;  padding: 0 10px 0 10px; } h2,{text-align:center; } .button { padding:10px 10px 10px 10px; width:100%;  background-color: #4CAF50; font-size: 120%;}</style><title>Serial</title></head><body><div id='main'>";
String response = "";
//String html_4 = "</div></body></html>";

String sendApp;
String getApp;
String sendQsmart;
String getQsmart;

WiFiServer server(80);
SoftwareSerial Phone(13,15); // Phone(RX, TX)

// FUNÇÕES

//Separa string por um char separador e um index-------getValue
String getValue(String data, char separator, int index){
  
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
}

// Envia para o celular pela serial e USB -------------sendToPhone
void sendToPhone(String request){
  Phone.print(request);
  Serial.println("enviado para celular: " + request);
}

//-----------------------------------------------------trataResposta

//Trata a resposta recebida do celular(formato: "%&comando&val1&val2&val3&...&fim%")
void trataRequest(String request){
  
  String comando;
  
  String tipo;
  String zmolho;
  String centrifugar;
  String zsecar;
  String temperatura;
  int temp;
  String nivel;

  String mensagem;

  String luz;
  comando = getValue(request,'&', 1);
    Serial.println("Comando: "+comando);
    if (comando == "start") {
      Serial.println("Case1");
      tipo = getValue(request,'&', 2);
      zmolho = getValue(request,'&', 3);
      centrifugar = getValue(request,'&', 4);
      zsecar = getValue(request,'&', 5);
      temperatura = getValue(request,'&', 6);
      nivel = getValue(request,'&', 7);
  
      if(temperatura == "1"){
        temp = 1;
      }else if(temperatura == "2"){
        temp = 2;
      }else if (temperatura == "3"){
        temp = 3;
      }
      
      iniciaCiclo(tipo, zmolho, centrifugar, zsecar, temp, nivel);
    
    }else if(comando == "luz"){
      Serial.println("Case2");
      luz = getValue(request,'&', 2);
   
    }else{
      mensagem = getValue(request, '&', 2);
      if(comando == "sap"){
        sendApp = mensagem;
        Serial.println("sendApp = "+mensagem);
      }else if(comando == "sqs"){
        sendQsmart = mensagem;
        Serial.println("sendQsmart = "+mensagem);
      }else {Serial.println("Else");}
    }
  
}
//-----------------------------------------------------iniciaCiclo
//Verifica cada parametro pra enviar o tempo certo pra cada função, executa as funções(ou não) na ordem certa;
void iniciaCiclo(String tipo, String zmolho, String centri, String zsecar, int temp, String nivel){
  
  //Enche
  if(nivel == "0"){fill(5);}else if(nivel == "1"){fill(3);}else if(nivel == "2"){fill(5);}else {fill(8);}
  //Molho
  if(zmolho == "1"){molho(10);}else if(zmolho == "2"){molho(15);}else if(zmolho = "3"){molho(20);}
  //Lava
  if(tipo == "1"){lavar(20);}else if(tipo == "2"){lavar(30);}else if(tipo = "3"){lavar(40);}
  //Centrifuga
  if(centri == "1"){centrifugar(35,500);}else if(centri == "2"){centrifugar(25, 750);}else if(centri = "3"){centrifugar(15, 1000);}
  //Seca
  if(zsecar == "1"){secar(20, temp);}else if(zsecar == "2"){secar(40, temp);}else if(zsecar = "3"){secar(60, temp);}
}

//-----------------------------------------------------Fill -mudar os LEDs etc
void fill(int tempo){
  
  int i = 0;
  while(i < tempo){
      digitalWrite(13,HIGH);
      delay(500);
      digitalWrite(13,LOW);
      delay(500);
  }
}  
//-----------------------------------------------------Molho - mudar os LEDs etc
void molho(int tempo){
  
  int i = 0;
  while(i < tempo){
      digitalWrite(13,HIGH);
      delay(500);
      digitalWrite(13,LOW);
      delay(500);
  }
} 
//-----------------------------------------------------Lavar
void lavar(int tempo) {
  
  Serial.println("LAVAR");
  digitalWrite(dirPin,HIGH); //Nossa máquina não faz variação de giro, logo deixar esse pino no HIGH é só protocolar mesmo
  int i = 0;
  while (i!=(tempo/0,05)){ //O calculo do tempo é feito em segundos, no caso com um passo de 5ms (quando maior o delay, menor a velocidade do giro), fica o tempo/0,05s = número de passos necessários;
    digitalWrite(stepPin, HIGH);
    delayMicroseconds(2500);
    digitalWrite(stepPin,LOW);
    delayMicroseconds(2500);
    i++;
  }
  i=0;
}
   
// ----------------------------------------------------Centrifugar
void centrifugar(int tempo, int RPM){
  
  Serial.println("CENTRIFUGAR");
  digitalWrite(dirPin, HIGH);
  int dly = 0; //variavel do delay do passo
  //Bloco de definição do RPM, cálculo pré-realizado;
  if (RPM == 500){ dly=1000;}
  else if (RPM == 750) {dly = 650;}
  else if (RPM ==1000) {dly = 500;}
  //
  int i = 0;
  while (i != int((tempo/((dly*2)/(pow(10, -6))))))
  {
    digitalWrite(stepPin, HIGH);
    delayMicroseconds(dly);
    digitalWrite(stepPin,LOW);
    delayMicroseconds(dly);
    i++; 
  } 
  i=0;
 }  

//-----------------------------------------------------Secar

void secar(int tempo, int temperatura) {
  
  Serial.println("SECAR");
  digitalWrite(dirPin, HIGH);
  digitalWrite(13, HIGH); //liga o led para simular temperatura//Talvez colocar um led RGB que vai de azul a vermelho conforme a temperatura
  int i=0;
  while (i!=(tempo/0,03))
  {
    digitalWrite(stepPin, HIGH);
    delayMicroseconds(1500);
    digitalWrite(stepPin, LOW);
    delayMicroseconds(1500);
    i++;
  }
  i=0;
}
//-----------------------------------------------------Setup
void setup() {
  
   Phone.begin(115200);
//************Acho que pra funcionar com android tem que ser 9600*************************
  Serial.begin(115200);
    Serial.println();
    Serial.print("Configuring access point...");
    WiFi.softAP(ssid, password);

    IPAddress myIP = WiFi.softAPIP();
    Serial.print("AP IP address: ");
    Serial.println(myIP);
    server.begin();
    Serial.println("HTTP server started");
  
  pinMode(stepPin,OUTPUT); 
  pinMode(dirPin,OUTPUT);
  pinMode(13,OUTPUT); //Precisamos setar os leds da fita e ver quantos pinos serão utilizados
  
  //while (!Serial){;}  //Protocolar pra porta USB /// ?? O que isso faz?
  
}
//-----------------------------------------------------Main
void loop() {
  
  String request;
  String resposta;
  String comando;

    WiFiClient client = server.available();
    if (!client)  {  return;  }
 
    request = client.readStringUntil('\r');
    if(request != "" || request != " "){
      request = getValue(request, '%', 1);
      Serial.println("Request: "+request);
      trataRequest(request);
      //sendToPhone(request);
      comando = getValue(request, '&', 1);
      Serial.println(comando);
      if(comando == "gap"){
      response = sendQsmart;
      Serial.println("resp send Qsmart = "+sendQsmart);
      }else if(comando == "gqs"){
      response = sendApp;
      Serial.println("resp sendApp = "+sendApp);
      }else { response = "nuthing";}
    
      //response = comando;
    }
    client.flush();
    //client.print( header );
    //client.print( html_1 );    
    client.print( response );
    //client.print( html_4);
    delay(5);
  

  /*if (Phone.available()){
    resposta = Phone.readString();
    Serial.println("Resposta: " + resposta);
    trataResposta(resposta);
   }*/
  
}

