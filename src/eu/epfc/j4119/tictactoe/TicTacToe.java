package eu.epfc.j4119.tictactoe;

import java.sql.*;
import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        initializeDB();
        run();
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);
        do {
            String choice = showMenu(scanner);
            if(choice.equals("Q")) break;
            switch (choice) {
                case "1":
                    createGame(scanner);
                    break;
                case "2":
                    showGames();
                    break;
                case "3":
                    updateGame(scanner);
                    break;
                case "4":
                    deleteGame(scanner);
                    break;
                default:
                    System.out.println("the choice"+choice+"is not known");
            }
        }while (true);
    }

    private static void deleteGame(Scanner scanner) {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:tictactoe")){
            String sql = "DELETE FROM game WHERE id=?";
            System.out.println("Enter the id of the game to delete");
            int id = Integer.parseInt(scanner.nextLine());
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    private static void updateGame(Scanner scanner) {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:tictactoe")){
            String sql = "UPDATE game SET playerX=?, playerO=?, cases=? WHERE id=?";
            System.out.print("Choose the id of the game to modify:");
            String idToModify = scanner.nextLine();
            int id = Integer.parseInt(idToModify);
            System.out.print("playerX: ");
            String playerX = scanner.nextLine();
            System.out.print("playerO: ");
            String playerO = scanner.nextLine();
            System.out.print("cases: ");
            String cases = scanner.nextLine();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playerX);
            statement.setString(2,playerO);
            statement.setString(3,cases);
            statement.setInt(4,id);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    private static void showGames() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tictactoe")) {
            String sql = "SELECT id, playerX, playerO, cases FROM game";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String playerX = resultSet.getString(2);
                String playerO = resultSet.getString(3);
                String cases = resultSet.getString(4);
                System.out.println(id+" "+playerX+" "+playerO+" "+cases);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    private static void createGame(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tictactoe")) {
            String sql = "INSERT INTO game(playerX,playerO,cases) VALUES(?,?,?)";
            System.out.println("PlayerX, enter your name:");
            String playerX = scanner.nextLine();
            System.out.println("PlayerO, enter your name:");
            String playerO = scanner.nextLine();
            String cases = "_________";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playerX);
            statement.setString(2, playerO);
            statement.setString(3, cases);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static String showMenu(Scanner scanner) {
        System.out.println("Choose one of the options here below:");
        System.out.println("1. create a game");
        System.out.println("2. show games list");
        System.out.println("3. modify the game");
        System.out.println("4. delete the game");
        System.out.println("Q. leave the game");
        String choice = scanner.nextLine();
        return choice;
    }

    private static void initializeDB() {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:tictactoe")) {
            String sql = "CREATE TABLE IF NOT EXISTS game("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "playerX TEXT,"+
                    "playerO TEXT,"+
                    "cases TEXT)";
            connection.createStatement().execute(sql);
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}
