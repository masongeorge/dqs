public class Main {
    private static MySQLHandler SqlHandler;

    public static void main (String args[]) {
        SqlHandler = new MySQLHandler("sql2279737", "fE6!aZ7*");

        if (SqlHandler.CheckAccountExists("George"))
            System.out.println("Account exists");
        else
            System.out.println("Account does not exist");
    }
}
