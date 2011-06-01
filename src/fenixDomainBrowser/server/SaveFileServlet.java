package fenixDomainBrowser.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	File savedFile = new File(new String(new sun.misc.BASE64Decoder().decodeBuffer(req.getParameter("filename"))));
	int                 length   = 0;
        ServletOutputStream op       = resp.getOutputStream();
        ServletContext      context  = getServletConfig().getServletContext();

        //
        //  Set the response and go!
        //
        //
        resp.setContentType( "application/octet-stream" );
        resp.setContentLength( (int)savedFile.length() );
        resp.setHeader( "Content-Disposition", "attachment; filename=\"save.fdb3\"" );

        //
        //  Stream to the requester.
        //
        byte[] bbuf = new byte[1024];
        DataInputStream in = new DataInputStream(new FileInputStream(savedFile));

        while ((in != null) && ((length = in.read(bbuf)) != -1))
        {
            op.write(bbuf,0,length);
        }

        in.close();
        op.flush();
        op.close();
    }
}
