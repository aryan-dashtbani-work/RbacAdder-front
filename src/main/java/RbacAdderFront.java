import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RbacAdderFront {
    public static void main(String[] args) throws IOException {

        for (String arg : args) {

            List<File> files = new ArrayList<>();

            Stream<Path> stream = Files.walk(Paths.get(arg));
            stream.filter(Files::isRegularFile)
                    .forEach(path -> files.add(path.toFile()));

            files.forEach(file1 -> {
                List<String> s;
                try {
                    s = Files.readAllLines(file1.toPath());

                    List<Integer> buttonStartLineNumber = new ArrayList<>();
                    List<Integer> buttonEndLineNumber = new ArrayList<>();

                    for (int i = 1; i < s.size(); i++) {
                        if (s.get(i).contains("<button")) {
                            buttonStartLineNumber.add(i + 1);
                            int b = i;
                            while (!s.get(b).contains(">")) {
                                b++;
                            }
                            buttonEndLineNumber.add(b + 1);
                        }
                    }

                    outerLoop:
                    for (int i = 0; i < buttonStartLineNumber.size(); i++) {
                        for (int j = buttonStartLineNumber.get(i) - 1; j <= buttonEndLineNumber.get(i) - 1; j++) {
                            if (s.get(j).contains("*jhiHasPermission=")
                                    || s.get(j).contains("(click)=\"clear()\"")
                                    || s.get(j).contains("(click)=\"cancel()\"")
                                    || s.get(j).contains("(click)=\"clearForm()\"")
                                    || s.get(j).contains("(click)=\"closeDialog()\"")) {
                                break outerLoop;
                            }
                        }
                        s.set(buttonEndLineNumber.get(i) - 1, s.get(buttonEndLineNumber.get(i) - 1).replaceFirst(">", " *jhiHasPermission=>"));
                    }

                    Files.write(file1.toPath(), s);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
