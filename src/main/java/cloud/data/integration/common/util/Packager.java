package cloud.data.integration.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;



public class Packager
{
	final static Logger logger = Logger.getLogger(Packager.class);
    public static void packZip(File output, List<File> sources) throws IOException
    {
    	logger.debug("Packaging to " + output.getName());
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(output));
        zipOut.setLevel(Deflater.DEFAULT_COMPRESSION);

        for (File source : sources)
        {
            if (source.isDirectory())
            {
                zipDir(zipOut, "", source);
            } else
            {
                zipFile(zipOut, "", source);
            }
        }
        zipOut.flush();
        zipOut.close();
        logger.debug("Done");
    }

    public static void unPackZip(String zipFileName, String unZipPath) throws IOException
    {
    	  String fileZip = zipFileName;
          byte[] buffer = new byte[1024];
          ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
          ZipEntry zipEntry = zis.getNextEntry();
          while(zipEntry != null){
              String fileName = zipEntry.getName();
              File newFile = new File(unZipPath + fileName);
              FileOutputStream fos = new FileOutputStream(newFile);
              int len;
              while ((len = zis.read(buffer)) > 0) {
                  fos.write(buffer, 0, len);
              }
              fos.close();
              zipEntry = zis.getNextEntry();
          }
          zis.closeEntry();
          zis.close();
    }

    private static String buildPath(String path, String file)
    {
        if (path == null || path.isEmpty())
        {
            return file;
        } else
        {
            return path + "/" + file;
        }
    }

    private static void zipDir(ZipOutputStream zos, String path, File dir) throws IOException
    {
        if (!dir.canRead())
        {
            logger.debug("Cannot read " + dir.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }

        File[] files = dir.listFiles();
        path = buildPath(path, dir.getName());
        logger.debug("Adding Directory " + path);

        for (File source : files)
        {
            if (source.isDirectory())
            {
                zipDir(zos, path, source);
            } else
            {
                zipFile(zos, path, source);
            }
        }

        logger.debug("Leaving Directory " + path);
    }

    private static void zipFile(ZipOutputStream zos, String path, File file) throws IOException
    {
        if (!file.canRead())
        {
            logger.debug("Cannot read " + file.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }

        logger.debug("Compressing " + file.getName());
        zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));

        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[4092];
        int byteCount = 0;
        while ((byteCount = fis.read(buffer)) != -1)
        {
            zos.write(buffer, 0, byteCount);
           // System.out.print('.');
           // System.out.flush();
        }
        logger.debug("compressing is done");

        fis.close();
        zos.closeEntry();
    }
}
