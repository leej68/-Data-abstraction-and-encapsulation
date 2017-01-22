package HTMLvalidator;

// CSE 143, Homework 2: HTML Manager
//
// Instructor-provided code.
// This program tests your HTML manager object on any file or URL you want.
//
// When it prompts you for a file name, if you type a simple string such
// as "tests/test1.html" (without the quotes) it will just look on your hard disk
// in the same directory as your code or Eclipse project.
//
// If you type a string such as "http://www.google.com/index.html", it will
// connect to that URL and download the HTML content from it.

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.MalformedURLException;

import cse143.util.*;

public class HTMLMain {
    public static void main(String[] args) {
        HTMLManager manager = null;
        Scanner console = new Scanner(System.in);
        String choice = "s";
        
        while (true) {
            if (choice.startsWith("s")) {
                // prompt for page, then download it if it's a URL
                System.out.print("Page URL or file name (blank for empty): ");
                String url = console.nextLine().trim();
                if (url.length() > 0) {
                    HTMLParser parser = getParser(url);
                    if (parser != null) {
                        Queue<HTMLTag> tags = parser.parse();
                        manager = new HTMLManager(tags);
                    }
                    else {
                        System.err.println("Couldn't resolve input.  Try again!");
                    }
                } 
                else {
                    Queue<HTMLTag> tags = new FIFOQueue<HTMLTag>();
                    manager = new HTMLManager(tags);
                }
            } 
            else if (choice.startsWith("a")) {
                System.out.print("What tag (such as '<table>stuff inside the table' or" + 
                                 " '</p>stuff after the p')? ");
                String tagText = console.nextLine().trim();
                System.out.print("What index? ");
                int index = Integer.parseInt(console.nextLine().trim());
                boolean isOpenTag = !tagText.contains("</");
                HTMLTagType type = isOpenTag ? HTMLTagType.OPENING : HTMLTagType.CLOSING;
                int split = tagText.indexOf(">");
                String element = tagText.substring(1, split);
                String contents = tagText.substring(split + 1, tagText.length());
                HTMLTag tag = new HTMLTag(element, type, contents);
                manager.add(index, tag);
            } 
            else if (choice.startsWith("g")) {
                System.out.println(manager.getTags());
            } 
            else if (choice.startsWith("p")) {
                System.out.println(manager.toString());
            } 
            else if (choice.startsWith("r")) {
                System.out.print("Remove what index? ");
                int index = Integer.parseInt(console.nextLine().trim());
                manager.remove(index);
            }
            else if (choice.startsWith("f")) {
                manager.fixHTML();
            } 
            else if (choice.startsWith("q")) {
                break;
            }

            System.out.println();
            System.out.print("(a)dd, (g)etTags, (r)emove, (f)ixHTML, (s)et URL, (p)rint, (q)uit? ");
            choice = console.nextLine().trim().toLowerCase();
        }
    }
    
    /**
     * Attempts to get a new HTMLParser for the provided address
     * post: returns null if no source found the given address
     */
    public static HTMLParser getParser(String address) {
        HTMLParser result = null;
        try {
            result = new HTMLParser(new URL(address));
            System.out.println("Found URL!");
        } catch (MalformedURLException e1) {
            result = new HTMLParser(new File(address));
            System.out.println("Found File!");
        }
        return result;
    }
}
