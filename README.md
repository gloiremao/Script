ePigeon-Run video test 
NMSL Lab. NTHU Hsinchu Taiwan
----------------
Document 
----------------
This code will run the all the test for transcoding.
Feature :s
1. Fetching informations of each videos
2. Extract raw videos from mpeg4 vidoe
3. Get original PSNR value
4. Output the metadata of input videos  

----------------
Install 
----------------
1. Need JDK Version above 1.7
2. FFmpeg tools 
3. FFprobe tools
4. x264 codec 

You need to specific the path inside the script.java 
input path ex ./
output path for raw video ex ./out/
output path for .264 video ex ./out/

Run:
javac script.java Video.java

----------------
How to use 
----------------
java script filelist.txt

filelist.txt : contain the name of input videos

ex: 

bike.mp4
walk.mp4
car.mp4
