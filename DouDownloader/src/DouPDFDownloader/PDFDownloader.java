package DouPDFDownloader;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFDownloader
{
	public static boolean DownloadPDF(String url, String fileDestination)
	{
		System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(new Date()) + " - Processing p: " + url);
		System.out.println("Downloading r: " + fileDestination);

		URL website;
		try
		{
			website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(fileDestination);
			Long bytes = fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

			System.out.println("Bytes: " + bytes);
			return (bytes > 1024); // quando nao existe retorna 414 bytes de lixo. 1024 por segurança

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//
		// try
		// {
		// PDFDownloader.DownloadPDF(urljp, filejp);
		// } catch (ConnectException ce)
		// {
		// // retentar !!
		// ce.printStackTrace();
		// j--;
		// } catch (SocketException se)
		// {
		// // retentar !!
		// se.printStackTrace();
		// j--;
		// }

	}
}
