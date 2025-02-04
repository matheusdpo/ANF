package br.com.ether.anf.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class FolderUtility {
    private final DateUtility dateUtility;

    public void isFolderExist(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        else {
            //deletar tudo q tem aqui
            File[] files = file.listFiles();
            for (File f : files) {
                f.delete();
            }
        }
    }

    public String moveFile(String pathDownload, String pathSaveNf, String os) {
        File file = new File(pathDownload);
        File[] files = file.listFiles();

        String caminho = "";

        for (File f : files) {
            if (f.getName().contains(".pdf") || f.getName().contains(".PDF")) {
                if (os.equalsIgnoreCase("linux"))
                    caminho = System.getProperty("user.dir") + pathSaveNf + dateUtility.getToday("MM-yy") + "/";
                else
                    caminho = System.getProperty("user.dir") + pathSaveNf + dateUtility.getToday("MM-yy") + "\\";

                isFolderExist(caminho);

                f.renameTo(new File(caminho + f.getName()));

                caminho = caminho + f.getName();
            }
        }

        return caminho;
    }

    public String getKey(String pathDownload) {
        //mover arquivo de uma pasta a outra
        File file = new File(pathDownload);
        File[] files = file.listFiles();

        for (File f : files) {
            if (f.getName().contains(".pdf") || f.getName().contains(".PDF")) {
                return f.getName()
                        .replace(".pdf", "")
                        .replace(".PDF", "");
            }
        }
        return "";
    }

    public String getFile(String pathDownload) {
        //mover arquivo de uma pasta a outra
        File file = new File(pathDownload);
        File[] files = file.listFiles();

        for (File f : files) {
            if (f.getName().contains(".pdf") || f.getName().contains(".PDF")) {
                return f.getAbsolutePath();
            }
        }
        return "";
    }

    public boolean isPDFHere(String pathDownload) {
        //mover arquivo de uma pasta a outra
        File file = new File(pathDownload);
        File[] files = file.listFiles();

        for (File f : files) {
            if (f.getName().endsWith(".pdf") || f.getName().endsWith(".PDF")) {
                return true;
            }
        }
        return false;
    }
}