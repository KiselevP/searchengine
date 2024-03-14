package searchengine;

public class Test {
    public static void main(String[] args) {

        String url = "https://www.playback.ru";

        String path = "https://youtube.com/watch?v=liGL55TIJug&ab_channel=Oneshot";

        String key = path.substring(url.lastIndexOf("://") + 3 , url.lastIndexOf("."));



        System.out.println();
    }
}
