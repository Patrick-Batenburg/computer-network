package com.patrick.library.logic;

import com.google.gson.Gson;
import com.patrick.library.domain.JsonResult;

import java.net.*;
import java.io.*;

public class LibraryProtocol
{
    private static final int WAITING = 0;
    private static final int FIND_BOOK_DETAILS = 1;
    private static final int EXIT = 4;
    private int state = WAITING;

    public String processInput(String theInput)
    {
        String output = "";

        if (state == WAITING)
        {
            output = "Type ISBN: ";
            state = FIND_BOOK_DETAILS;
        }
        else if (state == FIND_BOOK_DETAILS)
        {
            long number = 0;

            try
            {
                number = Long.parseLong(theInput);

                URL bookInfo = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + number);
                InputStream input = bookInfo.openStream();
                Reader reader = new InputStreamReader(input, "UTF-8");
                JsonResult result  = new Gson().fromJson(reader, JsonResult.class);

                //Output
                output += "ISBN: " + number + "\nTitle: " + result.getBookDetail().getTitle() + "\nSubtitle: " + result.getBookDetail().getSubTitle();

                for (String author : result.getBookDetail().getAuthors())
                {
                    output += "\nAuthor: " + author;
                }

                output += "\nDescription: " + result.getBookDetail().getDescription() + "\nPages: " + result.getBookDetail().getPageCount() + "\nLanguage: " + result.getBookDetail().getLanguage();
            }
            catch (NumberFormatException e)
            {
                output = "Error: The specified ISBN was invalid.";
            }
            catch (Exception e)
            {
                output = "Error: Unable to find the book details.";
            }

            state = WAITING;
        }

        return output;
    }
}