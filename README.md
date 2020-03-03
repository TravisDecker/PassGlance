# PassGlance
PassGlance is a simple command line program that scans a given word list for a given word.
It is designed to find passwords and variations on a given password in a word list and return a count.

The PassGlance class file contains a bash script allowing you to simply call bash fallowed by the program. 
This was done to make PassGlance more user friendly and more like other command line programs.

# Usage

To install PassGlance simply clone this repository locally:

````
git clone https://github.com/TravisDecker/PassGlance.git   
````

Then start the program using the bash command:

````
bash /where_you_cloned_the_repo/PassGlance/src/PassGlance.java [options] path=/path/to/word/list [password]
````

# Options

````
-cI Case Insensitive 
````
Checks Passwords Insenstivce to casing
````
-c Contains Password
````
Checks for passwords that contain the given word.
````
-d With Digits
````
Checks passwords with proceeding and/or trailing digits.
````
-s With Symbols
````
Checks passwords with proceeding and/or trailing symbols.
````
-v Verbose output
````
Outputs more information.


# Example Usage

```` 
bash /Tools/PassGlance/src/PassGlance.java -cI -d -v path=/Desktop/rockyou.txt
````

