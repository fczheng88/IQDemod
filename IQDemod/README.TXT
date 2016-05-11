------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: Radio Signal Demodulator
PURPOSE OF PROJECT: Play audio represented by IQ data streams captured from an RTL-SDR
VERSION or DATE: 11 May 2016 (Final)
HOW TO START THIS PROJECT: Run 'main' of DemodRunner
AUTHORS: Felix Zheng et al
USER INSTRUCTIONS: 
-Run the main method of DemodRunner to start the program. 
-A FileChooser will pop up and will ask the user to choose a file. Pick one of the given files (*_capture.bin), or provide your own. 
-The bandwidth of the IQ data is represented in the file names given in the second data group (e.g. 0.25e6 or 2.048e6), or, provide your own. 
-The bandwidth of the signal is around 0.20e6 for broadcast FM (88-108 MHz) and usually 0.05e6 for other FM sources. 
-The audio sample rate is flexible, and usually any rate from 8192 to 192000 is fine. Most systems like 44100.

OVERVIEW AND EXPLANATION:
This project was written in an attempt to replicate the inner workings of many other demodulation programs using Java. Other applications take the IQ data from an RTL-SDR dongle and can apply various demodulation algorithms to create sound or data output. Large amounts of research were required to gain an understanding of how 'I' and 'Q' compenents represent signals.

This project focused on the demodulation of FM signals given an IQ data file. It takes a raw capture file obtained from the rtl_sdr program, and parses the stream to obtain the I and the Q data, represented by 8 bit unsigned bytes. 'I' data represents the real element of the signal, and 'Q' data represents the imaginary element. Essentially, 'I' and 'Q' represent a point at I+Qi. 

Using the I+Qi data, the program finds the angle represented by the I+Qi coordinate by using the arctan function. It then "unwraps" the phase angles so that every angle is within PI of each other to ensure continuity. The data is then decimated, upscaled, and cast to bytes to be ready for transfer to the audio class. 

The raw audio data represented in the byte array is then smoothed by the audio class to attempt to reduce some high frequency noise, and a graph is created to represent the data. At this point, the audio is ready to be played, and clicking on the "play" button will cause the audio class to create a new audio format based on a couple parameters, and play the audio data.

Although the audio isn't completely clear, it is recognizable and understandable. Several factors may play into this:
-The program may lose some precision and audio data while casting to and from different data types. 
-The program has minimal signal processing and noise reduction
-The program cannot adjust the data given to it by increasing or decreasing gain of the stick itself
-The IQ data given may be off the center frequency due to the drift of the cheap crystal oscillators in the RTL-SDRs


This project shows that I know how to:
-turn complex algorithms into java code
-choose and handle data structures
-write my own functions based on descriptions of other functions
-write my own functions based on need
-modify existing code to meet the needs of the project
-separate classes and methods to follow the OOP model
-document and comment on written code
-collaborate and implement the recommendations of others
-debugging
-and more!



