#include <UnoWiFiDevEd.h>



#define CONNECTOR "mqtt"
#define TOPIC "/arduino/analog/A1"
//#define SERVER_ADDR   "192.168.0.103" // change ip address with your server ip address


  int sensor_pin = A0;
  int output_value ;
  int soilPower = 7;//Variable for Soil moisture Power

void setup() {

  Serial.begin(9600);
  Serial.println("Reading From the Sensor ...");
  pinMode(soilPower, OUTPUT);//Set D7 as an OUTPUT
  digitalWrite(soilPower, LOW);//Set to LOW so no power is flowing through the sensor
  Ciao.begin();
}


void loop(){
    digitalWrite(soilPower, HIGH);//turn D7 "On"
    delay(10);//wait 10 milliseconds  
   output_value = analogRead(sensor_pin);//Read the SIG value form sensor 
    digitalWrite(soilPower, LOW);//turn D7 "Off"
   // output_value= map(output_value, 0, 890, 0, 100);

   Serial.print("Moisture : ");

   Serial.print(output_value);

   Serial.println("%");


   delay(5000);
   int data_value = analogRead(sensor_pin); // data value from analog pin 1
   Ciao.print("Moisture :");
   Ciao.print(String(output_value));
   Ciao.println("");
   Ciao.write(CONNECTOR, TOPIC, String(data_value)); // pushes data into a channel called PIN_A1

}
