package com.goit;

import java.util.Scanner;

public class HttpImageStatusCli {
    public static void main(String[] args) {
        askStatus();
    }

    public static void askStatus() {
        Scanner scanner = new Scanner(System.in);
        int code = -1;
        while (code < 0) {
            System.out.println("Enter HTTP status code: ");
            try {
                String input = scanner.nextLine();
                code = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid number");
            }
        }
        try {
            HttpStatusImageDownloader.downloadStatusImage(code);
            System.out.println("File was downloaded successfully");
        } catch (Exception e) {
            System.out.printf("There is not image for HTTP status %d", code);
        }
    }
}
