package hypersonic;

import java.util.Scanner;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        int myId = in.nextInt();
        in.nextLine();

        Grid grid = new Grid(width, height, myId);

        // game loop
        while (true) {
            for (int i = 0; i < height; i++) {
                String row = in.nextLine();
                grid.addRow(i, row);
            }
            int entities = in.nextInt();
            for (int i = 0; i < entities; i++) {
                int entityType = in.nextInt();
                int owner = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int param1 = in.nextInt();
                int param2 = in.nextInt();
                grid.addEntity(entityType, owner, x, y, param1, param2);
            }
            in.nextLine();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            //System.out.println("BOMB 6 5 Amiral");

            grid.init();

            System.out.println(grid.doAction());
            grid.nextRound();
        }
    }
}