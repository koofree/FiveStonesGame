package gameClient;

/*
 * Version : 1.01
 *
 * 09/02 패치 : 바둑알 덧그리기 수정, Lobby에서 채팅창 강제개행 수정.
 *
 */

import gui.FindCrossPoint;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIClientLobby extends ClientLobby implements ClientInterface {

    public AIClientLobby(int id) {
        super(String.valueOf(id));
    }

    private int[] getPointByAI() throws Exception {
        URL url = null;
        try {
            Thread.sleep(1000);

            url = new URL("http://localhost:5000/" + this.playerId);
            System.out.println(url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            JSONParser p = new JSONParser();
            JSONObject obj = (JSONObject) p.parse(new InputStreamReader(con.getInputStream()));

            System.out.println(obj.toJSONString());

            int[] randomLocation = new int[2];
            randomLocation[0] = Integer.parseInt(obj.get("position_x").toString()) + 2;
            randomLocation[1] = Integer.parseInt(obj.get("position_y").toString()) + 2;

            boolean finished = Boolean.parseBoolean(obj.get("gameOver").toString());
            if (finished == true) {
                throw new Exception();
            }

            return randomLocation;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void doAfterWork() throws Exception {
        int[] randomLocation = getPointByAI();
        int[] point;
        do {
            randomLocation[0] = 29 * randomLocation[0];
            randomLocation[1] = 29 * randomLocation[1];

            point = FindCrossPoint.find(randomLocation[0], randomLocation[1]);
        }
        while (m_gameRoom.getM_board().isStoneDraw(point));


        System.out.println(String.format("%d, %d", point[0], point[1]));
        sendMessage(point);
    }

    public static void main(String[] args) {
        try {
            new AIClientLobby(Integer.parseInt(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}