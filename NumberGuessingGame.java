import java.util.Scanner;
import java.util.Random;

public class NumberGuessingGame {
    
    // Constants for difficulty levels
    private static final int EASY_MAX = 50;
    private static final int MEDIUM_MAX = 100;
    private static final int HARD_MAX = 200;
    private static final int MAX_ATTEMPTS = 10;
    
    // Track best score
    private static int bestScore = Integer.MAX_VALUE;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;
        
        displayWelcomeMessage();
        
        while (playAgain) {
            playGame(scanner);
            playAgain = askPlayAgain(scanner);
        }
        
        displayGoodbyeMessage();
        scanner.close();
    }
    
    /**
     * Displays welcome message and game instructions
     */
    private static void displayWelcomeMessage() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO NUMBER GUESSING GAME!    ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nTry to guess the secret number!");
        System.out.println("You'll receive hints after each guess.\n");
    }
    
    /**
     * Main game logic
     */
    private static void playGame(Scanner scanner) {
        int maxRange = selectDifficulty(scanner);
        int maxAttempts = MAX_ATTEMPTS;
        
        Random random = new Random();
        int secretNumber = random.nextInt(maxRange) + 1;
        int attempts = 0;
        boolean guessedCorrectly = false;
        
        System.out.println("\n🎯 A number between 1 and " + maxRange + " has been generated!");
        System.out.println("You have " + maxAttempts + " attempts to guess it.\n");
        
        while (attempts < maxAttempts && !guessedCorrectly) {
            System.out.print("Attempt " + (attempts + 1) + "/" + maxAttempts + " - Enter your guess: ");
            
            // Input validation
            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
                continue;
            }
            
            int userGuess = scanner.nextInt();
            attempts++;
            
            // Validate range
            if (userGuess < 1 || userGuess > maxRange) {
                System.out.println("⚠️  Please enter a number between 1 and " + maxRange + "!\n");
                attempts--; // Don't count invalid attempts
                continue;
            }
            
            // Compare guess with secret number
            if (userGuess == secretNumber) {
                guessedCorrectly = true;
                displaySuccessMessage(attempts, maxRange);
            } else if (userGuess < secretNumber) {
                System.out.println("📈 Too low! Try a higher number.\n");
                provideHint(userGuess, secretNumber, maxRange);
            } else {
                System.out.println("📉 Too high! Try a lower number.\n");
                provideHint(userGuess, secretNumber, maxRange);
            }
        }
        
        // If max attempts reached without correct guess
        if (!guessedCorrectly) {
            System.out.println("\n💔 Game Over! You've used all " + maxAttempts + " attempts.");
            System.out.println("The secret number was: " + secretNumber + "\n");
        }
    }
    
    /**
     * Lets user select difficulty level
     */
    private static int selectDifficulty(Scanner scanner) {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SELECT DIFFICULTY LEVEL:");
        System.out.println("1. Easy   (1 - " + EASY_MAX + ")");
        System.out.println("2. Medium (1 - " + MEDIUM_MAX + ")");
        System.out.println("3. Hard   (1 - " + HARD_MAX + ")");
        System.out.println("─────────────────────────────────────────");
        
        int choice = 0;
        boolean validChoice = false;
        
        while (!validChoice) {
            System.out.print("Enter your choice (1-3): ");
            
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 3) {
                    validChoice = true;
                } else {
                    System.out.println("❌ Invalid choice! Please select 1, 2, or 3.\n");
                }
            } else {
                System.out.println("❌ Invalid input! Please enter a number.\n");
                scanner.next(); // Clear invalid input
            }
        }
        
        switch (choice) {
            case 1:
                System.out.println("✅ Easy mode selected!\n");
                return EASY_MAX;
            case 2:
                System.out.println("✅ Medium mode selected!\n");
                return MEDIUM_MAX;
            case 3:
                System.out.println("✅ Hard mode selected!\n");
                return HARD_MAX;
            default:
                return MEDIUM_MAX;
        }
    }
    
    /**
     * Provides additional hints based on proximity
     */
    private static void provideHint(int guess, int secret, int range) {
        int difference = Math.abs(guess - secret);
        double percentage = (difference / (double) range) * 100;
        
        if (percentage <= 5) {
            System.out.println("🔥 You're VERY close!");
        } else if (percentage <= 15) {
            System.out.println("🌡️  You're getting warm!");
        } else if (percentage <= 30) {
            System.out.println("❄️  You're cold...");
        } else {
            System.out.println("🧊 You're freezing!");
        }
        System.out.println();
    }
    
    /**
     * Displays success message and calculates score
     */
    private static void displaySuccessMessage(int attempts, int range) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        🎉 CONGRATULATIONS! 🎉         ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n✨ You guessed the correct number!");
        System.out.println("📊 Attempts taken: " + attempts);
        
        // Calculate score (lower is better)
        int score = calculateScore(attempts, range);
        System.out.println("⭐ Your score: " + score + " points");
        
        // Update best score
        if (attempts < bestScore) {
            bestScore = attempts;
            System.out.println("🏆 NEW BEST SCORE! Your best is now " + bestScore + " attempts!");
        } else if (bestScore != Integer.MAX_VALUE) {
            System.out.println("🥇 Your best score is still: " + bestScore + " attempts");
        }
        
        displayPerformanceRating(attempts);
        System.out.println();
    }
    
    /**
     * Calculates score based on attempts and difficulty
     */
    private static int calculateScore(int attempts, int range) {
        // Scoring: base points - (attempts * penalty factor)
        int basePoints = 1000;
        int difficultyBonus = range / 10;
        int penalty = attempts * 50;
        
        return Math.max(basePoints + difficultyBonus - penalty, 100);
    }
    
    /**
     * Displays performance rating
     */
    private static void displayPerformanceRating(int attempts) {
        System.out.print("🎯 Performance: ");
        
        if (attempts == 1) {
            System.out.println("LEGENDARY! First try!");
        } else if (attempts <= 3) {
            System.out.println("EXCELLENT! Outstanding!");
        } else if (attempts <= 5) {
            System.out.println("GREAT! Well done!");
        } else if (attempts <= 7) {
            System.out.println("GOOD! Nice effort!");
        } else {
            System.out.println("Keep practicing!");
        }
    }
    
    /**
     * Asks if user wants to play again
     */
    private static boolean askPlayAgain(Scanner scanner) {
        System.out.println("─────────────────────────────────────────");
        System.out.print("Would you like to play again? (yes/no): ");
        
        String response = scanner.next().toLowerCase();
        
        while (!response.equals("yes") && !response.equals("no") && 
               !response.equals("y") && !response.equals("n")) {
            System.out.print("Please enter 'yes' or 'no': ");
            response = scanner.next().toLowerCase();
        }
        
        return response.equals("yes") || response.equals("y");
    }
    
    /**
     * Displays goodbye message
     */
    private static void displayGoodbyeMessage() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     Thanks for playing! Goodbye! 👋   ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        if (bestScore != Integer.MAX_VALUE) {
            System.out.println("\n🏆 Your best score this session: " + bestScore + " attempts");
        }
        
        System.out.println();
    }
}