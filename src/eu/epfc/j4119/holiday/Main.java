package eu.epfc.j4119.holiday;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        initializeDataBase();
        run();
    }

    private static void run() {
        Scanner in = new Scanner(System.in);
        do {
            String choice = showMenu(in);
            if(choice.equals("Q")) break;
            switch (choice){
                case "1":
                    showRequests();
                    break;
                case "2":
                    createRequest(in);
                    break;
                case "3":
                    cancelRequest(in);
                    break;
                default:
                    System.out.println("Le choix "+choice+" n'est pas connu" );
            }
        }while (true);
    }

    private static void cancelRequest(Scanner in) {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:holiday.db")){
            System.out.println("choose the holiday to delete ");
            String idToDelete = in.nextLine();
            int id = Integer.parseInt(idToDelete);
            String sql = "DELETE FROM request WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    private static void createRequest(Scanner in) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:holiday.db")){
            String sql = "INSERT INTO request (employee, start, end, status) VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            String employee = "Sylvie";
            System.out.print("Début: ");
            String start = in.nextLine();
            System.out.print("Fin: ");
            String fin = in.nextLine();
            String status = "REQUESTED";
            statement.setString(1,employee);
            statement.setString(2,start);
            statement.setString(3,fin);
            statement.setString(4,status);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    private static void showRequests() {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:holiday.db")){
            String sql = "SELECT id, employee, start, end, status FROM request";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String employee = resultSet.getString(2);
                String start = resultSet.getString(3);
                String end = resultSet.getString(4);
                String status = resultSet.getString(5);
                System.out.println(id+" "+employee+" "+start+" "+end+" "+status);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    private static String showMenu(Scanner in) {
        System.out.println();
        System.out.println("Choisissez parmi les options suivantes:");
        System.out.println("1. lister les demandes de congé");
        System.out.println("2. demander un nouveau congé");
        System.out.println("3. annuler une demande de congé");
        System.out.println("Q. quitter l'application");
        String choice = in.nextLine();
        return choice;
    }

    private static void initializeDataBase() {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:holiday.db")){
            String sql ="CREATE TABLE IF NOT EXI" +
                    "STS request(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "employee TEXT,"+
                    "start TEXT,"+
                    "end TEXT,"+
                    "status TEXT)";
            System.out.println(sql);
            connection.createStatement().execute(sql);
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}
