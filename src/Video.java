
public class Video {
	public int id;
	public String filename;
	public String width;
	public String height;
	public String frame;
	public String biterate;
	public double duration;
	public String size;
	public double psnr;
	public String type;
	
	public void setResolution(String w,String h){
		this.width = w;
		this.height = h;
	}
	
	public String getMetadata(){
		return "Video:"+this.filename+" "+this.width+"x"+this.height+" Duration"+this.duration+" bitrate:"+this.biterate+" size:"+this.size+" original psnr:"+this.psnr;
	}
	
	public String getInfo(){
		return this.filename +"\t"+this.duration+"\t"+this.biterate+"\t"+this.size+"\tn"+"\t"+this.psnr;
	}
	
}