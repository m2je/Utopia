package ir.utopia.core.menu;

import ir.utopia.core.ServiceFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: MenuServlet
 *
 */
 public class MenuServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
     
   private static byte[] content;
	{
		URL url=ServiceFactory.class.getClassLoader().getResource("ir/utopia/core/menu/deluxe-tree.js");
		File f=new File(url.getPath());
		content=new byte[(int)f.length()+1];
		byte [] buf=content.length>1024?new byte[1024]:new byte[content.length];
		try {
			FileInputStream fin=new FileInputStream(url.getFile());
			int start=0;
			while(true){
				int size=fin.read(buf);
				if(size==-1)break;
				System.arraycopy(buf, 0, content, start, size); 
				start+=buf.length;
//				start=
			}
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public MenuServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/javascript");
		response.getOutputStream().write(content);
		response.flushBuffer();
		
	}   	
	
	
}