package space.starylense;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

  private static String path;
  private static String userPassword;

  private static boolean caseInsensitiveFlag;
  private static boolean containsPasswordFlag;
  private static boolean containsDigitsFlag;
  private static boolean conatainsSymbolsFlag;
  private static boolean verboseFlag;
  private static boolean bannerFlag = true;

  private static int matchedPasswordCount;
  private static int passwordsContainingCount;
  private static int caseInsensitiveCount;
  private static int containsDigitsCount;
  private static int checkedPasswordsCount;

  public static void main(String[] args) {
    startUp();
    parseArgs(args);
    showSetup(userPassword);
    readFile(path);
    report();


  }

  private static void startUp() {
    readFile("src/banner.txt");
    bannerFlag = false;
    System.out.println();
  }

  private static void showSetup(String userPassword) {
    System.out.println("[*] Checking " + path + " for password: " + userPassword);

    if (verboseFlag) {
      if (caseInsensitiveFlag) {
        System.out.println("[*] Case Insensitive...");
      }
      if (containsPasswordFlag) {
        System.out.println("[*] Containing Password...");
      }
      if (containsDigitsFlag) {
        System.out.println("[*] Containing Digits...");
      }
    }

  }

  private static void parseArgs(String[] args) {
    for (String arg : args
    ) {
      if (arg.matches("-.*")) {
        setOptions(arg);
      } else if (arg.matches("PATH=.*")) {
        path = arg.substring(5);
      } else {
        userPassword = arg;
      }
    }
  }

  private static void setOptions(String option) {
    switch (option) {

      case "-cI":
        caseInsensitiveFlag = true;
        break;

      case "-c":
        containsPasswordFlag = true;
        break;

      case "-d":
        containsDigitsFlag = true;
        break;

      case "-v":
        verboseFlag = true;
        break;
    }
  }

  private static void readFile(String path) {
    if (path != null) {
      BufferedReader br;
      String str;
      try {
        br = new BufferedReader(new FileReader(path));
        while ((str = br.readLine()) != null) {
          process(str);
          if (!bannerFlag) {
            checkedPasswordsCount++;
          }

        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Please provide a word list.");
      System.exit(0);
    }
  }

  private static void process(String fileLine) {
    if (bannerFlag) {
      System.out.println(fileLine);
      try {
        Thread.sleep(90);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } else {

      matchPassword(userPassword, fileLine);

      if (containsPasswordFlag) {
        containsPassword(userPassword, fileLine);
      }

      if (containsDigitsFlag) {
        containsDigits(userPassword, fileLine);
      }
    }
  }

  private static void matchPassword(String userPassword, String fileLine) {
    if (caseInsensitiveFlag) {
      if (fileLine.toLowerCase().contentEquals(userPassword.toLowerCase())) {
        matchedPasswordCount++;
      }

    } else if (fileLine.contentEquals(userPassword)) {
      matchedPasswordCount++;
    }
  }


  private static void containsPassword(String userPassword, String fileLine) {

    if (caseInsensitiveFlag) {
      if (fileLine.toLowerCase().contains(userPassword.toLowerCase())) {
        passwordsContainingCount++;
      }
    } else if (fileLine.contains(userPassword)) {
      passwordsContainingCount++;
    }
  }

  private static void containsDigits(String userPassword, String fileLine) {
    if (caseInsensitiveFlag) {
      if (fileLine.toLowerCase().matches("\\d+" + userPassword) || fileLine.toLowerCase()
          .matches(userPassword + "\\d+")
          || fileLine.toLowerCase().matches("\\d+" + userPassword + "\\d+")) {
        containsDigitsCount++;
      }
    } else if (fileLine.matches("\\d+" + userPassword) || fileLine.matches(userPassword + "\\d+")
        || fileLine.matches("\\d+" + userPassword + "\\d+")) {
      containsDigitsCount++;
    }
  }

  private static void report() {
    System.out.println();
    System.out.println("Passwords checked: " + checkedPasswordsCount);
    System.out.println("Password matches: " + matchedPasswordCount);

    if (containsPasswordFlag) {
      System.out.println("Passwords containing your password: " + passwordsContainingCount);
    }

    if (containsDigitsFlag) {
      System.out.println("Passwords proceeded with/ending with digits: " + containsDigitsCount);
    }
  }
}
