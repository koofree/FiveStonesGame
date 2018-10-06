package gameClient;

/*
 * Version : 1.01
 *
 * 09/02 패치 : 바둑알 덧그리기 수정, Lobby에서 채팅창 강제개행 수정.
 *
 */

import java.util.Random;

public class AIClientLobby extends ClientLobby implements ClientInterface {

    public AIClientLobby(String id) {
        super(id);
    }

    public static Random random = new Random();

    @Override
    protected void doAfterWork() {
        int[] randomLocation = new int[2];
        do {
            randomLocation[0] = random.nextInt(20);
            randomLocation[1] = random.nextInt(20);
        }
        while (m_gameRoom.getM_board().isStoneDraw(randomLocation));

        System.out.println(String.format("%d, %d", randomLocation[0], randomLocation[1]));
        sendMessage(randomLocation);
    }

    public static void main(String[] args) {
        try {
            new AIClientLobby("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}