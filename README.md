# Network-Programming-APP
 
This code is presenting some advanced low level server-client data receiving and decoding techniques.

# echo()
By using UDP protocol this function after the giving request (echo specific code),  receives an echo packages (using UDP protocol) as a String which is a message showing the local time, day and the temperature that is given by a termometer on a highway in our city. 

# image()
By using UDP protocol, this function after the giving request (image specific code), receives an live image frame by a remote camera as a byte array and then saves the image to a jpg file.

# AudioDPCM()/AudioAQDPCM()
By using UDP protocol, these functions after the giving request (audio specific code), receives and decodes a byte array that includes all the samples of the audio. After that the audioplay() function plays the audio and the audiosave() saves the audio to a WAVE file. 


# telemetry
By using UDP protocol this function after the giving request (echo specific code),  receives an echo packages (using UDP protocol) as a String which is a message showing the local time, day and the temperature that is given by a termometer on a highway in our city. 

