package ir.utopia.core.utilities;

import java.util.ArrayList;
import java.util.List;

public abstract class ArrayUtils {

	public  static  List<Object []> breakArray(Object [] ids,int arraySize){
		if(ids==null)return null;
		ArrayList<Object[]>result=new ArrayList<Object[]>();
		if(ids.length<=arraySize){
			result.add(ids);
		}else{
			int size=ids.length;
			int loopCount=((size)/arraySize);
			loopCount+=(size%arraySize)>0?1:0;
			for(int i=1;i<=loopCount;i++){
				int start=(i-1)*arraySize;
				int end=i*arraySize;
				if(end>size){
					end=size;
				}
				Object []cur=new Object[end-start];
				System.arraycopy(ids, start,  cur,0, cur.length);
				result.add(cur);
			}
		}
		return result;
	}
	
	public static void main(String []args){
		Long []ids=new Long[19856];
		List<Object[]> res=ArrayUtils.breakArray(ids,10000);
		for(Object[]l:res){
			System.out.println(l.length);
		}
	}
}
