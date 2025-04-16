package com.zby.experiment.ex3.pro3;
import java.util.*;

class File {
    String name;
    int protectionCode; // 0: not executable, 1: executable
    int length;
    String content;

    File(String name, int protectionCode, int length, String content) {
        this.name = name;
        this.protectionCode = protectionCode;
        this.length = length;
        this.content = content;
    }
}

class User {
    String username;
    List<File> ufd; // User File Directory

    User(String username) {
        this.username = username;
        this.ufd = new ArrayList<>();
    }
}

class FileSystem {
    Map<String, User> mfd; // Master File Directory
    Map<String, File> afd; // Active File Directory

    FileSystem() {
        mfd = new HashMap<>();
        afd = new HashMap<>();
    }

    // User login
    User login(String username) {
        if (!mfd.containsKey(username)) {
            System.out.println("User not found.");
            return null;
        }
        return mfd.get(username);
    }

    // Create user
    void createUser(String username) {
        if (mfd.containsKey(username)) {
            System.out.println("User already exists.");
        } else {
            mfd.put(username, new User(username));
            System.out.println("User " + username + " created.");
        }
    }

    // Create file
    void createFile(User user, String fileName, int protectionCode, String content) {
        if (user.ufd.stream().anyMatch(file -> file.name.equals(fileName))) {
            System.out.println("File already exists.");
        } else {
            File file = new File(fileName, protectionCode, content.length(), content);
            user.ufd.add(file);
            System.out.println("File " + fileName + " created.");
        }
    }

    // Delete file
    void deleteFile(User user, String fileName) {
        File fileToRemove = user.ufd.stream().filter(file -> file.name.equals(fileName)).findFirst().orElse(null);
        if (fileToRemove == null) {
            System.out.println("File not found.");
        } else {
            user.ufd.remove(fileToRemove);
            afd.remove(fileName);
            System.out.println("File " + fileName + " deleted.");
        }
    }

    // Open file
    void openFile(User user, String fileName) {
        File fileToOpen = user.ufd.stream().filter(file -> file.name.equals(fileName)).findFirst().orElse(null);
        if (fileToOpen == null) {
            System.out.println("File not found.");
        } else if (afd.containsKey(fileName)) {
            System.out.println("File is already open.");
        } else {
            afd.put(fileName, fileToOpen);
            System.out.println("File " + fileName + " opened.");
        }
    }

    // Close file
    void closeFile(String fileName) {
        if (afd.remove(fileName) != null) {
            System.out.println("File " + fileName + " closed.");
        } else {
            System.out.println("File is not open.");
        }
    }

    // Read file
    void readFile(String fileName) {
        File file = afd.get(fileName);
        if (file == null) {
            System.out.println("File is not open.");
        } else {
            System.out.println("Reading file " + fileName + ": " + file.content);
        }
    }

    // Write file
    void writeFile(String fileName, String content) {
        File file = afd.get(fileName);
        if (file == null) {
            System.out.println("File is not open.");
        } else {
            file.content = content;
            file.length = content.length();
            System.out.println("File " + fileName + " written.");
        }
    }

    // List user files
    void listFiles(User user) {
        System.out.println("Files for user " + user.username + ":");
        for (File file : user.ufd) {
            System.out.println("- " + file.name + " (Protection: " + file.protectionCode + ", Length: " + file.length + ")");
        }
    }
}

public class SimpleFileManager {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);

        fileSystem.createUser("root");
        fileSystem.createUser("user1");
        fileSystem.createUser("user2");
        fileSystem.createUser("user3");
        fileSystem.createUser("user4");
        fileSystem.createUser("user5");
        fileSystem.createUser("user6");
        fileSystem.createUser("user7");
        fileSystem.createUser("user8");
        fileSystem.createUser("user9");

        User currentUser = null;

        while (true) {
            System.out.println("\n--- File Management System ---");
            if (currentUser == null) {
                System.out.print("Enter username to login: ");
                String username = scanner.nextLine();
                currentUser = fileSystem.login(username);
            } else {
                System.out.println("Logged in as: " + currentUser.username);
                System.out.println("Commands: create, delete, open, close, read, write, dir, logout, exit");
                System.out.print("Enter command: ");
                String command = scanner.nextLine();

                switch (command) {
                    case "create":
                        System.out.print("Enter file name: ");
                        String fileName = scanner.nextLine();
                        System.out.print("Enter protection code (0 or 1): ");
                        int protectionCode = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter file content: ");
                        String content = scanner.nextLine();
                        fileSystem.createFile(currentUser, fileName, protectionCode, content);
                        break;

                    case "delete":
                        System.out.print("Enter file name: ");
                        fileName = scanner.nextLine();
                        fileSystem.deleteFile(currentUser, fileName);
                        break;

                    case "open":
                        System.out.print("Enter file name: ");
                        fileName = scanner.nextLine();
                        fileSystem.openFile(currentUser, fileName);
                        break;

                    case "close":
                        System.out.print("Enter file name: ");
                        fileName = scanner.nextLine();
                        fileSystem.closeFile(fileName);
                        break;

                    case "read":
                        System.out.print("Enter file name: ");
                        fileName = scanner.nextLine();
                        fileSystem.readFile(fileName);
                        break;

                    case "write":
                        System.out.print("Enter file name: ");
                        fileName = scanner.nextLine();
                        System.out.print("Enter new content: ");
                        content = scanner.nextLine();
                        fileSystem.writeFile(fileName, content);
                        break;

                    case "dir":
                        fileSystem.listFiles(currentUser);
                        break;

                    case "logout":
                        currentUser = null;
                        break;

                    case "exit":
                        System.out.println("Exiting...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            }
        }
    }
}