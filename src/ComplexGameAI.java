// ComplexGameAI： 复杂游戏AI（实际上是简易人机AI）
// 继承SimpleGameAI的功能并增加了简易人机AI
public class ComplexGameAI extends SimpleGameAI{
    // 创建与SimpleGameAI相同
    public ComplexGameAI(GameBoard gameStatus) {
        super(gameStatus);
    }

    // 由GameBoard通知开始更新棋盘并返回结果
    public int update(int row, int column) {
        // 先输入玩家输入的行列数并判定胜负
        if (super.update(row, column) == 0) {
            // 若玩家未获胜，则由电脑计算并输入下一步棋
            System.out.println("电脑（X）正在输入下一步棋...");
            // 判定电脑是否获胜并返回结果（0或2）
            return this.aiStep();
        }
        else {
            // 若玩家获胜则返回结果1
            return 1;
        }
    }

    // 计算电脑下一步棋的位置
    private int aiStep() {
        // 初始化变量用于储存最佳选择
        int bestRow = 0;
        int bestCol = 0;
        int bestScore = 0;
        int nextScore;
        int[][] status = super.gameStatus.getBoardStatus();
        // 遍历棋盘所有位置
        for (int i = 0; i < status.length; i++) {
            for (int j = 0; j < status[i].length; j++) {
                // 若为空位置，则计算当前位置的值，并记录值最高的位置，直到遍历结束
                if (status[i][j] == 0) {
                    nextScore = this.evaluateStates(status, i, j);
                    if (nextScore > bestScore) {
                        bestScore = nextScore;
                        bestCol = j;
                        bestRow = i;
                    }

                }
            }
        }
        // 更新电脑输入的下一步棋位置并判定胜负
        return super.update(bestRow, bestCol);
    }

    // 通过 scoreOffense和scoreDefense计算给定行列数位置的值
    private int evaluateStates(int[][] curState, int nextRow, int nextCol) {
        // 以下通过优先级分五种结果：
        // 结果一（优先级最高）：下在给定位置可获得游戏胜利 (offenseScore == -1)
        // 结果二（优先级二）：阻挡玩家已相连的四子（或玩家差一步棋获胜） (defenseScore == -2)
        // 结果三（优先级三）：下在给定位置可使未被阻挡的四子相连 (offenseScore == -2)
        // 结果四（优先级四）：下在给定位置可阻挡玩家已相连且未被阻挡的三子 (defenseScore == -1)
        // 结果五（优先级五）：若无以上情况，则正常返回计算的值
        int offenseScore = scoreOffense(curState, nextRow, nextCol);
        if (offenseScore == -1) return 262143;
        int defenseScore = scoreDefense(curState, nextRow, nextCol);
        if (defenseScore == -2) return 131071;
        if (offenseScore == -2) return 65535;
        if (defenseScore == -1) return 32767;
        return offenseScore + defenseScore;
    }

    // 计算进攻分数，即通过下到指定位置后，与己方棋子相连以及被阻挡的情况计算值
    // 此方法会对上下、左右、左上右下、左下右上四条路线进行分别计算，若出现优先级结果则直接返回优先级结果。
    // 棋子相连越多，计算出的值越多；若两边出现玩家棋子阻挡，则根据阻挡情况判定：
    // 阻挡0：计算的值乘二   阻挡一：无变化   阻挡2：计算的值改为0
    // 最后将四条路线的值相加并返回值，若出现优先级结果则直接按优先级返回结果。
    private int scoreOffense(int[][] curState, int nextRow, int nextCol) {
        int connectedNum = 1;
        int blockNum = 0;
        int score = 0;

        if (nextRow == 0 || nextRow == 15) blockNum++;
        for (int i = nextRow+1; i < 16; i++) {
            if (curState[i][nextCol] == 2) {
                connectedNum++;
            }
            else if (curState[i][nextCol] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        for (int i = nextRow-1; i >= 0; i--) {
            if (curState[i][nextCol] == 2) {
                connectedNum++;
            }
            else if (curState[i][nextCol] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 5) return -1;
        else if (connectedNum == 4 && blockNum <= 1) return -2;
        else {
            score += Math.pow(connectedNum, 5) * (2 - blockNum);
        }

        blockNum = 0;
        connectedNum = 1;
        if (nextCol == 0 || nextCol == 15) blockNum++;
        for (int j = nextCol+1; j < 16; j++) {
            if (curState[nextRow][j] == 2) {
                connectedNum++;
            }
            else if (curState[nextRow][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }

        for (int j = nextCol-1; j >= 0; j--) {
            if (curState[nextRow][j] == 2) {
                connectedNum++;
            }
            else if (curState[nextRow][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 5) return -1;
        else if (connectedNum == 4 && blockNum <= 1) return -2;
        else {
            score += Math.pow(connectedNum, 5) * (2 - blockNum);
        }

        blockNum = 0;
        connectedNum = 1;
        if (nextRow == 0 || nextCol == 15) blockNum++;
        if (nextCol == 0 || nextRow == 15) blockNum++;
        for (int i = nextRow+1, j = nextCol+1; i < 16 && j < 16; i++, j++) {
            if (curState[i][j] == 2) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }

        for (int i = nextRow-1, j = nextCol-1; i>= 0 && j >= 0; i--, j--) {
            if (curState[i][j] == 2) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 5) return -1;
        else if (connectedNum == 4 && blockNum <= 1) return -2;
        else {
            score += Math.pow(connectedNum, 5) * (2 - blockNum);
        }


        blockNum = 0;
        connectedNum = 1;
        if (nextRow == 0 || nextCol == 0) blockNum++;
        if (nextRow == 15 || nextCol == 15) blockNum++;
        for (int i = nextRow-1, j = nextCol+1; i >= 0 && j < 16; i--, j++) {
            if (curState[i][j] == 2) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }

        for (int i = nextRow+1, j = nextCol-1; i < 16 && j >= 0; i++, j--) {
            if (curState[i][j] == 2) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 5) return -1;
        else if (connectedNum == 4 && blockNum <= 1) return -2;
        else {
            score += Math.pow(connectedNum, 5) * (2 - blockNum);
        }

        return score;
    }

    // 计算防御分数，即通过下到指定位置后通过阻挡到的玩家棋子相连的情况计算值
    // 此方法会对上下、左右、左上右下、左下右上四条路线进行分别计算，若出现优先级结果则直接返回优先级结果。
    // 阻挡住的相连棋子连接数，计算出的值越多；若另一端或两端已被阻挡，则根据阻挡情况判定：
    // 被阻挡数0：计算的值乘二   被阻挡数1：无变化   被阻挡数2：计算的值改为0
    // 最后将四条路线的值相加并返回值，若出现优先级结果则直接按优先级返回结果。
    private int scoreDefense(int[][] curState, int nextRow, int nextCol) {
        int connectedNum = 0;
        int blockNum = 0;
        int score = 0;

        if (nextRow == 0 || nextRow == 15) blockNum++;
        for (int i = nextRow+1; i < 16; i++) {
            if (curState[i][nextCol] == 1) {
                connectedNum++;
            }
            else if (curState[i][nextCol] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        for (int i = nextRow-1; i >= 0; i--) {
            if (curState[i][nextCol] == 1) {
                connectedNum++;
            }
            else if (curState[i][nextCol] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 3 && blockNum == 0) return -1;
        else if (connectedNum >= 4) return -2;
        else {
            score += Math.pow(connectedNum, 7) * (2 - blockNum);
            if (connectedNum == 1) score++;
        }

        blockNum = 0;
        connectedNum = 0;
        if (nextCol == 0 || nextCol == 15) blockNum++;
        for (int j = nextCol+1; j < 16; j++) {
            if (curState[nextRow][j] == 1) {
                connectedNum++;
            }
            else if (curState[nextRow][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }

        for (int j = nextCol-1; j >= 0; j--) {
            if (curState[nextRow][j] == 1) {
                connectedNum++;
            }
            else if (curState[nextRow][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 3 && blockNum == 0) return -1;
        else if (connectedNum >= 4) return -2;
        else {
            score += Math.pow(connectedNum, 7) * (2 - blockNum);
            if (connectedNum == 1) score++;
        }

        blockNum = 0;
        connectedNum = 0;
        if (nextRow == 0 || nextCol == 15) blockNum++;
        if (nextCol == 0 || nextRow == 15) blockNum++;
        for (int i = nextRow+1, j = nextCol+1; i < 16 && j < 16; i++, j++) {
            if (curState[i][j] == 1) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }

        for (int i = nextRow-1, j = nextCol-1; i>= 0 && j >= 0; i--, j--) {
            if (curState[i][j] == 1) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 3 && blockNum == 0) return -1;
        else if (connectedNum >= 4) return -2;
        else {
            score += Math.pow(connectedNum, 7) * (2 - blockNum);
            if (connectedNum == 1) score++;
        }


        blockNum = 0;
        connectedNum = 0;
        if (nextRow == 0 || nextCol == 0) blockNum++;
        if (nextRow == 15 || nextCol == 15) blockNum++;
        for (int i = nextRow-1, j = nextCol+1; i >= 0 && j < 16; i--, j++) {
            if (curState[i][j] == 1) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }

        for (int i = nextRow+1, j = nextCol-1; i < 16 && j >= 0; i++, j--) {
            if (curState[i][j] == 1) {
                connectedNum++;
            }
            else if (curState[i][j] == 0) {
                break;
            }
            else {
                blockNum++;
                break;
            }
        }
        if (connectedNum == 3 && blockNum == 0) return -1;
        else if (connectedNum >= 4) return -2;
        else {
            score += Math.pow(connectedNum, 7) * (2 - blockNum);
            if (connectedNum == 1) score++;
        }

        return score;
    }
}
