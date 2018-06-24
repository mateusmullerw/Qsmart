#include <SoftwareSerial.h>
// Definição dos pinos do motor de passo
const int stepPin = 3;
const int dirPin = 4; 
//

SoftwareSerial ESP(5,6); // 5 = RX ; 6 = TX
SoftwareSerial Phone(8,9); // Phone(RX, TX)

// FUNÇÕES

//Separa string por um char separador e um index-------getValue
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
}

// Envia para o celular pela serial e USB -------------sendToPhone
//Acho que a Baudrate precisa ser 9600
void sendToPhone(String request){
  Phone.print(request);
}

//-----------------------------------------------------trataResposta

//Trata a resposta recebida do celular(formato: "&comando&val1&val2&val3&" <<quantos valores for necessário)
void trataResposta(String resposta){
  
  String comando;
  
  String tipo;
  String molho;
  String centrifugar;
  String secar;
  String temperatura;
  String nivel;

  String luz;
  comando = getValue(resposta,'&', 1);
    Serial.println(comando);
     if (comando == "start") {
      Serial.println("Case1");
      tipo = getValue(resposta,'&', 2);
      molho = getValue(resposta,'&', 3);
      centrifugar = getValue(resposta,'&', 4);
      secar = getValue(resposta,'&', 5);
      temperatura = getValue(resposta,'&', 6);
      nivel = getValue(resposta,'&', 7);
      
      iniciaCiclo(tipo, molho, centrifugar, secar, temperatura, nivel);
      
     }else if(comando == "luz"){
      Serial.println("Case2");
      luz = getValue(resposta,'&', 2);
      
    }else if(comando == "start"){
      Serial.println("Case3");
      str1 = getValue(resposta,'&', 2);
      Serial.println(str1);
      sendToPhone(str1);
      
    }else {}
  
}
//-----------------------------------------------------iniciaCiclo
//Verifica cada parametro pra enviar o tempo certo pra cada função, executa as funções(ou não) na ordem certa;
void iniciaCiclo(String tipo, String molho, String centri, String secar, String temp, String nivel){
  //Enche
  if(nivel == 0){fill(5);}else if(nivel == 1){fill(3);}else if(nivel ==2){fill(5);}else {fill(8);}
  //Molho
  if(molho == 1){molho(10);}else if(molho == 2){molho(15);}else if(molho = 3){molho(20);}
  //Lava
  if(tipo == 1){lavar(20);}else if(tipo == 2){lavar(30);}else if(tipo = 3){lavar(40);}
  //Centrifuga
  if(centri == 1){centrifugar(35,500);}else if(centri == 2){centrifugar(25, 750);}else if(centri = 3){centrifugar(15, 1000);}
  //Seca
  if(secar == 1){secar(20, temp);}else if(secar == 2){secar(40, temp);}else if(secar = 3){secar(60, temp);}
}

//-----------------------------------------------------Fill -mudar os LEDs etc
void fill(tempo){
  int i = 0;
  while(i < tempo){
      digitalWrite(13,HIGH);
      delay(500);
      digitalWrite(13,LOW);
      delay(500);
  }
}  
//-----------------------------------------------------Molho - mudar os LEDs etc
void fill(tempo){
  int i = 0;
  while(i < tempo){
      digitalWrite(13,HIGH);
      delay(500);
      digitalWrite(13,LOW);
      delay(500);
  }
} 
//-----------------------------------------------------Lavar
void lavar(tempo) 
{
  Serial.println("LAVAR");
  digitalWrite(dirPin,HIGH); //Nossa máquina não faz variação de giro, logo deixar esse pino no HIGH é só protocolar mesmo
  int i = 0;
  while (i!=(tempo/0,05)); //O calculo do tempo é feito em segundos, no caso com um passo de 5ms (quando maior o delay, menor a velocidade do giro), fica o tempo/0,05s = número de passos necessários;
    digitalWrite(stepPin, HIGH);
    delayMicroseconds(2500);
    digitalWrite(stepPin,LOW);
    delayMicroseconds(2500);
    i++;
  }
  i=0;
}
   
// ----------------------------------------------------Centrifugar
void centrifugar(tempo, RPM);
  Serial.println("CENTRIFUGAR");
{
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
    delayMicroseconds(dlt);
    i++; 
  } 
  i=0;
 }  

//-----------------------------------------------------Secar

void secar(tempo, temperatura) 
  Serial.println("SECAR");
  digitalWrite(dirPin, HIGH);
{
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
void setup() 
{
  //************Acho que pra funcionar com android tem que ser 9600*************************
  Serial.begin(115200); 
  //************Acho que pra funcionar com android tem que ser 9600*************************

  
  pinMode(stepPin,OUTPUT); 
  pinMode(dirPin,OUTPUT);
  pinMode(13,OUTPUT); //Precisamos setar os leds da fita e ver quantos pinos serão utilizados
  
  while (!Serial){;}  //Protocolar pra porta USB /// ?? O que isso faz?
  
}
//-----------------------------------------------------Main
void loop() 
{
  String request;
  String resposta;
  if (ESP.available())
  {
    request = ESP.readString();
    Serial.println(request); 
    sendToPhone(request); 
  }

  if (Phone.available()){
    resposta = Phone.readString();
    Serial.println(request)
    trataResposta(resposta);
   }
  
}
