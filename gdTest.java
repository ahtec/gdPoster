import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.HTMLFilter;

@MultipartConfig
public class gdTest extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        ResourceBundle rb = ResourceBundle.getBundle("LocalStrings",request.getLocale());

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");

        String title = rb.getString("requestparams.title");
        title = "gdTest servlet";
        out.println("<title>" + "gdTest servlet " + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

        out.println("<a href=\"../reqparams.html\">");
        out.println("<img src=\"../images/code.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"view code\"></a>");
        out.println("<a href=\"../index.html\">");
        out.println("<img src=\"../images/return.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"return\"></a>");

        out.println("<h3>" + title + "</h3>");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String source = request.getParameter("source");
        out.println(rb.getString("requestparams.params-in-req") + "<br>");
        if (firstName != null || lastName != null) {
            out.println(rb.getString("requestparams.firstname"));
            out.println(" = " + HTMLFilter.filter(firstName) + "<br>");
            out.println(rb.getString("requestparams.lastname"));
            out.println(" = " + HTMLFilter.filter(lastName));
	    	out.println("<br>");
            out.println("file ");
            out.println(" = " + HTMLFilter.filter(source));
	    	out.println("<br>");
		try{
					File outFile = new File("/opt/tomcat/webapps/examples/servlets/images/","eruit.png");
//	            	File testFile = new File(source);
	            	Part filePart = request.getPart("source");
	            	String filename = filePart.getName();
	            	FileOutputStream outStream = new FileOutputStream(outFile);
	            	InputStream inputStream = filePart.getInputStream();
	            	byte[] buf = new byte[10 * 1024];
	            	int len;
	            	while ((len = inputStream.read(buf)) > 0) {
		            	outStream.write(buf,0,len);
//	            		for (int i = 0; i < len; ++i ) {
//							out.println("\n buf =");
//			            	out.println(buf[i] );
//	            		}
	            	
	            	}
					inputStream.close();	
					outStream.close();            	


	            	
//	            	FileWriter fw = new FileWriter(outFile);
//	            	fw.write("firstename was ");
//	            	fw.write(firstName);
//	            	fw.write("\nlastname was ");
//	            	fw.write(lastName);
//	            	
//	            	fw.close();
	            	
//		BufferedImage sourceBI = ImageIO.read(testFile);
//		ImageIO.write(sourceBI, "jpg" , outFile);
//		out.println("PATH=" + testFile.getPath());    
//		out.println("outfile=" + outFile.getPath());    
		out.println("<img src=\"../images/eruit.png\"  " +
		   "width=240 align=left border=0 alt=\"view code\"></a>");			        
		} catch (IOException ex ){
		ex.printStackTrace();    }



        } else {
            out.println(rb.getString("requestparams.no-params"));
        }
        out.println("<P>");
        out.print("<form action=\"");
        out.print("gdTest\" ");
        out.println(" enctype='multipart/form-data'");
        out.println(" method=POST>");
        out.println(rb.getString("requestparams.firstname"));
        out.println("<input type=text size=20 name=firstname>");
        out.println("<br>");
        out.println(rb.getString("requestparams.lastname"));
        out.println("<input type=text size=20 name=lastname>");
        out.println("<br>");
        out.println("<input name=source type=file>");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}
