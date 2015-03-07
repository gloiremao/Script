/*
 * Script for transcoding model
 * To get info from video
 * 
 * 
 * */
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class script {

	public static Runtime r = Runtime.getRuntime();
	public static String stdout;
	public static String[] cmd = {"/bin/sh","-c",""};
	public static ArrayList<Video> video_list = new ArrayList<Video>();

	public static void main(String[] args) throws IOException {
		
		String path = "~/../rajiv/MoreAJVideos/";
		String outpath = "//data5/MoreAJvideos/";
		
		if(args.length == 1){
			//read the video list
			String filename = args[0];
			File list = new File(filename);
			BufferedReader existed = new BufferedReader(new FileReader(list));	
			String fname;
			int index = 0;
			
			while((fname = existed.readLine()) != null){
				Video v = new Video();
				v.filename = fname;
				v.id = index;
				video_list.add(v);
				index++;
			}
			
			existed.close();
			
			
			//Get info. of the videos using ffprobe(ffmpeg)
			getInfo(path);
			
			//mp4 -> raw video
			convert(path,outpath);
			
			
			//Compute PSNR
			getOriginalPSNR(outpath);
			
			//compute TI/SI
			//getTISI();
			
			
			//write to info
			PrintWriter txt = new PrintWriter("MoreAJinfo.txt");
			for(Video v : video_list){
				txt.println(v.getInfo());
			}
			txt.close();
			
		}else {
			System.out.println("File not founded!");
		}
		
	}
	
	private static void getTISI() {
		// TODO Auto-generated method stub
		/*System.out.println("Compute TI/SI");
		double ti;
		double si;
		PrintWriter tisi = new PrintWriter("AJ_tisi.txt","UTF-8");
		for(int i = 0;i <= video_list.size();i++){
			
			//u need to change the output and input 
			//octave --eval "pkg load all;SI=SI('input.yuv',[width height])" 2>&1 | grep SI
			cmd[2] = "octave --eval \"pkg load all;SI=SI('//data5/AJrawvideo/AJ_"+i+".yuv',["+video_list.get(i).width+" "+video_list.get(i).height+"])\" 2>&1 | grep SI";
			sys_exec(cmd);
			stdout = stdout.replaceAll(" ", "");
			String tsinfo[] = stdout.split("=");
			si = Double.parseDouble(tsinfo[1]);
			tsinfo = null;
			//octave --eval "pkg load all;TI=TI('input.yuv',[width height])" 2>&1 | grep SI
			cmd[2] = "octave --eval \"pkg load all;TI=TI('//data5/AJrawvideo/AJ_"+i+".yuv',["+video_list.get(i).width+" "+video_list.get(i).height+"])\" 2>&1 | grep \"TI =\"";
			sys_exec(cmd);
			stdout = stdout.replaceAll(" ", "");
			tsinfo = stdout.split("=");
			ti = Double.parseDouble(tsinfo[1]);
			tisi.println(i+": TI="+ti+" SI="+si);
		}
		tisi.close();*/
	}

	private static void getOriginalPSNR(String outpath) {
		// TODO Auto-generated method stub
		System.out.println("Compute original psnr with x264 ...");
		
		for(int i = 0;i < video_list.size() ;i++){
			if(video_list.get(i).width != null && video_list.get(i).height != null){
				cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate "+video_list.get(i).biterate+" --psnr -o //data5/MoreAJvideos/video_"
						+video_list.get(i).id+".264 //data5/MoreAJvideos/video_"+video_list.get(i).id+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
				sys_exec(cmd);
				String[] tmp = stdout.split(":");
				video_list.get(i).psnr = Double.parseDouble(tmp[1]);
				tmp = null;
				
			}else {
				System.out.println("Fail at "+video_list.get(i).filename);
			}
		}
	}
	
	private static void getPSNRArray(){
		/*String[] psnr = new String[7];
		cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate 50 --psnr -o //data5/AJ264/AJm_5_"
		+video_list.get(i).id+".264 //data5/AJrawvideo/AJ_"+video_list.get(i).id+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
		sys_exec(cmd);
		String[] tmp = stdout.split(":");
		psnr[0] = tmp[1];
		tmp = null;
		cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate 100 --psnr -o //data5/AJ264/AJm_1_"
				+i+".264 //data5/AJrawvideo/AJ_"+i+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
		sys_exec(cmd);
		tmp = stdout.split(":");
		psnr[1] = tmp[1];
		tmp = null;
		cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate 200 --psnr -o //data5/AJ264/AJm_2_"
				+i+".264 //data5/AJrawvideo/AJ_"+i+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
		sys_exec(cmd);
		tmp = stdout.split(":");
		psnr[2] = tmp[1];
		tmp = null;
		cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate 400 --psnr -o //data5/AJ264/AJm_4_"
				+i+".264 //data5/AJrawvideo/AJ_"+i+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
		sys_exec(cmd);
		tmp = stdout.split(":");
		psnr[3] = tmp[1];
		tmp = null;
		cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate 600 --psnr -o //data5/AJ264/AJm_6_"
				+i+".264 //data5/AJrawvideo/AJ_"+i+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
		sys_exec(cmd);
		tmp = stdout.split(":");
		psnr[4] = tmp[1];
		tmp = null;
		cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate 800 --psnr -o //data5/AJ264/AJm_8_"
				+i+".264 //data5/AJrawvideo/AJ_"+i+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
		sys_exec(cmd);
		tmp = stdout.split(":");
		psnr[5] = tmp[1];
		tmp = null;
		cmd[2] = "x264 --input-res "+video_list.get(i).width+"x"+video_list.get(i).height+" --bitrate 10000 --psnr -o //data5/AJ264/AJ_10_"
				+i+".264 //data5/AJrawvideo/AJ_"+i+".yuv 2>&1 | grep \": PSNR\" | awk '{print $5}'";
		sys_exec(cmd);
		tmp = stdout.split(":");
		psnr[6] = tmp[1];
		tmp = null;
		psnrtxt.println(i+": "+psnr[0]+" "+psnr[1]+" "+psnr[2]+" "+psnr[3]+" "+psnr[4]+" "+psnr[5]+" "+psnr[6]);*/
	}

	private static void convert(String path, String output_path) {
		// TODO Auto-generated method stub
		System.out.println("Start converting ...");
		for(int i = 0;i < video_list.size();i++){
			//u need to specify the path 
			//ffmpeg -i input file -vcodec rawvideo -pix_fmt yuv420p output.yuv
			cmd[2] = "ffmpeg -i "+path+"\""+video_list.get(i).filename+"\" -vcodec rawvideo -pix_fmt yuv420p "+output_path+"video_"+video_list.get(i).id+".yuv";
			sys_exec(cmd);
		}
	}

	private static void getInfo(String path) throws IOException {
		// TODO Auto-generated method stub
		File metadata = new File("metadata_"+System.currentTimeMillis()+".txt");
		PrintWriter mp = new PrintWriter(metadata);
		System.out.println("Get information for encoing ...");
		for(int i = 0 ;i < video_list.size() ; i++){
			cmd[2] = "ls -lh "+path+"\""+video_list.get(i).filename+"\"";
			sys_exec(cmd);
			if(stdout != null){
				String tmp[] = stdout.split(" ");
				if(tmp.length > 0)video_list.get(i).size = tmp[4];
			}else {
				System.out.println("Failed to get metadata (FFprobe)");
			}
			
			cmd[2] = "ffprobe -show_streams "+path+"\""+video_list.get(i).filename+"\" 2>&1 | grep width= | head -1";
			sys_exec(cmd);
			if(stdout == null)continue;
			String tmp_w[] = stdout.split("=");
			if(tmp_w.length == 2)video_list.get(i).width = tmp_w[1];
			cmd[2] = "ffprobe -show_streams "+path+"\""+video_list.get(i).filename+"\" 2>&1 | grep height= | head -1";
			sys_exec(cmd);
            String tmp_h[] = stdout.split("=");
            if(tmp_h.length == 2)video_list.get(i).height = tmp_h[1];
            cmd[2] = "ffprobe -show_streams "+path+"\""+video_list.get(i).filename+"\" 2>&1 | grep bitrate | head -1";
            sys_exec(cmd);
            String tmp_b[] = stdout.split("bitrate:");
            if(tmp_b.length == 2)video_list.get(i).biterate = tmp_b[1].replaceAll("[^0-9]", "");

            cmd[2] = "ffprobe -show_streams "+path+"\""+video_list.get(i).filename+"\" 2>&1 | grep duration= | head -1";
            sys_exec(cmd);
            
            if(stdout != null){
            	String tmp[] = stdout.split("duration=");
            	if(tmp.length == 2){
            		DecimalFormat formatter = new DecimalFormat("#.000");
            		video_list.get(i).duration = Double.parseDouble(formatter.format(Double.parseDouble(tmp[1])));
            	}
            	tmp = null;
            }else {
            	System.out.println("Failed to get the metadata");
            }
            
            String matadata = video_list.get(i).getMetadata();
            mp.println(metadata);
			System.out.println(matadata);
		}
		mp.close();
		
	}

	private static void sys_exec(String[] command) {
		// TODO Auto-generated method stub
		try {
			stdout = null;
			//System.out.println("Debug:"+command[2]);	
			Process p = r.exec(command);
			p.waitFor();
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";

			while ((line = b.readLine()) != null) {
				stdout = line;
				System.out.println(stdout);
			}

			b.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
} 