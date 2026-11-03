import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.*;

/**
 * Wild Trails night-expedition interface update. Requires JDK 17+; no external dependencies.
 * Run inside the updated Animal-Tracking-Game directory:
 *   java /path/to/AnimalTrackingNightUpdate.java --check
 *   java /path/to/AnimalTrackingNightUpdate.java
 */
public final class AnimalTrackingNightUpdate {
    private static final String[] PAYLOAD = {
        "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"
    };
    private static final String PAYLOAD_SHA256 = "f23241413d7bfa44af67a6fc0ab276b0071e2307fd723b34ed848706d0b83fa3";
    private record Change(Path path, byte[] before, byte[] after) {}
    private record Package(Map<String,byte[]> files, Map<String,String> expected) {}
    private static final LinkOption[] NOFOLLOW = {LinkOption.NOFOLLOW_LINKS};

    public static void main(String[] args) {
        try {
            if (args.length > 1 || (args.length == 1 && !args[0].equals("--check")))
                throw new IllegalArgumentException("Usage: java AnimalTrackingNightUpdate.java [--check]");
            install(args.length == 1);
        } catch (Exception error) {
            System.err.println("FAIL | " + error.getMessage());
            System.exit(1);
        }
    }
    private static String digest(String algorithm, byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance(algorithm).digest(bytes));
    }
    private static String blobSha(byte[] bytes) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        sha.update(("blob " + bytes.length + "\0").getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(sha.digest(bytes));
    }
    private static Package unpack() throws Exception {
        byte[] zip = Base64.getDecoder().decode(String.join("", PAYLOAD));
        if (!digest("SHA-256", zip).equals(PAYLOAD_SHA256))
            throw new IOException("Update file is incomplete. Download it again.");
        Map<String,byte[]> files = new LinkedHashMap<>();
        Map<String,String> expected = new LinkedHashMap<>();
        try (ZipInputStream stream = new ZipInputStream(new ByteArrayInputStream(zip))) {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                byte[] content = stream.readAllBytes();
                if (entry.getName().equals("expected.tsv")) {
                    for (String line : new String(content,StandardCharsets.UTF_8).split("\n")) {
                        String[] pair = line.split("\t",2);
                        if (pair.length != 2) throw new IOException("Invalid update manifest");
                        expected.put(pair[0],pair[1]);
                    }
                } else if (entry.getName().startsWith("files/")) {
                    String name = entry.getName().substring(6);
                    if (files.put(name,content) != null) throw new IOException("Duplicate update path");
                } else throw new IOException("Unexpected update entry");
            }
        }
        if (files.isEmpty() || expected.isEmpty()) throw new IOException("Empty update package");
        return new Package(files,expected);
    }
    private static Path safePath(Path root,String relative) throws IOException {
        Path suffix = Path.of(relative);
        Path path = root.resolve(suffix).normalize();
        if (suffix.isAbsolute() || !path.startsWith(root) || path.equals(root))
            throw new IOException("Unexpected file path: " + relative);
        Path cursor = root;
        for (Path component : root.relativize(path)) {
            cursor = cursor.resolve(component);
            if (Files.isSymbolicLink(cursor)) throw new IOException("Symbolic link preserved: " + relative);
        }
        return path;
    }
    private static void install(boolean checkOnly) throws Exception {
        Path root = Path.of("").toRealPath();
        if (!Files.exists(root.resolve(".git"),NOFOLLOW)
            || !Files.isRegularFile(safePath(root,"src/GameEngine.java"),NOFOLLOW))
            throw new IOException("Run inside Animal-Tracking-Game after installing Wild Trails.");
        Package pack = unpack();
        List<Change> changes = new ArrayList<>();
        List<String> conflicts = new ArrayList<>();
        for (Map.Entry<String,byte[]> entry : pack.files().entrySet()) {
            Path path = safePath(root,entry.getKey());
            if (!Files.isRegularFile(path,NOFOLLOW)) {conflicts.add(entry.getKey());continue;}
            byte[] before = Files.readAllBytes(path);
            if (Arrays.equals(before,entry.getValue())) continue;
            if (!blobSha(before).equals(pack.expected().get(entry.getKey()))) {conflicts.add(entry.getKey());continue;}
            changes.add(new Change(path,before,entry.getValue()));
        }
        if (!conflicts.isEmpty())
            throw new IOException("CUSTOM_FILES_PRESERVED | No changes made:\n" + String.join("\n",conflicts));
        if (checkOnly) {System.out.println("PASS | Night theme verified; no changes written.");return;}
        if (changes.isEmpty()) {System.out.println("PASS | Night expedition theme is already installed.");return;}
        Path backup = safePath(root,".animal-tracking-backups/night-" + java.time.Instant.now().toEpochMilli());
        for (Change change : changes) {
            Path saved = backup.resolve(root.relativize(change.path()));
            Files.createDirectories(saved.getParent());
            Files.write(saved,change.before());
        }
        List<Change> written = new ArrayList<>();
        try {
            for (Change change : changes) {written.add(change);Files.write(change.path(),change.after());}
        } catch (IOException error) {
            for (Change change : written) {
                try {Files.write(change.path(),change.before());} catch(IOException rollback){error.addSuppressed(rollback);}
            }
            throw error;
        }
        System.out.println("PASS | Night expedition interface installed. Java game rules unchanged.");
        System.out.println("Backups: " + root.relativize(backup));
        System.out.println("Run sh scripts/web.sh, then open http://127.0.0.1:8082");
        System.out.println("If already running, refresh the browser with Command+Shift+R.");
    }
}
