import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // 创建初始对象
        Scanner scanner = new Scanner(System.in);
        GameBoard game = new GameBoard();
        SimpleGameAI ai;

        // 游戏开始，通过用户的输入选择不同游戏模式，游戏模式通过不同的GameAI实现
        System.out.println("欢迎来到双人五子棋");
        System.out.println("请选择游戏模式： 输入 0 - 双人对战\t1 - 人机对战");
        while (true) {
            try {
                int instruction = scanner.nextInt();
                if(instruction == 0) {
                    ai = new SimpleGameAI(game);
                    System.out.println("您选择了 - 双人对战，正在初始化棋盘......");
                    break;
                }
                else if(instruction == 1){
                    ai = new ComplexGameAI(game);
                    System.out.println("您选择了 - 人机对战，正在初始化棋盘......");
                    break;
                }
                else {
                    System.out.println("错误：请输入正确的数字！");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("InputMismatchException: 错误！请正确输入数字！");
                scanner.nextLine();
            }
        }

        // 将AI注册为为观察者
        game.registerAI(ai);
        // 创建变量row, column为用户输入的行数列数，stepResult为每个循环游戏运行结果的输出
        int row, column;
        int stepResult;
        System.out.println("玩家一：O\t玩家二：X");
        while(true) {
            try {
                if (ai.getCurrentPlayer() == 1)
                    System.out.println("请 玩家一（O） 输入下一步棋：");
                else
                    System.out.println("请 玩家二（X） 输入下一步棋：");

                // 用户输入下一步棋的行数、列数，或选择退出游戏
                System.out.println("请输入：\n -1 退出游戏或输入  行数（0 ~ 15）  列数（0 ~ 15）  作为您下一步棋的位置");
                row = scanner.nextInt();
                if (row == -1) {
                    System.out.println("已退出游戏！");
                    return;
                }
                column = scanner.nextInt();
                // 确定输入数字处于正确区间
                if (column > 15 || row > 15 || column < -1 || row < -1) {
                    System.out.println("错误的数字，请重新输入：");
                    continue;
                }

                // 游戏运行，若有玩家或电脑胜出，结束Main进程
                stepResult = game.nextStep(row, column);
                if (stepResult == 1 || stepResult == 2) return;

            }
            catch (InputMismatchException e) {
                // 用于捕获用户输入时可能输入非数字时的异常并进行解决
                System.out.println("InputMismatchException: 错误！请正确输入数字！");
                scanner.nextLine();
            }
        }
    }
}
