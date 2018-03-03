import processing.serial.*;
Serial mySerial;
PrintWriter output;
String val;     // Data received from the serial port
void setup() {
   String portName = Serial.list()[1];
   mySerial = new Serial( this, portName, 9600 );
   output = createWriter("data.txt" );
}
void draw() {
    if (mySerial.available() > 0 ) {
         String value = mySerial.readString();
         if ( value != null ) {
              output.println( value );
         }
    }
}

void keyPressed() {
    output.flush();  // Writes the remaining data to the file
    output.close();  // Finishes the file
    exit();  // Stops the program
}