#include <SoftwareSerial.h>  
  
#define rxPin 6                     // define SoftwareSerial rx data pin  
#define txPin 7                    // define SoftwareSerial tx data pin  
  
SoftwareSerial blueTooth(rxPin, txPin);  // create instance of SoftwareSerial  
  
void setup()  
{  
  Serial.begin(9600);            // Start hardware Serial  
  blueTooth.begin(9600);         // Start SoftwareSerial  
}  
  
void loop()  
{  
    char c;  
     if (Serial.available())  
     {  
       c = Serial.read();  
        
       Serial.println(c);             // Write the character to the Serial Monitor  
        
       blueTooth.write (c);           // Write the character to Bluetooth  
     }  
}  
