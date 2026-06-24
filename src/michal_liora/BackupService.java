package michal_liora;

import java.io.*;

public class BackupService {
    private final static String BackupPath = "collegeBackup.bin";

    public static College loadCollege() throws IOException, ClassNotFoundException {
        File file = new File(BackupPath);

        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            College collegeBackup = (College) objectInputStream.readObject();

            objectInputStream.close();
            return collegeBackup;
        }
        return null;
    }

    public static void saveCollege(College college) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(BackupPath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(college);
        objectOutputStream.close();
    }
}
