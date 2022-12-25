import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RbacAdder {
    public static void main(String[] args) throws IOException {


        File file = new File("E:\\Projects\\RbacAdder - front\\src\\main\\resources\\batch-adjustment-preprocess.component.html");

        List<String> s = Files.readAllLines(file.toPath());


        List<Integer> buttonStartLineNumber = new ArrayList<>();
        List<Integer> buttonEndLineNumber = new ArrayList<>();

        for (int i = 1; i < s.size(); i++) {
            if (s.get(i).contains("<button")) {
                buttonStartLineNumber.add(i+1);
                int b = i;
                while(!s.get(b).contains(">")){
                    b++;
                }
                buttonEndLineNumber.add(b+1);
            }
        }

        outerLoop:
        for (int i = 0; i < buttonStartLineNumber.size(); i++) {
            for (int j = buttonStartLineNumber.get(i); j <= buttonEndLineNumber.get(i); j++){
                if (s.get(j).contains("*jhiHasPermission=")) {
                    break outerLoop;
                }
            }
            s.set(i, s.get(buttonEndLineNumber.get(i)).replaceFirst(">", " *jhiHasPermission=>"));
        }



        Files.write(file.toPath(), s);




        /*try {
            File file1 = new File(args[0]);
            for (File listFile : file1.listFiles()) {

                if (listFile.isDirectory() || !listFile.getName().contains("Resource"))
                    continue;

                List<String> s = Files.readAllLines(listFile.toPath());

                for (int i = 0; i < s.size(); i++) {
                    if (s.get(i).contains("public ResponseEntity<")) {
                        if (s.get(i-1).contains("@Secured(ENTITY_NAME)"))
                            s.set(i-1, "    @Secured(ENTITY_NAME)");
                        else {
                            s.add(i, "    @Secured(ENTITY_NAME)");
                            i++;
                        }
                    }
                }

                Files.write(listFile.toPath(), s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
