package ir.utopia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class del {

	public static void main(String []args){
		DateFormat df=new SimpleDateFormat("HH:mm:ss");
		System.out.println(df.format(new Date())); 
		
	}
}
