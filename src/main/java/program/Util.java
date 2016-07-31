package program;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.cli.CommandLine;

import com.sun.xml.internal.ws.util.StringUtils;

public class Util {

	
	
	static File stream2file(InputStream in) {

		try {

			final File tempFile = File.createTempFile("stream2file", ".tmp");
			tempFile.deleteOnExit();
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = in.read(buffer)) > 0) {
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
				out.close();
			}
			return tempFile;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	
	public static String getOption(final char option, final CommandLine commandLine) {

	    if (commandLine.hasOption(option)) {
	        return commandLine.getOptionValue(option);
	    }

	    return "";
	}
	
	
	
	
	
}
