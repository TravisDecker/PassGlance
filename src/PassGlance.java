/*bin/mkdir -p /tmp/.java/classes 2> /dev/null

# Compile the program.
#
javac -d /tmp/.java/classes $0

# Run the compiled program only if compilation succeeds.
#
[[ $? -eq 0 ]] && java -cp /tmp/.java/classes $(basename ${0%.*}) "$@"
exit
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PassGlance {

  private static String path;
  private static String userPassword;
  private static String DIGITS_REGEX = "\\d+";
  private static String NANS_REGEX = "\\W+";

  private static boolean caseInsensitiveFlag;
  private static boolean containsPasswordFlag;
  private static boolean containsDigitsFlag;
  private static boolean containsSymbolsFlag;
  private static boolean SymbolicReplacment;
  private static boolean verboseFlag;
  private static boolean nonListFileFlag = true;

  private static int matchedPasswordCount;
  private static int passwordsContainingCount;
  private static int containsDigitsCount;
  private static int checkedPasswordsCount;
  private static int containsSymbolsCount;


  public static void main(String[] args) {
    startUp();
    parseArgs(args);
    showSetup(userPassword);
    readFile(path);
    report();


  }

  private static void startUp() {
    readFile("src/banner.txt");
    nonListFileFlag = false;
    System.out.println();
  }

  //TODO pull string literals into a file
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

  // TODO handle null password
  private static void parseArgs(String[] args) {
    for (String arg : args
    ) {
      if (arg.matches("-.*")) {
        setOptions(arg);
      } else if (arg.matches("path=.*")) {
        path = arg.substring(5);
      } else {
        userPassword = arg;
      }
    }
    if (path == null) {
      System.out.println("Please provide a word list.");
      nonListFileFlag = true;
      help();
      System.exit(0);
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

      case "-s":
        containsSymbolsFlag = true;
        break;

      case "-v":
        verboseFlag = true;
        break;

    }
  }

  private static void help() {
    nonListFileFlag = true;
    readFile("src/help.txt");
  }

  private static void readFile(String path) {
    String str;
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      while ((str = br.readLine()) != null) {
        process(str);
        if (!nonListFileFlag) {
          checkedPasswordsCount++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void process(String fileLine) {
    if (nonListFileFlag) {
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
        leadingTrailingVals(userPassword, fileLine, DIGITS_REGEX);
      }
      if (containsSymbolsFlag) {
        leadingTrailingVals(userPassword, fileLine, NANS_REGEX);
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

  private static void leadingTrailingVals(String userPassword, String fileLine, String option) {
    var count = 0;
    if (caseInsensitiveFlag) {
      if (fileLine.toLowerCase().matches(option + userPassword) || fileLine.toLowerCase()
          .matches(userPassword + option)
          || fileLine.toLowerCase().matches(option + userPassword + option)) {
        count++;
      }
    } else if (fileLine.matches(option + userPassword) || fileLine.matches(userPassword + option)
        || fileLine.matches(option + userPassword + option)) {
      count++;
    }
    if (option == NANS_REGEX) {
      containsSymbolsCount += count;
      count = 0;
    } else {
      containsDigitsCount += count;
      count = 0;
    }

  }

  //TODO pull string literals into a file
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

    if (containsSymbolsFlag) {
      System.out.println("Passwords proceeded with/ending with symbols: " + containsSymbolsCount);
    }
  }
}
