package makeOneFile;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class OneFileMaker {

    public static final String PLAYER_FILE_PATH = "C:\\Users\\MedBelmahi\\Desktop\\hypersonic\\hypersonic\\src\\main\\java\\playerFile\\Player.java";
    public static final String PACKAGE_PATH = "C:\\Users\\MedBelmahi\\Desktop\\hypersonic\\hypersonic\\src\\main\\java\\hypersonic";

    public static void main(String[] args) {
        while (true) {
            File hyperSonicPackage = new File(PACKAGE_PATH);

            String content = filesContent(hyperSonicPackage);

            //System.out.println(content);

            content = "import java.util.*;\n" + content;

            makePlayerFile(content);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void makePlayerFile(String content) {
        File file = new File(PLAYER_FILE_PATH);

        try (FileOutputStream fop = new FileOutputStream(file)) {

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes(StandardCharsets.UTF_8);

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String filesContent(File file) {

        String filesContent = "";

        if (file.isDirectory()) {

            for (File f : file.listFiles()) {
                filesContent += "\n" + filesContent(f);
            }

        } else {
            BufferedReader br = null;

            try {

                String sCurrentLine;

                br = new BufferedReader(new FileReader(file));

                while ((sCurrentLine = br.readLine()) != null) {
                    if (sCurrentLine.startsWith("package") || sCurrentLine.startsWith("import")) {
                        continue;
                    }else if (sCurrentLine.startsWith("public class") || sCurrentLine.startsWith("public abstract class")){
                        sCurrentLine = sCurrentLine.replaceFirst("public ", "");
                    }
                    System.out.println(sCurrentLine);
                    filesContent += "\n" + sCurrentLine;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return filesContent;

    }

}
